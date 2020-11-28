package com.stanislavgrujic.psttopdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class PdfDocument {

  private static final Font FONT_SUBJECT = FontFactory.getFont(FontFactory.COURIER, 14, Font.BOLDITALIC, BaseColor.BLACK);
  private static final Font FONT_TEXT = FontFactory.getFont(FontFactory.COURIER, 12, BaseColor.BLACK);

  private static final Chunk NEW_LINE = new Chunk(System.lineSeparator());
  private static final Chunk SEPARATOR = new Chunk("--------------------------------------");

  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.yy.yyyy.");

  public static final PdfDocument EMPTY = new PdfDocument();

  private Document document;

  private PdfDocument() {
    this.document = null;
  }

  public PdfDocument(Document document) {
    this.document = document;
  }

  public void addEmail(String subject, Date date, String text) {
    if (document == null) {
      return;
    }

    try {
      Paragraph header = createHeader(date, subject);
      document.add(header);

      Paragraph emailBody = new Paragraph(text, FONT_TEXT);
      document.add(emailBody);

      document.add(SEPARATOR);
    } catch (DocumentException e) {
      log.error("Could not write to document", e);
    }

  }

  private Paragraph createHeader(Date date, String subject) {
    Paragraph header = new Paragraph();
    Chunk emailDate = new Chunk(DATE_FORMAT.format(date), FONT_SUBJECT);
    header.add(emailDate);

    header.add(NEW_LINE);

    Chunk emailSubject = new Chunk(subject, FONT_SUBJECT);
    header.add(emailSubject);

    return header;
  }

  public void close() {
    document.close();
  }
}
