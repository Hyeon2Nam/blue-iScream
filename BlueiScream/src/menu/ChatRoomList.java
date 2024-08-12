package menu;

import javax.swing.*;

import chatRoom.*;
import components.ColorRoundLabel;
import components.DarkPanel;
import components.Header;
import utils.MakeComponent;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ChatRoomList extends JFrame {
    // UI
    private ChatRoomDao dao;
    private String clientId;
    private JPanel chatList;
    private JScrollPane scroll;
    private final int TOTALWIDTH = 400;
    private MakeComponent mc;
    private JPanel main;
    public static Alram alram;

    public ChatRoomList(String clientId) {
        super("Room list");

        this.clientId = clientId;
        this.alram = alram;

        mc = new MakeComponent();
        dao = new ChatRoomDao();
    }

    public JPanel makeNewChatRoomList() {
        main = new JPanel(new BorderLayout());

        initializeComponents();

        JOptionPane op = new JOptionPane("채팅방을 불러오고 있습니다.");
        loadChatRooms();
        op.setVisible(false);

        return main;
    }

    public void initializeComponents() {

        main.setSize(TOTALWIDTH, 800);
        main.setBackground(Color.white);

        createHeader();
        initChatList();
    }

    private void initChatList() {
        chatList = new JPanel();
        chatList.setBackground(Color.white);
        chatList.setLayout(new BoxLayout(chatList, BoxLayout.Y_AXIS));
        scroll = new JScrollPane(chatList);
        main.add(scroll, BorderLayout.CENTER);
        scroll.setBorder(BorderFactory.createEmptyBorder());
    }

    private void createHeader() {
        JButton optionBtn = mc.setIconButton("images/kebabIcon.png", 20);
        JButton refreshBtn = mc.setIconButton("images/refreshIcon.png", 25);
        Header headerP = new Header(refreshBtn, "Chat", optionBtn);

        optionBtn.setFocusPainted(false);
        refreshBtn.setFocusPainted(false);

        main.add(headerP, BorderLayout.NORTH);

        // -----[events] -----------------------

        refreshBtn.addActionListener(e -> {
            chatList.removeAll();
            loadChatRooms();
            main.revalidate();
            main.repaint();
        });

        optionBtn.addActionListener(e -> {
            new ChatRoomListMenu(clientId);
        });
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
        JButton leftB = new JButton(mc.resizeIcon("images/defaultRoomIcon.png", 40));
        JLabel roomNameLb = new JLabel(roomName);
        JPanel btmP = new JPanel();

        String date = dao.getSendMessageTime(roomId);
        if (date == null || date.isEmpty())
            date = LocalDate.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
        JLabel timeLb = new JLabel(date);

        JLabel alramCntLb;
        int alramCnt = dao.getNotReadMessageCnt(clientId, roomId);

        if (alramCnt <= 0) {
            alramCntLb = new JLabel();
            alramCntLb.setBackground(null);
            alramCntLb.setText(" ");
            alramCntLb.setForeground(null);
        } else {
            alramCntLb = new ColorRoundLabel(Color.pink);
            alramCntLb.setText(String.valueOf(alramCnt));
            alramCntLb.setForeground(Color.red);
        }

        leftB.setFocusable(false);
        leftB.setBackground(Color.white);
        leftB.setBorder(BorderFactory.createEmptyBorder());

        roomP.setLayout(new BorderLayout());
        roomP.setBackground(null);
        rightP.setLayout(new BorderLayout());
        rightP.setBackground(null);
        btmP.setBackground(Color.lightGray);
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
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                roomP.setBackground(Color.white);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                new ChatroomClient(clientId, roomId);
                alramCntLb.setVisible(false);
            }
        });

        chatList.add(roomP);
        chatList.add(btmP);
    }

//    public static void main(String[] args) {
//        ChatRoomList c = new ChatRoomList("qqq");
//    }
}
