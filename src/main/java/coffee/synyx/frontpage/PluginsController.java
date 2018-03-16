package coffee.synyx.frontpage;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;

import static coffee.synyx.frontpage.PluginDtoMapper.mapToPluginDtos;


@Controller
public class PluginsController {

    private final PluginCollector pluginCollector;

    @Autowired
    public PluginsController(PluginCollector pluginCollector) {

        this.pluginCollector = pluginCollector;
    }

    @GetMapping("/")
    public String getPlugins(Model model) {

        model.addAttribute("plugins", mapToPluginDtos(pluginCollector.getFrontpagePlugins()));

        return "plugins";
    }
}
