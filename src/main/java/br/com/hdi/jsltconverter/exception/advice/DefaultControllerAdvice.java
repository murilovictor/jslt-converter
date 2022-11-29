package br.com.hdi.jsltconverter.exception.advice;

import br.com.hdi.jsltconverter.exception.BusinessException;
import br.com.hdi.jsltconverter.exception.JsonSchemaException;
import br.com.hdi.jsltconverter.exception.TechnicalException;
import br.com.hdi.jsltconverter.exception.model.ApiErrorResponse;
import br.com.hdi.jsltconverter.exception.model.ApiMessage;
import br.com.hdi.jsltconverter.exception.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class DefaultControllerAdvice {

    private final MessageService messageService;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JsonSchemaException.class)
    public ResponseEntity<ApiErrorResponse> handleJsonSchemaException(JsonSchemaException jsonSchemaException) {

        List<ApiMessage> apiMessages = jsonSchemaException.getValidationMessages()
                .stream()
                .map(ApiMessage::validationMessageToApiMessage)
                .collect(Collectors.toList());

        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .code("400")
                .description(format("Erro ao validar o schema, foram encontrados %d erros.", jsonSchemaException.getValidationMessages().size()))
                .errors(apiMessages)
                .build();

        return ResponseEntity
                .badRequest()
                .body(apiErrorResponse);
    }

    @ExceptionHandler(TechnicalException.class)
    public ResponseEntity<ApiErrorResponse> handleTechnicalException(TechnicalException technicalException) {
        ApiMessage apiMessage = ApiMessage.builder()
                .message(messageService.getMessage(technicalException.getMessage()))
                .build();

        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .description("Houve um erro interno.")
                .errors(Collections.singletonList(apiMessage))
                .build();

        return ResponseEntity
                .internalServerError()
                .body(apiErrorResponse);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessException(BusinessException technicalException) {

        ApiMessage apiMessage = ApiMessage.builder()
                .message(messageService.getMessage(technicalException.getMessage()))
                .build();

        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .description("Houve um erro de negócio..")
                .errors(Collections.singletonList(apiMessage))
                .build();

        return ResponseEntity
                .status(457)
                .body(apiErrorResponse);
    }


}
