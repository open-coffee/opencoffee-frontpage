package coffee.synyx.frontpage;

class PluginInstanceDto {

    private final String uuid;
    private final String title;
    private final String content;

    PluginInstanceDto(String uuid, String title, String content) {
        this.uuid = uuid;
        this.title = title;
        this.content = content;
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

    @Override
    public String toString() {
        return "PluginInstanceDto{" +
            "uuid='" + uuid + '\'' +
            ", title='" + title + '\'' +
            ", content='" + content + '\'' +
            '}';
    }
}
