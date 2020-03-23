package rocks.coffeenet.frontpage.plugin.management;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.String.format;
import static java.lang.invoke.MethodHandles.lookup;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author Tobias Schneider
 */
@Service
public class SimplePluginInstallerService implements PluginInstallerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(lookup().lookupClass());

    private final RestTemplate restTemplate;
    private final PluginConfigurationProperties pluginConfigurationProperties;

    @Autowired
    public SimplePluginInstallerService(RestTemplate restTemplate, PluginConfigurationProperties pluginConfigurationProperties) {

        this.restTemplate = restTemplate;
        this.pluginConfigurationProperties = pluginConfigurationProperties;
    }

    @Override
    public void installPlugin(String url) {

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(url, GET, entity, byte[].class);

        if (response.getStatusCode() == OK) {

            createPluginDirectory();

            Path pluginUri = Paths.get(pluginConfigurationProperties.getDirectory(), getPluginName(url));
            try {
                Files.write(pluginUri, response.getBody());
            } catch (IOException e) {
                throw new PluginInstallationException(format("IO Error occurred while saving the plugin to %s", pluginUri));
            }
        }
    }

    private void createPluginDirectory() {

        if (new File(pluginConfigurationProperties.getDirectory()).mkdirs()) {
            LOGGER.debug("{} was created", pluginConfigurationProperties.getDirectory());
        }
    }

    private String getPluginName(String url) {

        String path;
        try {
            path = new URI(url).getPath();
        } catch (URISyntaxException e) {
            throw new PluginInstallationException("Url of plugin is in wrong format and violates RFC 2396");
        }

        return path.substring(path.lastIndexOf('/') + 1);
    }
}
