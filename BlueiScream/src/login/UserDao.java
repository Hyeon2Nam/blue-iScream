package login;

import java.sql.*;

public class UserDao {
    Connection conn;
    PreparedStatement pstmt;
    ResultSet rs;

    public void joinAcces() {
        try {
            String url = "jdbc:mysql://192.168.40.33:3306/blue_iscream?serverTimezone=UTC";
            String user = "nhy";
            String pw = "1234";
            conn = DriverManager.getConnection(url, user, pw);

            if (conn != null) {
                System.out.println("연결 성공");
            }

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
            System.out.println("실행결과: " + result);

        } catch (SQLException e) {
            e.printStackTrace();
            result = -1;
        } finally {
            closeAcces();
        }

        return result;
    }

    public int userNameCheck(String id) {
        joinAcces();
        int resultCnt = 0;

        try {
            String sql = "select COUNT(*) from users where user_id = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, id);
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

    public int userLoginCheck(String id, String pw) {
        joinAcces();

        int res = 0;

        try {
            String sql = "select password from frame_member where user_id = ?";
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
}
