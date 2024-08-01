package chatRoom;

import javax.swing.*;
import javax.swing.border.Border;

import components.ColorRoundLabel;
import components.DarkPanel;
import components.Header;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        dao = new ChatRoomDao();

        setSize(TOTALWIDTH, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.white);

        // header----------------------------------------------------

        createHeader();

        // chat list view --------------------------------------------------------

        initChatList();

        // bottom (input field, emoji etc...)------------------------------

        DarkPanel lineP = new DarkPanel();
        lineP.setSize(TOTALWIDTH, 10);

        int size = 40;
        JPanel btnP = new JPanel();
        JPanel BtnWrapper = new JPanel();
        JButton profileBtn = setIconButton("./images/profileBtnIcon.png", size);
        JButton chatBtn = setIconButton("./images/chatBtnIcon.png", size);
        JButton settingBtn = setIconButton("./images/settingBtnIcon.png", size);

        chatBtn.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        BtnWrapper.setLayout(new FlowLayout(FlowLayout.CENTER));
        btnP.setLayout(new BorderLayout());
        BtnWrapper.setBackground(Color.white);
        BtnWrapper.add(profileBtn);
        BtnWrapper.add(chatBtn);
//        BtnWrapper.add(settingBtn);

        btnP.add(lineP, BorderLayout.NORTH);
        btnP.add(BtnWrapper, BorderLayout.CENTER);
        add(btnP, BorderLayout.SOUTH);

        // event -------------------------------------------------------------

    }

    private void initChatList() {
        chatList = new JPanel();
        chatList.setBackground(Color.white);
        chatList.setLayout(new BoxLayout(chatList, BoxLayout.Y_AXIS));
        scroll = new JScrollPane(chatList);
        add(scroll, BorderLayout.CENTER);
        scroll.setBorder(BorderFactory.createEmptyBorder());
    }

    private void createHeader() {
        JButton optionBtn = setIconButton("images/kebabIcon.png", 20);
        JButton refreshBtn = setIconButton("images/refreshIcon.png", 25);
        Header headerP = new Header(refreshBtn, "Chat", optionBtn);

        optionBtn.setFocusPainted(false);
        refreshBtn.setFocusPainted(false);

        add(headerP, BorderLayout.NORTH);

        // -----[events] -----------------------

        refreshBtn.addActionListener(e -> {
            chatList.removeAll();
            loadChatRooms();
            revalidate();
            repaint();
        });

        optionBtn.addActionListener(e -> {
            new ChatRoomListMenu(clientId);
        });
    }

    private JButton setIconButton(String imgSrc, int size) {
        JButton b = new JButton();
        ImageIcon icon = resizeIcon(imgSrc, size);

        b.setIcon(icon);
        b.setBackground(null);
        b.setBorder(BorderFactory.createEmptyBorder());

        return b;
    }

    private ImageIcon resizeIcon(String src, int iconSize) {
        ImageIcon icon = new ImageIcon(src);
        Image image = icon.getImage();
        Image newimg = image.getScaledInstance(iconSize, iconSize, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        icon = new ImageIcon(newimg);

        return icon;
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

    public static void main(String[] args) {
        ChatRoomList c = new ChatRoomList("aaa");
    }
}
