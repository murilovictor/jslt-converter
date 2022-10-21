package br.com.hdi.jsltconverter.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConverterModel {

    private String pathIn;
    private String pathOut;
    private String pathJsltTemplate;
    private String schemaTemplate;

    private JsonNode jsonInput;
    private JsonNode jsonOutput;

}
