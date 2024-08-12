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
        openFileAndSaveToUserHome(); // 파일 선택 창을 자동으로 열도록 설정
    }

    // 데이터베이스 연결 메서드
    private void connectDatabase() {
        try {
            String url = "jdbc:mysql://114.70.127.232/blue_iscream?serverTimezone=UTC";
            String user = "kkk";
            String password = "1234";
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Database connected!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // 파일 선택 창을 열고 파일을 사용자 홈 디렉터리에 저장하는 메서드
    private void openFileAndSaveToUserHome() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();

            // 사용자 홈 디렉터리의 Documents 폴더에 저장하도록 설정
            String targetDirPath = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "MyAppData";
            File targetDir = new File(targetDirPath);
            if (!targetDir.exists()) {
                targetDir.mkdirs(); // 폴더가 없으면 생성
            }

            try {
                // 파일 복사
                Path destination = Paths.get(targetDirPath, selectedFile.getName());
                Files.copy(selectedFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
                
                // 파일 저장 성공 메시지
                JOptionPane.showMessageDialog(this, "File saved successfully to " + destination, "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // 파일 선택 리스너 호출
                if (listener != null) {
                    listener.onFileSelected(selectedFile.getName(), destination.toString());
                }
            } catch (IOException ex) {
                // 파일 저장 실패 메시지
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error during saving: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                // 창 닫기
                dispose();
            }
        } else {
            // 파일 선택을 취소하면 창 닫기
            dispose();
        }
    }

    // 파일이 선택되었을 때 호출되는 리스너 인터페이스
    public interface FileSelectedListener {
        void onFileSelected(String fileName, String filePath);
    }

    // 메인 메서드
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FileDownload(new FileSelectedListener() {
            @Override
            public void onFileSelected(String fileName, String filePath) {
                // 파일 선택 후 파일 경로 출력
                System.out.println("File selected: " + fileName + " at " + filePath);
                // 여기서 추가 로직을 구현할 수 있습니다.
            }
        }));
    }
}
