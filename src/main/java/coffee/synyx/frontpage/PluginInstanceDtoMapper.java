package coffee.synyx.frontpage;

import coffee.synyx.frontpage.plugin.api.FrontpagePlugin;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;


class PluginInstanceDtoMapper {

    private PluginInstanceDtoMapper() {

        // nothing
    }

    private static PluginInstanceDto mapToPluginInstanceDto(PluginInstance pluginInstance) {

        final ConfigurationInstanceImpl configurationInstance = pluginInstance.getConfigurationInstance();
        final FrontpagePlugin plugin = pluginInstance.getPlugin();

        return new PluginInstanceDto(pluginInstance.getId().toString(), plugin.title(configurationInstance), plugin.content(configurationInstance));

    }


    static List<PluginInstanceDto> mapToPluginInstanceDtos(Set<PluginInstance> pluginInstances) {

        return pluginInstances.stream().map(PluginInstanceDtoMapper::mapToPluginInstanceDto).collect(toList());
    }
}
