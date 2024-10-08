package login;

import components.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class UserJoin extends JFrame {
    private UserDao dao;
    private UnderLineTextField userNameField;
    private UnderLineTextField emailField;
    private UnderLineTextField userTextField;
    private UnderLinePasswordField passwordField;
    private final int LIMIT = 30;
    private final int totalSize = 400;
    private final int sidePaddingSize = 30;
    private final int tfWidth = totalSize - sidePaddingSize * 3 - 100;

    public UserJoin() {
        dao = new UserDao();
        Insets in = new Insets(30, 0, 0, 0);
        Color textColor = Color.white;
        Color dark = new Color(0, 38, 66);
        Color white =Color.white;

        float fontSize = 24f;

        setTitle("Join");
        setSize(totalSize, 800);
        setLocationRelativeTo(null);
        setResizable(false);

        // chat icon
        JPanel iconP = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel icon = new JLabel("Sign UP");
        iconP.setBackground(dark);
        icon.setForeground(white);
        icon.setFont(icon.getFont().deriveFont(40f));
        icon.setBackground(null);
        icon.setOpaque(true);
        icon.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        iconP.add(icon);
        add(iconP, BorderLayout.NORTH);

        // left, right padding
        DarkPanel leftPadding = new DarkPanel(sidePaddingSize, "left");
        add(leftPadding, BorderLayout.WEST);
        DarkPanel rightPadding = new DarkPanel(sidePaddingSize, "right");
        add(rightPadding, BorderLayout.EAST);

        // input field
        DarkPanel centerP = new DarkPanel();
        centerP.setSize(tfWidth + 100, 400);
        centerP.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.BOTH;

        // --

        DarkPanel p2 = new DarkPanel();
        JLabel lb2 = new JLabel("UserId");

        p2.setLayout(new FlowLayout());
        p2.setSize(totalSize - sidePaddingSize * 2, 100);
        lb2.setForeground(textColor);
        lb2.setFont(lb2.getFont().deriveFont(fontSize));
        userTextField = makeTextField();

        centerP.add(lb2, gbc);
        p2.add(userTextField);
        centerP.add(p2, gbc);
        gbc.insets = in;

        // --

        DarkPanel p3 = new DarkPanel();
        JLabel lb3 = new JLabel("Password");

        p3.setLayout(new FlowLayout());
        lb3.setFont(lb3.getFont().deriveFont(fontSize));
        lb3.setForeground(textColor);
        passwordField = new UnderLinePasswordField();
        passwordField.setPreferredSize(new Dimension(tfWidth, 50));
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (passwordField.getText().length() >= LIMIT)
                    e.consume();
            }
        });

        centerP.add(lb3, gbc);
        gbc.insets = new Insets(0, 0, 0, 0);
        p3.add(passwordField);
        centerP.add(p3, gbc);
        gbc.insets = in;

        // --

        JLabel lb = new JLabel("UserName");
        DarkPanel p1 = new DarkPanel();

        p1.setLayout(new FlowLayout());
        lb.setFont(lb.getFont().deriveFont(fontSize));
        lb.setForeground(textColor);
        userNameField = makeTextField();

        centerP.add(lb, gbc);
        gbc.insets = new Insets(0, 0, 0, 0);
        p1.add(userNameField);
        centerP.add(p1, gbc);
        gbc.insets = in;

        // --

        DarkPanel p4 = new DarkPanel();
        JLabel lb1 = new JLabel("Email");

        p4.setLayout(new FlowLayout());
        p4.setSize(totalSize - sidePaddingSize * 2, 100);
        lb1.setForeground(textColor);
        lb1.setFont(lb1.getFont().deriveFont(fontSize));
        emailField = makeTextField();

        centerP.add(lb1, gbc);
        gbc.insets = new Insets(0, 0, 0, 0);
        p4.add(emailField);
        centerP.add(p4, gbc);
        gbc.insets = in;

        add(centerP, BorderLayout.CENTER);

        // button

        DarkPanel buttonP = new DarkPanel();
        ColorRoundButton joinButton = new ColorRoundButton("Sign Up", new Color(229, 149, 0), dark, 25);
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
                JOptionPane.showMessageDialog(UserJoin.this, "사용하실 수 없는 ID입니다.");
                return;
            }

            dao.userInsert(id, name, pw, email);
            JOptionPane.showMessageDialog(UserJoin.this, "회원가입에 성공하셨습니다.");

            dispose();
        });

        setVisible(true);
    }

    private UnderLineTextField makeTextField() {
        UnderLineTextField ultf = new UnderLineTextField(Color.white, Color.white);
        ultf.setPreferredSize(new Dimension(tfWidth, 50));
        ultf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (ultf.getText().length() >= LIMIT)
                    e.consume();
            }
        });

        return ultf;
    }

    public static void main(String[] args) {
        new UserJoin();
    }
}
