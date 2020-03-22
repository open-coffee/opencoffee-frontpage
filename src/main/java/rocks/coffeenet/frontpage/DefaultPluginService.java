package rocks.coffeenet.frontpage;

import io.netty.util.internal.ConcurrentSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.stereotype.Service;
import rocks.coffeenet.frontpage.plugin.api.FrontpagePlugin;
import rocks.coffeenet.frontpage.plugin.api.FrontpagePluginQualifier;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static java.lang.invoke.MethodHandles.lookup;
import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * @author Tobias Schneider
 */
@Service
public class DefaultPluginService implements PluginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(lookup().lookupClass());

    private final ConcurrentSet<String> ignoredPlugins = new ConcurrentSet<>();
    private final PluginRegistry<FrontpagePlugin, FrontpagePluginQualifier> pluginRegistry;
    private final PluginRepository pluginRepository;

    @Autowired
    public DefaultPluginService(PluginRegistry<FrontpagePlugin, FrontpagePluginQualifier> pluginRegistry, PluginRepository pluginRepository) {

        this.pluginRegistry = pluginRegistry;
        this.pluginRepository = pluginRepository;
    }

    @Override
    public Optional<PluginInstance> getPluginInstance(String pluginInstanceId) {

        return Optional.ofNullable(pluginRepository.findById(UUID.fromString(pluginInstanceId)));
    }


    @Override
    public Set<PluginInstance> getPluginInstancesOf(String username) {

        return pluginRepository.findAllByUsername(username).stream()
            .filter(pluginInstance -> !ignoredPlugins.contains(pluginInstance.getPluginId()))
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
            PluginInstance pluginInstance = new PluginInstance(username, configurationInstance, plugin.get().id());
            pluginRepository.save(pluginInstance);

            LOGGER.info("Saved {} with configuration {} for {}", pluginId, configurationInstance, username);
        } else {
            LOGGER.warn("Plugin {} does not exists, but {} tries to add it", pluginId, username);
        }
    }

    @Override
    public void updatePluginInstance(String pluginInstanceId, ConfigurationInstanceImpl configurationInstance) {
        final Optional<PluginInstance> pluginInstance = getPluginInstance(pluginInstanceId);

        if (pluginInstance.isPresent()) {
            PluginInstance newPluginInstance = new PluginInstance(
                pluginInstance.get().getId(),
                pluginInstance.get().getUsername(),
                new ConfigurationInstanceImpl(configurationInstance.getParams()),
                pluginInstance.get().getPluginId()
            );

            pluginRepository.save(newPluginInstance);

            LOGGER.info("Updated {} pluginInstance with {} new configuration ", pluginInstance.get(), configurationInstance);
        } else {
            LOGGER.warn("PluginInstance {} does not exists", pluginInstanceId);
        }
    }

    @Override
    public void removePluginInstance(String username, String pluginInstanceId) {

        pluginRepository.deleteById(UUID.fromString(pluginInstanceId));
        LOGGER.info("Removed {} for {}", pluginInstanceId, username);
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
