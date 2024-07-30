package chatRoom;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ReactionMenu extends JFrame {

    private static ReactionMenu reactionMenu;

    public ReactionMenu(ChatroomClient chatroomClient, JButton c, int msgId) {
        setSize(300, 100);
        setLayout(new FlowLayout(FlowLayout.CENTER));
        AtomicInteger res = new AtomicInteger();

        JButton b1 = makeIconButton("images/alramOffIcon.png", 20);
        JButton b2 = makeIconButton("images/chat_icon.png", 20);
        JButton b3 = makeIconButton("images/emojiicon.png", 20);
        JButton b4 = new JButton("취소");

        b1.addActionListener(e -> chatroomClient.setNewReaction(1, c, msgId));
        b2.addActionListener(e -> chatroomClient.setNewReaction(2, c, msgId));
        b3.addActionListener(e -> chatroomClient.setNewReaction(3, c, msgId));
        b4.addActionListener(e -> chatroomClient.setNewReaction(0, c, msgId));

        add(b1);
        add(b2);
        add(b3);
        add(b4);

        setVisible(true);
    }

    public static ReactionMenu getInstance() {
        return reactionMenu;
    }

    public String teest() {
        return "슈퍼맨";
    }

    private JButton makeIconButton(String src, int iconSize) {
        JButton btn = new JButton();

        if (src != null && !src.isEmpty()) {
            ImageIcon icon = resizeIcon(src, iconSize);
            btn.setIcon(icon);
        }

        btn.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        btn.setBackground(null);

        return btn;
    }

    private ImageIcon resizeIcon(String src, int iconSize) {
        ImageIcon icon = new ImageIcon(src);
        Image image = icon.getImage();
        Image newimg = image.getScaledInstance(iconSize, iconSize, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        icon = new ImageIcon(newimg);

        return icon;
    }

}
