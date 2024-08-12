package comments;

import java.util.Date;

public class Comment {
    private int commentId;
    private int postId;
    private int userId;
    private String content;
    private Date createdAt;
    private boolean isDeleted;
    private String userName;  // 사용자 이름 필드 추가

    // Constructor
    public Comment(int commentId, int postId, int userId, String content, Date createdAt, boolean isDeleted, String userName) {
        this.commentId = commentId;
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
        this.userName = userName;  // 사용자 이름 설정
    }

    // Getters and Setters
    public int getCommentId() { return commentId; }
    public void setCommentId(int commentId) { this.commentId = commentId; }

    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }
    
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}
