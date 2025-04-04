package org.example.apartmentmanagement.controllers;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.example.apartmentmanagement.DAOLayer.ApartmentDAO;
import org.example.apartmentmanagement.models.Apartment;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class ExportPDF {
    public static void main(String[] args) throws FileNotFoundException {
        ExportPDF e = new ExportPDF();
        ApartmentDAO a = new ApartmentDAO();
        e.generatePDF(a.getAllApartments());
    }
    public void generatePDF(List<Apartment> apartments) throws FileNotFoundException {
        String dest = "D:\\Java\\Bai_Tap_Lon\\ApartmentManagement\\src\\main\\java\\org\\example\\apartmentmanagement\\utils\\demo.pdf";
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        pdfDocument.addNewPage();
        document.add(new Paragraph("Apartment List").setBold().setFontSize(16));

        float[] columnWidths = {100, 80, 50, 70, 50, 90, 80, 100};

        Table table = new Table(columnWidths);
        table.addCell("Apartment ID");
        table.addCell("Building ID");
        table.addCell("Floors");
        table.addCell("Area (m²)");
        table.addCell("Bedroom");
        table.addCell("Price (USD)");
        table.addCell("Status");
        table.addCell("Maintenance Fee (USD)");

        for (Apartment apartment : apartments){
            table.addCell(apartment.getApartmentID());
            table.addCell(String.valueOf(apartment.getBuildingID()));
            table.addCell(String.valueOf(apartment.getFloors()));
            table.addCell(String.valueOf(apartment.getArea()));
            table.addCell(String.valueOf(apartment.getBedRoom()));
            table.addCell(String.format("$%.2f", apartment.getPriceApartment()));
            table.addCell(apartment.getStatus());
            table.addCell(String.format("$%.2f", apartment.getMaintenanceFee()));
        }
        document.add(table);


        document.close();



    }

}
