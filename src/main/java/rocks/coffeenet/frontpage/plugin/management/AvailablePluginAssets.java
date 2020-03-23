package rocks.coffeenet.frontpage.plugin.management;

/**
 * @author Tobias Schneider
 */
class AvailablePluginAssets {

    private final String name;
    private final String url;

    AvailablePluginAssets(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
