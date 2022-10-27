package br.com.hdi.jsltconverter.message.service;

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

}
