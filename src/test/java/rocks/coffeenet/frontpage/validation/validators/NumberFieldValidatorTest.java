package rocks.coffeenet.frontpage.validation.validators;

import coffee.synyx.frontpage.plugin.api.ConfigurationField;
import coffee.synyx.frontpage.plugin.api.ConfigurationFieldType;
import org.junit.Test;
import org.springframework.validation.Errors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class NumberFieldValidatorTest {

    @Test
    public void ensureSupportsReturnsTrueForConfigTypeNumber() {

        final ConfigurationField numberField = new ConfigurationField.Builder()
            .type(ConfigurationFieldType.NUMBER)
            .build();

        NumberFieldValidator sut = new NumberFieldValidator();
        boolean actual = sut.supports(numberField);

        assertThat(actual).isTrue();
    }

    @Test
    public void ensureSupportsReturnsFalseForConfigTypeNotNumber() {

        final ConfigurationField notNumberField = new ConfigurationField.Builder()
            .type(ConfigurationFieldType.URL)
            .build();

        NumberFieldValidator sut = new NumberFieldValidator();
        boolean actual = sut.supports(notNumberField);

        assertThat(actual).isFalse();
    }

    @Test
    public void ensureSuccessfulValidation() {

        final ConfigurationField numberField = new ConfigurationField.Builder()
            .type(ConfigurationFieldType.NUMBER)
            .build();

        final Errors errors = mock(Errors.class);

        NumberFieldValidator sut = new NumberFieldValidator();
        sut.validate("42", numberField, errors);

        verifyZeroInteractions(errors);
    }

    @Test
    public void ensureFailingValidation() {

        final ConfigurationField numberField = new ConfigurationField.Builder()
            .id("numberField.id")
            .type(ConfigurationFieldType.NUMBER)
            .build();

        final Errors errors = mock(Errors.class);

        NumberFieldValidator sut = new NumberFieldValidator();
        sut.validate("5647654234324546768796574", numberField, errors);

        verify(errors).rejectValue("numberField.id", "plugin.validation.error.number");
    }
}
