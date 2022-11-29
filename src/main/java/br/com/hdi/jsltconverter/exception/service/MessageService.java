package br.com.hdi.jsltconverter.exception.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageSource messageSource;

    public String getMessage(String message) {
        String defaultMessage = "Mensagem não localizada. " + message;
        return messageSource.getMessage(message, null, defaultMessage, Locale.US);
    }

    public String getMessage(String message, Object... args) {
        String defaultMessage = "Mensagem não localizada. " + message;
        return messageSource.getMessage(message, args, defaultMessage, Locale.US);
    }

}
