package chatRoom;

import javax.swing.*;
import java.awt.*;

public class MoreMenu extends JFrame {
    private ChatRoomDao dao;
    private String clientId;
    private int roomId;

    public MoreMenu(String clientId, int roomId) {
        super("MENU");
        this.clientId = clientId;
        this.roomId = roomId;

        setSize(450, 200);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBackground(Color.white);

        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.CENTER));
        p.setBackground(Color.white);

        JButton picBtn = makePickButton("사진");
        JButton BgBtn = makePickButton("배경화면");
        JButton mbBtn = makePickButton("게시판");
        JButton qBtn = makePickButton("문의사항");

        p.add(picBtn);
        p.add(BgBtn);
        p.add(mbBtn);
        p.add(qBtn);
        add(p, BorderLayout.CENTER);

        // ----------[EVENTS]---------------
        // code...

        setVisible(true);
    }

    private JButton makePickButton(String s) {
        JButton btn = new JButton(s);
        int size = 90;

        btn.setBackground(new Color(0, 38, 66));
        btn.setPreferredSize(new Dimension(size,size));
        btn.setForeground(Color.white);
        btn.setBorderPainted(false);

        return btn;
    }
}
