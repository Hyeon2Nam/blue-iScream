package chatRoom;

import javax.swing.*;

import components.ColorRoundButton;
import components.ColorRoundTextView;
import components.PinkPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class ChatRoomList extends JFrame {
    // UI
    private JTextField inputMessage;
    private ColorRoundButton sendBtn;
    private JButton emoticonBtn;
    private JButton moreContentsBtn;
    private ChatRoomDao dao;
    private String clientId;
    private JPanel chatList;
    private final Color BGC = new Color(219, 219, 219);
    private JScrollPane scroll;
    private final int TOTALWIDTH = 400;
    private GridBagConstraints gbc;
    private int gy;

    public ChatRoomList(String clientId) {
        super( "Room list");
        this.clientId = clientId;

        initializeComponents();
        loadChatRooms();
        JScrollBar vertical = scroll.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
        setVisible(true);
    }

    public void initializeComponents() {
        gy = 0;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = gy;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;

        dao = new ChatRoomDao();

        setSize(TOTALWIDTH, 800);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBackground(Color.white);

        // header----------------------------------------------------

        PinkPanel headerP = new PinkPanel();
        JLabel titleLb = new JLabel("Chat");
        titleLb.setFont(new Font(titleLb.getFont().getFontName(), titleLb.getFont().getStyle(), 20));
        headerP.setSize(TOTALWIDTH, 70);
        headerP.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        headerP.add(titleLb);
        add(headerP, BorderLayout.CENTER);

        // chat list view --------------------------------------------------------

        chatList = new JPanel();
        chatList.setBackground(Color.white);
        chatList.setLayout(new GridBagLayout());
        scroll = new JScrollPane(chatList);
        add(scroll, BorderLayout.CENTER);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // bottom (input field, emoji etc...)------------------------------

        PinkPanel lineP = new PinkPanel();
        lineP.setSize(TOTALWIDTH, 10);

        JPanel buttonP = new JPanel();
        buttonP.setBackground(Color.white);
        buttonP.setSize(TOTALWIDTH, 100);
        buttonP.setLayout(new FlowLayout(FlowLayout.CENTER));

        moreContentsBtn = new JButton("+");
        inputMessage = new JTextField(18);
        emoticonBtn = new JButton("^^");
        sendBtn = new ColorRoundButton("send", new Color(255, 214, 214), Color.white, 10);

        inputMessage.setText("input message");
        inputMessage.setForeground(Color.lightGray);
        inputMessage.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        buttonP.add(moreContentsBtn);
        buttonP.add(inputMessage);
        buttonP.add(emoticonBtn);
        buttonP.add(sendBtn);

        JPanel bottomP = new JPanel();
        bottomP.setSize(TOTALWIDTH, 110);
        bottomP.setBackground(Color.white);
        bottomP.setLayout(new BorderLayout());
        bottomP.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        bottomP.add(lineP, BorderLayout.NORTH);
        bottomP.add(buttonP, BorderLayout.CENTER);
        add(bottomP, BorderLayout.SOUTH);

        // event -------------------------------------------------------------

    }

    private void loadChatRooms() {
        List<ChatRoom> crlist = dao.getChatRoomList(clientId);
        for (ChatRoom cr : crlist) {
            String name = dao.getChatRoomName(cr.getChatroomId());
//            makeChatRoomView(msg.getContent(), msg.getUserId(), name);
        }
    }

    public void makeChatRoomView(String c, String id, String name) {

    }

    private String reformText(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);

        for (int i = 15; i < str.length(); i += 15) {
            sb.insert(i, '\n');
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        ChatRoomList c = new ChatRoomList("aaa");
    }
}
