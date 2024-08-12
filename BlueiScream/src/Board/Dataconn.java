package Board;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Dataconn {
    private static final String url = "jdbc:mysql://114.70.127.232:3306/blue_iscream?serverTimezone=UTC";
    private static final String user = "won";
    private static final String pw = "1234";

    static {
        try {
            // JDBC 드라이버 로드
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC 드라이버를 찾을 수 없습니다.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, pw);
    }

    public static int createFile(String filePath) throws SQLException {
        String sql = "INSERT INTO files(file_path) VALUES (?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, filePath);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // 생성된 file_id 반환
            } else {
                throw new SQLException("파일 생성 실패");
            }
        }
    }
    
    public static void createInquiry(String title, String content, Timestamp createdAt, boolean isNotice) throws SQLException {
        String sql = "INSERT INTO posts (title, content, created_at, is_notice) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setTimestamp(3, createdAt);
            pstmt.setBoolean(4, isNotice);
            pstmt.executeUpdate();
        }
    }

    public static void createPost(int userId, int chatroomId, String content, String title, Timestamp createdAt, boolean isDelete, Timestamp editDate, Integer fileId, boolean isNotice) throws SQLException {
        String sql = "INSERT INTO posts(user_id, chatroom_id, content, title, created_at, is_delete, edit_date, is_notice) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, chatroomId);
            pstmt.setString(3, content);
            pstmt.setString(4, title);
            pstmt.setTimestamp(5, createdAt);
            pstmt.setBoolean(6, isDelete);
            pstmt.setTimestamp(7, editDate);
            pstmt.setBoolean(8, isNotice);

            // fileId가 null이면 SQL에서 해당 컬럼을 NULL로 설정
            if (fileId != null) {
                pstmt.setInt(8, fileId);
            } else {
                pstmt.setNull(8, Types.INTEGER);
            }
            
            pstmt.setBoolean(9, isNotice);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }


    public static void deletePost(int postId) throws SQLException {
        String sql = "update posts set is_delete = 1 where post_id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static List<Post> getPosts() throws SQLException {
        String sql = "SELECT * FROM posts WHERE is_delete = 0 and is_notice = 0";
        List<Post> posts = new ArrayList<>();
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int postId = rs.getInt("post_id");
                int userId = rs.getInt("user_id");
                int chatroomId = rs.getInt("chatroom_id");
                String content = rs.getString("content");
                String title = rs.getString("title");
                Timestamp createdAt = rs.getTimestamp("created_at");
                boolean isDelete = rs.getBoolean("is_delete");
                Timestamp editDate = rs.getTimestamp("edit_date");
                int file = rs.getInt("file");
                boolean isNotice = rs.getBoolean("is_notice");

                if (content == null) content = "";
                if (title == null) title = "";
                if (createdAt == null) createdAt = new Timestamp(System.currentTimeMillis());
                if (editDate == null) editDate = new Timestamp(System.currentTimeMillis());

                Post post = new Post(postId, userId, chatroomId, content, title, new java.util.Date(createdAt.getTime()), isDelete, new java.util.Date(editDate.getTime()), String.valueOf(file), isNotice);
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return posts;
    }

    public static List<Post> getNotice() throws SQLException {
        String sql = "SELECT * FROM posts WHERE is_delete = 0 and is_notice = 1";
        List<Post> posts = new ArrayList<>();
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int postId = rs.getInt("post_id");
                int userId = rs.getInt("user_id");
                int chatroomId = rs.getInt("chatroom_id");
                String content = rs.getString("content");
                String title = rs.getString("title");
                Timestamp createdAt = rs.getTimestamp("created_at");
                boolean isDelete = rs.getBoolean("is_delete");
                Timestamp editDate = rs.getTimestamp("edit_date");
                int file = rs.getInt("file");
                boolean isNotice = rs.getBoolean("is_notice");

                if (content == null) content = "";
                if (title == null) title = "";
                if (createdAt == null) createdAt = new Timestamp(System.currentTimeMillis());
                if (editDate == null) editDate = new Timestamp(System.currentTimeMillis());

                Post post = new Post(postId, userId, chatroomId, content, title, new java.util.Date(createdAt.getTime()), isDelete, new java.util.Date(editDate.getTime()), String.valueOf(file), isNotice);
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return posts;
    }

    public static void updatePost(int postId, int userId, int chatroomId, String content, String title, Timestamp createdAt, boolean isDelete, Timestamp editDate, Integer file, boolean isNotice) throws SQLException {
    		String sql = "UPDATE posts SET user_id = ?, chatroom_id = ?, content = ?, title = ?, created_at = ?, " +
    		"is_delete = ?, edit_date = ?, is_notice = ?";
    		// file이 있을 때만 file 컬럼을 업데이트하도록 쿼리를 동적으로 변경
    	    if (file != null) {
    	        sql += ", file = ?";
    	    }

    	    sql += " WHERE post_id = ?";

    	    try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
    	        pstmt.setInt(1, userId);
    	        pstmt.setInt(2, chatroomId);
    	        pstmt.setString(3, content);
    	        pstmt.setString(4, title);
    	        pstmt.setTimestamp(5, createdAt);
    	        pstmt.setBoolean(6, isDelete);
    	        pstmt.setTimestamp(7, editDate);
    	        pstmt.setBoolean(8, isNotice);

    	        // 파일이 있는 경우와 없는 경우에 대해 각각 다른 매개변수를 설정
    	        int paramIndex = 9;
    	        if (file != null) {
    	            pstmt.setInt(paramIndex++, file);
    	        }

    	        pstmt.setInt(paramIndex, postId);

    	        int affectedRows = pstmt.executeUpdate();
    	        if (affectedRows == 0) {
    	            throw new SQLException("게시물 업데이트에 실패했습니다: 대상 게시물이 존재하지 않습니다.");
    	        }
    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	        throw e;
    	    }
    	}

    public static List<String> getAllInquiries() throws SQLException {
        // inquiries 테이블에서 제목과 내용 조회, 삭제되지 않은 항목만 선택
        String sql = "SELECT title, content FROM posts WHERE is_delete = false"; 
        List<String> inquiries = new ArrayList<>();
        try (Connection conn = getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql); 
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String title = rs.getString("title");
                String content = rs.getString("content");
                inquiries.add("제목: " + title + " \n내용: " + content);
            }
        }
        return inquiries;
    }

	public static void createPost(String userId, int chatroomId, String content, String title,
			Timestamp currentTimestamp, boolean isDelete, Timestamp currentTimestamp2, Integer fileId,
			boolean isNotice) {
		// TODO Auto-generated method stub
		
	}

	public static void updatePost(int postId, String userId, int chatroomId, String content, String title,
			Date createdAt, boolean isDelete, Timestamp currentTimestamp, Integer fileId, boolean notice) {
		// TODO Auto-generated method stub
		
	}
}