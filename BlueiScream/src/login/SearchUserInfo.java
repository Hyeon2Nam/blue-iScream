package login;

import components.ColorRoundButton;
import components.DarkPanel;
import components.UnderLineTextField;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SearchUserInfo extends JFrame {
    private UserDao dao;
    private final Color DARK = new Color(0, 38, 66);
    private final Color ORANGE = new Color(229, 149, 0);
    private final int TOTALSIZE = 400;

    public SearchUserInfo() {
        dao = new UserDao();

        setTitle("Forgot Id/Password");
        setSize(TOTALSIZE, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        createHeader();
        createCenterContents();
        setVisible(true);
    }

    private void createHeader() {
        DarkPanel iconP = new DarkPanel();
        JLabel icon = new JLabel("Sign UP");
        String lbStr = "<html><body><center>원하는 항목에<br>이메일을 입력해주세요</center></body></html>";
        JLabel lb = new JLabel(lbStr);

        iconP.setLayout(new BorderLayout());

        icon.setForeground(Color.white);
        icon.setFont(icon.getFont().deriveFont(40f));
        icon.setHorizontalAlignment(JLabel.CENTER);
        icon.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));

        lb.setFont(lb.getFont().deriveFont(20f));
        lb.setForeground(Color.white);
        lb.setBackground(null);
        lb.setHorizontalAlignment(JLabel.CENTER);

        iconP.add(icon, BorderLayout.NORTH);
        iconP.add(lb, BorderLayout.CENTER);

        add(iconP, BorderLayout.NORTH);
    }

    private void createCenterContents() {
        DarkPanel fieldP = new DarkPanel();
        DarkPanel p1 = new DarkPanel();
        DarkPanel p2 = new DarkPanel();
        UnderLineTextField idEmail = new UnderLineTextField(Color.white, Color.white);
        UnderLineTextField pwEmail = new UnderLineTextField(Color.white, Color.white);
        ColorRoundButton idBtn = new ColorRoundButton("ID찾기", ORANGE, DARK, 15);
        ColorRoundButton pwBtn = new ColorRoundButton("PW찾기", ORANGE, DARK, 15);

        p1.setLayout(new FlowLayout());
        p2.setLayout(new FlowLayout());
        fieldP.setLayout(new GridBagLayout());
        p1.setMinimumSize(new Dimension(TOTALSIZE, 100));
        fieldP.setMinimumSize(new Dimension(TOTALSIZE, 500));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.BOTH;

        idEmail.setColumns(15);
        pwEmail.setColumns(15);

        p1.add(idEmail, BorderLayout.CENTER);
        p1.add(idBtn, BorderLayout.EAST);
        p2.add(pwEmail, BorderLayout.CENTER);
        p2.add(pwBtn, BorderLayout.EAST);

        fieldP.add(p1, gbc);
        fieldP.add(p2, gbc);

        add(fieldP, BorderLayout.CENTER);

        idBtn.addActionListener(e -> {
            String id = dao.searchUserId(idEmail.getText());

            if (idEmail.getText().isEmpty())
                return;

            if (id != null && !id.isEmpty())
                JOptionPane.showMessageDialog(SearchUserInfo.this, "ID는 " + id + "입니다.");
            else
                JOptionPane.showMessageDialog(SearchUserInfo.this, "가입되지 않은 이메일입니다.");
        });
        pwBtn.addActionListener(e -> {
            String pw = dao.searchUserPw(pwEmail.getText());
            String propfile = "config/config.properties";
            Properties p = new Properties();

            try {
                FileInputStream fis = new FileInputStream(propfile);
                p.load(new java.io.BufferedInputStream(fis));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            if (pw != null && !pw.isEmpty()) {
                String title = "Blue iScream 채팅 서비스입니다.";
                String content = "당신의 비밀번호는 [" + pw + "] 입니다.";
                String user_name = p.getProperty("admin_name");
                String password = p.getProperty("app_pw");

                System.out.println(p.getProperty("admin_name"));

                SendMail sendMail = new SendMail();
                try {
                    sendMail.goMail(sendMail.setting(new Properties(), user_name, password), title, content, pwEmail.getText());
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                JOptionPane.showMessageDialog(SearchUserInfo.this, "가입하신 이메일을 확인해주세요");
            } else
                JOptionPane.showMessageDialog(SearchUserInfo.this, "가입되지 않은 이메일입니다.");
        });
    }

    public static void main(String[] args) {
        new SearchUserInfo();
    }
}
