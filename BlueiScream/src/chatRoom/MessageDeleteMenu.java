package chatRoom;

import javax.swing.*;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import chatRoom.ChatroomClient;

public class MessageDeleteMenu extends JPopupMenu {
    private JMenuItem deleteItem;
    private ChatroomClient chatroomClient;
    private int msgId;
    private JPanel messagePanel;

    public MessageDeleteMenu(ChatroomClient chatroomClient, int msgId, JPanel messagePanel) {
        this.chatroomClient = chatroomClient;
        this.msgId = msgId;
        this.messagePanel = messagePanel;

        deleteItem = new JMenuItem("삭제");
        add(deleteItem);

        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
					deleteMessage();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
            }
        });
    }

    private void deleteMessage() throws SQLException {
        chatroomClient.getDao().deleteMessage(msgId);
		chatroomClient.refreshMessages();
		messagePanel.setVisible(false);
		Container parent = messagePanel.getParent();
		if (parent != null) {
		    parent.remove(messagePanel);
		    parent.revalidate();
		    parent.repaint();
		}
    }
}
