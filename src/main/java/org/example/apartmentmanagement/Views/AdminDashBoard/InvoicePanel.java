package org.example.apartmentmanagement.Views.AdminDashBoard;

import javax.swing.*;
import java.awt.*;

public class InvoicePanel extends JPanel {
    public InvoicePanel() {
        setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Cư dân", "Tháng", "Tổng tiền", "Trạng thái"};
        Object[][] data = {
                {1, "Nguyễn Văn A", "03/2024", "1.500.000 VNĐ", "Chưa thanh toán"},
                {2, "Trần Thị B", "03/2024", "2.000.000 VNĐ", "Đã thanh toán"}
        };

        JTable table = new JTable(data, columnNames);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton payBtn = new JButton("Xác nhận thanh toán");

        buttonPanel.add(payBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
