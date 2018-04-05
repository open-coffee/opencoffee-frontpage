package coffee.synyx.frontpage;

import coffee.synyx.frontpage.plugin.api.FrontpagePluginInterface;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.event.ContextRefreshedEvent;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

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

    private class DifferentIdPlugin implements FrontpagePluginInterface {

        @Override
        public String title() {

            return "There is a different plugin";
        }


        @Override
        public String content() {

            return "i am the diff";
        }

        @Override
        public String id() {
            return "DifferentId";
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
            return "SameId";
        }
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
            return "SameId";
        }
    }
}
