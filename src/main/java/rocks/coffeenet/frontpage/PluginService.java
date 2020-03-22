package rocks.coffeenet.frontpage;

import rocks.coffeenet.frontpage.plugin.api.FrontpagePlugin;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Tobias Schneider
 */
public interface PluginService {

    /**
     * Returns the plugin instance with the given id
     *
     * @param pluginInstanceId of the {@link PluginInstance}
     * @return optional of {@link PluginInstance}
     */
    Optional<PluginInstance> getPluginInstance(String pluginInstanceId);


    /**
     * Returns a list of frontpage plugin instances for the given user
     *
     * @param username to filter the plugins
     * @return list of {@link PluginInstance}
     */
    Set<PluginInstance> getPluginInstancesOf(String username);


    /**
     * Adds a plugin for the given user without a configuration
     *
     * @param username to add the plugin to
     * @param pluginId of the plugin to add
     */
    void savePluginInstance(String username, String pluginId);


    /**
     * Adds a plugin for the given user
     *
     * @param username              to add the plugin to
     * @param pluginId              of the plugin to add
     * @param configurationInstance to configure the plugin
     */
    void savePluginInstance(String username, String pluginId, ConfigurationInstanceImpl configurationInstance);


    void updatePluginInstance(String pluginInstanceId, ConfigurationInstanceImpl configurationInstance);


    /**
     * Removes a plugin from the given user
     *
     * @param username         to remove the plugin from
     * @param pluginInstanceId of the plugin to remove
     */
    void removePluginInstance(String username, String pluginInstanceId);


    /**
     * Returns the plugin with the given id
     *
     * @param pluginId of the plugin to return
     * @return plugin with the given id
     */
    Optional<FrontpagePlugin> getPlugin(String pluginId);


    /**
     * Returns all available plugins
     *
     * @return list of all available plugins
     */
    List<FrontpagePlugin> getAvailablePlugins();


    /**
     * Ignores this plugin for all users
     *
     * @param pluginId to ignore
     */
    void ignorePlugin(String pluginId);
}
