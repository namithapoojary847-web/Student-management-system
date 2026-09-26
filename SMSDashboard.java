package com.student.management.util;

import javax.swing.*;
import java.awt.*;

public class SMSDashboard {

    JFrame frame;

    public SMSDashboard() {

        frame = new JFrame("Admin Dashboard");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 🌈 BACKGROUND
        JPanel container = new JPanel(new BorderLayout()) {
        	private static final long serialVersionUID = 1L;

            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(20, 30, 48),
                        getWidth(), getHeight(), new Color(36, 59, 85)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // ===== SIDEBAR =====
        JPanel sidebarWrapper = new JPanel(new BorderLayout());
        sidebarWrapper.setOpaque(false);
        sidebarWrapper.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0)); // 🔥 reduce height

        JPanel sidebar = new RoundedPanel(30);
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setBackground(new Color(255, 255, 255, 40));
        sidebar.setLayout(new GridLayout(8, 1, 10, 15));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel heading = new JLabel("ADMIN");
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 18));
        heading.setHorizontalAlignment(SwingConstants.CENTER);

        sidebar.add(heading);
        sidebar.add(createButton("Add Student"));
        sidebar.add(createButton("Add Course"));
        sidebar.add(createButton("Enroll Student"));
        sidebar.add(createButton("View Enrollments"));
        sidebar.add(createButton("Test Scores"));
        sidebar.add(createButton("Attendance"));

        sidebarWrapper.add(sidebar, BorderLayout.CENTER);

        // ===== MAIN =====
        JPanel main = new JPanel();
        main.setOpaque(false);
        main.setLayout(null);

        // 🔹 LOGOUT BUTTON (TOP RIGHT)
        JButton logout = new JButton("Logout");
        logout.setBounds(1100, 680, 120, 40);
        logout.setBackground(new Color(220, 53, 69));
        logout.setForeground(Color.WHITE);
        logout.setFocusPainted(false);
        logout.setCursor(new Cursor(Cursor.HAND_CURSOR));

        logout.addActionListener(e -> {
            frame.dispose();
            new MainSystem(); // go back
        });

        main.add(logout);

        // 🔹 WELCOME
        RoundedPanel welcome = new RoundedPanel(30);
        welcome.setBounds(100, 30, 900, 80); // adjusted due to logout
        welcome.setBackground(new Color(255, 255, 255, 40));

        JLabel welcomeText = new JLabel("👋 Welcome to Dashboard, Hi Admin");
        welcomeText.setForeground(Color.WHITE);
        welcomeText.setFont(new Font("Segoe UI", Font.BOLD, 22));
        welcome.add(welcomeText);

        main.add(welcome);

        // 🔹 CARDS
        main.add(createCard("2000+ Students", 100, 140));
        main.add(createCard("100% Results", 350, 140));
        main.add(createCard("50+ Faculty", 600, 140));
        main.add(createCard("100% Placements", 850, 140));

        // 🔹 COURSE PANEL
        RoundedPanel coursePanel = new RoundedPanel(30);
        coursePanel.setBounds(100, 320, 1000, 350);
        coursePanel.setBackground(new Color(255, 255, 255, 20));
        coursePanel.setLayout(new GridLayout(2, 2, 20, 20));
        coursePanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        coursePanel.add(createCourse("BCA", "Computer Applications", "3 Years"));
        coursePanel.add(createCourse("BBA", "Business Administration", "3 Years"));
        coursePanel.add(createCourse("BA", "Arts & Humanities", "3 Years"));
        coursePanel.add(createCourse("BCom", "Commerce & Finance", "3 Years"));

        main.add(coursePanel);

        container.add(sidebarWrapper, BorderLayout.WEST);
        container.add(main, BorderLayout.CENTER);

        frame.setContentPane(container);
        frame.setVisible(true);
    }

    // 🔵 BUTTON
    JButton createButton(String text) {

        JButton btn = new JButton(text);

        btn.setFocusPainted(false);
        btn.setBackground(new Color(30, 30, 30));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btn.addActionListener(e -> {
            switch (text) {
                case "Add Student": new AddStudentForm(); break;
                case "Add Course": new AddCourseFormGUI(); break;
                case "Enroll Student": new EnrollStudent(); break;
                case "View Enrollments": new ViewEnrollments(); break;
                case "Test Scores": new TestUI(); break;
                case "Attendance": new Attendance(); break;
            }
        });

        return btn;
    }

    JPanel createCard(String text, int x, int y) {

        JPanel card = new RoundedPanel(25) {
        	private static final long serialVersionUID = 1L;


            Image image;

            {
                String path = "";

                if (text.contains("Students")) path = "img/a1.png";
                else if (text.contains("Results")) path = "img/a3.png";
                else if (text.contains("Faculty")) path = "img/a2.png";
                else if (text.contains("Placements")) path = "img/a4.png";

                image = new ImageIcon(path).getImage();
            }

            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                // 🔥 CLIP TO ROUNDED SHAPE
                g2.setClip(new java.awt.geom.RoundRectangle2D.Float(
                        0, 0, getWidth(), getHeight(), 25, 25));

                // 🔥 DRAW IMAGE (NO SHADE)
                g2.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };

        card.setBounds(x, y, 200, 140);
        card.setLayout(new BorderLayout());

        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(label, BorderLayout.SOUTH);

        return card;
    }

    // 🔵 COURSE BOX
    JPanel createCourse(String name, String desc, String duration) {

        RoundedPanel panel = new RoundedPanel(20);
        panel.setBackground(new Color(255,255,255,30));
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        JLabel title = new JLabel(name);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        JLabel description = new JLabel(desc);
        description.setForeground(Color.LIGHT_GRAY);

        JLabel dur = new JLabel("Duration: " + duration);
        dur.setForeground(new Color(200,200,200));

        JPanel center = new JPanel(new GridLayout(2,1));
        center.setOpaque(false);
        center.add(description);
        center.add(dur);

        panel.add(title, BorderLayout.NORTH);
        panel.add(center, BorderLayout.CENTER);

        return panel;
    }

    // 🔵 ROUNDED PANEL
    class RoundedPanel extends JPanel {
    	private static final long serialVersionUID = 1L;


        int radius;

        RoundedPanel(int r) {
            radius = r;
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        }
    }
}