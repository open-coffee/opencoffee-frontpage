package coffee.synyx.frontpage.installer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Tobias Schneider
 */
@Configuration
public class PluginConfiguration {

    @Bean
    PluginConfigurationProperties pluginConfigurationProperties() {

        return new PluginConfigurationProperties();
    }
}
