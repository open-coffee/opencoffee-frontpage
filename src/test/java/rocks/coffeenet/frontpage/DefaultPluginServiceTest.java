package rocks.coffeenet.frontpage;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.plugin.core.PluginRegistry;
import rocks.coffeenet.frontpage.plugin.api.ConfigurationDescription;
import rocks.coffeenet.frontpage.plugin.api.ConfigurationInstance;
import rocks.coffeenet.frontpage.plugin.api.FrontpagePlugin;
import rocks.coffeenet.frontpage.plugin.api.FrontpagePluginQualifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultPluginServiceTest {

    private DefaultPluginService sut;

    @Mock
    private PluginRegistry<FrontpagePlugin, FrontpagePluginQualifier> pluginRegistry;

    @Mock
    private PluginRepository pluginRepository;

    @Before
    public void setUp() {

        sut = new DefaultPluginService(pluginRegistry, pluginRepository);
    }


    @Test
    public void getPluginsInstances() {

        final UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        final PluginInstance pluginInstance = new PluginInstance(uuid, "derTobsch", new ConfigurationInstanceImpl(emptyMap()), "pluginId");
        when(pluginRepository.findById(uuid)).thenReturn(pluginInstance);

        final Optional<PluginInstance> plugins = sut.getPluginInstance("123e4567-e89b-12d3-a456-556642440000");

        assertThat(plugins).isEqualTo(Optional.of(pluginInstance));
    }


    @Test
    public void userDoesNotHaveAnyPluginsInstances() {

        when(pluginRepository.findById(UUID.fromString("123e4567-e89b-12d3-a456-556642440000"))).thenReturn(null);

        final Optional<PluginInstance> plugins = sut.getPluginInstance("123e4567-e89b-12d3-a456-556642440000");

        assertThat(plugins.isPresent()).isFalse();
    }


    @Test
    public void userDoesNotHaveAnyPlugins() {

        when(pluginRegistry.getPlugins()).thenReturn(singletonList(new TextPlugin()));

        final Set<PluginInstance> plugins = sut.getPluginInstancesOf("username");

        assertThat(plugins).isEmpty();
    }


    @Test
    public void updatePluginInstance() {

        final UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        final PluginInstance pluginInstance = new PluginInstance(uuid, "derTobsch", new ConfigurationInstanceImpl(emptyMap()), "pluginId");
        when(pluginRepository.findById(uuid)).thenReturn(pluginInstance);

        Map<String, String> params = new HashMap<>();
        params.put("thisKey", "withThatValue");
        sut.updatePluginInstance("123e4567-e89b-12d3-a456-556642440000", new ConfigurationInstanceImpl(params));

        ArgumentCaptor<PluginInstance> captor = ArgumentCaptor.forClass(PluginInstance.class);
        verify(pluginRepository).save(captor.capture());

        final PluginInstance instance = captor.getValue();
        assertThat(instance.getId()).isEqualTo(UUID.fromString("123e4567-e89b-12d3-a456-556642440000"));
        assertThat(instance.getUsername()).isEqualTo("derTobsch");
        assertThat(instance.getPluginId()).isEqualTo("pluginId");
        assertThat(instance.getConfigurationInstance().get("thisKey")).isEqualTo("withThatValue");
    }


    @Test
    public void updatePluginInstanceButNoInstanceForUserAvailable() {

        when(pluginRepository.findById(UUID.fromString("123e4567-e89b-12d3-a456-556642440000"))).thenReturn(null);

        Map<String, String> params = new HashMap<>();
        params.put("thisKey", "withThatValue");
        sut.updatePluginInstance("123e4567-e89b-12d3-a456-556642440000", new ConfigurationInstanceImpl(params));

        verify(pluginRepository, never()).save(any(PluginInstance.class));
    }


    @Test
    public void userHaveAPlugin() {

        when(pluginRegistry.getPlugins()).thenReturn(singletonList(new TextPlugin()));

        sut.savePluginInstance("username", "Text");

        final Set<PluginInstance> plugins = sut.getPluginInstancesOf("username");

        ArgumentCaptor<PluginInstance> captor = ArgumentCaptor.forClass(PluginInstance.class);
        verify(pluginRepository).save(captor.capture());

        PluginInstance pluginInstance = captor.getValue();
        assertThat(pluginInstance.getPluginId()).isEqualTo("Text");
        assertThat(pluginInstance.getUsername()).isEqualTo("username");
    }


    @Test
    public void userRemovesAPlugin() {

        String uuid = "704919d6-9a25-f4f4-3ecc-0ef88ca265ba";
        sut.removePluginInstance("username", uuid);
        verify(pluginRepository).deleteById(UUID.fromString(uuid));
    }


    @Test
    public void userGetsOnlyAvailablePluginsAndNotIgnoredOnes() {

        final TextPlugin textPlugin = new TextPlugin();
        final NumberPlugin numberPlugin = new NumberPlugin();
        when(pluginRegistry.getPlugins()).thenReturn(asList(textPlugin, numberPlugin));

        final PluginInstance textPluginInstance = new PluginInstance("username", new ConfigurationInstanceImpl(emptyMap()), textPlugin.id());
        final PluginInstance numberPluginInstance = new PluginInstance("username", new ConfigurationInstanceImpl(emptyMap()), numberPlugin.id());

        Set<PluginInstance> pluginInstances = Stream.of(textPluginInstance, numberPluginInstance).collect(Collectors.toSet());
        when(pluginRepository.findAllByUsername("username")).thenReturn(pluginInstances);

        sut.savePluginInstance("username", "Text");
        sut.savePluginInstance("username", "Number");
        assertThat(sut.getPluginInstancesOf("username")).hasSize(2);

        sut.ignorePlugin("Number");
        assertThat(sut.getPluginInstancesOf("username")).hasSize(1);
        assertThat(sut.getPluginInstancesOf("username").iterator().next().getPluginId()).isEqualTo("Text");
    }


    @Test
    public void getOnlyAvailablePlugins() {

        final TextPlugin textPlugin = new TextPlugin();
        final NumberPlugin numberPlugin = new NumberPlugin();
        when(pluginRegistry.getPlugins()).thenReturn(asList(textPlugin, numberPlugin));

        sut.ignorePlugin("Text");

        final List<FrontpagePlugin> availablePlugins = sut.getAvailablePlugins();
        assertThat(availablePlugins).hasSize(1);
        assertThat(availablePlugins).contains(numberPlugin);
    }


    private class TextPlugin implements FrontpagePlugin {


        @Override
        public String title(ConfigurationInstance configurationInstance) {

            return "There is a text";
        }

        @Override
        public String content(ConfigurationInstance configurationInstance) {

            return "There is a text";
        }

        @Override
        public String id() {

            return "Text";
        }

        @Override
        public Optional<ConfigurationDescription> getConfigurationDescription() {

            return Optional.empty();
        }
    }

    private class NumberPlugin implements FrontpagePlugin {

        @Override
        public String title(ConfigurationInstance configurationInstance) {

            return "There is a number";
        }


        @Override
        public String content(ConfigurationInstance configurationInstance) {

            return "i am the 2";
        }


        @Override
        public String id() {

            return "Number";
        }

        @Override
        public Optional<ConfigurationDescription> getConfigurationDescription() {

            return Optional.empty();
        }
    }
}
