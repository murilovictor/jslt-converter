package br.com.hdi.jsltconverter.function;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schibsted.spt.data.jslt.Function;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.apache.logging.log4j.util.Strings.isNotEmpty;

public class DistinctArrayFunction implements Function {


    @Override
    public String getName() {
        return "distinctModels";
    }

    @Override
    public int getMinArguments() {
        return 1;
    }

    @Override
    public int getMaxArguments() {
        return 2;
    }

    @Override
    public JsonNode call(JsonNode jsonNode, JsonNode[] jsonNodes) {
        String[] keys = jsonNodes[1].asText().split("\\|");

        HashMap<String, JsonNode> map = new HashMap<>();

        List<JsonNode> models = StreamSupport
                .stream(jsonNodes[0].spliterator(), true)
                .collect(Collectors.toList());

        for (JsonNode node : models) {
            String key = buildKey(keys, node);
            map.put(key, node);
        }

        return new ObjectMapper().valueToTree(map.values());
    }

    private String buildKey(String[] keys, JsonNode node) {
        StringBuilder keySb = new StringBuilder();

        for (String key : keys) {
            String field = node.findPath(key).asText();
            if (isNotEmpty(field)) {
                keySb.append(field).append("-");
            }
        }

        return keySb.toString();
    }
}
