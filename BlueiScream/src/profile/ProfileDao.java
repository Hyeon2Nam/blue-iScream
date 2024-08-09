package profile;

import java.io.*;
import java.nio.file.Files;
import java.sql.*;
import java.util.*;

public class ProfileDao {
    Connection conn;
    PreparedStatement pstmt;
    ResultSet rs;

    public void joinAcces() {
        String propfile = "src/config.properties";
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

    public void uploadFile(String id, int roomId, File f) {
        joinAcces();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
        int result = 0;

        try {
            FileInputStream inputStream = new FileInputStream(f);
            String sql = "insert into files (user_id, chatroom_id, file, file_path, file_type, uploaded_at) " +
                    "values (?,?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, id);
            pstmt.setInt(2, roomId);
            pstmt.setBlob(2, inputStream);
            pstmt.setString(3, f.getAbsolutePath());
            pstmt.setString(4, Files.probeContentType(f.toPath()));
            pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()), cal);

            result = pstmt.executeUpdate();
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
            result = -1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeAcces();
        }
    }

    public void uploadFile(String id, File f) {
        joinAcces();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
        int result = 0;

        try {
            FileInputStream inputStream = new FileInputStream(f);
            String sql = "insert into files (user_id, file, file_path, file_type, uploaded_at) " +
                    "values (?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, id);
            pstmt.setBlob(2, inputStream);
            pstmt.setString(3, f.getAbsolutePath());
            pstmt.setString(4, Files.probeContentType(f.toPath()));
            pstmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()), cal);

            result = pstmt.executeUpdate();
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
            result = -1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeAcces();
        }
    }

    public List<Profile> getClientProfileImage() {
        joinAcces();
        List<Profile> pfs = new ArrayList<>();

        try {
            String sql = "select file_id, user_id, chatroom_id, file, file_path, file_type, uploaded_at" +
                    " from files " +
                    "where chatroom_id is null and file_type like ? " +
                    "order by uploaded_at desc limit 1";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, "image/%");

            rs = pstmt.executeQuery();

            if (rs.next()) {
                Profile pf = new Profile();

                pf.setFileId(rs.getInt(1));
                pf.setUserId(rs.getString(2));
                pf.setChatroomId(rs.getInt(3));
                pf.setFile(rs.getBlob(4));
                pf.setFilePath(rs.getString(5));
                pf.setFileType(rs.getString(6));
                pf.setUploadedAt(rs.getTimestamp(7));

                pfs.add(pf);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAcces();
        }

        return pfs;
    }

    public Blob getClientProfileImage(String id) {
        joinAcces();
        Blob b = null;

        try {
            String sql = "select f.file " +
                    "from users u " +
                    "left outer join files f on u.profile_image = f.file_id " +
                    "where u.user_id = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, id);
            rs = pstmt.executeQuery();

            if (rs.next())
                b = rs.getBlob(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAcces();
        }

        return b;
    }

    public Blob getClientBackgroundImage(String id, int roomId) {
        joinAcces();
        Blob b = null;

        try {
            String sql = "select f.file " +
                    "from user_chat_rooms ucr " +
                    "left outer join files f on ucr.background_img = f.file_id " +
                    "where ucr.user_id = ? and ucr.chatroom_id = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, id);
            pstmt.setInt(2, roomId);
            rs = pstmt.executeQuery();

            if (rs.next())
                b = rs.getBlob(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAcces();
        }

        return b;
    }

    public int getLastUploadImageId(String id) {
        joinAcces();
        int res = 0;

        try {
            String sql = "select file_id " +
                    " from files " +
                    "where chatroom_id is null and user_id = ? and file_type like ? " +
                    "order by uploaded_at desc limit 1";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, id);
            pstmt.setString(2, "image/%");

            rs = pstmt.executeQuery();

            if (rs.next())
                res = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            res = 1;
        } finally {
            closeAcces();
        }

        return res;
    }

    public void updateProfileImage(String id) {
        int iid = getLastUploadImageId(id);
        joinAcces();

        int result = 0;

        try {
            String sql = "update users set profile_image = ? where user_id = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, iid);
            pstmt.setString(2, id);

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            result = -1;
        } finally {
            closeAcces();
        }
    }

    public void updateBackgroundImage(String id, int roomId) {
        int iid = getLastUploadImageId(id);
        joinAcces();

        int result = 0;

        try {
            String sql = "update user_chat_rooms set background_img = ? where user_id = ? and chatroom_id = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, iid);
            pstmt.setString(2, id);
            pstmt.setInt(3, roomId);

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            result = -1;
        } finally {
            closeAcces();
        }
    }
}