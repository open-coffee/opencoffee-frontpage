package coffee.synyx.frontpage;

import coffee.synyx.autoconfigure.security.service.CoffeeNetCurrentUserService;
import coffee.synyx.autoconfigure.security.service.CoffeeNetUserDetails;
import coffee.synyx.frontpage.plugin.api.FrontpagePluginInterface;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.Optional;

import static coffee.synyx.frontpage.PluginDtoMapper.mapToPluginDtos;


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

        String username = getUsername();

        final List<FrontpagePluginInterface> userPlugins = pluginService.getPluginsOf(username);
        model.addAttribute("userPlugins", mapToPluginDtos(userPlugins));

        final List<FrontpagePluginInterface> plugins = pluginService.getAvailablePlugins();
        model.addAttribute("plugins", mapToPluginDtos(plugins));

        return "plugins";
    }

    @PutMapping("/plugins/{id}")
    public String addPlugins(@PathVariable String id) {

        pluginService.addPlugin(id, getUsername());

        return "redirect:/";
    }

    @DeleteMapping("/plugins/{id}")
    public String removePlugins(@PathVariable String id) {

        pluginService.removePlugin(id, getUsername());

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
