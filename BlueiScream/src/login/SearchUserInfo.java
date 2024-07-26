package login;

import components.ColorRoundButton;
import components.PinkPanel;
import components.UnderLineTextField;

import java.util.Properties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SearchUserInfo extends JFrame {
    private UserDao dao;

    public SearchUserInfo() {
        int totalSize = 400;
//        int tfWidth = totalSize - sidePaddingSize * 3 - 100;
        dao = new UserDao();

        setTitle("Search id/pw");
        setSize(totalSize, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        PinkPanel p1 = new PinkPanel();
        p1.setLayout(new FlowLayout(FlowLayout.LEFT));
        PinkPanel p2 = new PinkPanel();
        p2.setLayout(new FlowLayout(FlowLayout.LEFT));
        ColorRoundButton idBtn = new ColorRoundButton("Id찾기", Color.white, Color.BLACK, 15);
        ColorRoundButton pwBtn = new ColorRoundButton("PW찾기", Color.white, Color.BLACK, 15);
        UnderLineTextField idEmail = new UnderLineTextField();
        UnderLineTextField pwEmail = new UnderLineTextField();

        idEmail.setPreferredSize(new Dimension(totalSize - 200, 50));
        pwEmail.setPreferredSize(new Dimension(totalSize - 200, 50));
        p1.add(idEmail);
        p1.add(idBtn);
        p2.add(pwEmail);
        p2.add(pwBtn);

        add(p1, BorderLayout.NORTH);
        add(p2, BorderLayout.CENTER);

        idBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = dao.searchUserId(idEmail.getText());

                if (id != null && !id.isEmpty())
                    JOptionPane.showMessageDialog(SearchUserInfo.this, "ID는 " + id + "입니다.");
                else
                    JOptionPane.showMessageDialog(SearchUserInfo.this, "가입되지 않은 이메일입니다.");
            }
        });
        pwBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pw = dao.searchUserPw(pwEmail.getText());

                if (pw != null && !pw.isEmpty()) {
                    String title = "Blue iScream 채팅 서비스입니다.";
                    String content = "당신의 비밀번호는 [" + pw + "] 입니다.";
                    String user_name = "gusdlnam";
                    String password = "yuqy achx novg pflr";

                    SendMail sendMail = new SendMail();
                    sendMail.goMail(sendMail.setting(new Properties(), user_name, password), title, content, pwEmail.getText());
                    JOptionPane.showMessageDialog(SearchUserInfo.this, "가입하신 이메일을 확인해주세요");
                } else
                    JOptionPane.showMessageDialog(SearchUserInfo.this, "가입되지 않은 이메일입니다.");
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new SearchUserInfo();
    }
}
