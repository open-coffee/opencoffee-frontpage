package rocks.coffeenet.frontpage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import rocks.coffeenet.frontpage.plugin.api.FrontpagePlugin;

import java.util.List;

import static java.lang.invoke.MethodHandles.lookup;
import static java.util.stream.Collectors.groupingBy;

/**
 * @author Tobias Schneider
 */
@Component
class PluginValidator implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(lookup().lookupClass());

    private final PluginService pluginService;

    @Autowired
    PluginValidator(PluginService pluginService) {

        this.pluginService = pluginService;
    }

    public void onApplicationEvent(ContextRefreshedEvent event) {

        List<FrontpagePlugin> availablePlugins = pluginService.getAvailablePlugins();
        LOGGER.debug("/> {} plugins are generally available: {}", pluginService.getAvailablePlugins().size(), availablePlugins.toArray());

        ignorePluginsWithDuplicatedIds(availablePlugins);
    }


    private void ignorePluginsWithDuplicatedIds(List<FrontpagePlugin> availablePlugins) {
        availablePlugins.stream()
            .collect(groupingBy(FrontpagePlugin::id)).values().stream()
            .filter(pluginsWithSameId -> pluginsWithSameId.size() > 1)
            .forEach(pluginWithSameIds -> {

                String pluginIdToIgnore = pluginWithSameIds.get(0).id();

                LOGGER.warn("/> There are {} plugins with the same pluginIdToIgnore '{}' that will be ignored", pluginWithSameIds.size(), pluginIdToIgnore);

                pluginService.ignorePlugin(pluginIdToIgnore);
            });
    }
}
