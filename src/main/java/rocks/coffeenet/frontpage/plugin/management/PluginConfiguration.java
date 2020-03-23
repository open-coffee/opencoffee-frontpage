package rocks.coffeenet.frontpage.plugin.management;

import org.kohsuke.github.GitHubBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author Tobias Schneider
 */
@Configuration
public class PluginConfiguration {

    @Bean
    PluginConfigurationProperties pluginConfigurationProperties() {

        return new PluginConfigurationProperties();
    }

    @Bean
    GitHubBuilder gitHubBuilder() {

        return new GitHubBuilder();
    }

    @Bean
    RestTemplate restTemplate(){

        return new RestTemplate();
    }
}
