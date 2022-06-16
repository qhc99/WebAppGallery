package dev.qhc99.gallery_server.service;

import java.io.FileNotFoundException;

public class EpubToPdfService {

  public void convert(){
    // Open an existing EPUB file for reading.
    try (java.io.FileInputStream fileInputStream = new java.io.FileInputStream("input.epub")) {
      // Create an instance of PdfSaveOptions.
      com.aspose.html.saving.PdfSaveOptions options = new com.aspose.html.saving.PdfSaveOptions();

      // Call the ConvertEPUB method to convert the EPUB to PDF.
      com.aspose.html.converters.Converter.convertEPUB(
              fileInputStream,
              options,
              "output.pdf"
      );
    }
    catch (Exception e){
      throw new RuntimeException(e);
    }
  }
}
