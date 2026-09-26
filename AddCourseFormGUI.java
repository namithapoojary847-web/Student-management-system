package com.student.management.util;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.*;

public class AddCourseFormGUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTextField txtCourseName, txtDuration, txtFees, txtNumSubjects, txtSemester;
    private JPanel subjectPanel;
    private JButton btnSubmit, btnGenerate;

    public AddCourseFormGUI() {

        setTitle("Add Course");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        JPanel mainPanel = new JPanel(new GridLayout(6, 2, 15, 15));
        mainPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(52, 152, 219), 2, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        mainPanel.setBackground(Color.WHITE);

        // Course Name
        mainPanel.add(new JLabel("Course Name:"));
        txtCourseName = new JTextField();
        txtCourseName.setFont(fieldFont);
        mainPanel.add(txtCourseName);

        // Duration
        mainPanel.add(new JLabel("Duration (Years):"));
        txtDuration = new JTextField();
        txtDuration.setFont(fieldFont);
        mainPanel.add(txtDuration);

        // Fees
        mainPanel.add(new JLabel("Fees:"));
        txtFees = new JTextField();
        txtFees.setFont(fieldFont);
        mainPanel.add(txtFees);

        // Number of Subjects
        mainPanel.add(new JLabel("No. of Subjects:"));
        txtNumSubjects = new JTextField();
        txtNumSubjects.setFont(fieldFont);
        mainPanel.add(txtNumSubjects);

        // Semester
        mainPanel.add(new JLabel("Semester:"));
        txtSemester = new JTextField();
        txtSemester.setFont(fieldFont);
        mainPanel.add(txtSemester);

        // Generate Button
        btnGenerate = new JButton("Generate Subjects");
        mainPanel.add(btnGenerate);
        mainPanel.add(new JLabel(""));

        add(mainPanel, BorderLayout.NORTH);

        // Subject Panel
        subjectPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        subjectPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        add(new JScrollPane(subjectPanel), BorderLayout.CENTER);

        // Submit Button
        btnSubmit = new JButton("Submit");
        btnSubmit.setBackground(new Color(52, 152, 219));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnSubmit);
        add(bottomPanel, BorderLayout.SOUTH);

        btnGenerate.addActionListener(e -> generateSubjectFields());
        btnSubmit.addActionListener(e -> addCourseToDB());

        setVisible(true);
    }

    // 🔥 Generate subject fields
    private void generateSubjectFields() {
        subjectPanel.removeAll();

        try {
            int count = Integer.parseInt(txtNumSubjects.getText().trim());

            if (count <= 0) {
                JOptionPane.showMessageDialog(this, "Enter valid subject count!");
                return;
            }

            for (int i = 1; i <= count; i++) {
                subjectPanel.add(new JLabel("Subject " + i + ":"));
                subjectPanel.add(new JTextField());
            }

            subjectPanel.revalidate();
            subjectPanel.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Enter valid number!");
        }
    }

    // 💾 Save Course + Subjects
    private void addCourseToDB() {

        String courseName = txtCourseName.getText().trim();
        String durationStr = txtDuration.getText().trim();
        String feesStr = txtFees.getText().trim();
        String semStr = txtSemester.getText().trim();

        if (courseName.isEmpty() || durationStr.isEmpty() || feesStr.isEmpty() || semStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill all fields!");
            return;
        }

        if (subjectPanel.getComponentCount() == 0) {
            JOptionPane.showMessageDialog(this, "Generate subjects first!");
            return;
        }

        Connection conn = null;

        try {
            int duration = Integer.parseInt(durationStr);
            double fees = Double.parseDouble(feesStr);
            int semester = Integer.parseInt(semStr);

            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // ✅ Insert course (NO RETURN_GENERATED_KEYS issue)
            PreparedStatement courseStmt = conn.prepareStatement(
                    "INSERT INTO courses (course_name, duration_weeks, fees) VALUES (?, ?, ?)"
            );

            courseStmt.setString(1, courseName);
            courseStmt.setInt(2, duration);
            courseStmt.setDouble(3, fees);
            courseStmt.executeUpdate();

            // ✅ Get last inserted course_id (Oracle safe way)
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT course_id FROM courses ORDER BY course_id DESC FETCH FIRST 1 ROWS ONLY"
            );

            int courseId = 0;
            if (rs.next()) {
                courseId = rs.getInt(1);
            }

            if (courseId == 0) {
                throw new Exception("Course ID not generated!");
            }

            // ✅ Insert subjects
            Component[] comps = subjectPanel.getComponents();

            for (int i = 0; i < comps.length; i += 2) {

                JTextField subField = (JTextField) comps[i + 1];
                String subjectName = subField.getText().trim();

                if (subjectName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All subject fields must be filled!");
                    conn.rollback();
                    return;
                }

                PreparedStatement subjectStmt = conn.prepareStatement(
                        "INSERT INTO subject (subject_name, course_id, semester) VALUES (?, ?, ?)"
                );

                subjectStmt.setString(1, subjectName);
                subjectStmt.setInt(2, courseId);
                subjectStmt.setInt(3, semester);
                subjectStmt.executeUpdate();
            }

            conn.commit();

            JOptionPane.showMessageDialog(this, "Course & Subjects Added Successfully ✅");
            clearFields();

        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());

        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void clearFields() {
        txtCourseName.setText("");
        txtDuration.setText("");
        txtFees.setText("");
        txtNumSubjects.setText("");
        txtSemester.setText("");
        subjectPanel.removeAll();
        subjectPanel.revalidate();
        subjectPanel.repaint();
    }
}