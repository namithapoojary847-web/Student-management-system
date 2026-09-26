package com.student.management.util;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.*;

public class AddStudentForm extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTextField txtName, txtDob, txtEmail, txtPhone;
    private JButton btnSubmit;

    public AddStudentForm() {

        setTitle("Add Student");
        setSize(450, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ❌ Disable maximize button
        setResizable(false);

        // 🌟 MAIN PANEL WITH BORDER
        JPanel mainPanel = new JPanel(new GridLayout(5, 2, 15, 15));
        mainPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(52, 152, 219), 2, true), // outer border
                new EmptyBorder(20, 20, 20, 20) // padding
        ));
        mainPanel.setBackground(Color.WHITE);

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Name
        JLabel lblName = new JLabel("Name:");
        lblName.setFont(labelFont);
        mainPanel.add(lblName);

        txtName = new JTextField();
        txtName.setFont(fieldFont);
        txtName.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        mainPanel.add(txtName);

        // DOB
        JLabel lblDob = new JLabel("DOB (YYYY-MM-DD):");
        lblDob.setFont(labelFont);
        mainPanel.add(lblDob);

        txtDob = new JTextField();
        txtDob.setFont(fieldFont);
        txtDob.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        mainPanel.add(txtDob);

        // Email
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(labelFont);
        mainPanel.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setFont(fieldFont);
        txtEmail.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        mainPanel.add(txtEmail);

        // Phone
        JLabel lblPhone = new JLabel("Phone:");
        lblPhone.setFont(labelFont);
        mainPanel.add(lblPhone);

        txtPhone = new JTextField();
        txtPhone.setFont(fieldFont);
        txtPhone.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        mainPanel.add(txtPhone);

        // Submit Button
        btnSubmit = new JButton("Submit");
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSubmit.setBackground(new Color(52, 152, 219));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFocusPainted(false);
        btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        btnSubmit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSubmit.setBackground(new Color(41, 128, 185));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSubmit.setBackground(new Color(52, 152, 219));
            }
        });

        mainPanel.add(btnSubmit);
        mainPanel.add(new JLabel(""));

        // Button action (UNCHANGED)
        btnSubmit.addActionListener(e -> addStudentToDB());

        add(mainPanel);
        setVisible(true);
    }

    private void addStudentToDB() {
        String name = txtName.getText().trim();
        String dob = txtDob.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();

        if (name.isEmpty() || dob.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO students (name, dob, email, phone) VALUES (?, ?, ?, ?)")) {

            pstmt.setString(1, name);
            pstmt.setDate(2, Date.valueOf(dob));
            pstmt.setString(3, email);
            pstmt.setString(4, phone);

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Student added successfully!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add student.");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.", "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearFields() {
        txtName.setText("");
        txtDob.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
    }
}