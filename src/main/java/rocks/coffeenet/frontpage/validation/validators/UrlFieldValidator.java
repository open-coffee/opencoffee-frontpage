package rocks.coffeenet.frontpage.validation.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import rocks.coffeenet.frontpage.plugin.api.ConfigurationField;
import rocks.coffeenet.frontpage.plugin.api.ConfigurationFieldType;
import rocks.coffeenet.frontpage.plugin.api.validation.ConfigurationFieldValidator;

import java.net.MalformedURLException;
import java.net.URL;

@Component
public class UrlFieldValidator implements ConfigurationFieldValidator {

    // message key
    private static final String URL_VALIDATION_ERROR = "plugin.validation.error.url";

    @Override
    public boolean supports(ConfigurationField field) {
        return ConfigurationFieldType.URL.equals(field.getType());
    }

    @Override
    public void validate(Object target, ConfigurationField field, Errors errors) {
        try {
            new URL(String.valueOf(target));
        } catch (MalformedURLException e) {
            errors.rejectValue(field.getId(), URL_VALIDATION_ERROR);
        }
    }
}
