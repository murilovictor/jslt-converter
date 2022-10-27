package br.com.hdi.jsltconverter.exception;

import br.com.hdi.jsltconverter.message.enumeration.MessageUtil;
import lombok.Data;

@Data
public class TechnicalException extends RuntimeException {

    public TechnicalException(MessageUtil messageUtil) {
        super(messageUtil.getMessage());
    }

}
