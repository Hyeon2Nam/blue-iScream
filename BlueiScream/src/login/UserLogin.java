package login;

import components.*;

import javax.swing.*;
import java.awt.*;

public class UserLogin extends JFrame {

  private UserDao dao;
  private UnderLineTextField userTextField;
  private UnderLinePasswordField passwordField;

  public UserLogin() {
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

    p3.setLayout(new FlowLayout());
    lb3.setFont(lb3.getFont().deriveFont(fontSize));
    lb3.setForeground(textColor);
    passwordField = new UnderLinePasswordField();
    passwordField.setPreferredSize(new Dimension(tfWidth, 50));

    centerP.add(lb3, gbc);
    gbc.insets = new Insets(0, 0, 0, 0);
    p3.add(passwordField);
    centerP.add(p3, gbc);

    add(centerP, BorderLayout.CENTER);

    // button

    PinkPanel buttonP = new PinkPanel();
    WhiteRoundeButton loginButton = new WhiteRoundeButton("Login");
    buttonP.setBorder(BorderFactory.createEmptyBorder(0, 0, 100, 0));
    buttonP.setLayout(new FlowLayout(FlowLayout.CENTER));
    buttonP.add(loginButton);
    add(buttonP, BorderLayout.SOUTH);

    // 암호화 추가
    loginButton.addActionListener(e -> {
      if (dao.userLoginCheck(userTextField.getText(), passwordField.getText()) > 0) {
        JOptionPane.showMessageDialog(UserLogin.this, "로그인 성공");
      } else {
        JOptionPane.showMessageDialog(UserLogin.this, "로그인 실패");
      }
    });

    setVisible(true);
  }

  public static void main(String[] args) {
    new UserLogin();
  }
}
