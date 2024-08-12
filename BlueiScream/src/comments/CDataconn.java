package comments;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CDataconn {
    private static final String url = "jdbc:mysql://114.70.127.232:3306/blue_iscream?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String user = "won";
    private static final String pw = "1234";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC 드라이버를 찾을 수 없습니다.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, pw);
    }

    // 댓글 추가 메서드
    public static void addComment(int postId, int userId, String content, Timestamp createdAt) throws SQLException {
        String sql = "INSERT INTO comments (post_id, user_id, content, created_at, is_delete) VALUES (?, ?, ?, ?, false)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            pstmt.setInt(2, userId);
            pstmt.setString(3, content);
            pstmt.setTimestamp(4, createdAt);
            pstmt.executeUpdate();
        }
    }

    // 특정 게시물에 대한 모든 댓글 가져오기
    public static List<Comment> getCommentsForPost(int postId) throws SQLException {
        String sql = "SELECT c.comment_id, c.post_id, c.user_id, c.content, c.created_at, c.is_delete, u.user_name " +
                     "FROM comments c " +
                     "JOIN users u ON c.user_id = u.user_id " +
                     "WHERE c.post_id = ? AND c.is_delete = false";
        
        List<Comment> comments = new ArrayList<>();
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int commentId = rs.getInt("comment_id");
                int userId = rs.getInt("user_id");
                String content = rs.getString("content");
                Timestamp createdAt = rs.getTimestamp("created_at");
                boolean isDelete = rs.getBoolean("is_delete");
                String userName = rs.getString("user_name");  // 사용자 이름 가져오기

                Comment comment = new Comment(commentId, postId, userId, content, new java.util.Date(createdAt.getTime()), isDelete, userName);
                comments.add(comment);
            }
        }
        return comments;
    }


    // 댓글 삭제 메서드 (is_deleted 플래그를 1로 설정)
    public static void deleteComment(int commentId) throws SQLException {
        String sql = "UPDATE comments SET is_delete = 1 WHERE comment_id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, commentId);
            pstmt.executeUpdate();
        }
    }
}
