package rocks.coffeenet.frontpage;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationInstanceImplTest {

    @Test
    public void getCorrectValue() {

        Map<String, String> params = new HashMap<>();
        params.put("key", "value");

        ConfigurationInstanceImpl configurationInstance = new ConfigurationInstanceImpl(params);

        assertThat(configurationInstance.get("key")).isEqualTo("value");
    }
}
