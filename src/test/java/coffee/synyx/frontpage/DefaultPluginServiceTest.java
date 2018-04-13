package coffee.synyx.frontpage;


import coffee.synyx.frontpage.plugin.api.ConfigurationDescription;
import coffee.synyx.frontpage.plugin.api.ConfigurationInstance;
import coffee.synyx.frontpage.plugin.api.FrontpagePlugin;
import coffee.synyx.frontpage.plugin.api.FrontpagePluginQualifier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.plugin.core.PluginRegistry;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultPluginServiceTest {

    private DefaultPluginService sut;

    @Mock
    private PluginRegistry<FrontpagePlugin, FrontpagePluginQualifier> pluginRegistry;


    @Before
    public void setUp() {

        sut = new DefaultPluginService(pluginRegistry);
    }


    @Test
    public void userDoesNotHaveAnyPlugins() {

        when(pluginRegistry.getPlugins()).thenReturn(singletonList(new TextPlugin()));

        final Set<PluginInstance> plugins = sut.getPluginInstancesOf("username");

        assertThat(plugins).isEmpty();
    }


    @Test
    public void userHaveAPlugin() {

        when(pluginRegistry.getPlugins()).thenReturn(singletonList(new TextPlugin()));

        sut.savePluginInstance("username", "Text");

        final Set<PluginInstance> plugins = sut.getPluginInstancesOf("username");

        assertThat(plugins).hasSize(1);
    }

    @Test
    public void userRemovesAPlugin() {

        when(pluginRegistry.getPlugins()).thenReturn(singletonList(new TextPlugin()));

        sut.savePluginInstance("username", "Text");
        final Set<PluginInstance> textPlugin = sut.getPluginInstancesOf("username");
        assertThat(textPlugin).hasSize(1);

        sut.removePluginInstance("username", textPlugin.iterator().next().getId().toString());
        assertThat(sut.getPluginInstancesOf("username")).hasSize(0);
    }


    @Test
    public void userGetsOnlyAvailablePluginsAndNotIgnoredOnes() {

        final TextPlugin textPlugin = new TextPlugin();
        final NumberPlugin numberPlugin = new NumberPlugin();
        when(pluginRegistry.getPlugins()).thenReturn(asList(textPlugin, numberPlugin));

        final PluginInstance textPluginInstance = new PluginInstance(new ConfigurationInstanceImpl(emptyMap()), textPlugin);
        final PluginInstance numberPluginInstance = new PluginInstance(new ConfigurationInstanceImpl(emptyMap()), numberPlugin);

        sut.savePluginInstance("username", "Text");
        sut.savePluginInstance("username", "Number");
        assertThat(sut.getPluginInstancesOf("username")).hasSize(2);

        sut.ignorePlugin("Number");
        assertThat(sut.getPluginInstancesOf("username")).hasSize(1);
        assertThat(sut.getPluginInstancesOf("username").iterator().next().getPlugin()).isInstanceOf(TextPlugin.class);
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
