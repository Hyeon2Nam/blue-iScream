package chatRoom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChatRoomListMenu extends JFrame {
    private String clientId;

    public ChatRoomListMenu(String cliendId)  {
        this.clientId = cliendId;

        setSize(250,150);
        setResizable(false);

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 0, 10, 0);

        JButton b1 = new JButton("채팅방 생성");
        JButton b2 = new JButton("채팅방 삭제");

        b1.setBackground(new Color(229, 149, 0));
        b2.setBackground(new Color(229, 149, 0));
        p.setBackground(Color.white);

        p.add(b1, gbc);
        p.add(b2, gbc);

        add(p);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {
                dispose();
            }
        });

        setVisible(true);

        b1.addActionListener(e -> {
            new CreateChatRoom(clientId);
            dispose();
        });
    }

    public static void main(String[] args) {
        new ChatRoomListMenu("aaa");
    }
}
