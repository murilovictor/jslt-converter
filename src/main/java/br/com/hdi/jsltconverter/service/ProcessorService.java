package br.com.hdi.jsltconverter.service;

import br.com.hdi.jsltconverter.model.ConverterModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProcessorService {

    private final JsltConverterService jsltConverterService;
    private final JsonSchemaValidatorService jsonSchemaValidatorService;

    private final FileExportService fileExportService;

    public ConverterModel execute(ConverterModel converterModel) {
        jsltConverterService.convertJsonFromJslt(converterModel);
        fileExportService.exportFile(converterModel);
        jsonSchemaValidatorService.validateSchema(converterModel);
        return converterModel;
    }


}
