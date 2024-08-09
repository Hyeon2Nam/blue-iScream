package profile;

import chatRoom.ChatRoomDao;
import components.DarkPanel;
import utils.MakeComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

public class MiniProfileView extends JFrame {
    private ProfileDao profileDao;
    private ChatRoomDao chatRoomDao;
    private String userId;
    private MakeComponent mc;

    public MiniProfileView(String userId, boolean isClient) {
        this.userId = userId;
        profileDao = new ProfileDao();
        chatRoomDao = new ChatRoomDao();
        mc = new MakeComponent();
        int totalHeight = 300;
        int imageSize = 120;

        setSize(250, 400);
        setLayout(new FlowLayout(FlowLayout.CENTER));
        getContentPane().setBackground(new Color(0, 38, 66));
        setResizable(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.BOTH;

        JPanel cp = new JPanel(new GridBagLayout());
        DarkPanel dp = new DarkPanel(totalHeight - imageSize - 50, "top");
        cp.setBackground(new Color(0, 38, 66));


        cp.add(dp, gbc);

        JLabel img = new JLabel();
        img.setIcon(mc.loadImage(userId, imageSize));
        JLabel name = new JLabel(chatRoomDao.getUserName(userId));
        name.setFont(name.getFont().deriveFont(20.0f));
        name.setForeground(Color.white);
        name.setHorizontalAlignment(JLabel.CENTER);
        name.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        cp.add(img, gbc);
        cp.add(name, gbc);

        if (isClient) {
            JButton btn = mc.setIconButton("BlueiScream/images/editProfileImageIcon.png", 30);

            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser();
                    File selectedFile = null;

                    int result = fileChooser.showOpenDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION)
                        selectedFile = fileChooser.getSelectedFile();

                    if (selectedFile == null)
                        return;

                    profileDao.uploadFile(userId, selectedFile);
                    profileDao.updateProfileImage(userId);

                    img.setIcon(mc.loadImage(userId, imageSize));
                }
            });

            btn.setFocusable(false);
            cp.add(btn, gbc);
        }

        add(cp);

        if (!isClient)
            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowDeactivated(WindowEvent e) {
                    dispose();
                }
            });

        setVisible(true);
    }
}