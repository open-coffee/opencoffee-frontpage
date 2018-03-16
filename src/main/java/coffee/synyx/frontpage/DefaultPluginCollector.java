package coffee.synyx.frontpage;

import coffee.synyx.frontpage.plugin.api.FrontpagePluginInterface;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DefaultPluginCollector implements PluginCollector {

    private List<FrontpagePluginInterface> frontpagePluginInterfaces;

    @Override
    public List<FrontpagePluginInterface> getFrontpagePlugins() {

        return frontpagePluginInterfaces;
    }


    @Autowired(required = false)
    public void setFrontpagePluginInterfaces(List<FrontpagePluginInterface> frontpagePluginInterfaces) {

        this.frontpagePluginInterfaces = frontpagePluginInterfaces;
    }
}
