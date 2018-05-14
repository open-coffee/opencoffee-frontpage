package coffee.synyx.frontpage;

import coffee.synyx.frontpage.plugin.api.FrontpagePlugin;

class PluginInstanceDtoMapper {

    private PluginInstanceDtoMapper() {
        // ok
    }

    static PluginInstanceDto mapToPluginInstanceDto(PluginInstance pluginInstance, FrontpagePlugin plugin) {

        final ConfigurationInstanceImpl configurationInstance = pluginInstance.getConfigurationInstance();

        return new PluginInstanceDto(
            pluginInstance.getId().toString(),
            plugin.title(configurationInstance),
            plugin.content(configurationInstance),
            plugin.id()
        );
    }
}
