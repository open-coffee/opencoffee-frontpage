package coffee.synyx.frontpage;

class PluginDto {

    private final String title;
    private final String content;
    private final String id;

    PluginDto(String title, String content, String id) {

        this.title = title;

        this.content = content;
        this.id = id;
    }

    public String getTitle() {

        return title;
    }


    public String getContent() {

        return content;
    }


    public String getId() {
        return id;
    }
}
