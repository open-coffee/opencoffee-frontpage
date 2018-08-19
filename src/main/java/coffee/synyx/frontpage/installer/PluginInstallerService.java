package coffee.synyx.frontpage.installer;

/**
 * @author Tobias Schneider
 */
public interface PluginInstallerService {

    /**
     * Installs a plugin into the defined directory of `frontpage.plugins.directory`
     *
     * @param url of the plugin to download and install
     *
     * @throws PluginInstallationException if an error occurred
     */
    void installPlugin(String url);
}
