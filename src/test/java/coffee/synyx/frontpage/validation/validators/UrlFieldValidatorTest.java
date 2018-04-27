package coffee.synyx.frontpage.validation.validators;

import coffee.synyx.frontpage.plugin.api.ConfigurationField;
import coffee.synyx.frontpage.plugin.api.ConfigurationFieldType;
import org.junit.Test;
import org.springframework.validation.Errors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class UrlFieldValidatorTest {

    @Test
    public void ensureSupportsReturnsTrueForConfigTypeUrl() {

        final ConfigurationField urlField = new ConfigurationFieldBuilder()
            .type(ConfigurationFieldType.URL)
            .build();

        UrlFieldValidator sut = new UrlFieldValidator();
        boolean actual = sut.supports(urlField);

        assertThat(actual).isTrue();
    }

    @Test
    public void ensureSupportsReturnsFalseForConfigTypeNotUrl() {

        final ConfigurationField notUrlField = new ConfigurationFieldBuilder()
            .type(ConfigurationFieldType.NUMBER)
            .build();

        UrlFieldValidator sut = new UrlFieldValidator();
        boolean actual = sut.supports(notUrlField);

        assertThat(actual).isFalse();
    }

    @Test
    public void ensureSuccessfulValidation() {

        final ConfigurationField field = new ConfigurationFieldBuilder()
            .type(ConfigurationFieldType.URL)
            .build();

        final Errors errors = mock(Errors.class);

        UrlFieldValidator sut = new UrlFieldValidator();
        sut.validate("http://synyx.de", field, errors);

        verifyZeroInteractions(errors);
    }

    @Test
    public void ensureFailingValidation() {

        final ConfigurationField field = new ConfigurationFieldBuilder()
            .id("field.id")
            .type(ConfigurationFieldType.URL)
            .build();

        final Errors errors = mock(Errors.class);

        UrlFieldValidator sut = new UrlFieldValidator();
        sut.validate("synyx.de", field, errors);

        verify(errors).rejectValue("field.id", "plugin.validation.error.url");
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
