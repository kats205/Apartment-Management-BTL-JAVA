package org.example.apartmentmanagement.Views.AdminDashBoard;

import javax.swing.*;
import java.awt.*;

public class StaffPanel extends JPanel {
    public StaffPanel() {
        setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Tên nhân viên", "Chức vụ"};
        Object[][] data = {
                {1, "Lê Văn C", "Bảo vệ"},
                {2, "Hoàng Thị D", "Vệ sinh"}
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
