package coffee.synyx.frontpage;

import coffee.synyx.frontpage.plugin.api.FrontpagePluginInterface;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import static java.util.Collections.emptyList;


@RunWith(MockitoJUnitRunner.class)
public class PluginsControllerTest {

    private PluginsController sut;

    @Mock
    private DefaultPluginCollector defaultPluginCollector;

    @Before
    public void setUp() {

        sut = new PluginsController(defaultPluginCollector);
    }


    @Test
    public void returnsEmptyListWhenNoPluginWasFound() throws Exception {

        when(defaultPluginCollector.getFrontpagePlugins()).thenReturn(emptyList());

        ResultActions result = perform(get("/"));
        result.andExpect(status().isOk());
        result.andExpect(view().name("plugins"));
        result.andExpect(model().attribute("plugins", emptyList()));
    }


    @Test
    public void pluginsWhereFound() throws Exception {

        List<FrontpagePluginInterface> plugins = new ArrayList<>();
        plugins.add(new NumberPlugin());
        plugins.add(new TextPlugin());

        when(defaultPluginCollector.getFrontpagePlugins()).thenReturn(plugins);

        ResultActions result = perform(get("/"));
        result.andExpect(status().isOk());
        result.andExpect(view().name("plugins"));
        result.andExpect(model().attribute("plugins", hasSize(2)));
    }


    private ResultActions perform(RequestBuilder builder) throws Exception {

        return standaloneSetup(sut).build().perform(builder);
    }

    class NumberPlugin implements FrontpagePluginInterface {

        @Override
        public String title() {

            return "There is a number";
        }


        @Override
        public String content() {

            return "i am the 2";
        }
    }

    class TextPlugin implements FrontpagePluginInterface {

        @Override
        public String title() {

            return "There is a text";
        }


        @Override
        public String content() {

            return "Good old text";
        }
    }
}
