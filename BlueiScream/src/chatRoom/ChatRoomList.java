package chatRoom;

import javax.swing.*;
import javax.swing.border.Border;

import components.ColorRoundButton;
import components.ColorRoundLabel;
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
    private ChatRoomDao dao;
    private String clientId;
    private JPanel chatList;
    private JScrollPane scroll;
    private final int TOTALWIDTH = 400;

    public ChatRoomList(String clientId) {
        super("Room list");
        this.clientId = clientId;

        initializeComponents();
        loadChatRooms();
        JScrollBar vertical = scroll.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
        setVisible(true);
    }

    public void initializeComponents() {
        Border noneBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);

        dao = new ChatRoomDao();

        setSize(TOTALWIDTH, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.white);

        // header----------------------------------------------------

        PinkPanel headerP = new PinkPanel();
        JLabel titleLb = new JLabel("Chat");
        titleLb.setFont(new Font(titleLb.getFont().getFontName(), titleLb.getFont().getStyle(), 20));
        headerP.setSize(TOTALWIDTH, 70);
        headerP.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        headerP.add(titleLb);
        add(headerP, BorderLayout.NORTH);

        // chat list view --------------------------------------------------------

        chatList = new JPanel();
        chatList.setBackground(Color.white);
        chatList.setLayout(new BoxLayout(chatList, BoxLayout.Y_AXIS));
        scroll = new JScrollPane(chatList);
        add(scroll, BorderLayout.CENTER);
        scroll.setBorder(noneBorder);

        // bottom (input field, emoji etc...)------------------------------

        PinkPanel lineP = new PinkPanel();
        lineP.setSize(TOTALWIDTH, 10);

        JPanel btnP = new JPanel();
        JPanel BtnWrapper = new JPanel();
        JButton profileBtn = setBottomButton("./images/profileBtnIcon.png");
        JButton chatBtn = setBottomButton("./images/chatBtnIcon.png");
        JButton settingBtn = setBottomButton("./images/settingBtnIcon.png");

        chatBtn.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        BtnWrapper.setLayout(new FlowLayout(FlowLayout.CENTER));
        btnP.setLayout(new BorderLayout());
        BtnWrapper.setBackground(Color.white);
        BtnWrapper.add(profileBtn);
        BtnWrapper.add(chatBtn);
        BtnWrapper.add(settingBtn);

        btnP.add(lineP, BorderLayout.NORTH);
        btnP.add(BtnWrapper, BorderLayout.CENTER);
        add(btnP, BorderLayout.SOUTH);
        // event -------------------------------------------------------------

    }

    private JButton setBottomButton(String imgSrc) {
        JButton b = new JButton();
        ImageIcon icon = new ImageIcon(imgSrc);

        b.setIcon(icon);
        b.setBackground(null);
        b.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

        return b;
    }

    private void loadChatRooms() {
        List<ChatRoom> crlist = dao.getChatRoomList(clientId);
        for (ChatRoom cr : crlist) {
            makeChatRoomView(cr.getChatroomName(), cr.getChatroomId());
        }
    }

    public void makeChatRoomView(String roomName, int roomId) {
        JPanel roomP = new JPanel();
        JPanel rightP = new JPanel();
        JButton leftB = new JButton();
        JLabel roomNameLb = new JLabel(roomName);
        JLabel timeLb = new JLabel(dao.getSendMessageTime(roomId));
        JPanel btmP = new JPanel();
        ColorRoundLabel alramCntLb = new ColorRoundLabel(Color.pink);
        int alramCnt = dao.getNotReadMessageCnt(clientId, roomId);

        alramCntLb.setText(alramCnt <= 0 ? " " : String.valueOf(alramCnt));

        roomP.setLayout(new BorderLayout());
        roomP.setBackground(Color.white);
        rightP.setLayout(new BorderLayout());
        rightP.setBackground(Color.white);
        btmP.setBackground(Color.lightGray);
        alramCntLb.setForeground(Color.red);
        btmP.setMaximumSize(new Dimension(TOTALWIDTH, 2));
        roomNameLb.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        rightP.add(timeLb, BorderLayout.NORTH);
        rightP.add(alramCntLb, BorderLayout.EAST);
        roomP.add(leftB, BorderLayout.WEST);
        roomP.add(roomNameLb, BorderLayout.CENTER);
        roomP.add(rightP, BorderLayout.EAST);
        roomP.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        roomP.setMaximumSize(new Dimension(TOTALWIDTH, (int) roomP.getPreferredSize().getHeight()));

        roomP.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                roomP.setBackground(getBackground().darker());
                rightP.setBackground(getBackground().darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                roomP.setBackground(Color.white);
                rightP.setBackground(Color.white);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                new ChatroomClient(clientId, roomId);
                alramCntLb.setText("");
                repaint();
            }
        });

        chatList.add(roomP);
        chatList.add(btmP);
    }

    public static void main(String[] args) {
        ChatRoomList c = new ChatRoomList("qqq");
    }
}
