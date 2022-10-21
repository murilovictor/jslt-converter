package br.com.hdi.jsltconverter.controller;

import br.com.hdi.jsltconverter.model.ConverterModel;
import br.com.hdi.jsltconverter.service.JsltConverterService;
import br.com.hdi.jsltconverter.service.ProcessorService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final JsltConverterService jsltConverterService;
    private final ProcessorService processorService;

    @GetMapping("auto")
    public ResponseEntity<JsonNode> auto() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        ConverterModel converterModel = ConverterModel
                .builder()
                .pathIn("src/main/resources/static/input.json")
                .pathOut("src/main/resources/static/output.json")
                .pathJsltTemplate("static/auto/transform.jslt")
                .build();

        return ResponseEntity.ok(processorService.execute(converterModel));
    }

    @GetMapping("home")
    public ResponseEntity<JsonNode> home() {
        ConverterModel converterModel = ConverterModel
                .builder()
                .pathIn("src/main/resources/static/home/input.json")
                .pathOut("src/main/resources/static/home/output.json")
                .pathJsltTemplate("static/home/transform.jslt")
                .schemaTemplate("/static/home/schema.json")
                .build();

        return ResponseEntity.ok(processorService.execute(converterModel));
    }

}
