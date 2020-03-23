package rocks.coffeenet.frontpage.plugin.management;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Tobias Schneider
 */
@RunWith(SpringRunner.class)
public class SimplePluginInstallerServiceIT {

    private PluginInstallerService sut;

    @Before
    public void setUp() {

        final PluginConfigurationProperties pluginConfigurationProperties = new PluginConfigurationProperties();

        sut = new SimplePluginInstallerService(new RestTemplate(), pluginConfigurationProperties);
    }

    @After
    public void tearDown() {

        deleteDirectory(new File("./plugins"));
    }


    @Test
    public void downloadPlugin() {
        String url = "https://github.com/coffeenet/coffeenet-frontpage-plugin-feed/releases/download/0.2.0/" +
            "frontpage-plugin-feed-0.2.0-jar-with-dependencies.jar";

        sut.installPlugin(url);

        File plugin = new File("./plugins/frontpage-plugin-feed-0.2.0-jar-with-dependencies.jar");

        assertThat(plugin.exists()).isTrue();
        assertThat(plugin.isFile()).isTrue();
    }

    private boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }
}
