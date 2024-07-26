package login;

import components.*;
import java.awt.*;
import javax.swing.*;

public class UserJoin extends JFrame {
    private UserDao dao;
    private UnderLineTextField userNameField;
    private UnderLineTextField emailField;
    private UnderLineTextField userTextField;
    private UnderLinePasswordField passwordField;

    public UserJoin() {
        dao = new UserDao();
        Insets in = new Insets(30, 0, 0, 0);
        Color textColor = Color.white;
        Color pink = new Color(255, 214, 214);

        float fontSize = 24f;
        int sidePaddingSize = 30;
        int totalSize = 400;
        int tfWidth = totalSize - sidePaddingSize * 3 - 100;

        setTitle("Join");
        setSize(totalSize, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // chat icon
        JPanel iconP = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel icon = new JLabel("Sign UP");
        iconP.setBackground(pink);
        icon.setForeground(Color.white);
        icon.setFont(icon.getFont().deriveFont(50f));
        icon.setBackground(null);
        icon.setOpaque(true);
        icon.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        iconP.add(icon);
        add(iconP, BorderLayout.NORTH);

        // left, right padding
        PinkPanel leftPadding = new PinkPanel(sidePaddingSize, "left");
        add(leftPadding, BorderLayout.WEST);
        PinkPanel rightPadding = new PinkPanel(sidePaddingSize, "right");
        add(rightPadding, BorderLayout.EAST);

        // input field
        PinkPanel centerP = new PinkPanel();
        centerP.setSize(tfWidth + 100, 400);
        centerP.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.BOTH;

        // --

        PinkPanel p2 = new PinkPanel();
        JLabel lb2 = new JLabel("UserId");

        p2.setLayout(new FlowLayout());
        p2.setSize(totalSize - sidePaddingSize * 2, 100);
        lb2.setForeground(textColor);
        lb2.setFont(lb2.getFont().deriveFont(fontSize));
        userTextField = new UnderLineTextField();
        userTextField.setPreferredSize(new Dimension(tfWidth, 50));

        centerP.add(lb2, gbc);
        p2.add(userTextField);
        centerP.add(p2, gbc);
        gbc.insets = in;

        // --

        PinkPanel p3 = new PinkPanel();
        JLabel lb3 = new JLabel("Password");

        p3.setLayout(new FlowLayout());
        lb3.setFont(lb3.getFont().deriveFont(fontSize));
        lb3.setForeground(textColor);
        passwordField = new UnderLinePasswordField();
        passwordField.setPreferredSize(new Dimension(tfWidth, 50));

        centerP.add(lb3, gbc);
        gbc.insets = new Insets(0, 0, 0, 0);
        p3.add(passwordField);
        centerP.add(p3, gbc);
        gbc.insets = in;

        add(centerP, BorderLayout.CENTER);

        // --

        JLabel lb = new JLabel("UserName");
        PinkPanel p1 = new PinkPanel();

        p1.setLayout(new FlowLayout());
        lb.setFont(lb.getFont().deriveFont(fontSize));
        lb.setForeground(textColor);
        userNameField = new UnderLineTextField();
        userNameField.setPreferredSize(new Dimension(tfWidth, 50));

        centerP.add(lb, gbc);
        gbc.insets = new Insets(0, 0, 0, 0);
        p1.add(userNameField);
        centerP.add(p1, gbc);
        gbc.insets = in;

        // --

        PinkPanel p4 = new PinkPanel();
        JLabel lb1 = new JLabel("Email");

        p4.setLayout(new FlowLayout());
        p4.setSize(totalSize - sidePaddingSize * 2, 100);
        lb1.setForeground(textColor);
        lb1.setFont(lb1.getFont().deriveFont(fontSize));
        emailField = new UnderLineTextField();
        emailField.setPreferredSize(new Dimension(tfWidth, 50));

        centerP.add(lb1, gbc);
        gbc.insets = new Insets(0, 0, 0, 0);
        p4.add(emailField);
        centerP.add(p4, gbc);
        gbc.insets = in;

        // button

        PinkPanel buttonP = new PinkPanel();
        ColorRoundButton joinButton = new ColorRoundButton("Sign Up", Color.white, pink, 25);
        buttonP.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonP.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        buttonP.add(joinButton);
        add(buttonP, BorderLayout.SOUTH);

        joinButton.addActionListener(e -> {
            String id = userTextField.getText();
            String name = userNameField.getText();
            String email = emailField.getText();
            String pw = passwordField.getText();

            if (id.isEmpty() || name.isEmpty() || email.isEmpty() || pw.isEmpty())
                return;

            if (dao.isAlreadyUser(id, email) > 0) {
                JOptionPane.showMessageDialog(UserJoin.this, "이미 존재하는 ID입니다.");
                return;
            }

            dao.userInsert(id, name, pw, email);
            JOptionPane.showMessageDialog(UserJoin.this, "회원가입에 성공하셨습니다.");

            new UserLogin();
            dispose();
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new UserJoin();
    }
}
