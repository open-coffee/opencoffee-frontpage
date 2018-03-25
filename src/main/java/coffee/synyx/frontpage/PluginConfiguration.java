package coffee.synyx.frontpage;

import coffee.synyx.frontpage.plugin.api.FrontpagePluginInterface;

import org.springframework.context.annotation.Configuration;

import org.springframework.plugin.core.config.EnablePluginRegistries;


@Configuration
@EnablePluginRegistries(FrontpagePluginInterface.class)
class PluginConfiguration {
}
