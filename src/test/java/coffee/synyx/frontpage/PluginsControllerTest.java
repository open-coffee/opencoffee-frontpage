package coffee.synyx.frontpage;

import coffee.synyx.autoconfigure.security.service.CoffeeNetCurrentUserService;
import coffee.synyx.autoconfigure.security.service.HumanCoffeeNetUser;
import coffee.synyx.frontpage.plugin.api.FrontpagePluginInterface;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.Optional;

import static java.util.Collections.emptySet;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.hasSize;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import static java.util.Collections.emptyList;


@RunWith(MockitoJUnitRunner.class)
public class PluginsControllerTest {

    private PluginsController sut;

    @Mock
    private PluginService pluginService;
    @Mock
    private CoffeeNetCurrentUserService coffeeNetCurrentUserService;

    private HumanCoffeeNetUser humanCoffeeNetUser;

    @Before
    public void setUp() {

        sut = new PluginsController(pluginService, coffeeNetCurrentUserService);

        humanCoffeeNetUser = new HumanCoffeeNetUser("CoffeeNet", "", emptySet());
        when(coffeeNetCurrentUserService.get()).thenReturn(Optional.of(humanCoffeeNetUser));
    }


    @Test
    public void returnsEmptyListWhenNoPluginWasFound() throws Exception {

        when(pluginService.getAvailablePlugins()).thenReturn(emptyList());
        when(pluginService.getPluginsOf(humanCoffeeNetUser.getUsername())).thenReturn(emptyList());

        ResultActions result = perform(get("/"));
        result.andExpect(status().isOk());
        result.andExpect(view().name("plugins"));
        result.andExpect(model().attribute("userPlugins", emptyList()));
        result.andExpect(model().attribute("plugins", emptyList()));
    }


    @Test
    public void pluginsWhereFound() throws Exception {

        when(pluginService.getAvailablePlugins()).thenReturn(Arrays.asList(new NumberPlugin(), new TextPlugin()));
        when(pluginService.getPluginsOf(humanCoffeeNetUser.getUsername())).thenReturn(singletonList(new TextPlugin()));

        ResultActions result = perform(get("/"));
        result.andExpect(status().isOk());
        result.andExpect(view().name("plugins"));
        result.andExpect(model().attribute("plugins", hasSize(2)));
        result.andExpect(model().attribute("userPlugins", hasSize(1)));
    }


    @Test
    public void addPlugin() throws Exception {

        ResultActions result = perform(put("/plugins/id"));
        result.andExpect(status().is3xxRedirection());
        result.andExpect(view().name("redirect:/"));

        verify(pluginService).addPlugin("id", humanCoffeeNetUser.getUsername());
    }


    @Test
    public void removePlugin() throws Exception {

        ResultActions result = perform(delete("/plugins/id"));
        result.andExpect(status().is3xxRedirection());
        result.andExpect(view().name("redirect:/"));

        verify(pluginService).removePlugin("id", humanCoffeeNetUser.getUsername());
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

        @Override
        public String id() {
            return "Number";
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

        @Override
        public String id() {
            return "Text";
        }
    }
}
