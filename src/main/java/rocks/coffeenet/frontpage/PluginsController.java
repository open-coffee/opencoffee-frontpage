package rocks.coffeenet.frontpage;

import rocks.coffeenet.autoconfigure.security.service.CoffeeNetCurrentUserService;
import rocks.coffeenet.autoconfigure.security.service.CoffeeNetUserDetails;
import rocks.coffeenet.frontpage.plugin.api.ConfigurationDescription;
import rocks.coffeenet.frontpage.plugin.api.FrontpagePlugin;
import rocks.coffeenet.frontpage.validation.ConfigurationInstanceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static rocks.coffeenet.frontpage.PluginDtoMapper.mapToPluginDto;
import static rocks.coffeenet.frontpage.PluginDtoMapper.mapToPluginDtos;
import static java.lang.String.format;
import static java.util.Collections.emptyMap;


@Controller
public class PluginsController {

    private final PluginService pluginService;
    private final CoffeeNetCurrentUserService coffeeNetCurrentUserService;
    private final ConfigurationInstanceValidator configurationInstanceValidator;

    @Autowired
    public PluginsController(PluginService pluginService,
                             CoffeeNetCurrentUserService coffeeNetCurrentUserService,
                             ConfigurationInstanceValidator configurationInstanceValidator) {

        this.pluginService = pluginService;
        this.coffeeNetCurrentUserService = coffeeNetCurrentUserService;
        this.configurationInstanceValidator = configurationInstanceValidator;
    }

    @GetMapping("/")
    public String getPlugins(Model model) {

        final Set<PluginInstance> myPlugins = pluginService.getPluginInstancesOf(getUsername());
        model.addAttribute("myPlugins", activateMyPlugins(myPlugins));

        return "plugins";
    }

    @GetMapping("/add")
    public String addPluginPage(Model model) {

        final List<FrontpagePlugin> plugins = pluginService.getAvailablePlugins();
        model.addAttribute("availablePlugins", mapToPluginDtos(plugins));

        return "plugins-add";
    }

    @GetMapping(value = "/plugins/{pluginId}", params = "configuration")
    public String showPluginInstanceConfiguration(@PathVariable String pluginId, Model model) {

        final Optional<FrontpagePlugin> pluginOptional = pluginService.getPlugin(pluginId);

        if (pluginOptional.isPresent()) {

            final FrontpagePlugin plugin = pluginOptional.get();
            final Optional<ConfigurationDescription> configDescription = plugin.getConfigurationDescription();

            if (configDescription.isPresent()) {

                MapBindingResult errors = new MapBindingResult(emptyMap(), plugin.id());
                model.addAttribute("fields", errors);

                model.addAttribute("configuration", configDescription.get().getConfigurations());
                model.addAttribute("plugin", mapToPluginDto(plugin));

                return "plugin-configuration";
            } else {
                pluginService.savePluginInstance(getUsername(), pluginId);
            }
        }

        return "redirect:/";
    }

    @PostMapping("/plugins/{pluginId}")
    public String createPluginInstanceForUser(@PathVariable String pluginId, @RequestParam Map<String, String> params, Model model) {

        final FrontpagePlugin plugin = getPlugin(pluginId);
        final Optional<ConfigurationDescription> configurationDescriptionOptional = plugin.getConfigurationDescription();

        if (configurationDescriptionOptional.isPresent()) {

            ConfigurationDescription description = configurationDescriptionOptional.get();
            MapBindingResult errors = validateFieldValues(params, plugin, description);

            if (errors.hasErrors()) {
                model.addAttribute("fields", errors);
                model.addAttribute("configuration", description.getConfigurations());
                model.addAttribute("configurationInstance", new ConfigurationInstanceImpl(params));
                model.addAttribute("plugin", mapToPluginDto(plugin));

                return "plugin-configuration";
            }
        }

        pluginService.savePluginInstance(getUsername(), pluginId, new ConfigurationInstanceImpl(params));
        return "redirect:/";
    }

