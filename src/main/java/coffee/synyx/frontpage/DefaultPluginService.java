package coffee.synyx.frontpage;

import coffee.synyx.frontpage.plugin.api.FrontpagePluginInterface;
import coffee.synyx.frontpage.plugin.api.FrontpagePluginQualifier;
import io.netty.util.internal.ConcurrentSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.invoke.MethodHandles.lookup;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;

/**
 * @author Tobias Schneider
 */
@Service
public class DefaultPluginService implements PluginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(lookup().lookupClass());

    private final ConcurrentHashMap<String, Set<String>> pluginsStore = new ConcurrentHashMap<>();
    private final ConcurrentSet<String> ignoredPlugins = new ConcurrentSet<>();

    private final PluginRegistry<FrontpagePluginInterface, FrontpagePluginQualifier> pluginRegistry;

    @Autowired
    public DefaultPluginService(PluginRegistry<FrontpagePluginInterface, FrontpagePluginQualifier> pluginRegistry) {
        this.pluginRegistry = pluginRegistry;
    }

    @Override
    public List<FrontpagePluginInterface> getPluginsOf(String username) {
        Set<String> userPluginIds = pluginsStore.getOrDefault(username, emptySet());

        return this.getAvailablePlugins().stream()
            .filter(plugin -> userPluginIds.contains(plugin.id()))
            .collect(toList());
    }

    @Override
    public void addPlugin(String pluginId, String username) {

        Set<String> usersPlugins = pluginsStore.getOrDefault(username, new HashSet<>());
        if (!usersPlugins.contains(pluginId)) {

            usersPlugins.add(pluginId);
            pluginsStore.put(username, usersPlugins);

            LOGGER.info("Added {} to the frontpage of {}", pluginId, username);
        }
    }

    @Override
    public void removePlugin(String pluginId, String username) {

        Set<String> usersPlugins = pluginsStore.getOrDefault(username, new HashSet<>());
        if (!usersPlugins.isEmpty()) {
            usersPlugins.remove(pluginId);

            LOGGER.info("Removed {} from the frontpage of {}", pluginId, username);
        }
    }

    @Override
    public List<FrontpagePluginInterface> getAvailablePlugins() {

        List<FrontpagePluginInterface> availablePlugins = this.pluginRegistry.getPlugins().stream()
            .filter(plugin -> !ignoredPlugins.contains(plugin.id()))
            .collect(toList());

        LOGGER.debug("/> {} plugins are available", availablePlugins.size());

        return availablePlugins;
    }

    @Override
    public void ignorePlugin(String pluginId) {

        ignoredPlugins.add(pluginId);

        LOGGER.warn("/> Plugins with the ids '{}' are ignored now", pluginId);
    }
}
