package Board;

import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

public class User {
    private String userId;
    private String userName;
    private boolean isAdmin;
    Connection conn;
    PreparedStatement pstmt;
    ResultSet rs;

    public User(String userId, String userName, boolean isAdmin) {
        this.userId = userId;
        this.userName = userName;
        this.isAdmin = isAdmin;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isAdmin() {
        return isAdmin;
    }


    public login.User getUser(String id) {
        joinAcces();
        login.User u = null;

        try {
            String sql = "select user_id, user_name, email, profile_image from users where user_id = ? and is_delete = 0";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                u = new login.User();
                u.setUserId(rs.getString(1));
                u.setUserName(rs.getString(2));
                u.setEmail(rs.getString(3));
                u.setProfileImage(rs.getInt(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAcces();
        }

        return u;
    }


    public void joinAcces() {
        String propfile = "config/config.properties";
        Properties p = new Properties();

        try {
            FileInputStream fis = new FileInputStream(propfile);
            p.load(new java.io.BufferedInputStream(fis));

            String url = p.getProperty("db_url");
            String user = p.getProperty("db_user");
            String pw = p.getProperty("db_pw");
            conn = DriverManager.getConnection(url, user, pw);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeAcces() {
        try {
            if (rs != null) rs.close();
        } catch (Exception e) {
        }
        try {
            if (pstmt != null) pstmt.close();
        } catch (Exception e) {
        }
        try {
            if (conn != null) pstmt.close();
        } catch (Exception e) {
        }
    }
}
