package br.com.hdi.jsltconverter.controller;

import br.com.hdi.jsltconverter.service.ConverterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final ConverterService converterService;

    @GetMapping
    public void execute() {
        converterService.transform();
    }

}
