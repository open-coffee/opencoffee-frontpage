package coffee.synyx.frontpage;

import coffee.synyx.frontpage.plugin.api.FrontpagePluginInterface;

import java.util.List;

import static java.util.stream.Collectors.toList;


class PluginDtoMapper {

    private PluginDtoMapper() {

        // nothing
    }

    private static PluginDto mapToPluginDto(FrontpagePluginInterface frontpagePluginInterface) {

        return new PluginDto(frontpagePluginInterface.title(), frontpagePluginInterface.content(), frontpagePluginInterface.id());
    }


    static List<PluginDto> mapToPluginDtos(List<FrontpagePluginInterface> frontpagePluginInterfaces) {

        return frontpagePluginInterfaces.stream().map(PluginDtoMapper::mapToPluginDto).collect(toList());
    }
}
