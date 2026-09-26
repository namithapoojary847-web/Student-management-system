package com.student.management.util;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginUI extends JFrame {
    private static final long serialVersionUID = 1L;

    JTextField usernameField, regField;

    public LoginUI() {

        setTitle("Student Login");
        setSize(500, 400);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 245, 245));
        panel.setBounds(0, 0, 500, 400);
        panel.setLayout(null);

        JLabel title = new JLabel("Student Login");
        title.setBounds(170, 30, 200, 40);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(130, 90, 240, 20);

        usernameField = new JTextField();
        usernameField.setBounds(130, 110, 240, 35);

        JLabel regLabel = new JLabel("Registration No:");
        regLabel.setBounds(130, 150, 240, 20);

        regField = new JTextField();
        regField.setBounds(130, 170, 240, 35);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(170, 240, 150, 40);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setBackground(new Color(52, 152, 219));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginBtn.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginBtn.setBackground(new Color(52, 152, 219));
            }
        });

        loginBtn.addActionListener(e -> loginUser());

        panel.add(title);
        panel.add(userLabel);
        panel.add(usernameField);
        panel.add(regLabel);
        panel.add(regField);
        panel.add(loginBtn);

        add(panel);
        setVisible(true);
    }

    // 🔥 FIXED LOGIN METHOD
    private void loginUser() {

        String username = usernameField.getText().trim();
        String reg = regField.getText().trim();

        // ✅ VALIDATION
        if (username.isEmpty() || reg.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all fields ❗");
            return;
        }

        try {
            int studentId = Integer.parseInt(reg);

            Connection con = DatabaseConnection.getConnection();

            // 🔥 IMPORTANT FIX: TRIM name also
            PreparedStatement ps = con.prepareStatement(
                "SELECT student_id FROM students WHERE TRIM(LOWER(name)) = TRIM(LOWER(?)) AND student_id=?"
            );

            ps.setString(1, username);
            ps.setInt(2, studentId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                JOptionPane.showMessageDialog(this, "Login Successful ✅");

                new StudentResultUI(studentId);
                dispose();

            } else {
                JOptionPane.showMessageDialog(this, "Invalid Name or ID ❌");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Registration No must be number ❗");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}