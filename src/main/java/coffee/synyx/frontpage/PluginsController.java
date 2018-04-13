package coffee.synyx.frontpage;

import coffee.synyx.autoconfigure.security.service.CoffeeNetCurrentUserService;
import coffee.synyx.autoconfigure.security.service.CoffeeNetUserDetails;
import coffee.synyx.frontpage.plugin.api.ConfigurationDescription;
import coffee.synyx.frontpage.plugin.api.FrontpagePlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static coffee.synyx.frontpage.PluginDtoMapper.mapToPluginDto;
import static coffee.synyx.frontpage.PluginDtoMapper.mapToPluginDtos;
import static coffee.synyx.frontpage.PluginInstanceDtoMapper.mapToPluginInstanceDtos;


@Controller
public class PluginsController {

    private final PluginService pluginService;
    private final CoffeeNetCurrentUserService coffeeNetCurrentUserService;

    @Autowired
    public PluginsController(PluginService pluginService, CoffeeNetCurrentUserService coffeeNetCurrentUserService) {

        this.pluginService = pluginService;
        this.coffeeNetCurrentUserService = coffeeNetCurrentUserService;
    }

    @GetMapping("/")
    public String getPlugins(Model model) {

        final Set<PluginInstance> myPlugins = pluginService.getPluginInstancesOf(getUsername());
        model.addAttribute("myPlugins", mapToPluginInstanceDtos(myPlugins));

        final List<FrontpagePlugin> plugins = pluginService.getAvailablePlugins();
        model.addAttribute("availablePlugins", mapToPluginDtos(plugins));

        return "plugins";
    }

    @GetMapping(value = "/plugins/{pluginId}", params = "configuration")
    public String showPluginConfiguration(@PathVariable String pluginId, Model model) {

        final Optional<FrontpagePlugin> pluginOptional = pluginService.getPlugin(pluginId);

        if (pluginOptional.isPresent()) {

            final FrontpagePlugin plugin = pluginOptional.get();
            final Optional<ConfigurationDescription> configDescription = plugin.getConfigurationDescription();

            if (configDescription.isPresent()) {

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
    public String savePluginInstanceForUser(@PathVariable String pluginId, @RequestParam Map<String, String> params) {

        pluginService.savePluginInstance(getUsername(), pluginId, new ConfigurationInstanceImpl(params));

        return "redirect:/";
    }

    @DeleteMapping("/plugins/{pluginInstanceId}")
    public String removePluginForUser(@PathVariable String pluginInstanceId) {

        pluginService.removePluginInstance(getUsername(), pluginInstanceId);

        return "redirect:/";
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
