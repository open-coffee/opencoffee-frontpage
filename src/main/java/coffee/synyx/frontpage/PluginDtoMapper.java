package coffee.synyx.frontpage;

import coffee.synyx.frontpage.plugin.api.FrontpagePlugin;

import java.util.List;

import static java.util.stream.Collectors.toList;


class PluginDtoMapper {

    private PluginDtoMapper() {

        // nothing
    }

    static PluginDto mapToPluginDto(FrontpagePlugin frontpagePlugin) {

        return new PluginDto(frontpagePlugin.id());
    }


    static List<PluginDto> mapToPluginDtos(List<FrontpagePlugin> frontpagePlugins) {

        return frontpagePlugins.stream().map(PluginDtoMapper::mapToPluginDto).collect(toList());
    }
}
