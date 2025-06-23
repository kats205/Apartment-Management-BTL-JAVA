package com.utc2.apartmentmanagement.PDF;

import com.utc2.apartmentmanagement.DAO.DatabaseConnection;
import net.sf.jasperreports.engine.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Jasper9ExportPDF {
    public static void main(String[] args) {
        try{
            Map<String, Object> params =new HashMap<>();
            params.put("request_id",12);
            // Biên dịch .jrxml thành file .jasper
            JasperReport report = JasperCompileManager.compileReport("src/main/resources/com/utc2/apartmentmanagement/jrxml/MaintenancePDF.jrxml");


            // Dữ liệu đổ vào báo cáo
            JasperPrint print = JasperFillManager.fillReport(report,params, DatabaseConnection.getConnection());

            // Xuất PDF
            JasperExportManager.exportReportToPdfFile(print,"output.pdf");
            System.out.println("Xuat thanh cong");
        } catch (JRException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
