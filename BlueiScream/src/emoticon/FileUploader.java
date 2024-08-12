package emoticon;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FileUploader {
    public static void main(String[] args) {
        String url = "jdbc:mysql://192.168.40.33/blue_iscream?serverTimezone=UTC";
        String user = "kkk";
        String password = "1234";

        String[] filePaths = {
            "C:\\MyAppData\\등장.png",
            "C:\\MyAppData\\당당.png"
        };

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO files (file_name, file_data, file_type) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (String filePath : filePaths) {
                    File file = new File(filePath);
                    try (FileInputStream fis = new FileInputStream(file)) {
                        pstmt.setString(1, file.getName());
                        pstmt.setBinaryStream(2, fis, (int) file.length());
                        pstmt.setString(3, "emoji");
                        pstmt.executeUpdate();
                        System.out.println("File uploaded successfully: " + filePath);
                    }
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
