package br.com.hdi.jsltconverter.service;

import br.com.hdi.jsltconverter.exception.TechnicalException;
import br.com.hdi.jsltconverter.model.ConverterModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;

import static br.com.hdi.jsltconverter.message.enumeration.MessageUtil.ERROR_WRITING_FILE;
import static org.apache.logging.log4j.util.Strings.isEmpty;

@Slf4j
@Service
public class FileExportService {

    public void exportFile(ConverterModel converterModel) {
        if (isEmpty(converterModel.getPathOut())) {
            log.warn("Enter the output path to export the file..");
            return;
        }

        log.info("Start - Writing output file.");

        try (FileWriter file = new FileWriter(converterModel.getPathOut())) {
            file.write(converterModel.getJsonOutput().toPrettyString());
            file.flush();
            log.info("File successfully saved to directory : {}", converterModel.getPathOut());
        } catch (IOException e) {
            log.error("Error writing output file: ", e);
            throw new TechnicalException(ERROR_WRITING_FILE);
        }

        log.info("End - Writing output file.");

    }

}
