package chatRoom;

import CustomAdapter.MouseCustomAdapter;
import components.ColorRoundButton;
import components.ColorRoundTextView;
import components.DarkPanel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class ChatroomClient extends JFrame {
    private static ChatroomClient chatroomClient;
    // UI
    private JTextField inputMessage;
    private ColorRoundButton sendBtn;
    private ChatRoomDao dao;
    private String clientId;
    private JPanel messageP;
    private int roomId;
    private boolean isFirst;
    private final Color BGC = new Color(219, 219, 219);
    private JScrollPane scroll;
    private final int TOTALWIDTH = 400;
    private GridBagConstraints gbc;
    private int gy;
    private boolean isAlram;
    private int newReact;

    // socket
    private JList<String> userList;
    private JList<String> chatList;
    private DefaultListModel<String> userListModel;
    private DefaultListModel<String> chatListModel;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public ChatroomClient(String clientId, int roomId) {
        super(clientId + "'s room");
        if (chatroomClient == null) {
            chatroomClient = this;
        }
        this.clientId = clientId;
        this.roomId = roomId;

        initializeComponents();
        makeReadyMadeMessages();
        JScrollBar vertical = scroll.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
        setupNetwirking();
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

        isFirst = true;
        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);

        // Chat list with a model
        chatListModel = new DefaultListModel<>();
        chatList = new JList<>(chatListModel);

        setSize(TOTALWIDTH, 800);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBackground(Color.white);

        // header----------------------------------------------------

        createHeader();

        // message view --------------------------------------------------------

        messageP = new JPanel();
        messageP.setBackground(Color.white);
        messageP.setLayout(new GridBagLayout());
        scroll = new JScrollPane(messageP);
        add(scroll, BorderLayout.CENTER);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // bottom (input field, emoji etc...)------------------------------

        createBottomButton();

        // event -------------------------------------------------------------

        inputMessage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isFirst) {
                    inputMessage.setText("");
                    isFirst = false;
                }
                inputMessage.setForeground(Color.BLACK);
            }
        });

        sendBtn.addActionListener(e -> {
            String c = inputMessage.getText();

            if (c.isEmpty() || isFirst)
                return;

            try {
                dao.insertMessage(roomId, clientId, c, "text");

                DataPost dp = new DataPost();
                String[] data = {clientId, c, String.valueOf(roomId)};
                dp.setChat(data);
                oos.writeObject(dp);
                oos.flush();

                Thread.sleep(100);
            } catch (Exception e1) {
                System.out.println(e1.getMessage());
            }

            inputMessage.setText("");
            JScrollBar vertical = scroll.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());

            validate();
            repaint();
        });

        inputMessage.addActionListener(e -> sendBtn.doClick());

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                dao.setLastReadTime(clientId, roomId);
            }
        });
    }

    private void createHeader() {
        DarkPanel headerP = new DarkPanel();
        DarkPanel leftP = new DarkPanel(30, "left");
        String chatroomName = dao.getChatRoomName(roomId);
        JLabel titleLb = new JLabel(chatroomName);
        JButton alramBtn;
        int iconSize = 26;

        isAlram = dao.getisAlram(clientId, roomId);
        if (isAlram)
            alramBtn = makeBottomIconButton("images/alramOnIcon.png", iconSize);
        else
            alramBtn = makeBottomIconButton("images/alramOffIcon.png", iconSize);
        alramBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
        alramBtn.setFocusPainted(false);
        alramBtn.setContentAreaFilled(false);

        titleLb.setForeground(Color.white);
        titleLb.setFont(new Font(titleLb.getFont().getFontName(), titleLb.getFont().getStyle(), 20));
        titleLb.setHorizontalAlignment(JLabel.CENTER);
        headerP.setLayout(new BorderLayout());
        headerP.setSize(TOTALWIDTH, 70);
        headerP.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        headerP.add(leftP, BorderLayout.WEST);
        headerP.add(titleLb, BorderLayout.CENTER);
        headerP.add(alramBtn, BorderLayout.EAST);
        add(headerP, BorderLayout.NORTH);

        alramBtn.addActionListener(e -> {
            isAlram = !isAlram;

            if (isAlram)
                alramBtn.setIcon(resizeIcon("images/alramOnIcon.png", iconSize));
            else
                alramBtn.setIcon(resizeIcon("images/alramOffIcon.png", iconSize));

            try {
                dao.setIsAlram(clientId, roomId, isAlram);
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void createBottomButton() {
        Border noneBorder = BorderFactory.createEmptyBorder();

        DarkPanel lineP = new DarkPanel();
        lineP.setSize(TOTALWIDTH, 1);

        JPanel buttonP = new JPanel();
        buttonP.setBackground(Color.white);
        buttonP.setSize(TOTALWIDTH, 100);
        buttonP.setLayout(new FlowLayout(FlowLayout.CENTER));

        inputMessage = new JTextField(18);
        sendBtn = new ColorRoundButton("send", new Color(0, 38, 66), Color.white, 10);

        JButton moreContentsBtn = makeBottomIconButton("images/plusIcon.png", 20);
        JButton emoticonBtn = makeBottomIconButton("images/emojiIcon.png", 20);

        inputMessage.setText("input message");
        inputMessage.setForeground(Color.lightGray);
        inputMessage.setBorder(noneBorder);

        buttonP.add(moreContentsBtn);
        buttonP.add(inputMessage);
        buttonP.add(emoticonBtn);
        buttonP.add(sendBtn);

        JPanel bottomP = new JPanel();
        bottomP.setSize(TOTALWIDTH, 110);
        bottomP.setBackground(Color.white);
        bottomP.setLayout(new BorderLayout());
        bottomP.setBorder(noneBorder);
        bottomP.add(lineP, BorderLayout.NORTH);
        bottomP.add(buttonP, BorderLayout.CENTER);
        add(bottomP, BorderLayout.SOUTH);

        moreContentsBtn.addActionListener(e -> {
            new MoreMenu(clientId, roomId);
        });
    }

    private JButton makeBottomIconButton(String src, int iconSize) {
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

    private void setupNetwirking() {
        try {
            socket = new Socket("192.168.40.33", 5000);
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();

            Thread listenerThread = new Thread(this::listenForMessages);
            listenerThread.start();
        } catch (IOException e) {
            System.out.println("서버와 연결할 수 없습니다.");
            e.printStackTrace();
        }
    }

    private void listenForMessages() {
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            DataPost receivedDataPost;

            while ((receivedDataPost = (DataPost) ois.readObject()) != null) {
                String[] chat = receivedDataPost.getChat();
                int msgId;

                if (!chat[2].equals(String.valueOf(roomId)))
                    continue;

                SwingUtilities.invokeLater(() -> userListModel.addElement(chat[0]));
                SwingUtilities.invokeLater(() -> chatListModel.addElement(chat[1]));

                msgId = dao.getMsgId(chat[0], roomId);
                makeMessageView(chat[1], chat[0], dao.getUserName(chat[0]), 0, msgId);
                validate();
                repaint();
                JScrollBar vertical = scroll.getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
            }
        } catch (IOException e) {
            System.err.println("Error reading from the server: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Close not found: " + e.getMessage());
        }
    }

    private void makeReadyMadeMessages() {
        List<Messages> msgs = dao.loadMessages(roomId);
        for (Messages msg : msgs) {
            String name = dao.getUserName(msg.getUserId());
            makeMessageView(msg.getContent(), msg.getUserId(), name, msg.getReaction(), msg.getMsgId());
        }
    }

    private ImageIcon setReactionImage(int reaction) {
        ImageIcon img = null;

        if (reaction == 0)
            img = null;
        else if (reaction == 1)
            img = resizeIcon("images/alramOffIcon.png", 20);
        else if (reaction == 2)
            img = resizeIcon("images/chat_icon.png", 20);
        else if (reaction == 3)
            img = resizeIcon("images/emojiicon.png", 20);

        return img;
    }

    public void makeMessageView(String c, String id, String name, int reaction, int msgId) {
        if (messageP.getComponentCount() > 0)
            messageP.remove(messageP.getComponentCount() - 1);

        gbc.gridy = gy++;
        gbc.weighty = 0.0;

        JButton bb = new JButton(" ");
        JButton reactionBtn = makeBottomIconButton("", 0);
//        JPanel pp = new JPanel();
        JPanel p = new JPanel();
        JPanel wp = new JPanel();
        JPanel rp = new JPanel();
        JLabel n = new JLabel(name);
        ColorRoundTextView m;
        Color backColor = new Color(229, 218, 218);

        if (id.equals(clientId))
            backColor = new Color(229, 149, 0);

        m = new ColorRoundTextView(reformText(c), backColor, Color.BLACK);
        reactionBtn.setEnabled(false);

        p.setBackground(null);
        wp.setBackground(null);
        rp.setBackground(null);
//        pp.setBackground(null);

        reactionBtn.setIcon(setReactionImage(reaction));
        rp.setLayout(new BorderLayout());
//        p.setLayout(new FlowLayout(FlowLayout.LEFT));

        if (!id.equals(clientId)) {
            p.setLayout(new FlowLayout(FlowLayout.LEFT));
//            pp.setLayout(new FlowLayout(FlowLayout.LEFT));
            wp.add(bb, BorderLayout.WEST);
            rp.add(n, BorderLayout.NORTH);
            rp.add(reactionBtn, BorderLayout.EAST);
        } else {
            p.setLayout(new FlowLayout(FlowLayout.RIGHT));
//            pp.setLayout(new FlowLayout(FlowLayout.RIGHT));
        }

        rp.add(m, BorderLayout.CENTER);
        wp.add(rp, BorderLayout.CENTER);

//        Dimension wpd = wp.getPreferredSize();
//        Dimension rpd = rp.getPreferredSize();
        Dimension ppd = new Dimension(TOTALWIDTH,
                (int) p.getPreferredSize().getHeight());
//        double tw = wpd.getWidth() + rpd.getWidth();
//        double th = wpd.getWidth() + rpd.getWidth();
//        Dimension td = new Dimension((int) tw, (int) th);

        p.add(wp);
        p.add(rp);
        p.setMaximumSize(ppd);
//        pp.add(p);
//        pp.setMaximumSize(ppd);
//        pp.setMinimumSize(ppd);
        messageP.add(p, gbc);

        gbc.gridy = gy;
        gbc.weighty = 1.0;
        messageP.add(Box.createVerticalGlue(), gbc);


        m.addMouseListener(new MouseCustomAdapter() {
            @Override
            public void shortActionPerformed(MouseEvent e) {
                return;
            }

            public void longActionPerformed(MouseEvent e) {
                new ReactionMenu(chatroomClient, reactionBtn, msgId);
            }
        });
    }

    public void setNewReaction(int newReact, JButton c, int msgId) {
        c.setIcon(setReactionImage(newReact));
        dao.setReaction(newReact, msgId);
        revalidate();
        repaint();
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
        ChatroomClient c = new ChatroomClient("qqq", 2);
//        ChatroomClient c = new ChatroomClient("aaa", 1);
    }
}
