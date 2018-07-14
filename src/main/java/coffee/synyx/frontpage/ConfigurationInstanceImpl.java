package coffee.synyx.frontpage;

import coffee.synyx.frontpage.plugin.api.ConfigurationInstance;

import java.util.Map;

public class ConfigurationInstanceImpl implements ConfigurationInstance {

    private final Map<String, String> params;

    ConfigurationInstanceImpl(Map<String, String> params) {
        this.params = params;
    }

    @Override
    public String get(String key) {

        return params.get(key);
    }

    public Map<String, String> getParams() {
        return params;
    }
}
