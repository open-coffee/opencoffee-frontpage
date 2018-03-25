package coffee.synyx.frontpage;

import coffee.synyx.frontpage.plugin.api.FrontpagePluginInterface;
import coffee.synyx.frontpage.plugin.api.FrontpagePluginQualifier;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.plugin.core.PluginRegistry;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DefaultPluginCollector implements PluginCollector {

    private final PluginRegistry<FrontpagePluginInterface, FrontpagePluginQualifier> pluginRegistry;

    @Autowired
    public DefaultPluginCollector(PluginRegistry<FrontpagePluginInterface, FrontpagePluginQualifier> pluginRegistry) {

        this.pluginRegistry = pluginRegistry;
    }

    @Override
    public List<FrontpagePluginInterface> getFrontpagePlugins() {

        return pluginRegistry.getPlugins();
    }
}
