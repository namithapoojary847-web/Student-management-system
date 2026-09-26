package com.student.management.util;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentResultUI extends JFrame {
	 private static final long serialVersionUID = 1L;

    public StudentResultUI(int studentId) {

        setTitle("Student Dashboard");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Color PRIMARY = new Color(41, 128, 185);
        Color LIGHT_BG = new Color(245, 250, 255);

        // ================= LEFT PANEL =================
        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(260, 0));
        leftPanel.setBackground(new Color(30, 60, 120));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel name = new JLabel();
        JLabel email = new JLabel();
        JLabel phone = new JLabel();
        JLabel studentIdLabel = new JLabel();
        JLabel courseLabel = new JLabel();
        JLabel semesterLabel = new JLabel();   // 🔥 NEW

        // style all labels
        JLabel[] labels = {name, email, phone, studentIdLabel, courseLabel, semesterLabel};

        for (JLabel l : labels) {
            l.setForeground(Color.WHITE);
            l.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        // name styling
        name.setFont(new Font("Segoe UI", Font.BOLD, 16));

        // ================= LEFT PANEL UI =================
        leftPanel.add(Box.createVerticalStrut(30));
        leftPanel.add(name);

        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(studentIdLabel);

        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(courseLabel);

        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(semesterLabel);   // semester (correct position)

        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(email);

        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(phone);
        
        // ================= DATA =================
        String studentName = "";
        List<String> subjects = new ArrayList<>();
        List<Integer> marksList = new ArrayList<>();

        int totalDays = 0, attendedDays = 0, attendancePercent = 0;

        try (Connection con = DatabaseConnection.getConnection()) {

            PreparedStatement ps1 = con.prepareStatement(
                    "SELECT name, email, phone FROM students WHERE student_id=?");
            ps1.setInt(1, studentId);
            ResultSet rs1 = ps1.executeQuery();

            if (rs1.next()) {
                studentName = rs1.getString("name");
                name.setText("👤 " + studentName);
                email.setText("Email: " + rs1.getString("email"));
                phone.setText("Phone: " + rs1.getString("phone"));
                studentIdLabel.setText("ID: " + studentId);
            }

            PreparedStatement psMarks = con.prepareStatement(
                    "SELECT s.subject_name, m.marks " +
                            "FROM marks m JOIN subject s ON m.subject_id=s.subject_id " +
                            "WHERE m.student_id=?");
            psMarks.setInt(1, studentId);
            ResultSet rsM = psMarks.executeQuery();

            while (rsM.next()) {
                subjects.add(rsM.getString("subject_name"));
                marksList.add(rsM.getInt("marks"));
            }

            PreparedStatement psAtt = con.prepareStatement(
                    "SELECT total_days, attended_days FROM attendance WHERE student_id=?");
            psAtt.setInt(1, studentId);
            ResultSet rsA = psAtt.executeQuery();

            if (rsA.next()) {
                totalDays = rsA.getInt("total_days");
                attendedDays = rsA.getInt("attended_days");

                if (totalDays > 0)
                    attendancePercent = (attendedDays * 100) / totalDays;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // ================= RIGHT PANEL =================
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBackground(LIGHT_BG);
        rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        
     // ================= WELCOME WITH BACKGROUND IMAGE =================
        RoundedImagePanel top = new RoundedImagePanel(40, "img/a9.png");
        top.setLayout(new BorderLayout());
        top.setPreferredSize(new Dimension(0, 160));

        JLabel welcome = new JLabel("Welcome Back, " + studentName);
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcome.setForeground(Color.WHITE);
        welcome.setBorder(new EmptyBorder(20, 20, 20, 20));

        top.add(welcome, BorderLayout.WEST);

        rightPanel.add(top, BorderLayout.NORTH);

        // ================= CENTER PANEL =================
        JPanel center = new JPanel(new GridLayout(2, 1, 10, 10));
        center.setBackground(LIGHT_BG);

        // ===== MARKS CARD =====
        JPanel marksPanel = new JPanel(new BorderLayout());
        marksPanel.setBackground(Color.WHITE);
        marksPanel.setBorder(new LineBorder(new Color(200, 220, 240), 2, true));

        // ================= HEADING (TOP) =================
        JLabel title = new JLabel("📊 Progress Report", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(PRIMARY);
        title.setBorder(new EmptyBorder(10, 10, 10, 10));

        marksPanel.add(title, BorderLayout.NORTH);

        // ================= TABLE =================
        String[] cols = {"Subject", "Marks", "Status"};
        Object[][] data = new Object[subjects.size()][3];

        int total = 0;

        for (int i = 0; i < subjects.size(); i++) {
            int m = marksList.get(i);
            total += m;

            data[i][0] = subjects.get(i);
            data[i][1] = m;
            data[i][2] = (m >= 40 ? "Good" : m >= 30 ? "Average" : "Improve");
        }

        JTable table = new JTable(data, cols);
        table.setRowHeight(25);
        table.getTableHeader().setBackground(PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);

        marksPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // ================= BOTTOM INFO =================
        int avg = marksList.size() > 0 ? total / marksList.size() : 0;
        double cgpa = avg / 9.5;
        double percent = avg; // adjust if needed

        JLabel info = new JLabel(
                "Avg: " + avg +
                "   |   %: " + String.format("%.2f", percent) +
                "   |   CGPA: " + String.format("%.2f", cgpa)
        );
        info.setBorder(new EmptyBorder(10, 10, 10, 10));

        marksPanel.add(info, BorderLayout.SOUTH);

        // ===== GRAPH =====
        JPanel graphPanel = new JPanel(new BorderLayout());
        graphPanel.setBackground(Color.WHITE);
        graphPanel.setBorder(new LineBorder(new Color(200, 220, 240), 2, true));

        graphPanel.add(new JLabel("Performance", SwingConstants.CENTER), BorderLayout.NORTH);
        graphPanel.add(new ComboGraph(subjects, marksList), BorderLayout.CENTER);

        center.add(marksPanel);
        center.add(graphPanel);

        rightPanel.add(center, BorderLayout.CENTER);
        
     // ================= LOGOUT BUTTON =================
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomBar.setBackground(LIGHT_BG);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(Color.RED);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutBtn.setPreferredSize(new Dimension(120, 35));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // optional action
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                dispose(); // close current window
                // new LoginUI(); // uncomment if you have login page
            }
        });

        bottomBar.add(logoutBtn);
        rightPanel.add(bottomBar, BorderLayout.SOUTH);

        // ================= DONUT =================
        DonutPanel donut = new DonutPanel(attendancePercent);

        JPanel donutWrapper = new JPanel();
        donutWrapper.setOpaque(false);
        donutWrapper.setPreferredSize(new Dimension(260, 260));
        donutWrapper.add(donut);

        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(donutWrapper);

        // ================= FINAL =================
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // ================= DONUT =================
    class DonutPanel extends JPanel {
    	 private static final long serialVersionUID = 1L;

        int percent;
        float progress = 0f;

        DonutPanel(int percent) {
            this.percent = percent;

            setPreferredSize(new Dimension(180, 400)); // 🔥 increased height
            setOpaque(false);

            Timer t = new Timer(15, e -> {
                progress += 0.01f;
                if (progress >= 1f) {
                    progress = 1f;
                    ((Timer) e.getSource()).stop();
                }
                repaint();
            });

            new Timer(3000, e -> {
                t.start();
                ((Timer) e.getSource()).stop();
            }).start();
        }

        private Color getColor() {
            if (percent < 40) return Color.RED;
            else if (percent < 75) return Color.ORANGE;
            else return new Color(46, 204, 113);
        }

        private String getLabel() {
            if (percent < 40) return "Low Attendance";
            else if (percent < 75) return "Average";
            else return "Good";
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            
         // ================= HEADING =================
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            g2.setColor(Color.WHITE);

            // center the heading
            String heading = "Attendance";
            FontMetrics fm = g2.getFontMetrics();
            int headingX = (getWidth() - fm.stringWidth(heading)) / 2;
            int headingY = 80; // move down

            g2.drawString(heading, headingX, headingY);

            int size = 130;

         // 🔥 MOVED DOWN
            int x = (getWidth() - size) / 2;
            int y = 150;

            // ================= CIRCLE =================
            g2.setStroke(new BasicStroke(20));
            g2.setColor(new Color(220, 230, 240));
            g2.drawOval(x, y, size, size);

            g2.setColor(getColor());
            g2.drawArc(x, y, size, size, 90,
                    -(int)(360 * (percent / 100.0) * progress));

            // ================= PERCENT =================
            g2.setFont(new Font("Segoe UI", Font.BOLD, 22));
            String text = percent + "%";
 

            int tx = (getWidth() - fm.stringWidth(text)) / 2;
            int ty = y + 70;

            g2.setColor(Color.BLACK);
            g2.drawString(text, tx, ty);

            // ================= MAIN LABEL =================
            g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            String label = getLabel();
            FontMetrics fm2 = g2.getFontMetrics();

            int lx = (getWidth() - fm2.stringWidth(label)) / 2;
            int ly = y + size + 45;

            g2.setColor(getColor());
            g2.drawString(label, lx, ly);

            // ================= LEGEND =================
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            FontMetrics fm3 = g2.getFontMetrics();

            String low = "● Low (<40%)";
            String avg = "● Average (40–75%)";
            String good = "● Good (>75%)";

            int baseY = ly + 95;

            // LOW (RED)
            g2.setColor(Color.RED);
            g2.drawString(low, (getWidth() - fm3.stringWidth(low)) / 2, baseY);

            // AVG (YELLOW)
            g2.setColor(Color.ORANGE);
            g2.drawString(avg, (getWidth() - fm3.stringWidth(avg)) / 2, baseY + 18);

            // GOOD (GREEN)
            g2.setColor(new Color(46, 204, 113));
            g2.drawString(good, (getWidth() - fm3.stringWidth(good)) / 2, baseY + 36);
        }
    }

    // ================= GRAPH =================
    class ComboGraph extends JPanel {
    	 private static final long serialVersionUID = 1L;

        List<String> subjects;
        List<Integer> marks;
        float progress = 0f;

        int hoveredIndex = -1;

        ComboGraph(List<String> s, List<Integer> m) {
            this.subjects = s;
            this.marks = m;

            setBackground(Color.WHITE);

            // 🔥 3 sec delay + animation
            Timer delay = new Timer(3000, e -> {

                Timer t = new Timer(10, ev -> {
                    progress += 0.01f;

                    if (progress >= 1f) {
                        progress = 1f;
                        ((Timer) ev.getSource()).stop();
                    }
                    repaint();
                });

                t.start();
                ((Timer) e.getSource()).stop();
            });

            delay.setRepeats(false);
            delay.start();

            // 🔥 hover detection
            addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                public void mouseMoved(java.awt.event.MouseEvent e) {

                    hoveredIndex = -1;

                    int base = getHeight() - 40;

                    for (int i = 0; i < marks.size(); i++) {
                        int x = 70 + i * 80;
                        int h = (int) (marks.get(i) * 2 * progress);
                        int y = base - h;

                        if (e.getX() >= x && e.getX() <= x + 30 &&
                            e.getY() >= y && e.getY() <= base) {

                            hoveredIndex = i;
                        }
                    }

                    repaint();
                }
            });
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int base = getHeight() - 40;

            int n = marks.size();

            int[] cx = new int[n];
            int[] cy = new int[n];

            // ================= BARS + DOTS =================
            for (int i = 0; i < n; i++) {

                int x = 70 + i * 110;
                int h = (int) (marks.get(i) * 2 * progress);
                int y = base - h;

                cx[i] = x + 15;
                cy[i] = y;

                // bars
                g2.setColor(new Color(41, 128, 185));
                g2.fillRoundRect(x, y, 50, h, 10, 10);

                // 🔴 red dot
                g2.setColor(Color.RED);
                g2.fillOval(x + 10, y - 6, 10, 10);

                // values
                g2.setColor(Color.BLACK);
                g2.drawString(String.valueOf(marks.get(i)), x, y - 10);

                // subject
                g2.drawString(subjects.get(i), x, base + 15);

                // hover tooltip
                if (i == hoveredIndex) {
                    g2.setColor(new Color(0, 0, 0, 180));
                    g2.fillRoundRect(x - 10, y - 40, 80, 25, 10, 10);

                    g2.setColor(Color.WHITE);
                    g2.drawString("Marks: " + marks.get(i), x - 5, y - 22);
                }
            }

            // ================= TREND LINE =================
            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(2f));

            for (int i = 0; i < n - 1; i++) {
                g2.drawLine(cx[i], cy[i], cx[i + 1], cy[i + 1]);
            }

            // reset stroke
            g2.setStroke(new BasicStroke(1f));
        }
    }
class RoundedImagePanel extends JPanel {
	 private static final long serialVersionUID = 1L;
    private Image img;
    private int radius;

    RoundedImagePanel(int radius, String path) {
        this.radius = radius;
        this.img = new ImageIcon(path).getImage();
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        Shape shape = new RoundRectangle2D.Float(
                0, 0, getWidth(), getHeight(), radius, radius);

        g2.setClip(shape);

        if (img != null) {
            g2.drawImage(img, 0, 0, getWidth(), getHeight(), this);
        }

        // dark overlay for text visibility
        g2.setColor(new Color(0, 0, 0, 120));
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.dispose();
    }
}
}