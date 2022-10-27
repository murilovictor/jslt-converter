package br.com.hdi.jsltconverter.message.enumeration;

import lombok.Getter;

@Getter
public enum MessageUtil {

    ERROR_WRITE_FILE("app.error.write.file");

    private final String message;

    MessageUtil(String message) {
        this.message = message;
    }

}
