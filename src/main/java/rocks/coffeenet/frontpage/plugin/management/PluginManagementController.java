package rocks.coffeenet.frontpage.plugin.management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Controller
public class PluginManagementController {

    private final PluginManagementService pluginManagementService;
    private final PluginInstallerService pluginInstallerService;

    @Autowired
    public PluginManagementController(PluginManagementService pluginManagementService, PluginInstallerService pluginInstallerService) {

        this.pluginManagementService = pluginManagementService;
        this.pluginInstallerService = pluginInstallerService;
    }

    @GetMapping("/plugins-install")
    public String getPlugins(Model model) {

        List<AvailablePlugins> availablePlugins = emptyList();

        Optional<List<AvailablePlugins>> listOfAvailablePlugins = pluginManagementService.getListOfAvailablePlugins();
        if (listOfAvailablePlugins.isPresent()) {
            availablePlugins = listOfAvailablePlugins.get();
        }

        model.addAttribute("availablePlugins", availablePlugins);
        return "management/plugins";
    }

    @PutMapping("/plugins-install")
    public String installPlugin(@RequestParam String url) {

        pluginInstallerService.installPlugin(url);

        return "redirect:/plugins-install";
    }
}
