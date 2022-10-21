package br.com.hdi.jsltconverter.exception;

import com.networknt.schema.ValidationMessage;
import lombok.Data;

import java.util.Set;

@Data
public class JsonSchemaValidationException extends RuntimeException {
    private final Set<ValidationMessage> validationMessages;

    public JsonSchemaValidationException(Set<ValidationMessage> validationMessages) {
        this.validationMessages = validationMessages;
    }

}
