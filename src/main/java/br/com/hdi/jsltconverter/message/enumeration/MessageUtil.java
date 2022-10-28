package br.com.hdi.jsltconverter.message.enumeration;

import lombok.Getter;

@Getter
public enum MessageUtil {

    ERROR_WRITING_FILE("app.error.write.file"),
    ERROR_CONVERTING_OBJECT_TO_JSONNODE("app.error.converting.object.to.jsonnode"),
    ERROR_LOADING_JSLT_CUSTOM_FUNCTION("app.error.loading.jslt.custom.functions"),
    ERROR_READING_INPUT_FILE("app.error.reading.input.file"),
    ERROR_READING_JSLT_TEMPLATE("app.error.reading.jslt.template");

    private final String message;

    MessageUtil(String message) {
        this.message = message;
    }

}
