package com.stanislavgrujic.psttopdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PdfDocument {

  private static final Font SUBJECT = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
  private static final Font TEXT = FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.BLACK);

  public static final PdfDocument EMPTY = new PdfDocument();

  private Document document;

  private PdfDocument() {
    this.document = null;
  }

  public PdfDocument(Document document) {
    this.document = document;
  }

  public void addEmail(String subject, String text) {
    if (document == null) {
      return;
    }

    try {
      Chunk emailSubject = new Chunk(subject, SUBJECT);
      document.add(emailSubject);

      Chunk emailBody = new Chunk(text, TEXT);
      document.add(emailBody);
    } catch (DocumentException e) {
      log.error("Could not write to document", e);
    }

  }

  public void close() {
    document.close();
  }
}
