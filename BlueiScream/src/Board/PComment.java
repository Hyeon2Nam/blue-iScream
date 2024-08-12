package Board;

import java.sql.Timestamp;

public class PComment {
    private int commentId;
    private int postId;
    private String text;
    private Timestamp createdAt;

    public PComment(int commentId, int postId, String text, Timestamp createdAt) {
        this.commentId = commentId;
        this.postId = postId;
        this.text = text;
        this.createdAt = createdAt;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

	public String getUserName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getContent() {
		// TODO Auto-generated method stub
		return null;
	}
}

