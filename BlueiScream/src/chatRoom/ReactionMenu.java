package chatRoom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ReactionMenu extends JFrame {

    private static ReactionMenu reactionMenu;

    public ReactionMenu(ChatroomClient chatroomClient, JLabel c, int msgId) {
        setSize(250, 100);
        setIconImage(null);

        JPanel p = new JPanel();
        p.setBackground(Color.white);
        p.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton b1 = makeIconButton("images/heartIcon.png", 20);
        JButton b2 = makeIconButton("images/exciteIcon.png", 20);
        JButton b3 = makeIconButton("images/umIcon.png", 20);
        JButton b4 = makeIconButton("images/angryIcon.png", 20);
        JButton b5 = new JButton("취소");

        b1.addActionListener(e -> {
            chatroomClient.setNewReaction(1, c, msgId);
            dispose();
        });
        b2.addActionListener(e -> {
            chatroomClient.setNewReaction(2, c, msgId);
            dispose();
        });
        b3.addActionListener(e -> {
            chatroomClient.setNewReaction(3, c, msgId);
            dispose();
        });
        b4.addActionListener(e -> {
            chatroomClient.setNewReaction(4, c, msgId);
            dispose();
        });
        b5.addActionListener(e -> {
            chatroomClient.setNewReaction(5, c, msgId);
            dispose();
        });

        p.add(b1);
        p.add(b2);
        p.add(b3);
        p.add(b4);
        p.add(b5);
        add(p);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {
                dispose();
            }
        });

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
        btn.setOpaque(false);

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
