package coffee.synyx.frontpage;

import coffee.synyx.frontpage.plugin.api.FrontpagePlugin;

import java.util.Objects;
import java.util.UUID;

public final class PluginInstance {

    private final UUID id;
    private final FrontpagePlugin plugin;
    private final ConfigurationInstanceImpl configurationInstance;


    PluginInstance(ConfigurationInstanceImpl configurationInstance, FrontpagePlugin plugin) {

        this.id = UUID.randomUUID();
        this.configurationInstance = configurationInstance;
        this.plugin = plugin;
    }

    UUID getId() {
        return id;
    }

    ConfigurationInstanceImpl getConfigurationInstance() {
        return configurationInstance;
    }

    public FrontpagePlugin getPlugin() {
        return plugin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PluginInstance that = (PluginInstance) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PluginInstance{" +
            "id=" + id +
            ", configurationInstance=" + configurationInstance +
            ", plugin='" + plugin + '\'' +
            '}';
    }
}
