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
        super("Room list");
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
//        chatList.setLayout(new GridBagLayout());
        scroll = new JScrollPane(chatList);
        add(scroll, BorderLayout.CENTER);
        scroll.setBorder(noneBorder);

        // bottom (input field, emoji etc...)------------------------------

        PinkPanel lineP = new PinkPanel();
        lineP.setSize(TOTALWIDTH, 10);

        JPanel btnP = new JPanel();
        JPanel BtnWrapper = new JPanel();
        JButton profileBtn = new JButton();
        JButton chatBtn = new JButton();
        JButton settingBtn = new JButton();
        ImageIcon pfIcon = new ImageIcon("./images/profileBtnIcon.png");
        ImageIcon chatIcon = new ImageIcon("./images/chatBtnIcon.png");
        ImageIcon setIcon = new ImageIcon("./images/settingBtnIcon.png");

        profileBtn.setIcon(pfIcon);
        chatBtn.setIcon(chatIcon);
        settingBtn.setIcon(setIcon);
        profileBtn.setBackground(null);
        chatBtn.setBackground(null);
        settingBtn.setBackground(null);
        profileBtn.setBorder(noneBorder);
        chatBtn.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        settingBtn.setBorder(noneBorder);

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
        ColorRoundLabel notiCntLb = new ColorRoundLabel(Color.pink);
        int notiCnt = dao.getNotReadMessageCnt(clientId, roomId);

        notiCntLb.setText(notiCnt <= 0 ? " " : String.valueOf(notiCnt));

        roomP.setLayout(new BorderLayout());
        roomP.setBackground(Color.white);
        rightP.setLayout(new BorderLayout());
        rightP.setBackground(Color.white);
        btmP.setBackground(Color.lightGray);
        notiCntLb.setForeground(Color.red);
        btmP.setMaximumSize(new Dimension(TOTALWIDTH, 2));
        roomNameLb.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        rightP.add(timeLb, BorderLayout.NORTH);
        rightP.add(notiCntLb, BorderLayout.EAST);
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
                notiCntLb.setText("");
                repaint();
            }
        });

        chatList.add(roomP);
        chatList.add(btmP);
    }

    public static void main(String[] args) {
        ChatRoomList c = new ChatRoomList("111");
    }
}
