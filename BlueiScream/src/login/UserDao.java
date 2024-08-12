package login;

import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserDao {
    Connection conn;
    PreparedStatement pstmt;
    ResultSet rs;

    public void joinAcces() {
        String propfile = "C:/Users/3401-06/Desktop/blue-iScream/BlueiScream/config/config.properties";
        Properties p = new Properties();

        try {
            FileInputStream fis = new FileInputStream(propfile);
            //getClass().getResourceAsStream(propfile);
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

    public int userInsert(String id, String name, String pw, String email) {
        joinAcces();

        int result = 0;

        try {
            String sql = "insert into users (user_id, user_name, password, email) values (?,?,?,?);";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, pw);
            pstmt.setString(4, email);

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            result = -1;
        } finally {
            closeAcces();
        }

        return result;
    }

    public int isAlreadyUser(String id, String email) {
        joinAcces();

        int resultCnt = 0;

        try {
            String sql = "select COUNT(*) from users where user_id = ? or email = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, email);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                resultCnt = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAcces();
        }

        return resultCnt;
    }

    public String searchUserId(String email) {
        String res = "";
        joinAcces();

        try {
            String sql = "select user_id from users where email = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                res = rs.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAcces();
        }

        return res;
    }

    public String searchUserPw(String email) {
        String res = "";
        joinAcces();

        try {
            String sql = "select password from users where email = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                res = rs.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAcces();
        }

        return res;
    }

    public int userLoginCheck(String id, String pw) {
        joinAcces();

        int res = 0;

        try {
            String sql = "select password from users where user_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                if (rs.getString(1).equals(pw)) {
                    res = 1;
                } else
                    res = -1;
            }


        } catch (SQLException e) {
            res = -1;
            e.printStackTrace();
        } finally {
            closeAcces();
        }

        return res;
    }

    public List<User> getAllUsers() {
        joinAcces();
        List<User> res = new ArrayList<>();

        try {
            String sql = "select user_id, user_name, email, profile_image from users where user_id != ? and user_id != ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "1");
            pstmt.setString(2, "admin");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getString(1));
                u.setUserName(rs.getString(2));
                u.setEmail(rs.getString(3));
                u.setProfileImage(rs.getInt(4));
                res.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAcces();
        }

        return res;
    }

    public User getUser(String id) {
        joinAcces();
        User u = null;

        try {
            String sql = "select user_id, user_name, email, profile_image from users where user_id = ? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                u = new User();
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
}
