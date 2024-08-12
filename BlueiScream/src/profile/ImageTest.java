package profile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

public class ImageTest extends JFrame {
    ProfileDao dao;
    File selectedFile;
    String clientId;
    String filePath;
    String fileName;
    private JLabel imageLabel;

    public ImageTest(String clientId) {
        this.clientId = clientId;
        dao = new ProfileDao();

        setTitle("Image Uploader");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JButton selectFileButton = new JButton("Select Image");
        JLabel fileLabel = new JLabel("No file selected");

        JButton uploadButton = new JButton("Upload");
        JButton fetchButton = new JButton("Fetch Image");

        imageLabel = new JLabel();

        add(selectFileButton);
        add(fileLabel);
        add(uploadButton);
        add(fetchButton);
        add(imageLabel);

        showImage();

        // image upload
        selectFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    filePath = selectedFile.getAbsolutePath();
                    fileName = selectedFile.getName();
                    fileLabel.setText("Selected: " + fileName + " (" + filePath + ")");
                }
            }
        });

        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedFile != null) {
                    dao.uploadFile(clientId, selectedFile);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select an image and enter a name.");
                }

            }
        });

        setVisible(true);
    }

    // load image
    private void showImage() {
        List<Profile> pfs = dao.getClientProfileImage();

        for (Profile p : pfs)
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
                imageLabel.setIcon(imageIcon);

                add(imageLabel);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    public static void main(String[] args) {
        new ImageTest("qqq");
    }
}
