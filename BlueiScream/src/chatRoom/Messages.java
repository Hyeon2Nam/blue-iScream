package chatRoom;

import java.awt.*;
import java.sql.Timestamp;

public class Messages {
    private int msgId;
    private String userId;
    private String content;
    private Timestamp createdAt;
    private String messageType;
    private String mesTo;
    private String mesFrom;
    private int reaction;
    private int isRead;

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public void setMesTo(String mesTo) {
        this.mesTo = mesTo;
    }

    public void setMesFrom(String mesFrom) {
        this.mesFrom = mesFrom;
    }

    public void setReaction(int reaction) {
        this.reaction = reaction;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public String getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getMesTo() {
        return mesTo;
    }

    public String getMesFrom() {
        return mesFrom;
    }

    public int getReaction() {
        return reaction;
    }

    public int getIsRead() {
        return isRead;
    }

    public int getMsgId() {
        return msgId;
    }
}
