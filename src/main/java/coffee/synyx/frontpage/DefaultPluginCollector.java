package coffee.synyx.frontpage;

import coffee.synyx.frontpage.plugin.api.FrontpagePluginInterface;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DefaultPluginCollector implements PluginCollector {

    private final List<FrontpagePluginInterface> frontpagePluginInterfaces;

    @Autowired
    public DefaultPluginCollector(List<FrontpagePluginInterface> frontpagePluginInterfaces) {

        this.frontpagePluginInterfaces = frontpagePluginInterfaces;
    }

    @Override
    public List<FrontpagePluginInterface> getFrontpagePlugins() {

        return frontpagePluginInterfaces;
    }
}
