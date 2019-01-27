package rocks.coffeenet.frontpage;

class PluginInstanceDto {

    private final String uuid;
    private final String title;
    private final String content;
    private final String pluginId;

    PluginInstanceDto(String uuid, String title, String content, String pluginId) {
        this.uuid = uuid;
        this.title = title;
        this.content = content;
        this.pluginId = pluginId;
    }

    public String getUuid() {
        return uuid;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getPluginId() {
        return pluginId;
    }

    @Override
    public String toString() {
        return "PluginInstanceDto{" +
            "uuid='" + uuid + '\'' +
            ", title='" + title + '\'' +
            ", content='" + content + '\'' +
            ", pluginId='" + pluginId + '\'' +
            '}';
    }
}
