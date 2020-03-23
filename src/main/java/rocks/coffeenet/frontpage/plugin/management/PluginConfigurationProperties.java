package rocks.coffeenet.frontpage.plugin.management;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * @author Tobias Schneider
 */
@Validated
@ConfigurationProperties("frontpage.plugins")
public class PluginConfigurationProperties {

    @NotEmpty
    private String directory = "./plugins";

    private List<Resource> resource;

    @Validated
    public static class Resource {

        @NotEmpty
        private String repositoryName;

        public String getRepositoryName() {
            return repositoryName;
        }

        public void setRepositoryName(String repositoryName) {
            this.repositoryName = repositoryName;
        }
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public List<Resource> getResource() {
        return resource;
    }

    public void setResource(List<Resource> resource) {
        this.resource = resource;
    }
}
