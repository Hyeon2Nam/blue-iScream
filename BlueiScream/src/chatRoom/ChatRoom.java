package chatRoom;

import java.sql.Timestamp;

public class ChatRoom {
    private int chatroomId;
    private String chatroomName;
    private Timestamp createdAt;
    private String category;
    private int backgroundImg;
    private boolean isAlarm;

    public void setChatroomId(int chatroomId) {
        this.chatroomId = chatroomId;
    }

    public void setChatroomName(String chatroomName) {
        this.chatroomName = chatroomName;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setBackgroundImg(int backgroundImg) {
        this.backgroundImg = backgroundImg;
    }

    public void setAlarm(boolean alarm) {
        isAlarm = alarm;
    }

    public int getChatroomId() {
        return chatroomId;
    }

    public String getChatroomName() {
        return chatroomName;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public String getCategory() {
        return category;
    }

    public int getBackgroundImg() {
        return backgroundImg;
    }

    public boolean isAlarm() {
        return isAlarm;
    }
}
