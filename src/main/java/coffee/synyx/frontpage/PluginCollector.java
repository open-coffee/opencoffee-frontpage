package coffee.synyx.frontpage;

import coffee.synyx.frontpage.plugin.api.FrontpagePluginInterface;

import java.util.List;


public interface PluginCollector {

    List<FrontpagePluginInterface> getFrontpagePlugins();
}
