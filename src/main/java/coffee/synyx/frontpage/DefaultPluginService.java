package coffee.synyx.frontpage;

import coffee.synyx.frontpage.plugin.api.FrontpagePluginInterface;
import coffee.synyx.frontpage.plugin.api.FrontpagePluginQualifier;
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

    private final PluginRegistry<FrontpagePluginInterface, FrontpagePluginQualifier> pluginRegistry;

    @Autowired
    public DefaultPluginService(PluginRegistry<FrontpagePluginInterface, FrontpagePluginQualifier> pluginRegistry) {
        this.pluginRegistry = pluginRegistry;
    }

    @Override
    public List<FrontpagePluginInterface> getPluginsOf(String username) {
        Set<String> userPluginIds = pluginsStore.getOrDefault(username, emptySet());

        return pluginRegistry.getPlugins().stream()
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

        return pluginRegistry.getPlugins();
    }
}
