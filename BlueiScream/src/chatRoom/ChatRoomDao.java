package chatRoom;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class ChatRoomDao {
    Connection conn;
    PreparedStatement pstmt;
    ResultSet rs;

    public void joinAcces() {
        try {
            String url = "jdbc:mysql://192.168.40.33:3306/blue_iscream?serverTimezone=UTC";
            String user = "nhy";
            String pw = "1234";
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

    public List<Messages> loadMessages(int roomId) {
        joinAcces();
        List<Messages> msgs = new ArrayList<>();

        try {
            String sql = "select content, created_at, message_type, " +
                    "mes_to, mes_from, reaction, is_read, user_id, message_id " +
                    "from messages " +
                    "where chatroom_id = ? and is_delete = ? " +
                    "order by created_at";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roomId);
            pstmt.setBoolean(2, false);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Messages msg = new Messages();
                msg.setContent(rs.getString(1));
                msg.setCreatedAt(rs.getTimestamp(2));
                msg.setMessageType(rs.getString(3));
                msg.setMesTo(rs.getString(4));
                msg.setMesFrom(rs.getString(5));
                msg.setReaction(rs.getInt(6));
                msg.setIsRead(rs.getInt(7));
                msg.setUserId(rs.getString(8));
                msg.setMsgId(rs.getInt(9));
                msgs.add(msg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAcces();
        }

        return msgs;
    }

    public int insertMessage(int roomId, String id, String content, String type) {
        int res = 0;
        int readCnt = getFirstReadCnt(roomId) - 1;
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));

        joinAcces();
        try {
            String sql = "insert into messages " +
                    "(chatroom_id, user_id, content, created_at, message_type, is_read, is_delete, mes_from) " +
                    "values (?,?,?,?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roomId);
            pstmt.setString(2, id);
            pstmt.setString(3, content);
            pstmt.setTimestamp(4, ts, cal);
            pstmt.setString(5, type);
            pstmt.setInt(6, readCnt);
            pstmt.setBoolean(7, false);
            pstmt.setString(8, id);
            res = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            res = -1;
        } finally {
            closeAcces();
        }
        return res;
    }

    public int insertMessage(int roomId, String id, String content, String type, String to, String from) {
        int res = 0;
        int readCnt = getFirstReadCnt(roomId) - 1;
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));

        joinAcces();
        try {
            String sql = "insert into messages " +
                    "(chatroom_id, user_id, content, created_at, message_type, mes_to, mes_from, is_read, is_delete) " +
                    "values (?,?,?,?,?,?,?,?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roomId);
            pstmt.setString(2, id);
            pstmt.setString(3, content);
            pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()), cal);
            pstmt.setString(5, type);
            pstmt.setString(6, to);
            pstmt.setString(7, from);
            pstmt.setBoolean(8, false);
            pstmt.setInt(6, readCnt);
            res = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            res = -1;
        } finally {
            closeAcces();
        }
        return res;
    }

    public int getFirstReadCnt(int roomId) {
        joinAcces();
        int res = 0;

        try {
            String sql = "select count(*) " +
                    "from user_chat_rooms " +
                    "where chatroom_id = ?";
            pstmt = conn.prepareCall(sql);
            pstmt.setInt(1, roomId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                res = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            res = -1;
        } finally {
            closeAcces();
        }

        return res;
    }

    public String getUserName(String id) {
        joinAcces();
        String res = "";

        try {
            String sql = "select user_name from users where user_id = ?";
            pstmt = conn.prepareCall(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();

            if (rs.next())
                res = rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
            res = "";
        } finally {
            closeAcces();
        }

        return res;
    }

    public String getChatRoomName(int id) {
        joinAcces();
        String res = "";

        try {
            String sql = "select chatroom_name from chat_rooms where chatroom_id = ?";
            pstmt = conn.prepareCall(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next())
                res = rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
            res = "";
        } finally {
            closeAcces();
        }

        return res;
    }

    public List<ChatRoom> getChatRoomList(String id) {
        List<ChatRoom> crlist = new ArrayList<>();
        joinAcces();

        try {
            String sql = "select c.chatroom_id, chatroom_name, created_at, category, background_img, c.isAlarm " +
                    "from chat_rooms c " +
                    "inner join user_chat_rooms uc on c.chatroom_id = uc.chatroom_id " +
                    "where user_id = ?";
            pstmt = conn.prepareCall(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ChatRoom cr = new ChatRoom();
                cr.setChatroomId(rs.getInt(1));
                cr.setChatroomName(rs.getString(2));
                cr.setCreatedAt(rs.getTimestamp(3));
                cr.setCategory(rs.getString(4));
                cr.setBackgroundImg(rs.getInt(5));
                cr.setAlarm(rs.getBoolean(6));
                crlist.add(cr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAcces();
        }

        return crlist;
    }

    public void setLastReadTime(String id, int roomId) {
        joinAcces();
        int res;

        try {
            String sql = "update user_chat_rooms set last_read_at = CURRENT_TIMESTAMP() where user_id = ? and chatroom_id = ?";
            pstmt = conn.prepareCall(sql);
            pstmt.setString(1, id);
            pstmt.setString(1, id);
            pstmt.setInt(2, roomId);
            res = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            res = -1;
        } finally {
            closeAcces();
        }
    }

    public String getSendMessageTime(int roomId) {
        joinAcces();
        String res = "";

        try {
            String sql = "select created_at from messages m where chatroom_id = ? order by created_at desc limit 1";
            pstmt = conn.prepareCall(sql);
            pstmt.setInt(1, roomId);
            rs = pstmt.executeQuery();

            if (rs.next())
                res = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(rs.getTimestamp(1));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAcces();
        }

        return res;
    }

    public int getNotReadMessageCnt(String id, int roomId) {
        joinAcces();
        int res = 0;

        try {
            String sql = "select (select count(*) from messages m where m.chatroom_id = ? " +
                    "                                             and uc.last_read_at <= m.created_at " +
                    "                                             and m.user_id != uc.user_id) as not_read_cnt " +
                    "from user_chat_rooms uc " +
                    "where chatroom_id = ? and uc.user_id = ?";
            pstmt = conn.prepareCall(sql);
            pstmt.setInt(1, roomId);
            pstmt.setInt(2, roomId);
            pstmt.setString(3, id);
            rs = pstmt.executeQuery();

            if (rs.next())
                res = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            res = -1;
        } finally {
            closeAcces();
        }

        return res;
    }

    public boolean getisAlram(String id, int roomId) {
        joinAcces();
        boolean res = false;

        try {
            String sql = "select is_alram from user_chat_rooms where user_id = ? and chatroom_id = ?";
            pstmt = conn.prepareCall(sql);
            pstmt.setString(1, id);
            pstmt.setInt(2, roomId);
            rs = pstmt.executeQuery();

            if (rs.next())
                res = rs.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
            res = false;
        } finally {
            closeAcces();
        }

        return res;
    }

    public void setIsAlram(String id, int roomId, boolean isAlram) {
        joinAcces();
        int res;

        try {
            String sql = "update user_chat_rooms set is_alram = ? where user_id = ? and chatroom_id = ?";
            pstmt = conn.prepareCall(sql);
            pstmt.setBoolean(1, isAlram);
            pstmt.setString(2, id);
            pstmt.setInt(3, roomId);
            res = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            res = -1;
        } finally {
            closeAcces();
        }
    }

    public void setReaction(int reaction, int id) {
        joinAcces();
        int res;

        try {
            String sql = "update messages set reaction = ? where message_id = ?";
            pstmt = conn.prepareCall(sql);
            pstmt.setInt(1, reaction);
            pstmt.setInt(2, id);
            res = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            res = -1;
        } finally {
            closeAcces();
        }
    }

    public int getMsgId(String id, int roomId) {
        joinAcces();
        int res = 0;

        try {
            String sql = "select message_id from messages where user_id = ? and chatroom_id = ? order by created_at desc  limit 1";
            pstmt = conn.prepareCall(sql);
            pstmt.setString(1, id);
            pstmt.setInt(2, roomId);
            rs = pstmt.executeQuery();

            if (rs.next())
                res = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            res = -1;
        } finally {
            closeAcces();
        }

        return res;
    }
}
