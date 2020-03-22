package rocks.coffeenet.frontpage.validation.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import rocks.coffeenet.frontpage.plugin.api.ConfigurationField;
import rocks.coffeenet.frontpage.plugin.api.ConfigurationFieldType;
import rocks.coffeenet.frontpage.plugin.api.validation.ConfigurationFieldValidator;

@Component
public class NumberFieldValidator implements ConfigurationFieldValidator {

    // message key
    private static final String NUMBER_VALIDATION_ERROR = "plugin.validation.error.number";

    @Override
    public boolean supports(ConfigurationField field) {
        return ConfigurationFieldType.NUMBER.equals(field.getType());
    }

    @Override
    public void validate(Object target, ConfigurationField field, Errors errors) {
        try {
            Integer.parseInt(String.valueOf(target));
        } catch (NumberFormatException e) {
            errors.rejectValue(field.getId(), NUMBER_VALIDATION_ERROR);
        }
    }
}
