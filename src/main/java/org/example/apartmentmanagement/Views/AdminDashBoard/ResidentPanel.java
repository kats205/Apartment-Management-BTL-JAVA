package org.example.apartmentmanagement.Views.AdminDashBoard;

import org.example.apartmentmanagement.DAO.ResidentDAO;
import org.example.apartmentmanagement.Model.Resident;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class ResidentPanel extends JPanel {
    public ResidentPanel() throws SQLException {
        setLayout(new BorderLayout());

        //        Object[][] data = {
//                {1, "Nguyễn Văn A","", "", "", "", "0987654321", "A101", "1990-05-15"},
//                {2, "Trần Thị B", "", "", "", "", "0934567890", "B202", "1985-08-21"}
//        };

        // Table hiển thị danh sách cư dân
        String[] columnNames = {"ID", "Họ và tên", "CCCD/CMND", "Ngày tháng năm sinh", "Giới tính", "cư dân ? ", "Ngày chuyển đến", "Căn hộ"};
        ResidentDAO residentDAO = new ResidentDAO();
        List<Resident> residentList =  residentDAO.getAllResidentDAO();
        Object[][] data = new Object[residentList.size()][8];
        for (int i = 0; i < residentList.size(); i++) {
            Resident r = residentList.get(i);
            data[i][0] = r.getResidentID();
            data[i][1] = r.getFullName();
            data[i][2] = r.getIdentityCard();
            data[i][3] = r.getDateOfBirth();
            data[i][4] = r.getGender();
            data[i][5] = r.isPrimaryResident();
            data[i][6] = r.getMoveInDate();
            data[i][7] = r.getApartmentID();
        }
        JTable table = new JTable(data, columnNames);
        add(new JScrollPane(table), BorderLayout.CENTER);
        table.setEnabled(false);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        JTableHeader header = table.getTableHeader();
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        header.setDefaultRenderer(headerRenderer);

        // Nút chức năng
        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Thêm");
//        xử lý sự kiện thêm cư dân trong giao diện của admin, manager cũng nên có quyền này
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        JButton editBtn = new JButton("Sửa");
        JButton deleteBtn = new JButton("Xóa");


        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);


        add(buttonPanel, BorderLayout.SOUTH);
    }
}
