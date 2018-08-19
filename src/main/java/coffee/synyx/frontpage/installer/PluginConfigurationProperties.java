package coffee.synyx.frontpage.installer;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * @author Tobias Schneider
 */
@Validated
@ConfigurationProperties("frontpage.plugins")
public class PluginConfigurationProperties {

    @NotEmpty
    private String directory = "./plugins";

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }
}
