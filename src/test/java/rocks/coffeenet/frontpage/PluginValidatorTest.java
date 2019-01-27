package rocks.coffeenet.frontpage;

import coffee.synyx.frontpage.plugin.api.ConfigurationDescription;
import coffee.synyx.frontpage.plugin.api.ConfigurationInstance;
import coffee.synyx.frontpage.plugin.api.FrontpagePlugin;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Optional;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class PluginValidatorTest {

    private PluginValidator sut;

    @Mock
    private PluginService pluginService;

    @Before
    public void setUp() {

        sut = new PluginValidator(pluginService);
    }

    @Test
    public void ignorePluginsWithDuplicatedIds() {

        final TextPlugin textPlugin = new TextPlugin();
        final NumberPlugin numberPlugin = new NumberPlugin();
        final DifferentIdPlugin diffPlugin = new DifferentIdPlugin();
        when(pluginService.getAvailablePlugins()).thenReturn(asList(textPlugin, numberPlugin, diffPlugin));

        sut.onApplicationEvent(mock(ContextRefreshedEvent.class));

        verify(pluginService).ignorePlugin("SameId");
    }

    private class DifferentIdPlugin implements FrontpagePlugin {

        @Override
        public String title(ConfigurationInstance configurationInstance) {
            return null;
        }

        @Override
        public String content(ConfigurationInstance configurationInstance) {
            return null;
        }

        @Override
        public String id() {
            return "DifferentId";
        }

        @Override
        public Optional<ConfigurationDescription> getConfigurationDescription() {
            return Optional.empty();
        }
    }

    private class NumberPlugin implements FrontpagePlugin {

        @Override
        public String title(ConfigurationInstance configurationInstance) {
            return null;
        }

        @Override
        public String content(ConfigurationInstance configurationInstance) {
            return null;
        }

        @Override
        public String id() {
            return "SameId";
        }

        @Override
        public Optional<ConfigurationDescription> getConfigurationDescription() {
            return Optional.empty();
        }
    }

    private class TextPlugin implements FrontpagePlugin {

        @Override
        public String title(ConfigurationInstance configurationInstance) {
            return null;
        }

        @Override
        public String content(ConfigurationInstance configurationInstance) {
            return null;
        }

        @Override
        public String id() {
            return "SameId";
        }

        @Override
        public Optional<ConfigurationDescription> getConfigurationDescription() {
            return Optional.empty();
        }
    }
}
