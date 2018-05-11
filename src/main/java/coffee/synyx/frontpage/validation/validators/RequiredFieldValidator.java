package coffee.synyx.frontpage.validation.validators;

import coffee.synyx.frontpage.plugin.api.ConfigurationField;
import coffee.synyx.frontpage.plugin.api.validation.ConfigurationFieldValidator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

@Component
public class RequiredFieldValidator implements ConfigurationFieldValidator {

    // message key
    private static final String REQUIRED_FIELD_VALIDATION_ERROR = "plugin.validation.error.required";

    @Override
    public boolean supports(ConfigurationField field) {
        return field.isRequired();
    }

    @Override
    public void validate(Object target, ConfigurationField field, Errors errors) {
        if (StringUtils.isEmpty(target)) {
            errors.rejectValue(field.getId(), REQUIRED_FIELD_VALIDATION_ERROR);
        }
    }
}
