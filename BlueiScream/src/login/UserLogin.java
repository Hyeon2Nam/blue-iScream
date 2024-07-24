package login;

import components.*;

import javax.swing.*;
import java.awt.*;

public class UserLogin extends JFrame {
    UserDao dao;

    private UnderLineTextField userNameField;
    private UnderLineTextField userTextField;
    private UnderLinePasswordField passwordField;
    private JButton loginButton;
    private JButton joinButton;
    private PinkPanel centerP;


    public UserLogin() {
        Color textColor = Color.white;
        float fontSize = 24f;
        int sidePaddingSize = 30;
        int totalSize = 400;
        int tfWidth = totalSize - sidePaddingSize * 3 -100 ;

        setTitle("Login");
        setSize(totalSize, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // chat icon
        ImageIcon chatIcon = new ImageIcon("images/chat_icon.png");
        JLabel icon = new JLabel(chatIcon);
        icon.setBackground(new Color(255, 214, 214));
        icon.setOpaque(true);
        icon.setBorder(BorderFactory.createEmptyBorder(150,0,0,0));

        add(icon, BorderLayout.NORTH);

        // left, right padding
        PinkPanel leftPadding = new PinkPanel(sidePaddingSize, "left");
        add(leftPadding, BorderLayout.WEST);
        PinkPanel rightPadding = new PinkPanel(sidePaddingSize, "right");
        add(rightPadding, BorderLayout.EAST);

        // input field
        centerP = new PinkPanel();
        centerP.setSize(tfWidth+100, 400);
        centerP.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.BOTH;

        // --

        JLabel lb = new JLabel("UserName");
        PinkPanel p1 = new PinkPanel();

        p1.setLayout(new FlowLayout());
        lb.setFont(lb.getFont().deriveFont(fontSize));
        lb.setForeground(textColor);
        userNameField = new UnderLineTextField();
        userNameField.setPreferredSize(new Dimension(tfWidth, 50));

        centerP.add(lb, gbc);
        p1.add(userNameField);
        centerP.add(p1, gbc);
        gbc.ipady = 1;


        // --

        PinkPanel p2 = new PinkPanel();
        JLabel lb2 = new JLabel("UserId");

        p2.setLayout(new FlowLayout());
        p2.setSize(totalSize - sidePaddingSize * 2 , 100);
        lb2.setForeground(textColor);
        lb2.setFont(lb2.getFont().deriveFont(fontSize));
        userTextField = new UnderLineTextField();
        userTextField.setPreferredSize(new Dimension(tfWidth, 50));

        centerP.add(lb2, gbc);
        p2.add(userTextField);
        gbc.ipady = 0;
        centerP.add(p2, gbc);

        // --

        PinkPanel p3 = new PinkPanel();
        JLabel lb3 = new JLabel("Password");

        p3.setLayout(new FlowLayout());
        lb3.setFont(lb3.getFont().deriveFont(fontSize));
        lb3.setForeground(textColor);
        passwordField = new UnderLinePasswordField();
        passwordField.setPreferredSize(new Dimension(tfWidth, 50));

        centerP.add(lb3, gbc);
        p3.add(passwordField);
        centerP.add(p3, gbc);

        //

        loginButton = new JButton("Login");
        add(loginButton, BorderLayout.SOUTH);

        add(centerP, BorderLayout.CENTER);

//        loginButton.addActionListener(e -> {
//            if (dao.userLoginCheck(userTextField.getText(), passwordField.getText()) > 0)
//                JOptionPane.showMessageDialog(UserLogin.this, "로그인 성공");
//            else
//                JOptionPane.showMessageDialog(UserLogin.this, "로그인 실패");
//        });
//
//        add(loginButton);
//
//        joinButton = new JButton("Join");
//        joinButton.addActionListener(e -> {
//            new UserJoin();
//            dispose();
//        });
//        add(joinButton);

        setVisible(true);
    }

    public static void main(String[] args) {
        new UserLogin();
    }
}
