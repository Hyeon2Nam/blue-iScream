package login;

import chatRoom.Alram;
import menu.ChatRoomList;
import components.*;
import menu.MainMenuView;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class UserLogin extends JFrame {
    private UserDao dao;
    private UnderLineTextField userTextField;
    private UnderLinePasswordField passwordField;
    private int LIMIT = 30;

    public UserLogin() {
        Color white = Color.white;

        dao = new UserDao();
        Insets in = new Insets(50, 0, 0, 0);
        Color textColor = white;
        float fontSize = 24f;
        int sidePaddingSize = 30;
        int totalSize = 400;
        int tfWidth = totalSize - sidePaddingSize * 3 - 100;

        setTitle("Login");
        setSize(totalSize, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // chat icon
        ImageIcon chatIcon = new ImageIcon("images/chat_icon.png");
        JLabel icon = new JLabel(chatIcon);
        icon.setBackground(new Color(0, 38, 66));
        icon.setOpaque(true);
        icon.setBorder(BorderFactory.createEmptyBorder(150, 0, 0, 0));
        add(icon, BorderLayout.NORTH);

        // left, right padding
        DarkPanel leftPadding = new DarkPanel(sidePaddingSize, "left");
        add(leftPadding, BorderLayout.WEST);
        DarkPanel rightPadding = new DarkPanel(sidePaddingSize, "right");
        add(rightPadding, BorderLayout.EAST);

        // input fieldaaa
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
        userTextField = new UnderLineTextField(white, white);
        userTextField.setPreferredSize(new Dimension(tfWidth, 50));
        userTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (userTextField.getText().length() >= LIMIT)
                    e.consume();
            }
        });

        centerP.add(lb2, gbc);
        gbc.insets = new Insets(0, 0, 0, 0);
        p2.add(userTextField);
        centerP.add(p2, gbc);
        gbc.insets = in;

        // --

        DarkPanel p3 = new DarkPanel();
        JLabel lb3 = new JLabel("Password");

        p3.setLayout(new BorderLayout());
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
        p3.add(passwordField, BorderLayout.CENTER);

        JPanel jbp = new JPanel(new BorderLayout());
        JButton joinBtn = new JButton("Sign up");
        JButton searchBtn = new JButton("forgot Id / password");
        Border empty = BorderFactory.createEmptyBorder(0, 0, 0, 0);

        joinBtn.setBackground(null);
        joinBtn.setForeground(white);
        joinBtn.setBorder(empty);
        joinBtn.setOpaque(true);

        searchBtn.setBackground(null);
        searchBtn.setForeground(white);
        searchBtn.setBorder(empty);
        searchBtn.setOpaque(true);

        Font jbf = joinBtn.getFont();
        joinBtn.setFont(new Font(jbf.getFontName(), jbf.getStyle(), 15));
        searchBtn.setFont(new Font(jbf.getFontName(), jbf.getStyle(), 15));

        jbp.setBackground(null);
        jbp.setBorder(empty);
        jbp.add(joinBtn, BorderLayout.EAST);
        jbp.add(searchBtn, BorderLayout.WEST);
        p3.add(jbp, BorderLayout.SOUTH);

        centerP.add(p3, gbc);
        add(centerP, BorderLayout.CENTER);

        // button

        DarkPanel buttonP = new DarkPanel();
        ColorRoundButton loginButton = new ColorRoundButton("Login", new Color(229, 149, 0), new Color(0, 38, 66), 30);
        buttonP.setBorder(BorderFactory.createEmptyBorder(0, 0, 100, 0));
        buttonP.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonP.add(loginButton);
        add(buttonP, BorderLayout.SOUTH);

        loginButton.addActionListener(e -> {
            int res = dao.userLoginCheck(userTextField.getText(), passwordField.getText());

            if (res > 0) {
                JOptionPane op = new JOptionPane("로딩중......");
                new MainMenuView(userTextField.getText());
                op.setVisible(false);
                dispose();
            } else {
                JOptionPane.showMessageDialog(UserLogin.this, "로그인 실패");
            }
        });
        joinBtn.addActionListener(e -> new UserJoin());
        searchBtn.addActionListener(e -> new SearchUserInfo());

        setVisible(true);
    }

    public static void main(String[] args) {
        new UserLogin();
    }
}
