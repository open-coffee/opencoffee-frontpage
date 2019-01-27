package rocks.coffeenet.frontpage.validation;

import coffee.synyx.frontpage.plugin.api.ConfigurationDescription;
import coffee.synyx.frontpage.plugin.api.ConfigurationField;
import coffee.synyx.frontpage.plugin.api.ConfigurationInstance;
import coffee.synyx.frontpage.plugin.api.validation.ConfigurationFieldValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.Set;

@Component
public class ConfigurationInstanceValidator {

    private final Set<ConfigurationFieldValidator> validators;

    @Autowired
    public ConfigurationInstanceValidator(Set<ConfigurationFieldValidator> validators) {
        this.validators = validators;
    }

    public void validate(ConfigurationInstance instance, ConfigurationDescription description, Errors errors) {

        for (ConfigurationField descriptionField : description) {
            final String id = descriptionField.getId();
            final String valueToVerify = instance.get(id);

            for (ConfigurationFieldValidator validator : this.validators) {
                if (validator.supports(descriptionField)) {
                    validator.validate(valueToVerify, descriptionField, errors);
                }
            }
        }
    }
}
