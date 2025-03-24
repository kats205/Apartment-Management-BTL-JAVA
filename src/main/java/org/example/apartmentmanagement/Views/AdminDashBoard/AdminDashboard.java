package org.example.apartmentmanagement.Views.AdminDashBoard;

import org.example.apartmentmanagement.Views.LoginView;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class AdminDashboard extends JFrame {
    public AdminDashboard() throws SQLException {
        setTitle("Hệ thống quản lý chung cư - Admin");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Menu chính
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Cư dân", new ResidentPanel());
        tabbedPane.addTab("Căn hộ", new ApartmentPanel());
        tabbedPane.addTab("Hóa đơn", new InvoicePanel());
        tabbedPane.addTab("Dịch vụ", new ServicePanel());
        tabbedPane.addTab("Nhân viên", new StaffPanel());
//        tabbedPane.addTab("Thống kê", new StatisticsPanel());

        add(tabbedPane, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }
    public void adminView(){
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

