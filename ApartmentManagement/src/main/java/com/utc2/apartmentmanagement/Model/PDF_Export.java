package com.utc2.apartmentmanagement.Model;


import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.utc2.apartmentmanagement.DAO.Apartment.ApartmentDAO;
import com.utc2.apartmentmanagement.DAO.Billing.PaymentDAO;
import com.utc2.apartmentmanagement.Model.Apartment.Apartment;
import com.utc2.apartmentmanagement.Model.Billing.Payment;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class PDF_Export {
    private static final String FONT_PATH = "src/main/resources/com/utc2/apartmentmanagement/Font/arial.ttf";
    private static final Color HEADER_COLOR = new DeviceRgb(209, 224, 227);
    private static final Color TEXT_HEAD_COLOR = new DeviceRgb(21, 88, 155);

    // Tạo thư mục lưu file nếu chưa tồn tại
    private static void createDirectoryIfNeeded(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    // Tạo font hỗ trợ tiếng Việt
    private static PdfFont createVietnameseFont() throws Exception {
        return PdfFontFactory.createFont(FONT_PATH, "Identity-H");
    }

    private static PdfFont createVietnameseFont(String fontPath) throws IOException {
        return PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H);
    }

    // Tạo tiêu đề đầu trang
    private static Table createHeaderSection() {
        float[] columnWidthsHeader = {280F, 280F};
        Table headerTable = new Table(columnWidthsHeader).setWidth(UnitValue.createPercentValue(100));

        // Cột trái
        Paragraph left = new Paragraph("CHUNG CƯ ABC")
                .setBold()
                .setFontSize(12)
                .setTextAlignment(TextAlignment.LEFT);

        Paragraph lineLeft = new Paragraph("_____________________")
                .setMarginTop(0)
                .setMarginBottom(5);

        Cell leftCell = new Cell()
                .add(left)
                .add(lineLeft)
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.LEFT);

        // Cột phải
        Paragraph right = new Paragraph("CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM")
                .setBold()
                .setFontSize(12)
                .setTextAlignment(TextAlignment.RIGHT);

        Paragraph lineRight = new Paragraph("_____________________")
                .setMarginTop(0)
                .setMarginBottom(5);

        Paragraph slogan = new Paragraph("Độc lập - Tự do - Hạnh phúc")
                .setItalic()
                .setFontSize(11)
                .setTextAlignment(TextAlignment.RIGHT);

        Cell rightCell = new Cell()
                .add(right)
                .add(lineRight)
                .add(slogan)
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT);

        headerTable.addCell(leftCell);
        headerTable.addCell(rightCell);

        return headerTable;
    }

    // Tạo tiêu đề báo cáo
    private static void addReportTitle(Document document) {
        document.add(new Paragraph("BÁO CÁO")
                .setBold()
                .setFontColor(TEXT_HEAD_COLOR)
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("Danh sách các thanh toán")
                .setBold()
                .setFontColor(TEXT_HEAD_COLOR)
                .setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph(" "));
    }

    // Tạo bảng dữ liệu thanh toán
    private static Table createPaymentTable(List<Payment> payments) throws SQLException {
        float[] columnWidths = {60F, 60F, 80F, 90F, 90F, 80F, 100F, 150F};

        Table table = new Table(columnWidths).setWidth(UnitValue.createPercentValue(100)).setTextAlignment(TextAlignment.CENTER);

        //    String sql = "SELECT \n" +
        //                "    p.payment_id, \n" +
        //                "    b.bill_id, \n" +
        //                "    bi.apartment_id, \n" +
        //                "    p.amount, \n" +
        //                "    p.payment_method, \n" +
        //                "    p.status, \n" +
        //                "    b.item_type, \n" +
        //                "    b.description\n" +
        //                "FROM Payment p\n" +
        //                "JOIN BillItem b ON p.bill_id = b.bill_id\n" +
        //                "JOIN Bill bi ON p.bill_id = bi.bill_id";
        // Header
        String[] headers = {"Mã thanh toán", "Mã hóa đơn", "Mã căn hộ", "Số tiền (VNĐ)", "Phương thức", "Trạng thái", "Loại hóa đơn","Chi tiết"};
        for (String header : headers) {
            table.addHeaderCell(new Cell().add(new Paragraph(header))
                    .setBackgroundColor(HEADER_COLOR)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER));
        }

        // Data rows
        List<Map<String, Object>>  paymentList = new PaymentDAO().listPaymentDetail();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        for (Map<String, Object> payment : paymentList) {
            table.addCell(new Cell().add(new Paragraph(String.valueOf(payment.get("payment_id")))));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(payment.get("bill_id")))));
            table.addCell(new Cell().add(new Paragraph(payment.get("apartment_id") != null ? payment.get("apartment_id").toString() : "")));
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f", payment.get("amount")))));
            table.addCell(new Cell().add(new Paragraph(payment.get("payment_method") != null ? payment.get("payment_method").toString() : "")));
            table.addCell(new Cell().add(new Paragraph(payment.get("status") != null ? payment.get("status").toString() : "")));
            table.addCell(new Cell().add(new Paragraph(payment.get("item_type") != null ? payment.get("item_type").toString() : "")));
            table.addCell(new Cell().add(new Paragraph(payment.get("description") != null ? payment.get("description").toString() : "")));
        }
        return table;
    }

    // Tạo bảng dữ liệu căn hộ
    private static Table createApartmentTable(List<Apartment> apartments, Color headerColor, Color textColor) {
        float[] columnWidths = {100F, 80F, 40F, 90F, 120F, 10F, 80F, 80F};
        Table table = new Table(columnWidths).setTextAlignment(TextAlignment.CENTER);

        String[] headers = {"Mã căn hộ", "Tòa nhà", "Tầng", "Diện tích (m^2)", "Số phòng ngủ", "Giá thuê (VNĐ)", "Trạng thái", "Phí bảo trì"};
        for (String header : headers) {
            table.addCell(new Cell().add(new Paragraph(header).setBold().setFontColor(textColor)).setBackgroundColor(headerColor));
        }

        for (Apartment apt : apartments) {
            table.addCell(new Cell().add(new Paragraph(apt.getApartmentID())));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(apt.getBuildingID()))));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(apt.getFloors()))));
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f", apt.getArea()))));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(apt.getBedRoom()))));
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f", apt.getPriceApartment()))));
            table.addCell(new Cell().add(new Paragraph(apt.getStatus())));
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f", apt.getMaintanceFee()))));
        }

        return table;
    }

    private static void addApartmentStatistics(Document document, List<Apartment> apartments) {
        int total = apartments.size();
        int occupied = 0, available = 0, maintenance = 0, reserved = 0;

        for (Apartment a : apartments) {
            switch (a.getStatus().trim()) {
                case "occupied" -> occupied++;
                case "available" -> available++;
                case "maintenance" -> maintenance++;
                case "reserved" -> reserved++;
            }
        }

        String stats = "Tổng số phòng: " + total +
                "\nTổng số phòng occupied: " + occupied +
                "\nTổng số phòng available: " + available +
                "\nTổng số phòng maintenance: " + maintenance +
                "\nTổng số phòng reserved: " + reserved;

        document.add(new Paragraph(stats).setFontSize(12).setItalic().setTextAlignment(TextAlignment.RIGHT));
    }

    private static void addExportDate(Document document) {
        String dateStr = "Ngày xuất báo cáo: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        document.add(new Paragraph(dateStr).setFontSize(12).setItalic().setTextAlignment(TextAlignment.LEFT));
    }


    public static String exportApartmentList(String fileName) {
        try {
            String directoryPath = "PDF/Apartment/";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");
            String timestamp = LocalDateTime.now().format(formatter);
            fileName = "Apartment_List_" + timestamp + ".pdf";
            String filePath = directoryPath + fileName;
            createDirectoryIfNeeded(directoryPath);

            PdfFont arial = createVietnameseFont("src/main/resources/com/utc2/apartmentmanagement/Font/arial.ttf");
            PdfFont times = createVietnameseFont("src/main/resources/com/utc2/apartmentmanagement/Font/times.ttf");

            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            document.setFont(times);

            Color headerColor = new DeviceRgb(209, 224, 227);
            Color textColor = new DeviceRgb(21, 88, 155);

            createHeaderSection();
            document.add(new Paragraph("BÁO CÁO").setBold().setFontColor(textColor).setFontSize(14).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("DANH SÁCH CÁC CĂN HỘ CỦA CHUNG CƯ").setBold().setFontColor(textColor).setFontSize(14).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph(""));

            List<Apartment> apartments = new ApartmentDAO().getAllApartments();
            document.add(createApartmentTable(apartments, headerColor, textColor));
            addApartmentStatistics(document, apartments);
            addExportDate(document);

            document.close();
            return filePath;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Phương thức xuất PDF chính
    public static String exportPaymentList(String fileName) throws Exception {
        String directoryPath = "PDF/Payment/";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");
        String timestamp = LocalDateTime.now().format(formatter);
        fileName = "Payment_List_" + timestamp + ".pdf";
        String filePath = directoryPath + fileName;

        createDirectoryIfNeeded(directoryPath);

        PdfWriter writer = new PdfWriter(filePath);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Thiết lập font tiếng Việt
        document.setFont(createVietnameseFont());

        // Tạo nội dung
        document.add(createHeaderSection());
        addReportTitle(document);

        List<Payment> payments = new PaymentDAO().getAllPayment();
        document.add(createPaymentTable(payments));
        addExportDate(document);

        document.close();
        return filePath;

    }


    // Chụp ảnh từ một Node bất kỳ (VBox, Chart, ...)
    public static WritableImage captureNode(Node node){
        return node.snapshot(new SnapshotParameters(),null);
    }

    // Chuyển ảnh JavaFX sang BufferedImage
    public static BufferedImage fromFXImage(WritableImage fxIgmage){
        return SwingFXUtils.fromFXImage(fxIgmage,null);
    }

    // Xuất node ra PDF
    public static String exportNodeToPDF(Node node, String filePath) throws Exception {

        String directoryPath = "PDF/Report/";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");
        String timestamp = LocalDateTime.now().format(formatter);
        String fileName = "report_List_" + timestamp + ".pdf";
        filePath = directoryPath + fileName;

        WritableImage fxImage = captureNode(node);
        BufferedImage bufferedImage = fromFXImage(fxImage);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage,"png", byteArrayOutputStream);
        byteArrayOutputStream.flush();
        byte[] imageInByte = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();

        PdfWriter writer = new PdfWriter(filePath);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        Image image = new Image(ImageDataFactory.create(imageInByte));
        image.scaleToFit(300, 500);

        document.add(image); // thêm hình ảnh
        document.close();
        return filePath;
    }

}
