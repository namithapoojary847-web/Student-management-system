package com.student.management.util;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class ViewEnrollments extends JFrame {
    private static final long serialVersionUID = 1L;

    JTable tableEnrollments;
    DefaultTableModel tableModel;
    JButton btnDelete;

    public ViewEnrollments() {

        setTitle("View Enrollments");
        setSize(750, 450);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 🌟 MAIN PANEL WITH BORDER
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(52, 152, 219), 2, true),
                new EmptyBorder(15, 15, 15, 15)
        ));
        mainPanel.setBackground(Color.WHITE);

        // 🧾 TABLE
        String[] columns = {"Student ID", "Student Name", "Course ID", "Course Name", "Enrollment Date"};
        tableModel = new DefaultTableModel(columns, 0);
        tableEnrollments = new JTable(tableModel);

        // 🎨 TABLE STYLING
        tableEnrollments.setRowHeight(25);
        tableEnrollments.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableEnrollments.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableEnrollments.getTableHeader().setBackground(new Color(52, 152, 219));
        tableEnrollments.getTableHeader().setForeground(Color.WHITE);
        tableEnrollments.setSelectionBackground(new Color(41, 128, 185));
        tableEnrollments.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tableEnrollments);
        scrollPane.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 🔘 DELETE BUTTON
        btnDelete = new JButton("Delete Selected Enrollment");
        btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDelete.setBackground(new Color(231, 76, 60)); // red
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        btnDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDelete.setBackground(new Color(192, 57, 43));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDelete.setBackground(new Color(231, 76, 60));
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(btnDelete);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // 🔽 LOAD DATA (UNCHANGED)
        loadEnrollments();

        // 🔽 DELETE ACTION (UNCHANGED)
        btnDelete.addActionListener(e -> deleteEnrollment());

        setVisible(true);
    }

    private void loadEnrollments() {
        tableModel.setRowCount(0);

        String query = "SELECT e.enrollment_id, s.student_id, s.name, c.course_id, c.course_name, e.enrollment_date " +
                       "FROM enrollments e " +
                       "JOIN students s ON e.student_id = s.student_id " +
                       "JOIN courses c ON e.course_id = c.course_id " +
                       "ORDER BY e.enrollment_date";

        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int studentId = rs.getInt("student_id");
                String studentName = rs.getString("name");
                int courseId = rs.getInt("course_id");
                String courseName = rs.getString("course_name");
                Date enrollmentDate = rs.getDate("enrollment_date");

                Object[] row = {studentId, studentName, courseId, courseName, enrollmentDate};
                tableModel.addRow(row);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading enrollments: " + ex.getMessage());
        }
    }

    private void deleteEnrollment() {
        int selectedRow = tableEnrollments.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an enrollment to delete.");
            return;
        }

        int studentId = (int) tableModel.getValueAt(selectedRow, 0);
        int courseId = (int) tableModel.getValueAt(selectedRow, 2);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this enrollment?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String deleteQuery = "DELETE FROM enrollments WHERE student_id = ? AND course_id = ?";

            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(deleteQuery)) {

                ps.setInt(1, studentId);
                ps.setInt(2, courseId);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Enrollment deleted successfully!");
                loadEnrollments();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting enrollment: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new ViewEnrollments();
    }
}