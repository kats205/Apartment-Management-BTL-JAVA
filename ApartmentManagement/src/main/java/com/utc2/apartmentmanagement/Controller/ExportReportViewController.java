//package com.utc2.apartmentmanagement.Controller;
//
//import com.itextpdf.text.*;
//import com.itextpdf.text.pdf.*;
//import com.utc2.apartmentmanagement.Model.MaintenanceRecord;
//import com.utc2.apartmentmanagement.Service.MaintenanceService;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.*;
//import javafx.scene.control.TextField;
//import javafx.stage.DirectoryChooser;
//import javafx.stage.Stage;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.net.URL;
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import java.util.ResourceBundle;
//
//public class ExportReportViewController implements Initializable {
//
//    @FXML
//    private TextField companyNameField;
//
//    @FXML
//    private TextField companyAddressField;
//
//    @FXML
//    private TextField reportTitleField;
//
//    @FXML
//    private DatePicker fromDatePicker;
//
//    @FXML
//    private DatePicker toDatePicker;
//
//    @FXML
//    private ComboBox<String> shiftComboBox;
//
//    @FXML
//    private ComboBox<String> machineComboBox;
//
//    @FXML
//    private ComboBox<String> repairPersonComboBox;
//
//    @FXML
//    private TextField reportCreatorField;
//
//    @FXML
//    private DatePicker reportDatePicker;
//
//    // CheckBoxes for columns
//    @FXML
//    private CheckBox dateCheckBox;
//
//    @FXML
//    private CheckBox weekCheckBox;
//
//    @FXML
//    private CheckBox shiftCheckBox;
//
//    @FXML
//    private CheckBox machineCheckBox;
//
//    @FXML
//    private CheckBox errorTimeCheckBox;
//
//    @FXML
//    private CheckBox machineStatusCheckBox;
//
//    @FXML
//    private CheckBox errorCauseCheckBox;
//
//    @FXML
//    private CheckBox repairPersonCheckBox;
//
//    @FXML
//    private CheckBox solutionCheckBox;
//
//    @FXML
//    private CheckBox startTimeCheckBox;
//
//    @FXML
//    private CheckBox endTimeCheckBox;
//
//    @FXML
//    private CheckBox noteCheckBox;
//
//    // Radio buttons for format
//    @FXML
//    private RadioButton pdfRadioButton;
//
//    @FXML
//    private RadioButton excelRadioButton;
//
//    @FXML
//    private RadioButton wordRadioButton;
//
//    @FXML
//    private ToggleGroup formatToggleGroup;
//
//    @FXML
//    private Button previewButton;
//
//    @FXML
//    private Button exportButton;
//
//    @FXML
//    private Button cancelButton;
//
//    private MaintenanceService maintenanceService;
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        // Khởi tạo service
//        maintenanceService = new MaintenanceService();
//
//        // Thiết lập ngày mặc định
//        fromDatePicker.setValue(LocalDate.now().minusMonths(1));
//        toDatePicker.setValue(LocalDate.now());
//        reportDatePicker.setValue(LocalDate.now());
//
//        // Khởi tạo ComboBox
//        initComboBoxes();
//
//        // Thiết lập sự kiện cho các nút
//        setupButtonActions();
//    }
//
//    // Khởi tạo các combobox
//    private void initComboBoxes() {
//        // Khởi tạo combobox ca
//        ObservableList<String> shifts = FXCollections.observableArrayList(
//                "Ca 1",
//                "Ca 2",
//                "Ca 3",
//                "Tất cả"
//        );
//        shiftComboBox.setItems(shifts);
//        shiftComboBox.getSelectionModel().select("Tất cả");
//
//        // Khởi tạo combobox máy (giả định dữ liệu)
//        ObservableList<String> machines = FXCollections.observableArrayList(
//                "M9.1",
//                "M3.1",
//                "M3.2",
//                "M2.4",
//                "M2.7",
//                "M10.1",
//                "M3.6",
//                "Tất cả"
//        );
//        machineComboBox.setItems(machines);
//        machineComboBox.getSelectionModel().select("Tất cả");
//
//        // Khởi tạo combobox người sửa chữa (giả định dữ liệu)
//        ObservableList<String> repairPersons = FXCollections.observableArrayList(
//                "CDC4.1",
//                "CDD5.2",
//                "CDC1.1",
//                "CDK2.1",
//                "CDD2.1+CDC2.1",
//                "Tất cả"
//        );
//        repairPersonComboBox.setItems(repairPersons);
//        repairPersonComboBox.getSelectionModel().select("Tất cả");
//    }
//
//    // Thiết lập sự kiện cho các nút
//    private void setupButtonActions() {
//        previewButton.setOnAction(this::handlePreviewButton);
//        exportButton.setOnAction(this::handleExportButton);
//        cancelButton.setOnAction(this::handleCancelButton);
//    }
//
//    // Xử lý sự kiện nút Xem trước
//    @FXML
//    public void handlePreviewButton(ActionEvent event) {
//        // Kiểm tra dữ liệu đầu vào
//        if (!validateInputs()) {
//            return;
//        }
//
//        try {
//            // Tạo báo cáo tạm thời để xem trước
//            File tempFile = File.createTempFile("report_preview_", ".pdf");
//
//            // Tạo báo cáo PDF
//            createPdfReport(tempFile.getAbsolutePath());
//
//            // Mở file PDF để xem trước
//            openPdfFile(tempFile.getAbsolutePath());
//
//            // Hiển thị thông báo thành công
//            showAlert(Alert.AlertType.INFORMATION, "Thông báo",
//                    "Tạo báo cáo xem trước thành công!");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            showAlert(Alert.AlertType.ERROR, "Lỗi",
//                    "Không thể tạo báo cáo xem trước: " + e.getMessage());
//        }
//    }
//
//    // Xử lý sự kiện nút Xuất báo cáo
//    @FXML
//    public void handleExportButton(ActionEvent event) {
//        // Kiểm tra dữ liệu đầu vào
//        if (!validateInputs()) {
//            return;
//        }
//
//        try {
//            // Chọn thư mục để lưu báo cáo
//            DirectoryChooser directoryChooser = new DirectoryChooser();
//            directoryChooser.setTitle("Chọn thư mục lưu báo cáo");
//            File selectedDirectory = directoryChooser.showDialog(exportButton.getScene().getWindow());
//
//            if (selectedDirectory != null) {
//                // Tạo tên file báo cáo
//                String fileName = "BaoCao_BaoDuongMay_" +
//                        LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) +
//                        "_" + LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
//
//                // Đường dẫn đầy đủ của file
//                String filePath = "";
//
//                // Tạo báo cáo theo định dạng được chọn
//                if (pdfRadioButton.isSelected()) {
//                    filePath = selectedDirectory.getAbsolutePath() + File.separator + fileName + ".pdf";
//                    createPdfReport(filePath);
//                } else if (excelRadioButton.isSelected()) {
//                    filePath = selectedDirectory.getAbsolutePath() + File.separator + fileName + ".xlsx";
//                    createExcelReport(filePath);
//                } else if (wordRadioButton.isSelected()) {
//                    filePath = selectedDirectory.getAbsolutePath() + File.separator + fileName + ".docx";
//                    createWordReport(filePath);
//                }
//
//                // Hiển thị thông báo thành công
//                showAlert(Alert.AlertType.INFORMATION, "Thông báo",
//                        "Xuất báo cáo thành công!\nĐường dẫn: " + filePath);
//
//                // Mở file vừa tạo
//                openFile(filePath);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            showAlert(Alert.AlertType.ERROR, "Lỗi",
//                    "Không thể xuất báo cáo: " + e.getMessage());
//        }
//    }
//
//    // Xử lý sự kiện nút Hủy
//    @FXML
//    public void handleCancelButton(ActionEvent event) {
//        // Đóng form
//        Stage stage = (Stage) cancelButton.getScene().getWindow();
//        stage.close();
//    }
//
//    // Kiểm tra dữ liệu đầu vào
//    private boolean validateInputs() {
//        StringBuilder errors = new StringBuilder();
//
//        if (companyNameField.getText().isEmpty()) {
//            errors.append("- Vui lòng nhập tên công ty\n");
//        }
//
//        if (reportTitleField.getText().isEmpty()) {
//            errors.append("- Vui lòng nhập tiêu đề báo cáo\n");
//        }
//
//        if (fromDatePicker.getValue() == null) {
//            errors.append("- Vui lòng chọn ngày bắt đầu\n");
//        }
//
//        if (toDatePicker.getValue() == null) {
//            errors.append("- Vui lòng chọn ngày kết thúc\n");
//        }
//
//        if (fromDatePicker.getValue() != null && toDatePicker.getValue() != null &&
//                fromDatePicker.getValue().isAfter(toDatePicker.getValue())) {
//            errors.append("- Ngày bắt đầu không thể sau ngày kết thúc\n");
//        }
//
//        if (reportDatePicker.getValue() == null) {
//            errors.append("- Vui lòng chọn ngày tạo báo cáo\n");
//        }
//
//        if (reportCreatorField.getText().isEmpty()) {
//            errors.append("- Vui lòng nhập tên người tạo báo cáo\n");
//        }
//
//        // Kiểm tra xem có ít nhất một cột được chọn
//        if (!dateCheckBox.isSelected() && !weekCheckBox.isSelected() && !shiftCheckBox.isSelected() &&
//                !machineCheckBox.isSelected() && !errorTimeCheckBox.isSelected() && !machineStatusCheckBox.isSelected() &&
//                !errorCauseCheckBox.isSelected() && !repairPersonCheckBox.isSelected() && !solutionCheckBox.isSelected() &&
//                !startTimeCheckBox.isSelected() && !endTimeCheckBox.isSelected() && !noteCheckBox.isSelected()) {
//            errors.append("- Vui lòng chọn ít nhất một cột để hiển thị trong báo cáo\n");
//        }
//
//        if (errors.length() > 0) {
//            showAlert(Alert.AlertType.ERROR, "Lỗi dữ liệu", errors.toString());
//            return false;
//        }
//
//        return true;
//    }
//
//    // Tạo báo cáo PDF
//    private void createPdfReport(String filePath) throws Exception {
//        // Tạo document
//        Document document = new Document(PageSize.A4.rotate(), 10, 10, 20, 20);
//        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
//
//        document.open();
//
//        // Thiết lập font chữ tiếng Việt
//        BaseFont baseFont = BaseFont.createFont("C:\\Windows\\Fonts\\arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//        Font titleFont = new Font(baseFont, 16, Font.BOLD);
//        Font headerFont = new Font(baseFont, 12, Font.BOLD);
//        Font normalFont = new Font(baseFont, 10, Font.NORMAL);
//        Font boldFont = new Font(baseFont, 10, Font.BOLD);
//
//        // Thêm thông tin công ty
//        Paragraph companyInfo = new Paragraph();
//        companyInfo.add(new Phrase(companyNameField.getText(), boldFont));
//        companyInfo.add(Chunk.NEWLINE);
//        companyInfo.add(new Phrase(companyAddressField.getText(), normalFont));
//        document.add(companyInfo);
//
//        // Thêm tiêu đề báo cáo
//        Paragraph title = new Paragraph(reportTitleField.getText(), titleFont);
//        title.setAlignment(Element.ALIGN_CENTER);
//        title.setSpacingBefore(10);
//        title.setSpacingAfter(10);
//        document.add(title);
//
//        // Tính toán số cột dựa trên các checkbox được chọn
//        int numberOfColumns = 0;
//        if (dateCheckBox.isSelected()) numberOfColumns++;
//        if (weekCheckBox.isSelected()) numberOfColumns++;
//        if (shiftCheckBox.isSelected()) numberOfColumns++;
//        if (machineCheckBox.isSelected()) numberOfColumns++;
//        if (errorTimeCheckBox.isSelected()) numberOfColumns++;
//        if (machineStatusCheckBox.isSelected()) numberOfColumns++;
//        if (errorCauseCheckBox.isSelected()) numberOfColumns++;
//        if (repairPersonCheckBox.isSelected()) numberOfColumns++;
//        if (solutionCheckBox.isSelected()) numberOfColumns++;
//        if (startTimeCheckBox.isSelected()) numberOfColumns++;
//        if (endTimeCheckBox.isSelected()) numberOfColumns++;
//        if (noteCheckBox.isSelected()) numberOfColumns++;
//
//        // Tạo bảng
//        PdfPTable table = new PdfPTable(numberOfColumns);
//        table.setWidthPercentage(100);
//
//        // Thiết lập header cho bảng
//        int columnIndex = 0;
//
//        if (dateCheckBox.isSelected()) {
//            PdfPCell cell = new PdfPCell(new Phrase("NGÀY", headerFont));
//            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
//            cell.setPadding(5);
//            table.addCell(cell);
//        }
//
//        if (weekCheckBox.isSelected()) {
//            PdfPCell cell = new PdfPCell(new Phrase("TUẦN, THÁNG", headerFont));
//            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
//            cell.setPadding(5);
//            table.addCell(cell);
//        }
//
//        if (shiftCheckBox.isSelected()) {
//            PdfPCell cell = new PdfPCell(new Phrase("CA", headerFont));
//            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
//            cell.setPadding(5);
//            table.addCell(cell);
//        }
//
//        if (machineCheckBox.isSelected()) {
//            PdfPCell cell = new PdfPCell(new Phrase("MÁY", headerFont));
//            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
//            cell.setPadding(5);
//            table.addCell(cell);
//        }
//
//        if (errorTimeCheckBox.isSelected()) {
//            PdfPCell cell = new PdfPCell(new Phrase("THỜI GIAN BỊ HỎNG", headerFont));
//            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
//            cell.setPadding(5);
//            table.addCell(cell);
//        }
//
//        if (machineStatusCheckBox.isSelected()) {
//            PdfPCell cell = new PdfPCell(new Phrase("TÌNH TRẠNG THIẾT BỊ", headerFont));
//            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
//            cell.setPadding(5);
//            table.addCell(cell);
//        }
//
//        if (errorCauseCheckBox.isSelected()) {
//            PdfPCell cell = new PdfPCell(new Phrase("NGUYÊN NHÂN", headerFont));
//            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
//            cell.setPadding(5);
//            table.addCell(cell);
//        }
//
//        if (repairPersonCheckBox.isSelected()) {
//            PdfPCell cell = new PdfPCell(new Phrase("NGƯỜI SỬA CHỮA", headerFont));
//            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
//            cell.setPadding(5);
//            table.addCell(cell);
//        }
//
//        if (solutionCheckBox.isSelected()) {
//            PdfPCell cell = new PdfPCell(new Phrase("BIỆN PHÁP XỬ LÝ", headerFont));
//            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
//            cell.setPadding(5);
//            table.addCell(cell);
//        }
//
//        if (startTimeCheckBox.isSelected()) {
//            PdfPCell cell = new PdfPCell(new Phrase("THỜI GIAN BẮT ĐẦU", headerFont));
//            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
//            cell.setPadding(5);
//            table.addCell(cell);
//        }
//
//        if (endTimeCheckBox.isSelected()) {
//            PdfPCell cell = new PdfPCell(new Phrase("THỜI GIAN KẾT THÚC", headerFont));
//            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
//            cell.setPadding(5);
//            table.addCell(cell);
//        }
//
//        if (noteCheckBox.isSelected()) {
//            PdfPCell cell = new PdfPCell(new Phrase("GHI CHÚ", headerFont));
//            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
//            cell.setPadding(5);
//            table.addCell(cell);
//        }
//
//        // Lấy dữ liệu bảo dưỡng từ service
//        List<MaintenanceRecord> records = getMaintenanceRecords();
//
//        // Thêm dữ liệu vào bảng
//        for (MaintenanceRecord record : records) {
//            if (dateCheckBox.isSelected()) {
//                PdfPCell cell = new PdfPCell(new Phrase(record.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), normalFont));
//                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                cell.setPadding(5);
//                table.addCell(cell);
//            }
//
//            if (weekCheckBox.isSelected()) {
//                PdfPCell cell = new PdfPCell(new Phrase(record.getWeekMonth(), normalFont));
//                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                cell.setPadding(5);
//                table.addCell(cell);
//            }
//
//            if (shiftCheckBox.isSelected()) {
//                PdfPCell cell = new PdfPCell(new Phrase(record.getShift(), normalFont));
//                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                cell.setPadding(5);
//                table.addCell(cell);
//            }
//
//            if (machineCheckBox.isSelected()) {
//                PdfPCell cell = new PdfPCell(new Phrase(record.getMachine(), normalFont));
//                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                cell.setPadding(5);
//                table.addCell(cell);
//            }
//
//            if (errorTimeCheckBox.isSelected()) {
//                PdfPCell cell = new PdfPCell(new Phrase(record.getErrorTime(), normalFont));
//                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                cell.setPadding(5);
//                table.addCell(cell);
//            }
//
//            if (machineStatusCheckBox.isSelected()) {
//                PdfPCell cell = new PdfPCell(new Phrase(record.getMachineStatus(), normalFont));
//                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                cell.setPadding(5);
//                table.addCell(cell);
//            }
//
//            if (errorCauseCheckBox.isSelected()) {
//                PdfPCell cell = new PdfPCell(new Phrase(record.getErrorCause(), normalFont));
//                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                cell.setPadding(5);
//                table.addCell(cell);
//            }
//
//            if (repairPersonCheckBox.isSelected()) {
//                PdfPCell cell = new PdfPCell(new Phrase(record.getRepairPerson(), normalFont));
//                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                cell.setPadding(5);
//                table.addCell(cell);
//            }
//
//            if (solutionCheckBox.isSelected()) {
//                PdfPCell cell = new PdfPCell(new Phrase(record.getSolution(), normalFont));
//                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                cell.setPadding(5);
//                table.addCell(cell);
//            }
//
//            if (startTimeCheckBox.isSelected()) {
//                PdfPCell cell = new PdfPCell(new Phrase(record.getStartTime(), normalFont));
//                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                cell.setPadding(5);
//                table.addCell(cell);
//            }
//
//            if (endTimeCheckBox.isSelected()) {
//                PdfPCell cell = new PdfPCell(new Phrase(record.getEndTime(), normalFont));
//                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                cell.setPadding(5);
//                table.addCell(cell);
//            }
//
//            if (noteCheckBox.isSelected()) {
//                PdfPCell cell = new PdfPCell(new Phrase(record.getNote(), normalFont));
//                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                cell.setPadding(5);
//                table.addCell(cell);
//            }
//        }
//
//        // Thêm bảng vào document
//        document.add(table);
//
//        // Thêm chân trang
//        Paragraph footer = new Paragraph();
//        footer.setSpacingBefore(20);
//        footer.add(new Phrase("Người lập báo cáo: " + reportCreatorField.getText(), normalFont));
//        footer.add(Chunk.NEWLINE);
//        footer.add(new Phrase("Ngày lập báo cáo: " + reportDatePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), normalFont));
//        document.add(footer);
//
//        // Đóng document
//        document.close();
//    }
//
//    // Tạo báo cáo Excel (giả định)
//    private void createExcelReport(String filePath) throws Exception {
//        // Trong một ứng dụng thực tế, bạn sẽ sử dụng thư viện như Apache POI để tạo file Excel
//        showAlert(Alert.AlertType.WARNING, "Thông báo",
//                "Chức năng xuất Excel đang trong quá trình phát triển!");
//    }
//
//    // Tạo báo cáo Word (giả định)
//    private void createWordReport(String filePath) throws Exception {
//        // Trong một ứng dụng thực tế, bạn sẽ sử dụng thư viện như Apache POI để tạo file Word
//        showAlert(Alert.AlertType.WARNING, "Thông báo",
//                "Chức năng xuất Word đang trong quá trình phát triển!");
//    }
//
//    // Mở file
//    private void openFile(String filePath) {
//        try {
//            File file = new File(filePath);
//            if (file.exists()) {
//                java.awt.Desktop.getDesktop().open(file);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Mở file PDF
//    private void openPdfFile(String filePath) {
//        openFile(filePath);
//    }
//
//    // Lấy dữ liệu bảo dưỡng (giả định)
//    private List<MaintenanceRecord> getMaintenanceRecords() {
//        // Trong một ứng dụng thực tế, bạn sẽ lấy dữ liệu từ cơ sở dữ liệu
//        // Tạo dữ liệu mẫu để minh họa
//        List<MaintenanceRecord> records = FXCollections.observableArrayList();
//
//        // Lấy các giá trị filter
//        LocalDate fromDate = fromDatePicker.getValue();
//        LocalDate toDate = toDatePicker.getValue();
//        String selectedShift = shiftComboBox.getValue();
//        String selectedMachine = machineComboBox.getValue();
//        String selectedRepairPerson = repairPersonComboBox.getValue();
//
//        // Thêm các bản ghi mẫu
//        MaintenanceRecord record1 = new MaintenanceRecord();
//        record1.setDate(LocalDate.of(2013, 1, 4));
//        record1.setWeekMonth("Tuần14/T4");
//        record1.setShift("Ca 1");
//        record1.setMachine("M9.1");
//        record1.setErrorTime("3h15p");
//        record1.setMachineStatus("");
//        record1.setErrorCause("");
//        record1.setRepairPerson("CDC4.1");
//        record1.setSolution("Sửa máy mica do sp bị đen : Thay phớt phi ; vệ sinh bơm định lượng");
//        record1.setStartTime("8h00p");
//        record1.setEndTime("17h00p");
//        record1.setNote("SP bị đen và phốt nhựa tại bơm định lượng .");
//        records.add(record1);
//
//        MaintenanceRecord record2 = new MaintenanceRecord();
//        record2.setDate(LocalDate.of(2013, 1, 4));
//        record2.setWeekMonth("Tuần14/T4");
//        record2.setShift("Ca 1");
//        record2.setMachine("M3.1");
//        record2.setErrorTime("16h30p");
//        record2.setMachineStatus("Hỏng Máy");
//        record2.setErrorCause("Phớt");
//        record2.setRepairPerson("CDC4.1");
//        record2.setSolution("Kiểm tra lại phớt động cơ thùng lạnh cho máy");
//        record2.setStartTime("16h30p");
//        record2.setEndTime("17h00p");
//        record2.setNote("Động cơ thùng lạnh của máy bị ngắt");
//        records.add(record2);
//
//        MaintenanceRecord record3 = new MaintenanceRecord();
//        record3.setDate(LocalDate.of(2013, 1, 4));
//        record3.setWeekMonth("Tuần14/T3");
//        record3.setShift("Ca 1");
//        record3.setMachine("M3.2");
//        record3.setErrorTime("14h30p");
//        record3.setMachineStatus("Hỏng Máy");
//        record3.setErrorCause("Khởi động từ");
//        record3.setRepairPerson("CDD5.2");
//        record3.setSolution("Đã thay 01 KĐT từ máy trộn M3.3 sang do khớ hết vật tư.");
//        record3.setStartTime("14h30p");
//        record3.setEndTime("16h30p");
//        record3.setNote("Máy không chuyển chế độ trộn tự động được do KĐT bị đình tiếp điểm");
//        records.add(record3);
//
//        MaintenanceRecord record4 = new MaintenanceRecord();
//        record4.setDate(LocalDate.of(2013, 1, 4));
//        record4.setWeekMonth("Tuần14/T3");
//        record4.setShift("Ca 1");
//        record4.setMachine("M2.4");
//        record4.setErrorTime("");
//        record4.setMachineStatus("Sửa chữa");
//        record4.setErrorCause("Nhiệt nóng keo lỏng");
//        record4.setRepairPerson("CDD5.2");
//        record4.setSolution("Kiểm tra bật nhiệt độ keo lỏng");
//        record4.setStartTime("8h00p");
//        record4.setEndTime("9h00p");
//        record4.setNote("");
//        records.add(record4);
//
//        // Lọc dữ liệu theo điều kiện
//        return records.filtered(record -> {
//            // Lọc theo ngày
//            boolean dateMatches = !record.getDate().isBefore(fromDate) && !record.getDate().isAfter(toDate);
//
//            // Lọc theo ca
//            boolean shiftMatches = "Tất cả".equals(selectedShift) || selectedShift.equals(record.getShift());
//
//            // Lọc theo máy
//            boolean machineMatches = "Tất cả".equals(selectedMachine) || selectedMachine.equals(record.getMachine());
//
//            // Lọc theo người sửa chữa
//            boolean repairPersonMatches = "Tất cả".equals(selectedRepairPerson) || selectedRepairPerson.equals(record.getRepairPerson());
//
//            return dateMatches && shiftMatches && machineMatches && repairPersonMatches;
//        });
//    }
//
//    // Hiển thị thông báo
//    private void showAlert(Alert.AlertType alertType, String title, String content) {
//        Alert alert = new Alert(alertType);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(content);
//        alert.showAndWait();
//    }