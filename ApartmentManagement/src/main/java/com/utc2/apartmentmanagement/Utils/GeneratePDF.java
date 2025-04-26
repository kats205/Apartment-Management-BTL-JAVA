package com.utc2.apartmentmanagement.Utils;


import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.utc2.apartmentmanagement.DAO.ApartmentDAO;
import com.utc2.apartmentmanagement.Model.Apartment;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.PdfEncodings;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import static javax.swing.text.StyleConstants.setBold;

public class GeneratePDF  {
    public static void generateApartmentPDF(List<Apartment> apartments, String filePath) {
        try {

            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            PdfFont vietnameseFont = PdfFontFactory.createFont("src/main/resources/times.ttf", PdfEncodings.IDENTITY_H);
            Document document = new Document(pdf);
            document.setFont(vietnameseFont);

            //title
            Paragraph Title = new Paragraph("DANH MỤC VÀ BIỂU MẪU THỐNG KÊ")
                    .setBold()
                    .setTextAlignment(TextAlignment.LEFT)
                    .setFontSize(12);
            // Add national header
            Paragraph nationalHeader = new Paragraph("CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM")
                    .setBold()
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontSize(12);
            float[] columnWidthsSlogan = {300, 300};
            Table headerTable = new Table(columnWidthsSlogan);

// Add Title to the first cell (left aligned)
            headerTable.addCell(new Cell().add(Title).setBorder(null));

// Add National Header to the second cell (right aligned)
            headerTable.addCell(new Cell().add(nationalHeader).setBorder(null));
            document.add(headerTable);
            Paragraph slogan = new Paragraph("Độc lập - Tự do - Hạnh phúc")
                    .setBold()
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontSize(12)
                    .setMarginBottom(10).setMarginRight(45);
            document.add(slogan);

            // Create a table with 2 columns to place title and date on the same row

            // Title
            Paragraph title = new Paragraph("Apartment List")
                    .setBold()
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title.setMarginBottom(15).setMarginTop(20));


            // Date
            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            Paragraph dateParagraph = new Paragraph(currentDate)
                    .setBold()
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(dateParagraph.setMarginBottom(15));

            float[] columnWidths = {100, 80, 50, 60, 60, 80, 80, 100};
            Table table = new Table(columnWidths);
            table.setTextAlignment(TextAlignment.CENTER);

            // Header row
            String[] headers = {"ID", "Building ID", "Floor", "Area", "Bedrooms", "Price", "Status", "Maintenance Fee"};
            for (String header : headers) {
                table.addHeaderCell(new Cell().add(new Paragraph(header).setBold()).setTextAlignment(TextAlignment.CENTER));
            }

            // Data rows
            for (Apartment apartment : apartments) {
                table.addCell(new Cell().add(new Paragraph(apartment.getApartmentID())).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(apartment.getBuildingID()))).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(apartment.getFloors()))).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(apartment.getArea()))).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(apartment.getBedRoom()))).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(apartment.getPriceApartment()))).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(apartment.getStatus())).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(apartment.getMaintanceFee()))).setTextAlignment(TextAlignment.CENTER));
            }

            document.add(table);
            document.close();
            System.out.println("PDF created successfully: " + filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
            List<Apartment> apartmentList = new ApartmentDAO().getAllApartments();
            generateApartmentPDF(apartmentList, "Apartment25.pdf");
    }
}
