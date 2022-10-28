package br.com.hdi.jsltconverter.service;

import br.com.hdi.jsltconverter.exception.JsonSchemaException;
import br.com.hdi.jsltconverter.model.ConverterModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
public class JsonSchemaValidatorService {

    public void validateSchema(ConverterModel converterModel) {
        if (Strings.isEmpty(converterModel.getSchemaTemplate())) {
            log.warn("To validate the schama, enter the path.");
            return;
        }

        log.info("Start - Json Schema Validation");

        JsonNode jsonForValidate = Objects.nonNull(converterModel.getJsonOutput()) ? converterModel.getJsonOutput() : converterModel.getJsonInput();

        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        JsonSchema jsonSchema = factory.getSchema(JsonSchemaValidatorService.class.getResourceAsStream(converterModel.getSchemaTemplate()));
        Set<ValidationMessage> errors = jsonSchema.validate(jsonForValidate);

        log.info("End - Json Schema Validation");

        if (errors.isEmpty()) {
            log.info("Successfully validated schema. ");
        } else {
            log.info("Number of errors found: {}", errors.size());
            throw new JsonSchemaException(errors);
        }

    }

}
