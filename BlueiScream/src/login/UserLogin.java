package login;

import chatRoom.ChatRoomList;
import components.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserLogin extends JFrame {

    private UserDao dao;
    private UnderLineTextField userTextField;
    private UnderLinePasswordField passwordField;

    public UserLogin() {
        dao = new UserDao();
        Insets in = new Insets(50, 0, 0, 0);
        Color textColor = Color.white;
        float fontSize = 24f;
        int sidePaddingSize = 30;
        int totalSize = 400;
        int tfWidth = totalSize - sidePaddingSize * 3 - 100;

        setTitle("Login");
        setSize(totalSize, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // chat icon
        ImageIcon chatIcon = new ImageIcon("images/chat_icon.png");
        JLabel icon = new JLabel(chatIcon);
        icon.setBackground(new Color(255, 214, 214));
        icon.setOpaque(true);
        icon.setBorder(BorderFactory.createEmptyBorder(150, 0, 0, 0));
        add(icon, BorderLayout.NORTH);

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
        gbc.insets = new Insets(0, 0, 0, 0);
        p2.add(userTextField);
        centerP.add(p2, gbc);
        gbc.insets = in;

        // --

        PinkPanel p3 = new PinkPanel();
        JLabel lb3 = new JLabel("Password");

        p3.setLayout(new BorderLayout());
        lb3.setFont(lb3.getFont().deriveFont(fontSize));
        lb3.setForeground(textColor);
        passwordField = new UnderLinePasswordField();
        passwordField.setPreferredSize(new Dimension(tfWidth, 50));

        centerP.add(lb3, gbc);
        gbc.insets = new Insets(0, 0, 0, 0);
        p3.add(passwordField, BorderLayout.CENTER);

        JPanel jbp = new JPanel(new BorderLayout());
        JButton joinBtn = new JButton("Sign up");
        JButton searchBtn = new JButton("forgot Id / password");
//        Font jbf = joinBtn.getFont();
        Border empty = BorderFactory.createEmptyBorder(0, 0, 0, 0);

        joinBtn.setBackground(null);
        joinBtn.setForeground(Color.white);
        joinBtn.setBorder(empty);
        joinBtn.setOpaque(true);

        searchBtn.setBackground(null);
        searchBtn.setForeground(Color.white);
        searchBtn.setBorder(empty);
        searchBtn.setOpaque(true);
//        joinBtn.setFont(new Font(jbf.getFontName(), jbf.getStyle(), 20));

        jbp.setBackground(null);
        jbp.setBorder(empty);
        jbp.add(joinBtn, BorderLayout.EAST);
        jbp.add(searchBtn, BorderLayout.WEST);
        p3.add(jbp, BorderLayout.SOUTH);

        centerP.add(p3, gbc);
        add(centerP, BorderLayout.CENTER);

        // button

        PinkPanel buttonP = new PinkPanel();
        ColorRoundButton loginButton = new ColorRoundButton("Login", Color.white, new Color(255, 214, 214), 30);
        buttonP.setBorder(BorderFactory.createEmptyBorder(0, 0, 100, 0));
        buttonP.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonP.add(loginButton);
        add(buttonP, BorderLayout.SOUTH);

        // 암호화 추가
        loginButton.addActionListener(e -> {
            int res = dao.userLoginCheck(userTextField.getText(), passwordField.getText());

            if (res > 0) {
                new ChatRoomList(userTextField.getText());
            } else {
                JOptionPane.showMessageDialog(UserLogin.this, "로그인 실패");
            }
        });
        joinBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UserJoin();
                dispose();
            }
        });
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SearchUserInfo();
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new UserLogin();
    }
}
