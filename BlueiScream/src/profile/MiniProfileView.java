package profile;

import chatRoom.ChatRoomDao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

public class MiniProfileView extends JFrame {
    private ProfileDao profileDao;
    private ChatRoomDao chatRoomDao;
    private String userId;

    public MiniProfileView(String userId) {
        this.userId = userId;
        profileDao = new ProfileDao();
        chatRoomDao = new ChatRoomDao();

        setSize(220, 300);
        setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel img = showImage();
        JLabel name = new JLabel(chatRoomDao.getUserName(userId));
        name.setFont(name.getFont().deriveFont(20));

        add(img);
        add(name);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }

    private ImageIcon resizeIcon(ImageIcon icon, int iconSize) {
        Image image = icon.getImage();
        Image newimg = image.getScaledInstance(iconSize, iconSize, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        icon = new ImageIcon(newimg);

        return icon;
    }

    private ImageIcon resizeIcon(String src, int iconSize) {
        ImageIcon icon = new ImageIcon(src);
        Image image = icon.getImage();
        Image newimg = image.getScaledInstance(iconSize, iconSize, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        icon = new ImageIcon(newimg);

        return icon;
    }


    private JLabel showImage() {
        ProfileDao pDao = new ProfileDao();
        Profile p = pDao.getClientProfileImage(userId);
        JLabel imageLabel = new JLabel();
        int iconSize = 200;


        if (p == null) {
            ImageIcon icon = resizeIcon("BlueiScream/images/userDefaultImg.jpg", iconSize);
            imageLabel.setIcon(icon);

            return imageLabel;
        }

        try {
            Blob blob = p.getFile();
            InputStream inputStream = blob.getBinaryStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            byte[] imageBytes = outputStream.toByteArray();
            inputStream.close();
            outputStream.close();

            ImageIcon imageIcon = new ImageIcon(imageBytes);
            imageIcon = resizeIcon(imageIcon, iconSize);
            imageLabel.setIcon(imageIcon);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return imageLabel;
    }

    public static void main(String[] args) {
        new MiniProfileView("aaa");
    }
}