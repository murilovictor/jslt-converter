package br.com.hdi.jsltconverter.exception.advice;

import br.com.hdi.jsltconverter.exception.JsonSchemaException;
import br.com.hdi.jsltconverter.exception.model.ApiErrorResponse;
import br.com.hdi.jsltconverter.exception.model.ApiMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class DefaultControllerAdvice {


    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JsonSchemaException.class)
    public ResponseEntity<ApiErrorResponse> handleJsonSchemaException(JsonSchemaException jsonSchemaException) {

        List<ApiMessage> apiMessages = jsonSchemaException.getValidationMessages()
                .stream()
                .map(ApiMessage::validationMessageToApiMessage)
                .collect(Collectors.toList());

        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .code("400")
                .description("Erro ao validar o schema")
                .errors(apiMessages)
                .build();

        return ResponseEntity
                .badRequest()
                .body(apiErrorResponse);
    }

}
