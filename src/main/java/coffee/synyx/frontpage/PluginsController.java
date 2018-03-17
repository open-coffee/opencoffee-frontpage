package coffee.synyx.frontpage;

import coffee.synyx.frontpage.plugin.api.FrontpagePluginInterface;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

import static coffee.synyx.frontpage.PluginDtoMapper.mapToPluginDtos;

import static java.util.Collections.emptyList;


@Controller
public class PluginsController {

    private final PluginCollector pluginCollector;

    @Autowired
    public PluginsController(PluginCollector pluginCollector) {

        this.pluginCollector = pluginCollector;
    }

    @GetMapping("/")
    public String getPlugins(Model model) {

        final Optional<List<FrontpagePluginInterface>> frontpagePlugins = pluginCollector.getFrontpagePlugins();

        List<PluginDto> plugins = emptyList();

        if (frontpagePlugins.isPresent()) {
            plugins = mapToPluginDtos(frontpagePlugins.get());
        }

        model.addAttribute("plugins", plugins);

        return "plugins";
    }
}
