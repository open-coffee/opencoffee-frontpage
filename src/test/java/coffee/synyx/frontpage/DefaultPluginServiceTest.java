package coffee.synyx.frontpage;


import coffee.synyx.frontpage.plugin.api.FrontpagePluginInterface;
import coffee.synyx.frontpage.plugin.api.FrontpagePluginQualifier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.plugin.core.PluginRegistry;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultPluginServiceTest {

    private DefaultPluginService sut;

    @Mock
    private PluginRegistry<FrontpagePluginInterface, FrontpagePluginQualifier> pluginRegistry;


    @Before
    public void setUp() {

        sut = new DefaultPluginService(pluginRegistry);
    }


    @Test
    public void userDoesNotHaveAnyPlugins() {

        when(pluginRegistry.getPlugins()).thenReturn(singletonList(new TextPlugin()));

        final List<FrontpagePluginInterface> plugins = sut.getPluginsOf("username");

        assertThat(plugins).isEmpty();
    }


    @Test
    public void userHaveAPlugin() {

        when(pluginRegistry.getPlugins()).thenReturn(singletonList(new TextPlugin()));

        sut.addPlugin("Text", "username");

        final List<FrontpagePluginInterface> plugins = sut.getPluginsOf("username");

        assertThat(plugins).hasSize(1);
    }

    @Test
    public void userRemovesAPlugin() {

        when(pluginRegistry.getPlugins()).thenReturn(singletonList(new TextPlugin()));

        sut.addPlugin("Text", "username");
        assertThat(sut.getPluginsOf("username")).hasSize(1);

        sut.removePlugin("Text", "username");
        assertThat(sut.getPluginsOf("username")).hasSize(0);
    }


    @Test
    public void userGetsOnlyAvailablePluginsAndNotIgnoredOnes() {

        final TextPlugin textPlugin = new TextPlugin();
        final NumberPlugin numberPlugin = new NumberPlugin();
        when(pluginRegistry.getPlugins()).thenReturn(asList(textPlugin, numberPlugin));


        sut.addPlugin("Text", "username");
        assertThat(sut.getPluginsOf("username")).hasSize(1);
        assertThat(sut.getPluginsOf("username")).contains(textPlugin);

        sut.addPlugin("Number", "username");
        assertThat(sut.getPluginsOf("username")).hasSize(2);
        assertThat(sut.getPluginsOf("username")).contains(textPlugin);
        assertThat(sut.getPluginsOf("username")).contains(numberPlugin);

        sut.ignorePlugin("Number");
        assertThat(sut.getPluginsOf("username")).hasSize(1);
        assertThat(sut.getPluginsOf("username")).contains(textPlugin);
    }


    private class TextPlugin implements FrontpagePluginInterface {

        @Override
        public String title() {

            return "There is a text";
        }


        @Override
        public String content() {

            return "Good old text";
        }

        @Override
        public String id() {
            return "Text";
        }
    }

    private class NumberPlugin implements FrontpagePluginInterface {

        @Override
        public String title() {

            return "There is a number";
        }


        @Override
        public String content() {

            return "i am the 2";
        }

        @Override
        public String id() {
            return "Number";
        }
    }
}
