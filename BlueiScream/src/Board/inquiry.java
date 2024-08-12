package Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.sql.Timestamp;

public class inquiry extends JFrame {
    private JTextField titleField;
    private JTextArea contentArea;
    private JButton saveButton;
    private JButton attachFileButton;
    private String attachedFilePath = null;

    public inquiry() {
        setTitle("문의사항");
        initialize();
    }

    private void initialize() {
        setSize(400, 600);
        setLayout(new BorderLayout());

        // Top panel for the title
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(0, 51, 102)); // Dark blue color
        topPanel.setPreferredSize(new Dimension(getWidth(), 50));
        JLabel titleLabel = new JLabel("문의사항");
        titleLabel.setFont(new Font("GOOGLE FONTS", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel);

        // Main panel for the form
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titleTextLabel = new JLabel("제목");
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(titleTextLabel, gbc);

        titleField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        mainPanel.add(titleField, gbc);

        JLabel contentTextLabel = new JLabel("내용");
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(contentTextLabel, gbc);

        contentArea = new JTextArea(10, 20);
        contentArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(contentArea);
        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(scrollPane, gbc);
        
        JLabel imgLabel = new JLabel("첨부파일");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        mainPanel.add(imgLabel, gbc);
        attachFileButton = new JButton(new ImageIcon("C:/Users/3401-06/Desktop/blue-iScream/blue-iScream/blue-iScream/BlueiScream/images/camera.png")); // Replace with actual path to the icon
        attachFileButton.setPreferredSize(new Dimension(50, 50));
        attachFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                attachFile();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        mainPanel.add(attachFileButton, gbc);

        saveButton = new JButton("작성");
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(saveButton, gbc);

        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveInquiry();
            }
        });

        setVisible(true);
    }

    private void attachFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            attachedFilePath = fileChooser.getSelectedFile().getAbsolutePath();
        }
    }

    private void saveInquiry() {
        String newTitle = titleField.getText();
        String newContent = contentArea.getText();
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        try {
            int fileId = -1; // Default file ID
            if (attachedFilePath != null) {
                fileId = Dataconn.createFile(attachedFilePath); // Save attached file
            }

            Dataconn.createInquiry(newTitle, newContent, currentTimestamp, fileId);
            JOptionPane.showMessageDialog(this, "Inquiry created successfully!");

            dispose();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving inquiry: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new inquiry();
            }
        });
    }
}
