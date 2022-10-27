package br.com.hdi.jsltconverter.exception.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse implements Serializable {

    private static final long serialVersionUID = 3156631615464553575L;

    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("description")
    private String description;

    @JsonProperty("errors")
    private List<ApiMessage> errors;

    public List<ApiMessage> getErrors() {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        return errors;
    }

}