    @PutMapping("/plugins/{pluginId}/instances/{pluginInstanceId}")
    public String updatePluginInstanceForUser(@PathVariable String pluginId, @PathVariable String pluginInstanceId, @RequestParam Map<String, String> params, Model model) {

        final FrontpagePlugin plugin = getPlugin(pluginId);
        final Optional<ConfigurationDescription> configurationDescriptionOptional = plugin.getConfigurationDescription();

        if (configurationDescriptionOptional.isPresent()) {
            ConfigurationDescription description = configurationDescriptionOptional.get();
            MapBindingResult errors = validateFieldValues(params, plugin, description);

            if (errors.hasErrors()) {
                model.addAttribute("fields", errors);
                model.addAttribute("configuration", description.getConfigurations());
                model.addAttribute("configurationInstance", new ConfigurationInstanceImpl(params));
                model.addAttribute("plugin", mapToPluginDto(plugin));
                model.addAttribute("pluginInstanceId", pluginInstanceId);

                return "plugin-configuration";
            }
        }

        pluginService.updatePluginInstance(pluginInstanceId, new ConfigurationInstanceImpl(params));
        return "redirect:/";
    }

    @DeleteMapping("/plugins/{pluginId}/instances/{pluginInstanceId}")
    public String removePluginInstanceOfUser(@PathVariable String pluginInstanceId) {

        pluginService.removePluginInstance(getUsername(), pluginInstanceId);

        return "redirect:/";
    }

    @GetMapping("/plugins/{pluginId}/instances/{pluginInstanceId}")
    public String editPluginInstanceConfiguration(@PathVariable String pluginId, @PathVariable String pluginInstanceId, Model model) {

        final Optional<FrontpagePlugin> pluginOptional = pluginService.getPlugin(pluginId);
        final Optional<PluginInstance> pluginInstanceOptional = pluginService.getPluginInstance(pluginInstanceId);

        if (pluginOptional.isPresent() && pluginInstanceOptional.isPresent()) {

            final FrontpagePlugin plugin = pluginOptional.get();
            final Optional<ConfigurationDescription> configDescription = plugin.getConfigurationDescription();

            if (configDescription.isPresent()) {

                MapBindingResult errors = new MapBindingResult(emptyMap(), plugin.id());
                model.addAttribute("fields", errors);

                model.addAttribute("configuration", configDescription.get().getConfigurations());
                model.addAttribute("configurationInstance", pluginInstanceOptional.get().getConfigurationInstance());
                model.addAttribute("plugin", mapToPluginDto(plugin));

                return "plugin-configuration";
            }
        }

        return "redirect:/";
    }

    private MapBindingResult validateFieldValues(@RequestParam Map<String, String> params, FrontpagePlugin plugin, ConfigurationDescription description) {

        MapBindingResult errors = new MapBindingResult(params, plugin.id());
        configurationInstanceValidator.validate(new ConfigurationInstanceImpl(params), description, errors);
        return errors;
    }

    private FrontpagePlugin getPlugin(@PathVariable String pluginId) {

        Optional<FrontpagePlugin> pluginOptional = pluginService.getPlugin(pluginId);
        if (!pluginOptional.isPresent()) {
            throw new IllegalArgumentException(format("plugin %s does not exist.", pluginId));
        }
        return pluginOptional.get();
    }

    private List<PluginInstanceDto> activateMyPlugins(Set<PluginInstance> myPlugins) {

        List<PluginInstanceDto> pluginInstanceDtos = new ArrayList<>();

        for (PluginInstance myPluginInstance : myPlugins) {
            final Optional<FrontpagePlugin> optionalPlugin = pluginService.getPlugin(myPluginInstance.getPluginId());
            optionalPlugin.ifPresent(plugin -> pluginInstanceDtos.add(PluginInstanceDtoMapper.mapToPluginInstanceDto(myPluginInstance, plugin)));
        }

        return pluginInstanceDtos;
    }

    private String getUsername() {

        String username = "";
        Optional<CoffeeNetUserDetails> coffeeNetUserDetails = coffeeNetCurrentUserService.get();

        if (coffeeNetUserDetails.isPresent()) {
            username = coffeeNetUserDetails.get().getUsername();
        }
        return username;
    }
}
