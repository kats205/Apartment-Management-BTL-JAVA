package com.utc2.apartmentmanagement.Model;

import com.utc2.apartmentmanagement.DAO.DatabaseConnection;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xddf.usermodel.text.XDDFFont;
import org.apache.poi.xddf.usermodel.text.XDDFRunProperties;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.*;
import org.openxmlformats.schemas.drawingml.x2006.chart.*;
import org.openxmlformats.schemas.drawingml.x2006.main.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class ExportExcel {

    public static void main(String[] args) throws Exception {

        // Chỉ định khoảng thời gian bạn muốn xuất dữ liệu
        LocalDate fromDate = LocalDate.of(2023, 1, 1);
        LocalDate toDate = LocalDate.of(2023, 12, 31);
        exportRevenueChart(fromDate,toDate);
        exportPieChart(fromDate,toDate);

    }
    public static void exportRevenueChart(LocalDate fromDate, LocalDate toDate) throws Exception {
        String sql = "SELECT FORMAT(p.payment_date, 'yyyy-MM') AS Thang, SUM(p.amount) AS TongDoanhThu " +
                "FROM Payment p JOIN Bill b ON p.bill_id = b.bill_id " +
                "WHERE p.status = 'completed' AND p.payment_date BETWEEN ? AND ? " +
                "GROUP BY FORMAT(p.payment_date, 'yyyy-MM') ORDER BY Thang ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(fromDate));
            stmt.setDate(2, Date.valueOf(toDate));
            ResultSet rs = stmt.executeQuery();

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Revenue Chart");
            sheet.setColumnWidth(0, 5000);  // Có thể set kích thước cột tùy ý


            // Tạo font Times New Roman
            XSSFFont timesFont = workbook.createFont();
            timesFont.setFontName("Times New Roman");
            timesFont.setFontHeight(16);

            CellStyle timesStyle = workbook.createCellStyle();
            timesStyle.setFont(timesFont);

            // Dòng 0 và 1
            Row row0 = sheet.createRow(0);
            Row row1 = sheet.createRow(1);

            // Tạo cell từ cột 0 đến 13 cho cả hai dòng để tránh lỗi khi merge
            for (int i = 0; i <= 13; i++) {
                row0.createCell(i);
                row1.createCell(i);
            }

            // Gán giá trị vào ô A1
            Cell titleCell = row0.getCell(0);
            titleCell.setCellValue("BIỂU ĐỒ DOANH THU THEO THÁNG");

            CellStyle titleStyle = workbook.createCellStyle();
            XSSFFont titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeight(16);
            titleFont.setFontName("Times New Roman");
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            titleCell.setCellStyle(titleStyle);

            // Gộp từ A1 đến J2
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 13));


            // Tạo style với border
            CellStyle borderStyle = workbook.createCellStyle();
            borderStyle.setBorderTop(BorderStyle.THIN);
            borderStyle.setBorderBottom(BorderStyle.THIN);
            borderStyle.setBorderLeft(BorderStyle.THIN);
            borderStyle.setBorderRight(BorderStyle.THIN);

            // Ghi tiêu đề Header
            Row header = sheet.createRow(12);
            Cell h1 = header.createCell(0);
            h1.setCellValue("Tổng Doanh Thu");
            h1.setCellStyle(timesStyle);
            h1.setCellStyle(borderStyle);

            Cell h2 = header.createCell(1);
            h2.setCellValue("Tháng");
            h2.setCellStyle(timesStyle);
            h2.setCellStyle(borderStyle);

            // Dữ liệu
            int rowIdx = 13;
            while (rs.next()) {
                Row row = sheet.createRow(rowIdx++);

                Cell c1 = row.createCell(0);
                c1.setCellValue(rs.getDouble("TongDoanhThu"));
                c1.setCellStyle(borderStyle);

                Cell c2 = row.createCell(1);
                c2.setCellValue(rs.getString("Thang"));
                c2.setCellStyle(borderStyle);
            }

            // Tạo biểu đồ
            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            // tọa độ vị trí biểu đồ
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 3, 4, 13, 22);

            XSSFChart chart = drawing.createChart(anchor);
            chart.setTitleText("Doanh thu theo tháng");
            chart.setTitleOverlay(false);

            XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.BOTTOM);

            CTChart ctChart = chart.getCTChart();
            CTTitle title = ctChart.getTitle();

            if (title != null && title.getTx() != null && title.getTx().isSetRich()) {
                CTTextBody rich = title.getTx().getRich();
                for (CTTextParagraph p : rich.getPList()) {
                    for (CTRegularTextRun r : p.getRList()) {
                        CTTextCharacterProperties rPr = r.isSetRPr() ? r.getRPr() : r.addNewRPr();
                        rPr.addNewLatin().setTypeface("Times New Roman");
                        rPr.addNewCs().setTypeface("Times New Roman");
                        rPr.setSz(1400); // 14pt
                    }
                }
            }

            if (ctChart.getLegend() != null && ctChart.getLegend().getTxPr() != null) {
                CTTextBody txPr = ctChart.getLegend().getTxPr();
                // Đảm bảo có các phần cần thiết
                if (txPr.getPArray().length == 0) {
                    CTTextParagraph p = txPr.addNewP();
                    CTRegularTextRun r = p.addNewR();
                    r.setT(""); // required, even if empty
                    CTTextCharacterProperties rPr = r.addNewRPr();
                    rPr.addNewLatin().setTypeface("Times New Roman");
                    rPr.addNewCs().setTypeface("Times New Roman");
                    rPr.setSz(1100); // 11pt
                } else {
                    for (CTTextParagraph p : txPr.getPArray()) {
                        for (CTRegularTextRun r : p.getRArray()) {
                            CTTextCharacterProperties rPr = r.isSetRPr() ? r.getRPr() : r.addNewRPr();
                            rPr.addNewLatin().setTypeface("Times New Roman");
                            rPr.addNewCs().setTypeface("Times New Roman");
                            rPr.setSz(1100); // 11pt
                        }
                    }
                }
            }


            if (rowIdx <= 11) {
                // Không có dữ liệu để vẽ biểu đồ
                System.out.println("Không có dữ liệu để xuất biểu đồ.");
                return;
            }

            // Tạo dữ liệu cho biểu đồ
            XDDFNumericalDataSource<Double> revenues = XDDFDataSourcesFactory.fromNumericCellRange(
                    sheet, new CellRangeAddress(13, rowIdx - 1, 0, 0)); // Doanh thu sẽ là trục X
            XDDFDataSource<String> months = XDDFDataSourcesFactory.fromStringCellRange(
                    sheet, new CellRangeAddress(13, rowIdx - 1, 1, 1)); // Tháng sẽ là trục Y

            XDDFCategoryAxis leftAxis = chart.createCategoryAxis(AxisPosition.LEFT); // Trục Y = tháng
            leftAxis.setTitle("Tháng");


            XDDFValueAxis bottomAxis = chart.createValueAxis(AxisPosition.BOTTOM); // Trục X = doanh thu
            bottomAxis.setTitle("Tổng Doanh Thu");

            CTPlotArea plotArea = ctChart.getPlotArea();


            XDDFChartData data = chart.createData(ChartTypes.BAR, leftAxis, bottomAxis);
            XDDFChartData.Series series = data.addSeries(months,revenues);
            series.setTitle("Tháng", null);
            chart.plot(data);



            // Tạo thư mục nếu chưa có
            String path = "src/main/resources/com/utc2/apartmentmanagement/PDF_File";
            File directory = new File(path);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Ghi file
            String filePath = path + "/DoanhThuTheoThang.xlsx";
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
            workbook.close();

            System.out.println("Excel với biểu đồ đã xuất thành công.");
        }
    }
    public static void exportPieChart(LocalDate fromDate, LocalDate toDate){
        String sql = """
        WITH StatusCounts AS (
            SELECT 
                status, 
                COUNT(*) AS count_status
            FROM Payment
            WHERE payment_date BETWEEN ? AND ?
            GROUP BY status
        )
        SELECT 
            status,
            (count_status * 100.0 / 
                (SELECT COUNT(*) 
                 FROM Payment 
                 WHERE payment_date BETWEEN ? AND ?)
            ) AS percentage
        FROM StatusCounts
    """;
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);) {

            statement.setDate(1,Date.valueOf(fromDate));
            statement.setDate(2, Date.valueOf(toDate));
            statement.setDate(3, java.sql.Date.valueOf(fromDate));
            statement.setDate(4, java.sql.Date.valueOf(toDate));

            ResultSet rs = statement.executeQuery();

            // Tạo workbook và sheet
            XSSFWorkbook workbook  = new XSSFWorkbook();
            XSSFSheet  sheet = workbook.createSheet("Biểu đồ trạng thái thanh toaán");
            sheet.setColumnWidth(0, 5000);  // Có thể set kích thước cột tùy ý


            // Dòng 0 và 1
            Row row0 = sheet.createRow(0);
            Row row1 = sheet.createRow(1);

            // Tạo cell từ cột 0 đến 9 cho cả hai dòng để tránh lỗi khi merge
            for (int i = 0; i <= 9; i++) {
                row0.createCell(i);
                row1.createCell(i);
            }

             // Gán giá trị vào ô A1
            Cell titleCell = row0.getCell(0);
            titleCell.setCellValue("BIỂU ĐỒ TỶ LỆ TRẠNG THÁI THANH TOÁN");

            // Gộp từ A1 đến J2
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 9));

            CellStyle style = workbook.createCellStyle();;
            XSSFFont titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeight(16);
            titleFont.setFontName("Times New Roman");
            style.setFont(titleFont);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            titleCell.setCellStyle(style);

            int rowIdx = 11;

            // === Tạo style có font Times New Roman và viền ===
            XSSFFont timesFont = workbook.createFont();
            timesFont.setFontName("Times New Roman");
            timesFont.setFontHeight(12);

            CellStyle borderStyle = workbook.createCellStyle();
            borderStyle.setFont(timesFont);
            borderStyle.setBorderTop(BorderStyle.THIN);
            borderStyle.setBorderBottom(BorderStyle.THIN);
            borderStyle.setBorderLeft(BorderStyle.THIN);
            borderStyle.setBorderRight(BorderStyle.THIN);

            // Header bảng
            Row header = sheet.createRow(rowIdx++);

            Cell statusHeader =  header.createCell(0);
            statusHeader.setCellValue("Status");
            statusHeader.setCellStyle(borderStyle);

            Cell percentHeader = header.createCell(1);
            percentHeader.setCellValue("Percentage");
            percentHeader.setCellStyle(borderStyle);

            // Lấy dữ liệu (DAO)
            while (rs.next()){
                Row row = sheet.createRow(rowIdx++);

                Cell statusCell = row.createCell(0);
                statusCell.setCellValue(rs.getString("status"));
                statusCell.setCellStyle(borderStyle);

                Cell percentCell = row.createCell(1);
                percentCell.setCellValue(rs.getDouble("percentage"));
                percentCell.setCellStyle(borderStyle);
                }

            // Tạo biểu đồ
            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 3, 5, 10, 22);
            XSSFChart chart = drawing.createChart(anchor);
            chart.setTitleText("Tỷ lệ trạng thái thanh toán");
            chart.setTitleOverlay(false);

            // Đổi font của tiêu đề biểu đồ sang Times New Roman
            CTChart ctChart = chart.getCTChart();
            CTTitle title = ctChart.getTitle();

            if (title != null && title.getTx() != null && title.getTx().isSetRich()) {
                CTTextBody rich = title.getTx().getRich();
                for (CTTextParagraph p : rich.getPList()) {
                    for (CTRegularTextRun r : p.getRList()) {
                        CTTextCharacterProperties rPr = r.isSetRPr() ? r.getRPr() : r.addNewRPr();
                        rPr.addNewLatin().setTypeface("Times New Roman");
                        rPr.addNewCs().setTypeface("Times New Roman");
                    }
                }
            }

            // Thêm dữ liệu biểu đồ
            XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.BOTTOM);

            XDDFDataSource<String> statuses = XDDFDataSourcesFactory.fromStringCellRange(sheet,
                    new CellRangeAddress(12,rowIdx-1,0,0));
            XDDFNumericalDataSource<Double> percentages = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
                    new CellRangeAddress(12, rowIdx - 1, 1, 1));

            CTPlotArea plotArea = ctChart.getPlotArea();

            if (plotArea != null && plotArea.getPieChartList().size() > 0) {
                CTPieChart pieChart = plotArea.getPieChartArray(0);
                for (CTPieSer ser : pieChart.getSerList()) {
                    CTDLbls dLbls = ser.isSetDLbls() ? ser.getDLbls() : ser.addNewDLbls();
                    if (!dLbls.isSetTxPr()) {
                        dLbls.addNewTxPr();
                    }

                    CTTextBody lblText = dLbls.getTxPr();
                    lblText.setBodyPr(CTTextBodyProperties.Factory.newInstance());
                    lblText.setLstStyle(CTTextListStyle.Factory.newInstance());

                    CTTextParagraph p = CTTextParagraph.Factory.newInstance();
                    CTRegularTextRun r = CTRegularTextRun.Factory.newInstance();
                    r.setT(""); // required, even if empty
                    CTTextCharacterProperties rPr = r.addNewRPr();
                    rPr.addNewLatin().setTypeface("Times New Roman");
                    rPr.addNewCs().setTypeface("Times New Roman");

                    p.setRArray(new CTRegularTextRun[]{r});
                    lblText.setPArray(new CTTextParagraph[]{p});
                }
            }

            XDDFChartData data = chart.createData(ChartTypes.PIE, null, null);
            XDDFChartData.Series series = data.addSeries(statuses, percentages);
            series.setTitle("Trạng thái", null);
            chart.plot(data);


            // Tạo thư mục nếu chưa có
            String path = "src/main/resources/com/utc2/apartmentmanagement/PDF_File";
            File directory = new File(path);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Ghi file
            String filePath = path + "/TyLeTrangThaiThanhToan.xlsx";
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            workbook.close();

            System.out.println("Excel với biểu đồ đã xuất thành công.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
