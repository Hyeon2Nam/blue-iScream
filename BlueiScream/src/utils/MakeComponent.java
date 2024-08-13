package utils;

import profile.Profile;
import profile.ProfileDao;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

public class MakeComponent extends JFrame {
    public JButton setNoneBorderIconButton(String src, int iconSize) {
        JButton btn = new JButton();

        if (src != null && !src.isEmpty()) {
            ImageIcon icon = resizeIcon(src, iconSize);
            btn.setIcon(icon);
        }

        btn.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        btn.setBackground(null);

        return btn;
    }

    public JButton setIconButton(String imgSrc, int size) {
        JButton b = new JButton();
        ImageIcon icon = resizeIcon(imgSrc, size);

        b.setIcon(icon);
        b.setBackground(null);
        b.setBorder(BorderFactory.createEmptyBorder());

        return b;
    }

    public ImageIcon resizeIcon(String src, int iconSize) {
        ImageIcon icon = new ImageIcon(src);
        Image image = icon.getImage();
        Image newimg = image.getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH); // scale it the smooth way
        icon = new ImageIcon(newimg);

        return icon;
    }

    public ImageIcon resizeIcon(ImageIcon icon, int iconSize) {
        Image image = icon.getImage();
        Image newimg = image.getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH); // scale it the smooth way
        icon = new ImageIcon(newimg);

        return icon;
    }

    public ImageIcon loadImage(String userId, int iconSize) {
        ProfileDao pDao = new ProfileDao();
        Blob blob = pDao.getClientProfileImage(userId);
        ImageIcon icon = resizeIcon("images/userDefaultImg.png", iconSize);

        if (blob == null) return icon;

        try {
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

            icon = new ImageIcon(imageBytes);
            icon = resizeIcon(icon, iconSize);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return icon;
    }

    public Image loadBgImage(String userId, int roomId) {
        ProfileDao pDao = new ProfileDao();
        Blob blob = pDao.getClientBackgroundImage(userId, roomId);
        Image image = null;

        if (blob == null) return image;

        try {
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

            ImageIcon icon = new ImageIcon(imageBytes);
            image = icon.getImage();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return image;
    }

    public JButton setImageButton(String userId, int iconSize) {
        JButton btn = new JButton();

        btn.setIcon(loadImage(userId, iconSize));
        btn.setBackground(null);
        btn.setBorder(BorderFactory.createEmptyBorder());

        return btn;
    }
}
