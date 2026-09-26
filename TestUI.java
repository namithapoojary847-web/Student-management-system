package com.student.management.util;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.*;

public class TestUI extends JFrame {

    private static final long serialVersionUID = 1L;

    JTextField txtStudentId;
    JButton btnLoad, btnSubmit;
    JPanel marksPanel;

    public TestUI() {

        setTitle("Student Marks Entry System");
        setSize(550, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        // HEADER
        JPanel header = new JPanel(new GridLayout(2, 2, 15, 15));
        header.setBorder(new CompoundBorder(
                new LineBorder(new Color(52, 152, 219), 2, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        header.setBackground(Color.WHITE);

        header.add(new JLabel("Enter Student ID:"));
        txtStudentId = new JTextField();
        header.add(txtStudentId);

        btnLoad = new JButton("Load Subjects");
        btnLoad.setBackground(new Color(52, 152, 219));
        btnLoad.setForeground(Color.WHITE);

        header.add(btnLoad);
        header.add(new JLabel(""));

        add(header, BorderLayout.NORTH);

        // CENTER PANEL
        marksPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        marksPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        marksPanel.setBackground(Color.WHITE);

        add(new JScrollPane(marksPanel), BorderLayout.CENTER);

        // FOOTER
        btnSubmit = new JButton("Save Marks");
        btnSubmit.setBackground(new Color(39, 174, 96));
        btnSubmit.setForeground(Color.WHITE);

        JPanel footer = new JPanel();
        footer.add(btnSubmit);
        add(footer, BorderLayout.SOUTH);

        // ACTIONS
        btnLoad.addActionListener(e -> loadSubjects());
        btnSubmit.addActionListener(e -> saveMarks());

        setVisible(true);
    }

    // 🔥 LOAD SUBJECTS (BASED ON ENROLLMENT TABLE)
    private void loadSubjects() {

        marksPanel.removeAll();

        String studentId = txtStudentId.getText().trim();

        if (studentId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Student ID");
            return;
        }

        try (Connection con = DatabaseConnection.getConnection()) {

            // Step 1: get course_id from enrollment
            PreparedStatement ps1 = con.prepareStatement(
                "SELECT course_id FROM enrollments WHERE student_id = ?"
            );

            ps1.setInt(1, Integer.parseInt(studentId));
            ResultSet rs1 = ps1.executeQuery();

            if (!rs1.next()) {
                JOptionPane.showMessageDialog(this, "No enrollment found for this student!");
                return;
            }

            int courseId = rs1.getInt("course_id");

            // Step 2: get subjects using course_id
            PreparedStatement ps2 = con.prepareStatement(
                "SELECT subject_id, subject_name FROM subject WHERE course_id = ?"
            );

            ps2.setInt(1, courseId);
            ResultSet rs2 = ps2.executeQuery();

            boolean found = false;

            while (rs2.next()) {

                found = true;

                int subjectId = rs2.getInt("subject_id");
                String subjectName = rs2.getString("subject_name");

                JLabel lbl = new JLabel(subjectName);
                lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));

                JTextField txt = new JTextField();
                txt.putClientProperty("subjectId", subjectId);

                marksPanel.add(lbl);
                marksPanel.add(txt);
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "No subjects found for this course!");
            }

            marksPanel.revalidate();
            marksPanel.repaint();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    // 💾 SAVE MARKS
    private void saveMarks() {

        String studentId = txtStudentId.getText().trim();

        if (studentId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Student ID");
            return;
        }

        try (Connection con = DatabaseConnection.getConnection()) {

            Component[] comps = marksPanel.getComponents();

            for (int i = 0; i < comps.length; i += 2) {

                JTextField txt = (JTextField) comps[i + 1];

                Object obj = txt.getClientProperty("subjectId");
                if (obj == null) continue;

                int subjectId = (int) obj;

                String value = txt.getText().trim();
                if (value.isEmpty()) continue;

                int marks = Integer.parseInt(value);

                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO marks (student_id, subject_id, marks) VALUES (?, ?, ?)"
                );

                ps.setInt(1, Integer.parseInt(studentId));
                ps.setInt(2, subjectId);
                ps.setInt(3, marks);

                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Marks Saved Successfully ✅");

            marksPanel.removeAll();
            marksPanel.revalidate();
            marksPanel.repaint();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}