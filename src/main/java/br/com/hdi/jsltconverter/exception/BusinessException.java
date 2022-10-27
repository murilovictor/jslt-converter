package br.com.hdi.jsltconverter.exception;

import br.com.hdi.jsltconverter.message.enumeration.MessageUtil;
import lombok.Data;

@Data
public class BusinessException extends RuntimeException {

    public BusinessException(MessageUtil messageUtil) {
        super(messageUtil.getMessage());
    }

}
