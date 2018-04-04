package coffee.synyx.frontpage;

import coffee.synyx.frontpage.plugin.api.FrontpagePluginInterface;

import java.util.List;

/**
 * @author Tobias Schneider
 */
public interface PluginService {

    /**
     * Returns a list of frontpage plugins for the given user
     *
     * @param username to filter the plugins
     * @return list of plugins
     */
    List<FrontpagePluginInterface> getPluginsOf(String username);


    /**
     * Adds a plugin for the given user
     *
     * @param pluginId of the plugin to add
     * @param username to add the plugin to
     */
    void addPlugin(String pluginId, String username);


    /**
     * Removes a plugin from the given user
     *
     * @param pluginId of the plugin to remove
     * @param username to remove the plugin from
     */
    void removePlugin(String pluginId, String username);


    /**
     * Returns all available plugins
     *
     * @return list of all available plugins
     */
    List<FrontpagePluginInterface> getAvailablePlugins();
}
