package coffee.synyx.frontpage;

import coffee.synyx.autoconfigure.security.service.CoffeeNetCurrentUserService;
import coffee.synyx.autoconfigure.security.service.CoffeeNetUserDetails;
import coffee.synyx.frontpage.plugin.api.ConfigurationDescription;
import coffee.synyx.frontpage.plugin.api.FrontpagePlugin;
import coffee.synyx.frontpage.validation.ConfigurationInstanceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static coffee.synyx.frontpage.PluginDtoMapper.mapToPluginDto;
import static coffee.synyx.frontpage.PluginDtoMapper.mapToPluginDtos;
import static coffee.synyx.frontpage.PluginInstanceDtoMapper.mapToPluginInstanceDto;
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
    public String showPluginConfiguration(@PathVariable String pluginId, Model model) {

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
    public String savePluginInstanceForUser(@PathVariable String pluginId, @RequestParam Map<String, String> params, Model model) {
        final ConfigurationInstanceImpl configurationInstance = new ConfigurationInstanceImpl(params);

        Optional<FrontpagePlugin> pluginOptional = pluginService.getPlugin(pluginId);
        if (!pluginOptional.isPresent()) {
            throw new IllegalArgumentException(String.format("plugin {} does not exist.", pluginId));
        }

        final FrontpagePlugin plugin = pluginOptional.get();
        Optional<ConfigurationDescription> configurationDescriptionOptional = plugin.getConfigurationDescription();

        if (configurationDescriptionOptional.isPresent()) {

            ConfigurationDescription description = configurationDescriptionOptional.get();

            MapBindingResult errors = new MapBindingResult(params, plugin.id());
            configurationInstanceValidator.validate(configurationInstance, description, errors);

            if (errors.hasErrors()) {
                model.addAttribute("fields", errors);
                model.addAttribute("configuration", description.getConfigurations());
                model.addAttribute("plugin", mapToPluginDto(plugin));
                return "plugin-configuration";
            }
        }

        pluginService.savePluginInstance(getUsername(), pluginId, configurationInstance);
        return "redirect:/";
    }

    @DeleteMapping("/plugins/{pluginInstanceId}")
    public String removePluginForUser(@PathVariable String pluginInstanceId) {

        pluginService.removePluginInstance(getUsername(), pluginInstanceId);

        return "redirect:/";
    }

    private List<PluginInstanceDto> activateMyPlugins(Set<PluginInstance> myPlugins) {

        List<PluginInstanceDto> pluginInstanceDtos = new ArrayList<>();

        for (PluginInstance myPluginInstance : myPlugins) {
            final Optional<FrontpagePlugin> optionalPlugin = pluginService.getPlugin(myPluginInstance.getPluginId());
            optionalPlugin.ifPresent(plugin -> pluginInstanceDtos.add(mapToPluginInstanceDto(myPluginInstance, plugin)));
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
