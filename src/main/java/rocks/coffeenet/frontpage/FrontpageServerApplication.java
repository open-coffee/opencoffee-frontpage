package rocks.coffeenet.frontpage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.plugin.core.config.EnablePluginRegistries;
import rocks.coffeenet.frontpage.plugin.api.FrontpagePlugin;


/**
 * The CoffeeNet Frontpage Server.
 *
 * @author Tobias Schneider - schneider@synyx.de
 */
@SpringBootApplication
@EnablePluginRegistries(FrontpagePlugin.class)
public class FrontpageServerApplication {

    public static void main(String[] args) {

        SpringApplication.run(FrontpageServerApplication.class, args);
    }
}
