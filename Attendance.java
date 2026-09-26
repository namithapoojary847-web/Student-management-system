package com.student.management.util;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.*;

public class Attendance extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTextField txtStudentId, txtTotalDays, txtAttendedDays;
    private JButton btnSubmit;

    public Attendance() {

        setTitle("Add Attendance");
        setSize(450, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ❌ Disable maximize
        setResizable(false);

        // 🌟 MAIN PANEL WITH BORDER (SAME STYLE)
        JPanel mainPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        mainPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(52, 152, 219), 2, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        mainPanel.setBackground(Color.WHITE);

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Student ID
        JLabel lblStudentId = new JLabel("Student ID:");
        lblStudentId.setFont(labelFont);
        mainPanel.add(lblStudentId);

        txtStudentId = new JTextField();
        txtStudentId.setFont(fieldFont);
        txtStudentId.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        mainPanel.add(txtStudentId);

        // Total Days
        JLabel lblTotalDays = new JLabel("Total Days:");
        lblTotalDays.setFont(labelFont);
        mainPanel.add(lblTotalDays);

        txtTotalDays = new JTextField();
        txtTotalDays.setFont(fieldFont);
        txtTotalDays.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        mainPanel.add(txtTotalDays);

        // Attended Days
        JLabel lblAttendedDays = new JLabel("Attended Days:");
        lblAttendedDays.setFont(labelFont);
        mainPanel.add(lblAttendedDays);

        txtAttendedDays = new JTextField();
        txtAttendedDays.setFont(fieldFont);
        txtAttendedDays.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        mainPanel.add(txtAttendedDays);

        // Submit Button
        btnSubmit = new JButton("Submit");
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSubmit.setBackground(new Color(52, 152, 219));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFocusPainted(false);
        btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect (SAME)
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

        // Action (same logic style)
        btnSubmit.addActionListener(e -> saveAttendance());

        add(mainPanel);
        setVisible(true);
    }

    private void saveAttendance() {
        String studentIdStr = txtStudentId.getText().trim();
        String totalDaysStr = txtTotalDays.getText().trim();
        String attendedDaysStr = txtAttendedDays.getText().trim();

        if (studentIdStr.isEmpty() || totalDaysStr.isEmpty() || attendedDaysStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int studentId = Integer.parseInt(studentIdStr);
            int totalDays = Integer.parseInt(totalDaysStr);
            int attendedDays = Integer.parseInt(attendedDaysStr);

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(
                         "INSERT INTO attendance (student_id, total_days, attended_days) VALUES (?, ?, ?)")) {

                pstmt.setInt(1, studentId);
                pstmt.setInt(2, totalDays);
                pstmt.setInt(3, attendedDays);

                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Attendance added successfully!");
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add attendance.");
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Input Error", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        txtStudentId.setText("");
        txtTotalDays.setText("");
        txtAttendedDays.setText("");
    }
}