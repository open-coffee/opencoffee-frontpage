package coffee.synyx.frontpage.validation.validators;

import coffee.synyx.frontpage.plugin.api.ConfigurationField;
import org.junit.Test;
import org.springframework.validation.Errors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class RequiredFieldValidatorTest {

    @Test
    public void ensureSupportsReturnsTrueForRequiredField() {

        final ConfigurationField requiredField = new ConfigurationField.Builder()
            .required(true)
            .build();

        RequiredFieldValidator sut = new RequiredFieldValidator();
        boolean actual = sut.supports(requiredField);

        assertThat(actual).isTrue();
    }

    @Test
    public void ensureSupportsReturnsFalseForOptionalField() {

        final ConfigurationField optionalField = new ConfigurationField.Builder()
            .required(false)
            .build();

        RequiredFieldValidator sut = new RequiredFieldValidator();
        boolean actual = sut.supports(optionalField);

        assertThat(actual).isFalse();
    }

    @Test
    public void ensureNullIsFalsy() {
        ensureFalsyForValue(null);
    }

    @Test
    public void ensureEmptyStringIsFalsy() {
        ensureFalsyForValue("");
    }

    @Test
    public void ensureStringIsTruthy() {
        ensureTruthyForValue("awesome string");
    }

    @Test
    public void ensureNumberIsTruthy() {
        ensureTruthyForValue(0);
    }

    private void ensureFalsyForValue(Object falsyValue) {

        final ConfigurationField field = new ConfigurationField.Builder()
            .id("field.id")
            .required(true)
            .build();

        final Errors errors = mock(Errors.class);

        RequiredFieldValidator sut = new RequiredFieldValidator();
        sut.validate(falsyValue, field, errors);

        verify(errors).rejectValue("field.id", "plugin.validation.error.required");
    }

    private void ensureTruthyForValue(Object truthyValue) {

        final ConfigurationField field = new ConfigurationField.Builder()
            .required(true)
            .build();

        final Errors errors = mock(Errors.class);

        RequiredFieldValidator sut = new RequiredFieldValidator();
        sut.validate(truthyValue, field, errors);

        verifyZeroInteractions(errors);
    }
}
