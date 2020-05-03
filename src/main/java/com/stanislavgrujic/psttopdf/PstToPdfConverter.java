package com.stanislavgrujic.psttopdf;

import com.pff.PSTException;
import com.pff.PSTFile;
import com.pff.PSTFolder;
import com.pff.PSTMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Vector;

@Component
@Slf4j
public class PstToPdfConverter {

  @Value("${com.stanislavgrujic.psttopdf.filetoprocess}")
  private String fileToProcess;

  private final PdfDocumentCreator pdfDocumentCreator;

  public PstToPdfConverter(PdfDocumentCreator pdfDocumentCreator) {
    this.pdfDocumentCreator = pdfDocumentCreator;
  }

  @PostConstruct
  public void processFile() {
    try {
      log.info("Process file '{}'", fileToProcess);
      PSTFile pstFile = new PSTFile(fileToProcess);
      processFolder(pstFile.getRootFolder());
    } catch (Exception e) {
      log.error("There was an error in reading file:", e);
    }
  }

  int depth = -1;

  public void processFolder(PSTFolder folder) throws PSTException, java.io.IOException {

    log.info("Processing folder '{}'", folder.getDisplayName());
    depth++;
    // the root folder doesn't have a display name
    if (depth > 0) {
      printDepth();
      log.info(folder.getDisplayName());
    }

    // go through the folders...
    if (folder.hasSubfolders()) {
      Vector<PSTFolder> childFolders = folder.getSubFolders();
      for (PSTFolder childFolder : childFolders) {
        processFolder(childFolder);
      }
    }

    PdfDocument pdfDocument = pdfDocumentCreator.createDocument(folder.getDisplayName());

    // and now the emails for this folder
    if (folder.getContentCount() > 0) {
      depth++;
      PSTMessage email = (PSTMessage) folder.getNextChild();
      while (email != null) {
        pdfDocument.addEmail(email.getSubject(), email.getBody());
        email = (PSTMessage) folder.getNextChild();
        break;
      }
      pdfDocument.close();
      depth--;
    }
    depth--;
  }

  public void printDepth() {
    for (int x = 0; x < depth - 1; x++) {
      log.info(" | ");
    }
    log.info(" |- ");
  }
}
