package br.com.hdi.jsltconverter.exception;

import com.networknt.schema.ValidationMessage;

import java.util.Set;

public class JsonSchemaException extends JsonSchemaValidationException {

    public JsonSchemaException(Set<ValidationMessage> validationMessages) {
        super(validationMessages);
    }
    
}
