package com.student.management.util;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.*;

public class EnrollStudent extends JFrame {
    private static final long serialVersionUID = 1L;

    JComboBox<String> comboStudents, comboCourses;
    JButton btnEnroll;

    public EnrollStudent() {

        setTitle("Enroll Student");
        setSize(450, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ❌ Disable maximize
        setResizable(false);

        // 🌟 MAIN PANEL WITH BORDER
        JPanel mainPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        mainPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(52, 152, 219), 2, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        mainPanel.setBackground(Color.WHITE);

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Labels
        JLabel lblStudent = new JLabel("Select Student:");
        lblStudent.setFont(labelFont);
        mainPanel.add(lblStudent);

        comboStudents = new JComboBox<>();
        comboStudents.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        mainPanel.add(comboStudents);

        JLabel lblCourse = new JLabel("Select Course:");
        lblCourse.setFont(labelFont);
        mainPanel.add(lblCourse);

        comboCourses = new JComboBox<>();
        comboCourses.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        mainPanel.add(comboCourses);

        // Button
        btnEnroll = new JButton("Enroll Student");
        btnEnroll.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEnroll.setBackground(new Color(52, 152, 219));
        btnEnroll.setForeground(Color.WHITE);
        btnEnroll.setFocusPainted(false);
        btnEnroll.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        btnEnroll.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEnroll.setBackground(new Color(41, 128, 185));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEnroll.setBackground(new Color(52, 152, 219));
            }
        });

        mainPanel.add(btnEnroll);
        mainPanel.add(new JLabel(""));

        add(mainPanel);

        // 🔽 LOAD STUDENTS (UNCHANGED LOGIC)
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT student_id, name FROM students")) {

            comboStudents.removeAllItems();
            while (rs.next()) {
                comboStudents.addItem(rs.getInt("student_id") + " - " + rs.getString("name"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading students: " + e.getMessage());
        }

        // 🔽 LOAD COURSES (UNCHANGED LOGIC)
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT course_id, course_name FROM courses")) {

            comboCourses.removeAllItems();
            while (rs.next()) {
                comboCourses.addItem(rs.getInt("course_id") + " - " + rs.getString("course_name"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading courses: " + e.getMessage());
        }

        // 🔽 ENROLL ACTION (UNCHANGED)
        btnEnroll.addActionListener(e -> {
            if (comboStudents.getSelectedItem() == null || comboCourses.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "No student or course available!");
                return;
            }

            String studentId = comboStudents.getSelectedItem().toString().split(" - ")[0];
            String courseId = comboCourses.getSelectedItem().toString().split(" - ")[0];

            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(
                         "INSERT INTO enrollments(student_id, course_id) VALUES (?, ?)")) {

                ps.setInt(1, Integer.parseInt(studentId));
                ps.setInt(2, Integer.parseInt(courseId));
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,
                        "Student enrolled successfully!\nYour Registration Number is: " + studentId);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error enrolling student: " + ex.getMessage());
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new EnrollStudent();
    }
}