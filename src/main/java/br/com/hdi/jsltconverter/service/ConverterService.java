package br.com.hdi.jsltconverter.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schibsted.spt.data.jslt.Expression;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class ConverterService {

    public void transform() {
        long start = System.currentTimeMillis();

        JsonNode input = loadJsonInput();
        String jsltTemplate = loadJsltTemplate();

        Expression jslt = Parser.compileString(jsltTemplate);
        JsonNode output = jslt.apply(input);

        exportFile(output);

        long end = System.currentTimeMillis();

        log.info("The End. Time: " + (end - start) + "ms");
    }

    private void exportFile(JsonNode output) {
        log.info("Inicio - Gravar arquivo.");
        try (FileWriter file = new FileWriter("src/main/resources/static/output.json")) {
            file.write(output.toPrettyString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("Fim - Gravar arquivo.");
    }

    @SneakyThrows
    private JsonNode loadJsonInput() {
        log.info("Inicio - Carregando Input.");
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader("src/main/resources/static/input.json"));
        log.info("Fim - Carregando Input.");
        return toJsonNode(jsonObject);
    }

    @SneakyThrows
    private String loadJsltTemplate() {
        log.info("Inicio - Carregando Template JSLT.");
        Path path = Paths.get(getClass().getClassLoader()
                .getResource("static/transform.jslt").toURI());

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
