package org.example.apartmentmanagement.Views.AdminDashBoard;

import javax.swing.*;
import java.awt.*;

public class ServicePanel extends JPanel {
    public ServicePanel() {
        setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Dịch vụ", "Giá tiền"};
        Object[][] data = {
                {1, "Gửi xe", "100.000 VNĐ/tháng"},
                {2, "Vệ sinh", "200.000 VNĐ/tháng"}
        };

        JTable table = new JTable(data, columnNames);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Thêm");
        JButton deleteBtn = new JButton("Xóa");

        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
