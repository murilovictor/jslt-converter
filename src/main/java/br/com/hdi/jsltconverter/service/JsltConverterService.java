package br.com.hdi.jsltconverter.service;

import br.com.hdi.jslt.utils.CustomFunctionsUtil;
import br.com.hdi.jsltconverter.exception.TechnicalException;
import br.com.hdi.jsltconverter.function.DistinctArrayFunction;
import br.com.hdi.jsltconverter.message.enumeration.MessageUtil;
import br.com.hdi.jsltconverter.model.ConverterModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schibsted.spt.data.jslt.Expression;
import com.schibsted.spt.data.jslt.Function;
import com.schibsted.spt.data.jslt.Parser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static org.apache.logging.log4j.util.Strings.isEmpty;

@Slf4j
@Service
public class JsltConverterService {

    @SneakyThrows
    public void convertJsonFromJslt(ConverterModel converterModel) {
        converterModel.setJsonInput(this.loadJsonInput(converterModel));

        if (isNull(converterModel.getPathOut()) || isNull(converterModel.getPathJsltTemplate())) {
            log.info("JsltConverterService - SKIP");
            return;
        }

        log.info("Start - Converter Json");
        long start = System.currentTimeMillis();


        String jsltTemplate = this.loadJsltTemplate(converterModel);

        List<Function> functions = CustomFunctionsUtil.loadCustomFunctions();
        functions.add(new DistinctArrayFunction());

        Expression jslt = Parser.compileString(jsltTemplate, functions);
        JsonNode output = jslt.apply(converterModel.getJsonInput());

        converterModel.setJsonOutput(output);

        exportFile(output, converterModel);

        long end = System.currentTimeMillis();

        log.info("End - Converter Json -> Time: " + (end - start) + "ms");

    }

    private void exportFile(JsonNode output, ConverterModel directory) {
        if (isEmpty(directory.getPathOut())) {
            log.info("Informe o caminho de output para exportar o arquivo.");
            return;
        }

        log.info("Inicio - Gravar arquivo.");

        try (FileWriter file = new FileWriter(directory.getPathOut())) {
            file.write(output.toPrettyString());
            file.flush();
        } catch (IOException e) {
            throw new TechnicalException(MessageUtil.ERROR_WRITE_FILE);
        }

        log.info("Fim - Gravar arquivo.");

    }

    @SneakyThrows
    private JsonNode loadJsonInput(ConverterModel converterModel) {
        log.info("Inicio - Carregando File Input.");
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(converterModel.getPathIn()));
        log.info("Fim - Carregando Input.");
        return toJsonNode(jsonObject);
    }

    @SneakyThrows
    private String loadJsltTemplate(ConverterModel converterModel) {
        log.info("Inicio - Carregando Template JSLT.");
        Path path = Paths.get(getClass().getClassLoader()
                .getResource(converterModel.getPathJsltTemplate()).toURI());

        Stream<String> lines = Files.lines(path);
        String data = lines.collect(Collectors.joining("\n"));
        lines.close();
        log.info("Fim - Carregando Template JSLT.");
        return data;
    }

    @SneakyThrows
    public JsonNode toJsonNode(JSONObject jsonObj) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(jsonObj.toString());
    }


}
