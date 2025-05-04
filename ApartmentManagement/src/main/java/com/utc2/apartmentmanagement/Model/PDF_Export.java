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
import com.itextpdf.layout.properties.*;
import com.utc2.apartmentmanagement.DAO.ApartmentDAO;
import com.utc2.apartmentmanagement.DAO.PaymentDAO;
import com.utc2.apartmentmanagement.DAO.ReportDAO;
import com.utc2.apartmentmanagement.DAO.UserDAO;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
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
    private static final String FONT_PATH = "src/main/resources/com/utc2/apartmentmanagement/Font/times.ttf";
    private static final Color HEADER_COLOR = new DeviceRgb(209, 224, 227);
    private static final Color TEXT_HEAD_COLOR = new DeviceRgb(0, 0, 0);

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
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("Danh sách các thanh toán")
                .setBold()
                .setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph(" "));
    }

    // Tạo bảng dữ liệu thanh toán
    private static Table createPaymentTable(List<Payment> payments) throws SQLException {
        float[] columnWidths = {80F, 60F, 60F, 70F, 90F, 80F, 100F, 150F};

        Table table = new Table(columnWidths).setWidth(UnitValue.createPercentValue(100)).setTextAlignment(TextAlignment.CENTER);

        String[] headers = {"Mã thanh toán", "Mã hóa đơn", "Mã căn hộ", "Số tiền (VNĐ)", "Phương thức", "Trạng thái", "Loại hóa đơn","Chi tiết"};
    // Header
        for (String header : headers) {
            table.addHeaderCell(new Cell()
                    .add(new Paragraph(header))
                    .setBackgroundColor(HEADER_COLOR)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));
        }


        // Data rows
        List<Map<String, Object>>  paymentList = new PaymentDAO().listPaymentDetail();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        for (Map<String, Object> payment : paymentList) {
            table.addCell(new Cell().add(new Paragraph(String.valueOf(payment.get("payment_id"))))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));

            table.addCell(new Cell().add(new Paragraph(String.valueOf(payment.get("bill_id"))))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));

            table.addCell(new Cell().add(new Paragraph(payment.get("apartment_id") != null ? payment.get("apartment_id").toString() : ""))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));

            table.addCell(new Cell().add(new Paragraph(String.format("%.2f", payment.get("amount"))))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));

            table.addCell(new Cell().add(new Paragraph(payment.get("payment_method") != null ? payment.get("payment_method").toString() : ""))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));

            table.addCell(new Cell().add(new Paragraph(payment.get("status") != null ? payment.get("status").toString() : ""))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));

            table.addCell(new Cell().add(new Paragraph(payment.get("item_type") != null ? payment.get("item_type").toString() : ""))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));

            table.addCell(new Cell().add(new Paragraph(payment.get("description") != null ? payment.get("description").toString() : ""))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));
        }
        return table;
    }

    // Tạo bảng dữ liệu căn hộ
    private static Table createApartmentTable(List<Apartment> apartments, Color headerColor, Color textColor) {
        float[] columnWidths = {100F, 80F, 40F, 90F, 120F, 10F, 80F, 80F};
        Table table = new Table(columnWidths).setWidth(UnitValue.createPercentValue(100)).setTextAlignment(TextAlignment.CENTER);

        String[] headers = {"Mã căn hộ", "Tòa nhà", "Tầng", "Diện tích (m^2)", "Số phòng ngủ", "Giá thuê (VNĐ)", "Trạng thái", "Phí bảo trì"};
        for (String header : headers) {
            table.addCell(new Cell()
                    .add(new Paragraph(header)
                            .setVerticalAlignment(VerticalAlignment.MIDDLE)
                            .setTextAlignment(TextAlignment.CENTER)
                            .setBold()
                            .setFontColor(textColor))
                            .setBackgroundColor(headerColor));
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

    // Tạo bảng dữ liệu báo cáo
    private static Table createReportTable(List<Report> reports, Color headerColor, Color textColor) {
        // Date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    // Tạo bảng có 5 cột
        Table table = new Table(5);

    // Tiêu đề cột
        table.addCell(new Cell().add(new Paragraph("Loại báo cáo")).setTextAlignment(TextAlignment.CENTER).setBackgroundColor(HEADER_COLOR));
        table.addCell(new Cell().add(new Paragraph("Ngày tạo báo cáo")).setTextAlignment(TextAlignment.CENTER).setBackgroundColor(HEADER_COLOR));
        table.addCell(new Cell().add(new Paragraph("Người tạo báo cáo")).setTextAlignment(TextAlignment.CENTER).setBackgroundColor(HEADER_COLOR));
        table.addCell(new Cell().add(new Paragraph("Tham số")).setTextAlignment(TextAlignment.CENTER).setBackgroundColor(HEADER_COLOR));
        table.addCell(new Cell().add(new Paragraph("Ngày cập nhật")).setTextAlignment(TextAlignment.CENTER).setBackgroundColor(HEADER_COLOR));
        table.setHorizontalAlignment(HorizontalAlignment.CENTER); // căn giữa bảng

// Data rows
        List<Map<String, Object>> reportList = new ReportDAO().getAllReportsWithNamesWhoMakeReport();

        for (Map<String, Object> report : reportList) {
            table.addCell(new Cell().add(new Paragraph(report.get("report_type") != null ? report.get("report_type").toString() : ""))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));

            table.addCell(new Cell().add(new Paragraph(
                            report.get("generation_date") != null ? dateFormat.format(report.get("generation_date")) : ""))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));

            table.addCell(new Cell().add(new Paragraph(report.get("full_name") != null ? report.get("full_name").toString() : ""))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));

            table.addCell(new Cell().add(new Paragraph(report.get("parameters") != null ? report.get("parameters").toString() : ""))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));

            table.addCell(new Cell().add(new Paragraph(
                            report.get("updated_at") != null ? dateFormat.format(report.get("updated_at")) : ""))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));
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

    // Xuất file PDF và lấy đường dẫn tệp Apartment
    public static String exportApartmentList(String fileName) {
        try {
            String directoryPath = "src/main/resources/com/utc2/apartmentmanagement/PDF_File/";
            String filePath = directoryPath + "Apartment_List.pdf";
            createDirectoryIfNeeded(directoryPath);

            PdfFont arial = createVietnameseFont("src/main/resources/com/utc2/apartmentmanagement/Font/arial.ttf");
            PdfFont times = createVietnameseFont("src/main/resources/com/utc2/apartmentmanagement/Font/times.ttf");

            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            document.setFont(times);

            // Tạo nội dung
            document.add(createHeaderSection());

            document.add(new Paragraph("BÁO CÁO").setBold().setFontSize(15).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("DANH SÁCH CÁC CĂN HỘ CỦA CHUNG CƯ").setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph(""));

            List<Apartment> apartments = new ApartmentDAO().getAllApartments();
            document.add(createApartmentTable(apartments, HEADER_COLOR, TEXT_HEAD_COLOR));
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

    // Xuất file PDF và lấy đường dẫn tệp Payment
    public static String exportPaymentList(String fileName) throws Exception {
        String directoryPath = "src/main/resources/com/utc2/apartmentmanagement/PDF_File/";
        createDirectoryIfNeeded(directoryPath);

        String filePath = directoryPath + fileName;
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

    // Xuất file PDF và lấy đường dẫn tệp Report
    public static String exportReportList(Node node1,Node node2, String fileName) throws Exception {

        String directoryPath = "src/main/resources/com/utc2/apartmentmanagement/PDF_File/";
        createDirectoryIfNeeded(directoryPath);

        String filePath = directoryPath + fileName;
        PdfWriter writer = new PdfWriter(filePath);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);


        // Thiết lập font tiếng Việt
        document.setFont(createVietnameseFont());

        // Tạo nội dung
        document.add(createHeaderSection());

        document.add(new Paragraph("BÁO CÁO").setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("DANH SÁCH CÁC BÁO CÁO").setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph(""));


        List<Report> reportList = new ReportDAO().getAllReports();
        document.add(createReportTable(reportList,HEADER_COLOR,TEXT_HEAD_COLOR));

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
        image1.scaleToFit(450, 550);

        Image image2 = new Image(ImageDataFactory.create(imageInByte2));
        image2.scaleToFit(450, 550);

        Table table = new Table(UnitValue.createPercentArray(new float[]{2, 1})).useAllAvailableWidth();

        // Tạo và thêm đường kẻ ngang nửa trang (chiều dài khoảng 250 điểm, ~nửa trang A4)
        LineSeparator separator = new LineSeparator(new SolidLine());
        separator.setWidth(250); // hoặc dùng .setWidthPercent(50);
        separator.setHorizontalAlignment(HorizontalAlignment.CENTER); // căn giữa đường kẻ

        document.add(new Paragraph().add(separator).setTextAlignment(TextAlignment.CENTER));
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE)); // chuyển sang trang mới

        document.add(new Paragraph(""));
        document.add(new Paragraph("BIỂU ĐỒ").setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph(""));

        document.add(image1); // thêm hình ảnh
        document.add(new Paragraph("Doanh thu theo tháng").setBold().setTextAlignment(TextAlignment.CENTER));

        document.add(image2);
        document.add(new Paragraph("Tỷ lệ trạng thái hóa đơn").setBold().setTextAlignment(TextAlignment.CENTER));


        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE)); // chuyển sang trang mới

        document.add(new Paragraph("Tóm tắt").setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER));
        document.add(createTomTatPDF());

        addExportDate(document);

        document.close();

        return  filePath;
    }


}
