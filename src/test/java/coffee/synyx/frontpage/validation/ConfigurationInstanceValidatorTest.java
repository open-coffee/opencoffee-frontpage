package coffee.synyx.frontpage.validation;

import coffee.synyx.frontpage.plugin.api.ConfigurationDescription;
import coffee.synyx.frontpage.plugin.api.ConfigurationField;
import coffee.synyx.frontpage.plugin.api.ConfigurationFieldType;
import coffee.synyx.frontpage.plugin.api.ConfigurationInstance;
import coffee.synyx.frontpage.plugin.api.validation.ConfigurationFieldValidator;
import org.junit.Test;
import org.springframework.validation.Errors;

import java.util.HashSet;
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

        final ConfigurationField noNumberField = new ConfigurationField.Builder()
            .type(ConfigurationFieldType.URL)
            .build();

        final ConfigurationDescription configDescription = new ConfigurationDescription.Builder()
            .withConfigurationField(noNumberField)
            .build();

        final ConfigurationInstance configInstance = new ConfigurationInstance.Builder()
            .withField("foo.number", "42")
            .build();

        ConfigurationInstanceValidator sut = new ConfigurationInstanceValidator(emptySet());
        sut.validate(configInstance, configDescription, errors);

        verifyZeroInteractions(errors);
    }

    @Test
    public void ensureValidationWhenThereAreUncalledValidators() {

        final Errors errors = mock(Errors.class);

        final ConfigurationField noNumberField = new ConfigurationField.Builder()
            .type(ConfigurationFieldType.URL)
            .build();

        final ConfigurationDescription configDescription = new ConfigurationDescription.Builder()
            .withConfigurationField(noNumberField)
            .build();

        final ConfigurationInstance configInstance = new ConfigurationInstance.Builder()
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

        final ConfigurationField numberField = new ConfigurationField.Builder()
            .id("foo.number")
            .type(ConfigurationFieldType.NUMBER)
            .label("label")
            .build();

        final ConfigurationDescription configDescription = new ConfigurationDescription.Builder()
            .withConfigurationField(numberField)
            .build();

        final ConfigurationInstance configInstance = new ConfigurationInstance.Builder()
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
}
