package coffee.synyx.frontpage;

import coffee.synyx.frontpage.plugin.api.FrontpagePlugin;
import coffee.synyx.frontpage.plugin.api.FrontpagePluginQualifier;
import io.netty.util.internal.ConcurrentSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.invoke.MethodHandles.lookup;
import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * @author Tobias Schneider
 */
@Service
public class DefaultPluginService implements PluginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(lookup().lookupClass());

    private final ConcurrentHashMap<String, Set<PluginInstance>> pluginsStore = new ConcurrentHashMap<>();
    private final ConcurrentSet<String> ignoredPlugins = new ConcurrentSet<>();

    private final PluginRegistry<FrontpagePlugin, FrontpagePluginQualifier> pluginRegistry;

    @Autowired
    public DefaultPluginService(PluginRegistry<FrontpagePlugin, FrontpagePluginQualifier> pluginRegistry) {

        this.pluginRegistry = pluginRegistry;
    }

    @Override
    public Set<PluginInstance> getPluginInstancesOf(String username) {

        return pluginsStore.getOrDefault(username, emptySet()).stream()
            .filter(pluginInstance -> !ignoredPlugins.contains(pluginInstance.getPlugin().id()))
            .collect(toSet());

    }

    @Override
    public void savePluginInstance(String username, String pluginId) {

        savePluginInstance(username, pluginId, new ConfigurationInstanceImpl(emptyMap()));
    }

    @Override
    public void savePluginInstance(String username, String pluginId, ConfigurationInstanceImpl configurationInstance) {
        final Optional<FrontpagePlugin> plugin = getPlugin(pluginId);

        if (plugin.isPresent()) {

            Set<PluginInstance> usersPlugins = pluginsStore.getOrDefault(username, new HashSet<>());
            PluginInstance pluginInstance = new PluginInstance(configurationInstance, plugin.get());
            if (!usersPlugins.contains(pluginInstance)) {

                usersPlugins.add(pluginInstance);
                pluginsStore.put(username, usersPlugins);

                LOGGER.info("Saved {} with configuration {} for {}", pluginId, configurationInstance, username);
            }
        } else {
            LOGGER.warn("Plugin {} does not exists, but {} tries to add it", pluginId, username);
        }
    }

    @Override
    public void removePluginInstance(String username, String pluginInstanceId) {

        Set<PluginInstance> usersPlugins = pluginsStore.getOrDefault(username, new HashSet<>());
        if (!usersPlugins.isEmpty()) {

            final Optional<PluginInstance> first = usersPlugins.stream()
                .filter(pluginInstance -> pluginInstance.getId().toString().equals(pluginInstanceId))
                .findFirst();

            first.ifPresent(usersPlugins::remove);

            LOGGER.info("Removed {} for {}", pluginInstanceId, username);
        }
    }

    @Override
    public Optional<FrontpagePlugin> getPlugin(String pluginId) {

        return getAvailablePlugins().stream().filter(plugin -> plugin.id().equals(pluginId)).findFirst();
    }

    @Override
    public List<FrontpagePlugin> getAvailablePlugins() {

        List<FrontpagePlugin> availablePlugins = this.pluginRegistry.getPlugins().stream()
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
