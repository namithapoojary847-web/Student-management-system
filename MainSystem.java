package com.student.management.util;

import javax.swing.*;
import java.awt.*;

public class MainSystem {

    JFrame frame;

    public MainSystem() {
        showRoleSelection();
    }

    // 🔵 ROLE SELECTION (MODERN UI)
    void showRoleSelection() {

        frame = new JFrame("Select Role");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new GridLayout(1, 2));

        // 🔵 LEFT PANEL (IMAGE + OVERLAY + TEXT)
        JPanel leftPanel = new JPanel() {
            private static final long serialVersionUID = 1L;

            Image image = new ImageIcon("img/a5.png").getImage();

            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Draw image
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);

                // Dark overlay
                g.setColor(new Color(0, 0, 0, 80));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        leftPanel.setLayout(new GridBagLayout());

        // 🌟 GLOW TITLE
        JLabel glowTitle = new JLabel("Welcome to EduFlow", SwingConstants.CENTER);
        glowTitle.setFont(new Font("Segoe UI", Font.BOLD, 48));
        glowTitle.setForeground(Color.WHITE);

        // Add glow using border trick instead of paint override
        glowTitle.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20),
            BorderFactory.createLineBorder(new Color(52, 152, 219, 80), 2, true)
        ));

        // ✨ SUBTITLE
        JLabel subtitle = new JLabel("Simplifying Student Management");
        subtitle.setForeground(Color.WHITE);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 20));

        // 📦 TEXT PANEL (FIXED LAYOUT)
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        glowTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        textPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        textPanel.add(glowTitle);
        textPanel.add(Box.createVerticalStrut(20));
        textPanel.add(subtitle);

        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.gridx = 0;
        gbcLeft.gridy = 0;
        gbcLeft.anchor = GridBagConstraints.CENTER;

        leftPanel.add(textPanel, gbcLeft);

        // 🔴 RIGHT PANEL
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel roleTitle = new JLabel("Select Role");
        roleTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        roleTitle.setForeground(new Color(60, 60, 60));

        JButton adminBtn = createButton("Admin");
        JButton studentBtn = createButton("Student");

        adminBtn.setPreferredSize(new Dimension(200, 45));
        studentBtn.setPreferredSize(new Dimension(200, 45));

        adminBtn.addActionListener(e -> {
            frame.dispose();
            showAdminLogin();
        });

        studentBtn.addActionListener(e -> {
            frame.dispose();
            new LoginUI();
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        rightPanel.add(roleTitle, gbc);

        gbc.gridy++;
        rightPanel.add(adminBtn, gbc);

        gbc.gridy++;
        rightPanel.add(studentBtn, gbc);

        // 🔥 SPLIT
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.6));
        splitPane.setDividerSize(0);
        splitPane.setEnabled(false);

        frame.add(splitPane);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // 🔴 ADMIN LOGIN
    void showAdminLogin() {

        frame = new JFrame("Admin Login");
        frame.setSize(500, 400);
        frame.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 245, 245));
        panel.setBounds(0, 0, 500, 400);
        panel.setLayout(null);

        JLabel title = new JLabel("Admin Login");
        title.setBounds(180, 30, 200, 40);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JTextField user = new JTextField();
        user.setBounds(130, 100, 240, 35);
        user.setBorder(BorderFactory.createTitledBorder("Username"));

        JPasswordField pass = new JPasswordField();
        pass.setBounds(130, 160, 240, 35);
        pass.setBorder(BorderFactory.createTitledBorder("Password"));

        JButton login = createButton("Login");
        login.setBounds(170, 230, 150, 40);

        login.addActionListener(e -> {

            String username = user.getText().trim();
            String password = new String(pass.getPassword()).trim();

            if (username.equals("admin") && password.equals("admin123")) {

                JOptionPane.showMessageDialog(frame, "Login Successful ✅");
                frame.dispose();
                new SMSDashboard();

            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Credentials ❌");
            }
        });

        panel.add(title);
        panel.add(user);
        panel.add(pass);
        panel.add(login);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    // 🔥 BUTTON
    JButton createButton(String text) {
        JButton btn = new JButton(text) {
        	private static final long serialVersionUID = 1L;

            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                super.paintComponent(g);
            }
        };

        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(52, 152, 219));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(41, 128, 185));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(52, 152, 219));
            }
        });

        return btn;
    }

    public static void main(String[] args) {
        new MainSystem();
    }
}