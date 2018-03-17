package coffee.synyx.frontpage;

import coffee.synyx.frontpage.plugin.api.FrontpagePluginInterface;

import java.util.List;
import java.util.Optional;


public interface PluginCollector {

    Optional<List<FrontpagePluginInterface>> getFrontpagePlugins();
}
