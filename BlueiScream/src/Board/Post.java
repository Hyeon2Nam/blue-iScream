package Board;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post {
    private int postId;
    private int userId;
    private int chatroomId;
    private String content;
    private String title;
    private Date createdAt;
    private boolean isDelete;
    private Date editDate;
    private String file;
    private boolean isNotice;
    private List<String> comments;

    // Constructor
    public Post(int postId, int userId, int chatroomId, String content, String title, Date createdAt, boolean isDelete, Date editDate, String file, boolean isNotice) {
        this.postId = postId;
        this.userId = userId;
        this.chatroomId = chatroomId;
        this.content = content;
        this.title = title;
        this.createdAt = createdAt;
        this.isDelete = isDelete;
        this.editDate = editDate;
        this.file = file;
        this.isNotice = isNotice;
        this.comments = new ArrayList<>();
    }

    // Getters and Setters
    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public int getChatroomId() { return chatroomId; }
    public void setChatroomId(int chatroomId) { this.chatroomId = chatroomId; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    public boolean isDelete() { return isDelete; }
    public void setDelete(boolean delete) { isDelete = delete; }
    
    public Date getEditDate() { return editDate; }
    public void setEditDate(Date editDate) { this.editDate = editDate; }
    
    public String getFile() { return file; }
    public void setFile(String file) { this.file = file; }
    
    public boolean isNotice() { return isNotice; }
    public void setNotice(boolean notice) { isNotice = notice; }
    
    public List<String> getComments() { return comments; }
    public void addComments(String comments) { this.comments.add(comments); }
}
