package org.example.apartmentmanagement.Views;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class LoginView extends JFrame {
    private JPanel backgroundPanel;
    private JPanel loginPanel;
    private ImageIcon backgroundImage;

    public LoginView() {
        setTitle("Login");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Load hình nền từ đường dẫn
        String imagePath = "C:/Users/lerua/IdeaProjects/QLCC/src/main/java/org/example/apartmentmanagement/Images/chung-cu-quan-7.jpg";
        backgroundImage = new ImageIcon(imagePath);

        // Panel chứa hình nền
        backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(null);
        setContentPane(backgroundPanel);

        // Panel form đăng nhập (nền mờ)
        loginPanel = new JPanel();
        loginPanel.setLayout(null);
        loginPanel.setBackground(new Color(0, 0, 0, 180)); // Màu đen trong suốt
        loginPanel.setBorder(new LineBorder(new Color(0, 128, 0), 2)); // Viền xanh lá
        backgroundPanel.add(loginPanel);

        // Tiêu đề chính
        JLabel title = new JLabel("LOGIN HERE");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        loginPanel.add(title);

        // Tiêu đề Username
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setForeground(Color.WHITE);
        lblUsername.setFont(new Font("Arial", Font.BOLD, 14));
        loginPanel.add(lblUsername);

        JTextField txtEmail = new JTextField();
        txtEmail.setBorder(new LineBorder(new Color(0, 128, 0), 2));
        loginPanel.add(txtEmail);

        // Tiêu đề Password
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setFont(new Font("Arial", Font.BOLD, 14));
        loginPanel.add(lblPassword);

        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setBorder(new LineBorder(new Color(0, 128, 0), 2));
        loginPanel.add(txtPassword);

        // Checkbox Remember me
        JCheckBox rememberMe = new JCheckBox("Remember me");
        rememberMe.setForeground(Color.WHITE);
        rememberMe.setOpaque(false);
        loginPanel.add(rememberMe);

        // Forgot Password
        JLabel forgotPass = new JLabel("Forgot password?");
        forgotPass.setForeground(Color.WHITE);
        forgotPass.setFont(new Font("Arial", Font.ITALIC, 12));
        loginPanel.add(forgotPass);

        // Nút Login
        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setBackground(new Color(0, 128, 0));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));
        btnLogin.setBorderPainted(false);
        loginPanel.add(btnLogin);

        // Nút Register
//        JLabel registerText = new JLabel("To Register New Account → ");
//        registerText.setForeground(Color.WHITE);
//        loginPanel.add(registerText);
//
//        JButton btnRegister = new JButton("Click Here");
//        btnRegister.setForeground(new Color(0, 128, 0));
//        btnRegister.setBorder(null);
//        btnRegister.setContentAreaFilled(false);
//        loginPanel.add(btnRegister);

        // Thêm sự kiện thay đổi kích thước cửa sổ
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateLayout();
            }
        });

        setVisible(true);
        updateLayout();
    }

    private void updateLayout() {
        int width = getWidth();
        int height = getHeight();

        // Cập nhật kích thước panel nền
        backgroundPanel.setBounds(0, 0, width, height);

        // Kích thước form đăng nhập tự điều chỉnh theo màn hình
        int formWidth = width / 3;
        int formHeight = height / 2;
        int formX = (width - formWidth) / 2;
        int formY = (height - formHeight) / 2;
        loginPanel.setBounds(formX, formY, formWidth, formHeight);

        // Cập nhật vị trí và kích thước của các thành phần trong form
        int padding = 20;
        int textFieldWidth = formWidth - 2 * padding;
        int textFieldHeight = 30;

        loginPanel.getComponent(0).setBounds((formWidth - 150) / 2, 20, 150, 30); // Tiêu đề chính
        loginPanel.getComponent(1).setBounds(padding, 60, 100, 20); // Username Label
        loginPanel.getComponent(2).setBounds(padding, 80, textFieldWidth, textFieldHeight); // Email TextField
        loginPanel.getComponent(3).setBounds(padding, 120, 100, 20); // Password Label
        loginPanel.getComponent(4).setBounds(padding, 140, textFieldWidth, textFieldHeight); // Password Field
        loginPanel.getComponent(5).setBounds(padding, 180, 120, 20); // Remember Me
        loginPanel.getComponent(6).setBounds(formWidth - 130, 180, 120, 20); // Forgot password
        loginPanel.getComponent(7).setBounds(padding, 220, textFieldWidth, 40); // Login button
//        loginPanel.getComponent(8).setBounds((formWidth - 200) / 2, 270, 200, 20); // Register text
//        loginPanel.getComponent(9).setBounds((formWidth - 90) / 2, 290, 90, 20); // Register button
    }

    public static void main(String[] args) {
        new LoginView();
    }
}
