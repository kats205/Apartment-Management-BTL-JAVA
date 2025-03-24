package org.example.apartmentmanagement.Views.AdminDashBoard;

import javax.swing.*;
import java.awt.*;

public class ApartmentPanel extends JPanel {
    public ApartmentPanel() {
        setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Số phòng", "Diện tích", "Chủ sở hữu", "Trạng thái"};
        Object[][] data = {
                {1, "A101", "75m²", "Nguyễn Văn A", "Đã thuê"},
                {2, "B202", "85m²", "Trần Thị B", "Trống"}
        };

        JTable table = new JTable(data, columnNames);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Thêm");
        JButton editBtn = new JButton("Sửa");
        JButton deleteBtn = new JButton("Xóa");

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}