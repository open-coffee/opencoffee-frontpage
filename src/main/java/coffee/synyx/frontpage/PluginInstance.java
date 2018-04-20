package coffee.synyx.frontpage;

import org.springframework.data.annotation.Id;

import java.util.Objects;
import java.util.UUID;

public final class PluginInstance {

    @Id
    private final UUID id;
    private final String username;
    private final String pluginId;
    private final ConfigurationInstanceImpl configurationInstance;


    PluginInstance(String username, ConfigurationInstanceImpl configurationInstance, String pluginId) {

        this.id = UUID.randomUUID();
        this.username = username;
        this.configurationInstance = configurationInstance;
        this.pluginId = pluginId;
    }

    UUID getId() {
        return id;
    }

    ConfigurationInstanceImpl getConfigurationInstance() {
        return configurationInstance;
    }

    public String getPluginId() {
        return pluginId;
    }

    public String getUsername() {
        return username;
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
            ", username='" + username + '\'' +
            ", pluginId='" + pluginId + '\'' +
            ", configurationInstance=" + configurationInstance +
            '}';
    }
}
