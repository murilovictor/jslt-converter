package br.com.hdi.jsltconverter.service;

import br.com.hdi.jslt.utils.CustomFunctionsUtil;
import br.com.hdi.jsltconverter.exception.TechnicalException;
import br.com.hdi.jsltconverter.function.DistinctArrayFunction;
import br.com.hdi.jsltconverter.model.ConverterModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schibsted.spt.data.jslt.Expression;
import com.schibsted.spt.data.jslt.Function;
import com.schibsted.spt.data.jslt.Parser;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static br.com.hdi.jsltconverter.message.enumeration.MessageUtil.ERROR_CONVERTING_OBJECT_TO_JSONNODE;
import static br.com.hdi.jsltconverter.message.enumeration.MessageUtil.ERROR_LOADING_JSLT_CUSTOM_FUNCTION;
import static br.com.hdi.jsltconverter.message.enumeration.MessageUtil.ERROR_READING_INPUT_FILE;
import static br.com.hdi.jsltconverter.message.enumeration.MessageUtil.ERROR_READING_JSLT_TEMPLATE;
import static org.apache.logging.log4j.util.Strings.isEmpty;

@Slf4j
@Service
public class JsltConverterService {

    public void convertJsonFromJslt(ConverterModel converterModel) {
        converterModel.setJsonInput(this.loadJsonInput(converterModel));

        if (isEmpty(converterModel.getPathOut()) || isEmpty(converterModel.getPathJsltTemplate())) {
            log.warn("JsltConverterService - Ignored");
            return;
        }

        log.info("Start - Converting json based on jslt template.");

        Expression jslt = Parser.compileString(this.loadJsltTemplate(converterModel), this.loadingJsltCustomFunctions());
        JsonNode output = jslt.apply(converterModel.getJsonInput());

        converterModel.setJsonOutput(output);
        log.info("End - Start - Converting json based on jslt template.");

    }

    private List<Function> loadingJsltCustomFunctions() {
        try {
            List<Function> functions = CustomFunctionsUtil.loadCustomFunctions();
            functions.add(new DistinctArrayFunction());
            return functions;
        } catch (Exception e) {
            log.error("Error loading jslt custom functions: ", e);
            throw new TechnicalException(ERROR_LOADING_JSLT_CUSTOM_FUNCTION);
        }
    }

    private JsonNode loadJsonInput(ConverterModel converterModel) {
        try {
            log.info("Start - Loading the input File.");
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(converterModel.getPathIn()));
            log.info("End - Loading the input File.");
            return toJsonNode(jsonObject);
        } catch (Exception e) {
            log.error("Error reading input file: ", e);
            throw new TechnicalException(ERROR_READING_INPUT_FILE);
        }

    }

    private String loadJsltTemplate(ConverterModel converterModel) {
        try {
            log.info("Start - Loading jslt Template.");
            Path path = Paths.get(getClass().getClassLoader()
                    .getResource(converterModel.getPathJsltTemplate())
                    .toURI());

            Stream<String> lines = Files.lines(path);
            String data = lines.collect(Collectors.joining("\n"));
            lines.close();
            log.info("End - Loading jslt Template");
            return data;
        } catch (Exception e) {
            log.error("Error reading jslt template: ", e);
            throw new TechnicalException(ERROR_READING_JSLT_TEMPLATE);
        }
    }

    public JsonNode toJsonNode(JSONObject jsonObj) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(jsonObj.toString());
        } catch (JsonProcessingException e) {
            log.error("Error converting object to JsonNode: ", e);
            throw new TechnicalException(ERROR_CONVERTING_OBJECT_TO_JSONNODE);
        }
    }


}
