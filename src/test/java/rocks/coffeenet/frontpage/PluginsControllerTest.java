package rocks.coffeenet.frontpage;

import coffee.synyx.autoconfigure.security.service.CoffeeNetCurrentUserService;
import coffee.synyx.autoconfigure.security.service.HumanCoffeeNetUser;
import coffee.synyx.frontpage.plugin.api.ConfigurationDescription;
import coffee.synyx.frontpage.plugin.api.ConfigurationField;
import coffee.synyx.frontpage.plugin.api.ConfigurationFieldType;
import coffee.synyx.frontpage.plugin.api.ConfigurationInstance;
import coffee.synyx.frontpage.plugin.api.FrontpagePlugin;
import coffee.synyx.frontpage.plugin.api.validation.ConfigurationFieldValidator;
import rocks.coffeenet.frontpage.validation.ConfigurationInstanceValidator;
import rocks.coffeenet.frontpage.validation.validators.NumberFieldValidator;
import rocks.coffeenet.frontpage.validation.validators.RequiredFieldValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.NestedServletException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


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

        Set<ConfigurationFieldValidator> validators = new HashSet<>();
        validators.add(new NumberFieldValidator());
        validators.add(new RequiredFieldValidator());
        ConfigurationInstanceValidator configurationInstanceValidator = new ConfigurationInstanceValidator(validators);

        sut = new PluginsController(pluginService, coffeeNetCurrentUserService, configurationInstanceValidator);

        humanCoffeeNetUser = new HumanCoffeeNetUser("CoffeeNet", "", emptySet());
        when(coffeeNetCurrentUserService.get()).thenReturn(Optional.of(humanCoffeeNetUser));
    }


    @Test
    public void addNewPlugin() throws Exception {

        TextPlugin textPlugin = new TextPlugin();
        when(pluginService.getAvailablePlugins()).thenReturn(asList(new NumberPlugin(), textPlugin));

        ResultActions result = perform(get("/add"));
        result.andExpect(status().isOk());
        result.andExpect(view().name("plugins-add"));
        result.andExpect(model().attribute("availablePlugins", hasSize(2)));
    }


    @Test
    public void returnsEmptyListWhenNoAvailablePluginWasFound() throws Exception {

        when(pluginService.getAvailablePlugins()).thenReturn(emptyList());

        ResultActions result = perform(get("/add"));
        result.andExpect(status().isOk());
        result.andExpect(view().name("plugins-add"));
        result.andExpect(model().attribute("availablePlugins", emptyList()));
    }


    @Test
    public void returnsEmptyListWhenNoPluginWasFound() throws Exception {

        when(pluginService.getPluginInstancesOf(humanCoffeeNetUser.getUsername())).thenReturn(emptySet());

        ResultActions result = perform(get("/"));
        result.andExpect(status().isOk());
        result.andExpect(view().name("plugins"));
        result.andExpect(model().attribute("myPlugins", emptyList()));
    }


    @Test
    public void pluginsWhereFound() throws Exception {

        TextPlugin textPlugin = new TextPlugin();
        when(pluginService.getAvailablePlugins()).thenReturn(asList(new NumberPlugin(), textPlugin));

        final PluginInstance textPluginInstance = new PluginInstance("username", new ConfigurationInstanceImpl(emptyMap()), textPlugin.id());
        when(pluginService.getPluginInstancesOf(humanCoffeeNetUser.getUsername())).thenReturn(Collections.singleton(textPluginInstance));
        when(pluginService.getPlugin(textPluginInstance.getPluginId())).thenReturn(Optional.of(textPlugin));

        ResultActions result = perform(get("/"));
        result.andExpect(status().isOk());
        result.andExpect(view().name("plugins"));
        result.andExpect(model().attribute("myPlugins", hasSize(1)));
    }


    @Test
    public void ensureSavePluginWhenThereIsNoConfig() throws Exception {

        final FrontpagePlugin plugin = mock(FrontpagePlugin.class);
        when(plugin.getConfigurationDescription()).thenReturn(Optional.empty());
        when(pluginService.getPlugin("pluginId")).thenReturn(Optional.of(plugin));

        ResultActions result = perform(post("/plugins/pluginId"));
        result.andExpect(status().is3xxRedirection());
        result.andExpect(view().name("redirect:/"));

        verify(pluginService).savePluginInstance(eq(humanCoffeeNetUser.getUsername()), eq("pluginId"), any(ConfigurationInstanceImpl.class));
    }


    @Test
    public void ensureSavePluginWhenConfigIsValid() throws Exception {

        final FrontpagePlugin plugin = new NumberPlugin();
        when(pluginService.getPlugin("pluginId")).thenReturn(Optional.of(plugin));

        ResultActions result = perform(post("/plugins/pluginId"));
        result.andExpect(status().is3xxRedirection());
        result.andExpect(view().name("redirect:/"));

        verify(pluginService).savePluginInstance(eq(humanCoffeeNetUser.getUsername()), eq("pluginId"), any(ConfigurationInstanceImpl.class));
    }


    @Test
    public void ensureDoNotSavePluginWhenErrors() throws Exception {

        final FrontpagePlugin plugin = new TextPlugin();
        when(pluginService.getPlugin("pluginId")).thenReturn(Optional.of(plugin));

        ResultActions result = perform(post("/plugins/pluginId")
            .param("text.number", "NotANumber")
            .param("text.label", "text")
        );
        result.andExpect(status().is2xxSuccessful());
        result.andExpect(view().name("plugin-configuration"));
        result.andExpect(model().attribute("fields", is(notNullValue())));
        result.andExpect(model().attribute("configuration", is(notNullValue())));
        result.andExpect(model().attribute("configurationInstance", is(notNullValue())));
        result.andExpect(model().attribute("plugin", is(notNullValue())));
    }


    @Test
    public void removePluginInstance() throws Exception {

        ResultActions result = perform(delete("/plugins/pluginId/instances/pluginInstanceId"));
        result.andExpect(status().is3xxRedirection());
        result.andExpect(view().name("redirect:/"));

        verify(pluginService).removePluginInstance(humanCoffeeNetUser.getUsername(), "pluginInstanceId");
    }


    @Test
    public void pluginIsNotAvailable() throws Exception {

        when(pluginService.getPlugin("pluginId")).thenReturn(Optional.empty());

        ResultActions result = perform(get("/plugins/pluginId?configuration=true"));
        result.andExpect(status().is3xxRedirection());
        result.andExpect(view().name("redirect:/"));
    }


    @Test
    public void configurationIsNotAvailable() throws Exception {

        when(pluginService.getPlugin("pluginId")).thenReturn(Optional.of(new NumberPlugin()));

        ResultActions result = perform(get("/plugins/pluginId?configuration=true"));
        result.andExpect(status().is3xxRedirection());
        result.andExpect(view().name("redirect:/"));

        verify(pluginService).savePluginInstance("CoffeeNet", "pluginId");
    }


    @Test
    public void returnPluginConfiguration() throws Exception {

        when(pluginService.getPlugin("pluginId")).thenReturn(Optional.of(new TextPlugin()));

        ResultActions result = perform(get("/plugins/pluginId?configuration=true"));
        result.andExpect(status().is2xxSuccessful());
        result.andExpect(view().name("plugin-configuration"));
        result.andExpect(model().attribute("configuration", hasSize(2)));
        result.andExpect(model().attribute("plugin", instanceOf(PluginDto.class)));
    }


    @Test
    public void updatePluginInstanceForUser() throws Exception {

        when(pluginService.getPlugin("pluginId")).thenReturn(Optional.of(new TextPlugin()));

        ResultActions result = perform(put("/plugins/pluginId/instances/pluginInstanceId")
            .param("text.number", "0")
            .param("text.label", "text")
        );
        result.andExpect(status().is3xxRedirection());
        result.andExpect(view().name("redirect:/"));
        result.andExpect(model().attribute("fields", is(nullValue())));
        result.andExpect(model().attribute("configuration", is(nullValue())));
        result.andExpect(model().attribute("configurationInstance", is(nullValue())));
        result.andExpect(model().attribute("plugin", is(nullValue())));
        result.andExpect(model().attribute("pluginInstanceId", is(nullValue())));

        verify(pluginService).updatePluginInstance(eq("pluginInstanceId"), any(ConfigurationInstanceImpl.class));
    }


    @Test(expected = NestedServletException.class)
    public void updatePluginInstanceForUserButNoPluginAvailable() throws Exception {

        when(pluginService.getPlugin("pluginId")).thenReturn(Optional.ofNullable(null));

        perform(put("/plugins/pluginId/instances/pluginInstanceId"));
    }


    @Test
    public void updatePluginInstanceForUserValidationError() throws Exception {

        when(pluginService.getPlugin("pluginId")).thenReturn(Optional.of(new TextPlugin()));

        final ResultActions result = perform(put("/plugins/pluginId/instances/pluginInstanceId")
            .param("text.number", "NotAnumber")
            .param("text.label", "text")
        );
        result.andExpect(status().is2xxSuccessful());
        result.andExpect(view().name("plugin-configuration"));
        result.andExpect(model().attribute("fields", is(notNullValue())));
        result.andExpect(model().attribute("configuration", is(notNullValue())));
        result.andExpect(model().attribute("configurationInstance", is(notNullValue())));
        result.andExpect(model().attribute("plugin", is(notNullValue())));
        result.andExpect(model().attribute("pluginInstanceId", is("pluginInstanceId")));
    }


    @Test
    public void editPluginInstanceConfiguration() throws Exception {

        when(pluginService.getPlugin("pluginId")).thenReturn(Optional.of(new TextPlugin()));
        when(pluginService.getPluginInstance("pluginInstanceId")).thenReturn(Optional.of(new PluginInstance("username", new ConfigurationInstanceImpl(emptyMap()), "pluginId")));

        ResultActions result = perform(get("/plugins/pluginId/instances/pluginInstanceId"));
        result.andExpect(status().is2xxSuccessful());
        result.andExpect(view().name("plugin-configuration"));
        result.andExpect(model().attribute("configuration", is(notNullValue())));
        result.andExpect(model().attribute("configurationInstance", is(notNullValue())));
        result.andExpect(model().attribute("plugin", is(notNullValue())));
    }


    @Test
    public void editPluginInstanceConfigurationButNoPlugin() throws Exception {

        when(pluginService.getPlugin("pluginId")).thenReturn(Optional.ofNullable(null));
        when(pluginService.getPluginInstance("pluginInstanceId")).thenReturn(Optional.of(new PluginInstance("username", new ConfigurationInstanceImpl(emptyMap()), "pluginId")));

        ResultActions result = perform(get("/plugins/pluginId/instances/pluginInstanceId"));
        result.andExpect(status().is3xxRedirection());
        result.andExpect(view().name("redirect:/"));
    }


    @Test
    public void editPluginInstanceConfigurationButNoPluginInstance() throws Exception {

        when(pluginService.getPlugin("pluginId")).thenReturn(Optional.of(new TextPlugin()));
        when(pluginService.getPluginInstance("pluginInstanceId")).thenReturn(Optional.ofNullable(null));

        ResultActions result = perform(get("/plugins/pluginId/instances/pluginInstanceId"));
        result.andExpect(status().is3xxRedirection());
        result.andExpect(view().name("redirect:/"));
    }


    private ResultActions perform(RequestBuilder builder) throws Exception {

        return standaloneSetup(sut).build().perform(builder);
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


    private class TextPlugin implements FrontpagePlugin {

        @Override
        public String title(ConfigurationInstance configurationInstance) {

            return "There is a text";
        }


        @Override
        public String content(ConfigurationInstance configurationInstance) {

            return "Good old text";
        }

        @Override
        public String id() {
            return "Text";
        }

        @Override
        public Optional<ConfigurationDescription> getConfigurationDescription() {

            Set<ConfigurationField> fields = new HashSet<>();
            fields.add(createField("label", ConfigurationFieldType.TEXT, "text.label"));
            fields.add(createField("number", ConfigurationFieldType.NUMBER, "text.number"));

            return Optional.of(() -> fields);
        }

        private ConfigurationField createField(final String label, final ConfigurationFieldType type, final String id) {
            return new ConfigurationField() {
                @Override
                public String getLabel() {
                    return label;
                }

                @Override
                public ConfigurationFieldType getType() {
                    return type;
                }

                @Override
                public String getId() {
                    return id;
                }
            };
        }
    }
}
