package br.com.hdi.jsltconverter.exception.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.networknt.schema.ValidationMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiMessage implements Serializable {

    private static final long serialVersionUID = 7242344463403853932L;

    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("detail")
    private String detail;

    @JsonIgnore
    private transient Object[] parameters;


    public static ApiMessage validationMessageToApiMessage(ValidationMessage validationMessage) {
        return ApiMessage.builder()
                .message(validationMessage.getMessage())
                .detail(validationMessage.toString())
                .build();
    }
}