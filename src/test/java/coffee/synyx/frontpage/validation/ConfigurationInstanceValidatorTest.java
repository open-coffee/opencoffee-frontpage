package coffee.synyx.frontpage.validation;

import coffee.synyx.frontpage.plugin.api.ConfigurationDescription;
import coffee.synyx.frontpage.plugin.api.ConfigurationField;
import coffee.synyx.frontpage.plugin.api.ConfigurationFieldType;
import coffee.synyx.frontpage.plugin.api.ConfigurationInstance;
import coffee.synyx.frontpage.plugin.api.validation.ConfigurationFieldValidator;
import org.junit.Test;
import org.springframework.validation.Errors;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class ConfigurationInstanceValidatorTest {

    @Test
    public void ensureValidationWhenThereAreNoValidators() {

        final Errors errors = mock(Errors.class);

        final ConfigurationField noNumberField = new ConfigurationFieldBuilder()
            .type(ConfigurationFieldType.URL)
            .build();

        final ConfigurationDescription configDescription = new ConfigurationDescriptionBuilder()
            .withConfigurationField(noNumberField)
            .build();

        final ConfigurationInstance configInstance = new ConfigurationInstanceBuilder()
            .withField("foo.number", "42")
            .build();

        ConfigurationInstanceValidator sut = new ConfigurationInstanceValidator(emptySet());
        sut.validate(configInstance, configDescription, errors);

        verifyZeroInteractions(errors);
    }

    @Test
    public void ensureValidationWhenThereAreUncalledValidators() {

        final Errors errors = mock(Errors.class);

        final ConfigurationField noNumberField = new ConfigurationFieldBuilder()
            .type(ConfigurationFieldType.URL)
            .build();

        final ConfigurationDescription configDescription = new ConfigurationDescriptionBuilder()
            .withConfigurationField(noNumberField)
            .build();

        final ConfigurationInstance configInstance = new ConfigurationInstanceBuilder()
            .withField("foo.number", "42")
            .build();

        final ConfigurationFieldValidator numberValidator = mock(ConfigurationFieldValidator.class);
        when(numberValidator.supports(any(ConfigurationField.class))).thenReturn(false);

        ConfigurationInstanceValidator sut = new ConfigurationInstanceValidator(asSet(numberValidator));
        sut.validate(configInstance, configDescription, errors);

        verifyZeroInteractions(errors);

        verify(numberValidator).supports(noNumberField);
        verifyNoMoreInteractions(numberValidator);
    }

    @Test
    public void ensureValidationWhenThereAreValidators() {

        final Errors errors = mock(Errors.class);

        final ConfigurationField numberField = new ConfigurationFieldBuilder()
            .id("foo.number")
            .type(ConfigurationFieldType.NUMBER)
            .label("label")
            .build();

        final ConfigurationDescription configDescription = new ConfigurationDescriptionBuilder()
            .withConfigurationField(numberField)
            .build();

        final ConfigurationInstance configInstance = new ConfigurationInstanceBuilder()
            .withField("foo.number", "42")
            .build();

        final ConfigurationFieldValidator numberValidator = mock(ConfigurationFieldValidator.class);
        when(numberValidator.supports(any(ConfigurationField.class))).thenReturn(true);

        ConfigurationInstanceValidator sut = new ConfigurationInstanceValidator(asSet(numberValidator));
        sut.validate(configInstance, configDescription, errors);

        verify(numberValidator).supports(numberField);
        verify(numberValidator).validate("42", numberField, errors);
    }

    private static <T> Set<T> asSet(T... items) {
        return new HashSet(asList(items));
    }

    private static class ConfigurationInstanceBuilder {
        private Map<String, String> fields = new HashMap<>();

        ConfigurationInstanceBuilder withField(String key, String value) {
            fields.put(key, value);
            return this;
        }

        ConfigurationInstance build() {
            return key -> fields.get(key);
        }
    }

    private static class ConfigurationDescriptionBuilder {
        private final Set<ConfigurationField> fields = new HashSet();

        ConfigurationDescriptionBuilder withConfigurationField(ConfigurationField field) {
            this.fields.add(field);
            return this;
        }

        ConfigurationDescription build() {
            return () -> Collections.unmodifiableSet(fields);
        }
    }

    private static class ConfigurationFieldBuilder {

        private String id;
        private String label;
        private ConfigurationFieldType type;

        ConfigurationFieldBuilder id(String id) {
            this.id = id;
            return this;
        }

        ConfigurationFieldBuilder label(String label) {
            this.label = label;
            return this;
        }

        ConfigurationFieldBuilder type(ConfigurationFieldType type) {
            this.type = type;
            return this;
        }

        ConfigurationField build() {
            return new ConfigurationField() {
                @Override
                public String getLabel() {
                    return id;
                }

                @Override
                public ConfigurationFieldType getType() {
                    return type;
                }

                @Override
                public String getId() {
                    return id;
                }
            };
        }
    }
}
