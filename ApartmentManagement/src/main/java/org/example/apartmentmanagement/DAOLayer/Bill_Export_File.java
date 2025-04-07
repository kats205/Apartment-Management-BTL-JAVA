package org.example.apartmentmanagement.DAOLayer;


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



        // nét đứt
        DashedLine dashedLine = new DashedLine(); // Mặc định độ dài nét và khoảng

        LineSeparator separator = new LineSeparator(dashedLine);
        doc.add(separator);

        doc.close();
        System.out.println("✔ PDF đã tạo: " + dest);




//        doc.add(new Paragraph("4/1 Nam Cao, Tân Phú, Thủ Đức, TP.HCM"));
//        doc.add(new Paragraph("SĐT: 0909 999 999"));
//        doc.add(new Paragraph("\nMã Hóa Đơn: HD001"));
//        doc.add(new Paragraph("Ngày: 05/04/2025\n"));
//
//        // Bảng sản phẩm
//        Table table = new Table(UnitValue.createPercentArray(new float[]{4, 1, 2, 2}))
//                .useAllAvailableWidth();
//
//        table.addHeaderCell("Tên SP");
//        table.addHeaderCell("SL");
//        table.addHeaderCell("Đơn giá");
//        table.addHeaderCell("Thành tiền");
//
//        // Dữ liệu mẫu
//        table.addCell("Gạo ST25");
//        table.addCell("2");
//        table.addCell("18000");
//        table.addCell("36000");
//
//        table.addCell("Nước suối");
//        table.addCell("5");
//        table.addCell("4000");
//        table.addCell("20000");
//
//        table.addCell("Dầu ăn");
//        table.addCell("1");
//        table.addCell("40000");
//        table.addCell("40000");
//
//        doc.add(table);
//
//        doc.add(new Paragraph("\nTổng cộng: 96.000 đ").setBold().setTextAlignment(TextAlignment.RIGHT));
//
//        // Tạo QR code (giả sử chứa nội dung thanh toán)
//        Image qrImage = new Image(ImageDataFactory.create(generateQRCodeImage("Thanh toán HD001 - 96000")));
//        qrImage.setWidth(100);
//        qrImage.setMarginTop(10);
//        doc.add(qrImage);
//
//        doc.add(new Paragraph("\nCảm ơn quý khách. Hẹn gặp lại!").setTextAlignment(TextAlignment.CENTER));
//
//        doc.close();
//        System.out.println("✔ PDF đã tạo: " + dest);
//    }

        // Hàm tạo ảnh QR code (dưới dạng byte[])
//    private static byte[] generateQRCodeImage(String text) throws WriterException {
//        int size = 150;
//        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//        BitMatrix matrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, size, size);
//
//        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
//        for (int x = 0; x < size; x++) {
//            for (int y = 0; y < size; y++) {
//                image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
//            }
//        }
//
//        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
//            ImageIO.write(image, "png", baos);
//            return baos.toByteArray();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
    }
}

