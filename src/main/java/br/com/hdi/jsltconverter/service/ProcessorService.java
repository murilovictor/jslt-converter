package br.com.hdi.jsltconverter.service;

import br.com.hdi.jsltconverter.model.ConverterModel;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProcessorService {

    private final JsltConverterService jsltConverterService;
    private final JsonSchemaValidatorService jsonSchemaValidatorService;

    @SneakyThrows
    public JsonNode execute(ConverterModel converterModel) {
        jsltConverterService.convertJsonFromJslt(converterModel);
        jsonSchemaValidatorService.validateSchema(converterModel);
        return converterModel.getJsonOutput();
    }


}
