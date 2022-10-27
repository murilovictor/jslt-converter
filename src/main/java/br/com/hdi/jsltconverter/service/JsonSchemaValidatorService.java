package br.com.hdi.jsltconverter.service;

import br.com.hdi.jsltconverter.exception.JsonSchemaException;
import br.com.hdi.jsltconverter.model.ConverterModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
public class JsonSchemaValidatorService {

    @SneakyThrows
    public void validateSchema(ConverterModel converterModel) {
        if (Strings.isEmpty(converterModel.getSchemaTemplate())) {
            log.info("Informe o caminho do schema para validar.");
            return;
        }

        log.info("Start - Schema Validation");
        long start = System.currentTimeMillis();

        JsonNode jsonForValidate = Objects.nonNull(converterModel.getJsonOutput()) ? converterModel.getJsonOutput() : converterModel.getJsonInput();

        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        JsonSchema jsonSchema = factory.getSchema(JsonSchemaValidatorService.class.getResourceAsStream(converterModel.getSchemaTemplate()));
        Set<ValidationMessage> errors = jsonSchema.validate(jsonForValidate);

        long end = System.currentTimeMillis();
        log.info("End - Schema Validation -> Time: " + (end - start) + "ms");

        if (errors.isEmpty()) {
            log.info("SCHAMA: SUCCESS");
        } else {
            log.info("Qtde. Errors: {}", errors.size());
            throw new JsonSchemaException(errors);
        }

    }

}
