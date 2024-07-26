package chatRoom;

import java.io.Serializable;

public class DataPost implements Serializable {
    private static final long serialVersionUID = 1L;

    private String[] chat;    //chat �궡�슜
    private String img;     //img �궡�슜

    public String[] getChat() {
        return chat;
    }

    public void setChat(String[] chat) {
        this.chat = chat;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
