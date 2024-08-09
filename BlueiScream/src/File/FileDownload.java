package File;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.sql.*;

public class FileDownload extends JFrame {
    private static final long serialVersionUID = 1L;
    private File selectedFile;
    private Connection conn;
    private FileSelectedListener listener;

    public FileDownload(FileSelectedListener listener) {
        this.listener = listener;
        connectDatabase();
        openFileAndSaveToCDrive(); // 창이 열리면 파일 선택 창을 자동으로 열도록 변경
    }

    private void connectDatabase() {
        try {
            String url = "jdbc:mysql://114.70.127.232/blue_iscream?serverTimezone=UTC";
            String user = "kkk";
            String password = "1234";
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Database connected!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openFileAndSaveToCDrive() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();

            String targetDirPath = "C:\\MyAppData";
            File targetDir = new File(targetDirPath);
            if (!targetDir.exists()) {
                targetDir.mkdirs(); // 폴더가 없으면 생성
            }

            try {
                Path destination = Paths.get(targetDirPath, selectedFile.getName());
                Files.copy(selectedFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
                JOptionPane.showMessageDialog(null, "File saved successfully to " + destination, "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // 파일 선택 리스너 호출
                if (listener != null) {
                    listener.onFileSelected(selectedFile.getName(), destination.toString());
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error during saving: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                // 창 닫기
                dispose();
            }
        } else {
            dispose(); // 파일 선택을 취소하면 창 닫기
        }
    }

    public interface FileSelectedListener {
        void onFileSelected(String fileName, String filePath);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FileDownload(new FileSelectedListener() {
            @Override
            public void onFileSelected(String fileName, String filePath) {
                // 파일 선택 후 파일 전송 로직을 호출
                System.out.println("File selected: " + fileName + " at " + filePath);
            }
        }));
    }
}
