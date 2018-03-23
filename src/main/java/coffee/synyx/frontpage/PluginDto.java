package coffee.synyx.frontpage;

class PluginDto {

    private final String title;
    private final String content;

    PluginDto(String title, String content) {

        this.title = title;

        this.content = content;
    }

    public String getTitle() {

        return title;
    }


    public String getContent() {

        return content;
    }
}
