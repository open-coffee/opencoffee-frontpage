package coffee.synyx.frontpage;

import coffee.synyx.frontpage.plugin.api.FrontpagePluginInterface;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class DefaultPluginCollector implements PluginCollector {

    private List<FrontpagePluginInterface> frontpagePluginInterfaces;

    @Override
    public Optional<List<FrontpagePluginInterface>> getFrontpagePlugins() {

        return Optional.ofNullable(frontpagePluginInterfaces);
    }


    @Autowired(required = false)
    public void setFrontpagePluginInterfaces(List<FrontpagePluginInterface> frontpagePluginInterfaces) {

        this.frontpagePluginInterfaces = frontpagePluginInterfaces;
    }
}
