package org.example.apartmentmanagement.Utils;


import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.FileNotFoundException;

public class GeneratePDF  {
    private static String FILE_PATH = "Apartment.pdf";
    String path = "Apartment.pdf";
    PdfWriter pdf = new PdfWriter(path);

    private void createPDF(String path) throws FileNotFoundException {
        // Bước 1: Tạo PdfWriter
        PdfWriter writer = new PdfWriter(path);

        // Bước 2: Tạo PdfDocument
        PdfDocument pdf = new PdfDocument(writer);

        // Bước 3: Tạo tài liệu và thêm nội dung
        Document document = new Document(pdf);
        document.add(new Paragraph("Báo cáo căn hộ"));

        // Bước 4: Đóng tài liệu
        document.close();
        System.out.println("PDF đã tạo: " + path);
    }
    public GeneratePDF() throws FileNotFoundException {
        createPDF(FILE_PATH);
    }

    public static void main(String[] args) {
        try{
            new GeneratePDF();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }
}
