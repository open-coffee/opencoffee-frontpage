package rocks.coffeenet.frontpage.plugin.management;

import java.util.List;

/**
 * @author Tobias Schneider
 */
public class AvailablePlugins {

    private final String name;
    private final List<AvailablePluginAssets> versions;

    public AvailablePlugins(String name, List<AvailablePluginAssets> versions) {
        this.name = name;
        this.versions = versions;
    }

    public String getName() {
        return name;
    }

    public List<AvailablePluginAssets> getVersions() {
        return versions;
    }
}
