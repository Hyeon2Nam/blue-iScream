package profile;

import java.sql.Blob;
import java.sql.Timestamp;

public class Profile {
    private int fileId;
    private String userId;
    private int chatroomId;
    private Blob file;
    private String filePath;
    private String fileType;
    private Timestamp uploadedAt;

    public int getFileId() {
        return fileId;
    }

    public String getUserId() {
        return userId;
    }

    public int getChatroomId() {
        return chatroomId;
    }

    public Blob getFile() {
        return file;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public Timestamp getUploadedAt() {
        return uploadedAt;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setChatroomId(int chatroomId) {
        this.chatroomId = chatroomId;
    }

    public void setFile(Blob file) {
        this.file = file;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setUploadedAt(Timestamp uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
