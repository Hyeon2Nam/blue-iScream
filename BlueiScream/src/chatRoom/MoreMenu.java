package chatRoom;

import profile.ProfileDao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MoreMenu extends JFrame {
    private ProfileDao profileDao;
    private ChatRoomDao dao;
    private String clientId;
    private int roomId;

    public MoreMenu(String clientId, int roomId) {
        super("MENU");
        this.clientId = clientId;
        this.roomId = roomId;
        profileDao = new ProfileDao();

        setSize(450, 200);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBackground(Color.white);
        setResizable(false);

        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.CENTER));
        p.setBackground(Color.white);

        JButton picBtn = makePickButton("사진");
        JButton bgBtn = makePickButton("배경화면");
        JButton mbBtn = makePickButton("게시판");
        JButton qBtn = makePickButton("문의사항");

        p.add(picBtn);
        p.add(bgBtn);
        p.add(mbBtn);
        p.add(qBtn);
        add(p, BorderLayout.CENTER);

        // ----------[EVENTS]---------------

        bgBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            File selectedFile = null;

            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION)
                selectedFile = fileChooser.getSelectedFile();

            if (selectedFile == null)
                return;

            profileDao.uploadFile(clientId, selectedFile);
            profileDao.updateBackgroundImage(clientId, roomId);
        });

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
