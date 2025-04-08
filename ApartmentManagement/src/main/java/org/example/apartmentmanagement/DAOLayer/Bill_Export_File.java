package org.example.apartmentmanagement.DAOLayer;


import com.itextpdf.barcodes.Barcode128;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.DashedLine;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;

import java.io.FileOutputStream;

import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;

public class Bill_Export_File {
    public static void main(String[] args) throws Exception {
        String dest = "hoadon_dichvu.pdf";
        PdfWriter writer = new PdfWriter(new FileOutputStream(dest));
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);

        PdfFont font = PdfFontFactory.createFont("D:\\Java\\Bai_Tap_Lon\\ApartmentManagement\\src\\main\\resources\\Font\\arial.ttf", PdfEncodings.IDENTITY_H);
        doc.setFont(font);
        // 1. Tạo ảnh (logo)
        Image logo = new Image(ImageDataFactory.create("D:\\Java\\Bai_Tap_Lon\\ApartmentManagement\\src\\main\\resources\\Icon\\building.png"));
        logo.setWidth(100);

        // 2. Tạo bảng 2 cột: [Ảnh | Chữ]
        Table tableHeader = new Table(UnitValue.createPercentArray(new float[]{1,4}));
        tableHeader.setWidth(UnitValue.createPercentValue(100));

//         3. Cột 1: ảnh (không viền)
        Cell imgCell = new Cell().add(logo)
                .setBorder(Border.NO_BORDER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);
        tableHeader.addCell(imgCell);

        // 4. Cột 2: chữ căn giữa trong ô
        Paragraph tenChungCu = new Paragraph("Tên chung cư")
                .setFontSize(30)
                .setTextAlignment(TextAlignment.CENTER);
        Cell tenChungCuCell = new Cell().add(tenChungCu)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorder(Border.NO_BORDER);
        tableHeader.setMarginLeft(50)
                .setMarginRight(20);
        tableHeader.addCell(tenChungCuCell);

        doc.add(tableHeader);

        Paragraph diaChi = new Paragraph("Địa chỉ chung cư: ").setMarginLeft(50).setFontSize(18);
        doc.add(diaChi);

        // thêm centerTopParagraph
        Paragraph centerTopPara = new Paragraph().setTextAlignment(TextAlignment.CENTER);

        Text text1 = new Text("HÓA ĐƠN...\n").setBold().setFontSize(20);
        centerTopPara.add(text1);
        Text text2 = new Text("Số Hóa Đơn: ....\n").setFontSize(18);
        centerTopPara.add(text2);
        Text text3 = new Text("Ngày xuất hóa đơn:....\n ").setFontSize(18);
        centerTopPara.add(text3);
        doc.add(centerTopPara);

        Paragraph infoCustomer = new Paragraph().setTextAlignment(TextAlignment.LEFT).setMarginLeft(50);

        Text ten = new Text("Khách Hàng:...\n").setFontSize(18);
        Text sdt = new Text("Số điện thoại:...\n").setFontSize(18);
        Text address = new Text("Địa chỉ:...\n").setFontSize(18);
        infoCustomer.add(ten).add(sdt).add(address);
        doc.add(infoCustomer);

        // đường gạch ngang
        LineSeparator ls = new LineSeparator(new SolidLine(1)).setMarginTop(5)
                .setMarginBottom(5); // độ dày 1pt
        doc.add(ls);

        // thêm bảng
        float[] columnWidths = {1, 1, 1}; // Tỉ lệ cột (Đơn giá, Số lượng, Thành tiền)
        Table table = new Table(UnitValue.createPercentArray(columnWidths)).useAllAvailableWidth();

        Paragraph donGia = new Paragraph("Đơn giá");
        Paragraph soLuong = new Paragraph("Số lượng");
        Paragraph thanhTien = new Paragraph("Thành tiền");

        Paragraph dichVu1 = new Paragraph("Sửa ống nước");
        Paragraph dichVu2 = new Paragraph("Sửa đèn");
        Paragraph dichVu3 = new Paragraph("Giúp việc");

        // Thêm tiêu đề bảng
        table.addCell(new Cell().add(donGia).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER);
        table.addCell(new Cell().add(soLuong).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER);
        table.addCell(new Cell().add(thanhTien).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER);

        table.addCell(new Cell(1, 3).add(dichVu1).setBorder(Border.NO_BORDER));

        // Thêm các dòng dữ liệu
        table.addCell("100000").setBorder(Border.NO_BORDER);  // Đơn giá
        table.addCell("2").setBorder(Border.NO_BORDER);       // Số lượng
        table.addCell("200000").setBorder(Border.NO_BORDER);  // Thành tiền

        table.addCell(new Cell(1, 3).add(dichVu2).setBorder(Border.NO_BORDER));

        table.addCell("150000").setBorder(Border.NO_BORDER);  // Đơn giá
        table.addCell("3").setBorder(Border.NO_BORDER);       // Số lượng
        table.addCell("450000").setBorder(Border.NO_BORDER);  // Thành tiền

        table.addCell(new Cell(1, 3).add(dichVu3).setBorder(Border.NO_BORDER));

        table.addCell("200000").setBorder(Border.NO_BORDER);  // Đơn giá
        table.addCell("1").setBorder(Border.NO_BORDER);       // Số lượng
        table.addCell("200000").setBorder(Border.NO_BORDER);  // Thành tiền

        table.setBorder(Border.NO_BORDER);

        // Thêm bảng vào tài liệu
        doc.add(table);

        Barcode128 barcode = new Barcode128(pdf);
        barcode.setCode("MaHoaDon");
        barcode.setCodeType(Barcode128.CODE128);
        Image barcodeImage = new Image(barcode.createFormXObject(pdf));

        barcodeImage.setHorizontalAlignment(HorizontalAlignment.CENTER).setMarginTop(20);
        doc.add(barcodeImage);

//        Paragraph maHoaDonText = new Paragraph("MaHoaDon")
//                .setTextAlignment(TextAlignment.CENTER)
//                .setMarginTop(5);
//        doc.add(maHoaDonText);


        // nét đứt
        DashedLine dashedLine = new DashedLine(); // Mặc định độ dài nét và khoảng

        LineSeparator separator = new LineSeparator(dashedLine);
        doc.add(separator);

        doc.close();
        System.out.println("✔ PDF đã tạo: " + dest);





    }
}

