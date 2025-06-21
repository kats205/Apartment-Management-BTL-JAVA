package com.utc2.apartmentmanagement.Model;


import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.utc2.apartmentmanagement.DAO.Apartment.ApartmentDAO;
import com.utc2.apartmentmanagement.DAO.Billing.PaymentDAO;
import com.utc2.apartmentmanagement.DAO.Report.ReportDAO;
import com.utc2.apartmentmanagement.DAO.User.UserDAO;
import com.utc2.apartmentmanagement.Model.Apartment.Apartment;
import com.utc2.apartmentmanagement.Model.Billing.Payment;
import com.utc2.apartmentmanagement.Model.Report.Report;
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
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
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
                .setTextAlignment(TextAlignment.CENTER);

        Paragraph lineLeft = new Paragraph("_____________________")
                .setMarginTop(0)
                .setMarginBottom(5)
                .setTextAlignment(TextAlignment.CENTER);

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
                .setMarginBottom(5)
                .setTextAlignment(TextAlignment.CENTER);

        Paragraph slogan = new Paragraph("Độc lập - Tự do - Hạnh phúc")
                .setItalic()
                .setFontSize(11)
                .setTextAlignment(TextAlignment.CENTER);

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
    private static Table createReportTable(List<Report> reports, Color headerColor, Color textColor) {
        float[] columnWidths = {100F, 80F, 80F, 60F, 120F, 80F, 80F};

        Table table = new Table(columnWidths).setTextAlignment(TextAlignment.CENTER);

        String[] headers = {"Loại báo cáo", "Ngày tạo báo cáo", "Người tạo báo cáo",
                "Tham số", "Đường dẫn tệp", "Ngày tạo", "Ngày cập nhật"};

        for (String header : headers) {
            table.addCell(new Cell()
                    .add(new Paragraph(header).setBold().setFontColor(textColor))
                    .setBackgroundColor(headerColor));
        }

        for (Report report : reports) {
            table.addCell(new Cell()
                    .add(new Paragraph(report.getReportType()).setTextAlignment(TextAlignment.CENTER))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setPadding(5));

            table.addCell(new Cell()
                    .add(new Paragraph(report.getGenerationDate().toString()).setTextAlignment(TextAlignment.CENTER))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setPadding(5));

            table.addCell(new Cell()
                    .add(new Paragraph(String.valueOf(report.getGeneratedByUserId())).setTextAlignment(TextAlignment.CENTER))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setPadding(5));

            table.addCell(new Cell()
                    .add(new Paragraph(report.getParameters()).setTextAlignment(TextAlignment.CENTER))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setPadding(5));

            table.addCell(new Cell()
                    .add(new Paragraph(report.getFilePath())
                            .setFontSize(9)
                            .setTextAlignment(TextAlignment.CENTER)) // Chuyển từ LEFT → CENTER để đồng bộ
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setPadding(5)
                    .setMaxWidth(150));

            table.addCell(new Cell()
                    .add(new Paragraph(report.getCreatedAt().toString()).setTextAlignment(TextAlignment.CENTER))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setPadding(5));

            table.addCell(new Cell()
                    .add(new Paragraph(report.getUpdatedAt().toString()).setTextAlignment(TextAlignment.CENTER))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setPadding(5));
        }
        table.setTextAlignment(TextAlignment.CENTER);
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
            String directoryPath = "src/main/resources/com/utc2/apartmentmanagement/PDF_File/";
            String filePath = directoryPath + "Apartment_List.pdf";
            createDirectoryIfNeeded(directoryPath);



            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            Color headerColor = new DeviceRgb(209, 224, 227);
            Color textColor = new DeviceRgb(21, 88, 155);


            // Tạo nội dung
            document.add(createHeaderSection());

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
        String directoryPath = "src/main/resources/com/utc2/apartmentmanagement/PDF_File/";
        createDirectoryIfNeeded(directoryPath);

        String filePath = directoryPath + fileName;
        PdfWriter writer = new PdfWriter(filePath);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Thiết lập font tiếng Việt
//        document.setFont(createVietnameseFont());

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



    // Xuất bảng phụ tóm tắt
    private static Table createTomTatPDF(){
        List<Report> reportList = new ReportDAO().getAllReports();
        int totalReport = reportList.size();
        String newestReport = reportList.getLast().getReportType();
        // Map 1: Đếm số lần mỗi loại báo cáo
        Map<String, Integer> reportTypeCountMap = new HashMap<>();

        // Map 2: Đếm số lần mỗi user tạo báo cáo
        Map<Integer, Integer> userReportCountMap = new HashMap<>();

        for (Report report : reportList) {
            // Đếm loại báo cáo
            String type = report.getReportType();
            reportTypeCountMap.put(type, reportTypeCountMap.getOrDefault(type, 0) + 1);

            // Đếm theo user
            int userId = report.getGeneratedByUserId();
            userReportCountMap.put(userId, userReportCountMap.getOrDefault(userId, 0) + 1);
        }

        // Tìm loại báo cáo phổ biến nhất
        String popularReport = reportTypeCountMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        // Tìm user tạo nhiều báo cáo nhất
        Integer mostActiveUserId = userReportCountMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        String nameUserById = new UserDAO().getUserByID(mostActiveUserId).getFullName();

        float[] columnWidths = {200F, 300F}; // Chiều rộng 2 cột

        Table summaryTable = new Table(columnWidths);
        summaryTable.setWidth(UnitValue.createPercentValue(100)); // Chiếm toàn bộ chiều ngang

// Hàng 1
        summaryTable.addCell(new Cell().add(new Paragraph("Tổng số báo cáo:").setBold())
                .setBorder(Border.NO_BORDER));
        summaryTable.addCell(new Cell().add(new Paragraph(String.valueOf(totalReport))) // bạn có thể thay bằng biến
                .setTextAlignment(TextAlignment.LEFT) // hoặc CENTER / RIGHT nếu cần
                .setBorder(Border.NO_BORDER));

// Hàng 2
        summaryTable.addCell(new Cell().add(new Paragraph("Báo cáo mới nhất:").setBold())
                .setBorder(Border.NO_BORDER));
        summaryTable.addCell(new Cell().add(new Paragraph(newestReport))
                .setBorder(Border.NO_BORDER));

// Hàng 3
        summaryTable.addCell(new Cell().add(new Paragraph("Loại báo cáo phổ biến:").setBold())
                .setBorder(Border.NO_BORDER));
        summaryTable.addCell(new Cell().add(new Paragraph(popularReport))
                .setBorder(Border.NO_BORDER));

// Hàng 4
        summaryTable.addCell(new Cell().add(new Paragraph("Người tạo nhiều nhất:").setBold())
                .setBorder(Border.NO_BORDER));
        summaryTable.addCell(new Cell().add(new Paragraph(String.valueOf(mostActiveUserId)))
                .setBorder(Border.NO_BORDER));

// Thêm bảng vào document
        return summaryTable;

    }
    // Xuất node ra PDF
    public static String exportNodeToPDF(Node node1,Node node2, String fileName) throws Exception {

        String directoryPath = "src/main/resources/com/utc2/apartmentmanagement/PDF_File/";
        createDirectoryIfNeeded(directoryPath);

        String filePath = directoryPath + fileName;
        PdfWriter writer = new PdfWriter(filePath);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        Color headerColor = new DeviceRgb(209, 224, 227);
        Color textColor = new DeviceRgb(21, 88, 155);


        // Tạo nội dung
        document.add(createHeaderSection());

        document.add(new Paragraph("BÁO CÁO").setBold().setFontColor(textColor).setFontSize(14).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("DANH SÁCH CÁC BÁO CÁO").setBold().setFontColor(textColor).setFontSize(14).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph(""));


        List<Report> reportList = new ReportDAO().getAllReports();
        document.add(createReportTable(reportList,headerColor,textColor));

        WritableImage fxImage1 = captureNode(node1);
        BufferedImage bufferedImage1 = fromFXImage(fxImage1);

        WritableImage fxImage2 = captureNode(node2);
        BufferedImage bufferedImage2 = fromFXImage(fxImage2);

        ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage1,"png", byteArrayOutputStream1);
        byteArrayOutputStream1.flush();
        byte[] imageInByte1 = byteArrayOutputStream1.toByteArray();
        byteArrayOutputStream1.close();

        ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage2,"png", byteArrayOutputStream2);
        byteArrayOutputStream2.flush();
        byte[] imageInByte2 = byteArrayOutputStream2.toByteArray();
        byteArrayOutputStream2.close();

        Image image1 = new Image(ImageDataFactory.create(imageInByte1));
        image1.scaleToFit(500, 700);

        Image image2 = new Image(ImageDataFactory.create(imageInByte2));
        image2.scaleToFit(500, 700);

        Table table = new Table(UnitValue.createPercentArray(new float[]{2, 1})).useAllAvailableWidth();

        // Tạo và thêm đường kẻ ngang nửa trang (chiều dài khoảng 250 điểm, ~nửa trang A4)
        LineSeparator separator = new LineSeparator(new SolidLine());
        separator.setWidth(250); // hoặc dùng .setWidthPercent(50);
        separator.setHorizontalAlignment(HorizontalAlignment.CENTER); // căn giữa đường kẻ

        document.add(new Paragraph().add(separator).setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph(""));
        document.add(new Paragraph("BIỂU ĐỒ").setBold().setFontColor(textColor).setFontSize(14).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph(""));

        document.add(new Paragraph("Doanh thu theo tháng").setBold().setTextAlignment(TextAlignment.CENTER));
        document.add(image1); // thêm hình ảnh

        document.add(new Paragraph("Tỷ lệ trạng thái hóa đơn").setBold().setTextAlignment(TextAlignment.CENTER));
        document.add(image2);

        document.add(new Paragraph().add(separator).setTextAlignment(TextAlignment.CENTER));

        document.add(createTomTatPDF());


        document.close();

        return  filePath;
    }

}
