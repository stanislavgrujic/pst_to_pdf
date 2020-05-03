package com.stanislavgrujic.psttopdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Component
public class PdfDocumentCreator {

  private Document document;

  public PdfDocument createDocument(String name) {
    File file = new File(name + ".pdf");

    try {
      file.createNewFile();
    } catch (IOException e) {
      log.error("Could not create file: ", e);
      return PdfDocument.EMPTY;
    }

    try {
      FileOutputStream outputStream = new FileOutputStream(file);
      document = new Document();
      PdfWriter.getInstance(document, outputStream);
      document.open();

      return new PdfDocument(document);
    } catch (FileNotFoundException e) {
      log.error("Cannot create PDF file", e);
    } catch (DocumentException e) {
      log.error("There was an error while getting PdfWriter instance", e);
    }

    return PdfDocument.EMPTY;
  }

}
