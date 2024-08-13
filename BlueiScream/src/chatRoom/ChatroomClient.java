package chatRoom;

import CustomAdapter.MouseCustomAdapter;
import components.ColorRoundButton;
import components.ColorRoundTextView;
import components.DarkPanel;
import components.Header;
import emoticon.emoji;
import menu.MainMenuView;
import profile.MiniProfileView;
import utils.MakeComponent;


import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
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
    private MakeComponent mc;

    // socket
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

        mc = new MakeComponent();

        initializeComponents();
        makeReadyMadeMessages();
        JScrollBar vertical = scroll.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
        setupNetworking();
        setVisible(true);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                MainMenuView.alram.setActiveRoom(roomId);
            }
        });
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

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBackground(Color.white);
        setBounds(800, 100, TOTALWIDTH, 800);

        // header----------------------------------------------------

        createHeader();

        // message view --------------------------------------------------------

        Image bgImg = mc.loadBgImage(clientId, roomId);

        messageP = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImg != null) {
                    g.drawImage(bgImg, 0, 0, getWidth(), getHeight(), this);
                } else {
                    setBackground(Color.white);
                }
            }
        };

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
                String[] data = {clientId, c, String.valueOf(roomId), "text"};
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
        String chatroomName = dao.getChatRoomName(roomId);
        JButton alramBtn;
        int iconSize = 26;

        isAlram = dao.getisAlram(clientId, roomId);
        if (isAlram)
            alramBtn = mc.setNoneBorderIconButton("images/alramOnIcon.png", iconSize);
        else
            alramBtn = mc.setNoneBorderIconButton("images/alramOffIcon.png", iconSize);
        alramBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
        alramBtn.setFocusPainted(false);
        alramBtn.setContentAreaFilled(false);

        Header headerP = new Header(chatroomName, alramBtn);

        add(headerP, BorderLayout.NORTH);

        alramBtn.addActionListener(e -> {
            isAlram = !isAlram;

            if (isAlram)
                alramBtn.setIcon(mc.resizeIcon("images/alramOnIcon.png", iconSize));
            else
                alramBtn.setIcon(mc.resizeIcon("images/alramOffIcon.png", iconSize));

            try {
                dao.setIsAlram(clientId, roomId, isAlram);
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            MainMenuView.alram.setAlramOffRooms();
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

        JButton moreContentsBtn = mc.setNoneBorderIconButton("images/plusIcon.png", 20);
        JButton emoticonBtn = mc.setNoneBorderIconButton("images/emojiIcon.png", 20);

        moreContentsBtn.setFocusable(false);
        emoticonBtn.setFocusable(false);

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

        moreContentsBtn.addActionListener(e -> new MoreMenu(clientId, roomId, this));
        emoticonBtn.addActionListener(e -> new emoji(this));
    }

    private void setupNetworking() {
        try {
            socket = new Socket("114.70.127.232", 5000);
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

                System.out.println(chat.length);
                msgId = dao.getMsgId(chat[0], roomId);
                makeMessageView(chat[1], chat[0], dao.getUserName(chat[0]), chat[3], msgId, 0);
//                makeMessageView(chat[1], chat[0], dao.getUserName(chat[0]), 0, msgId);
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
            makeMessageView(msg.getContent(), msg.getUserId(), name, msg.getMessageType(), msg.getMsgId(), msg.getReaction());
        }
    }

    private ImageIcon setReactionImage(int reaction) {
        ImageIcon img = null;

        if (reaction == 1)
            img = mc.resizeIcon("images/heartIcon.png", 20);
        else if (reaction == 2)
            img = mc.resizeIcon("images/exciteIcon.png", 20);
        else if (reaction == 3)
            img = mc.resizeIcon("images/umIcon.png", 20);
        else if (reaction == 4)
            img = mc.resizeIcon("images/angryIcon.png", 20);

        return img;
    }

    public void makeMessageView(String c, String id, String name, String messageType, int msgId, int reaction) {
        if (messageP.getComponentCount() > 0)
            messageP.remove(messageP.getComponentCount() - 1);

        gbc.gridy = gy++;
        gbc.weighty = 0.0;

        JButton bb = new JButton();
        JLabel reactionLb = new JLabel(setReactionImage(reaction));
        JPanel p = new JPanel();
        JPanel wp = new JPanel();
        JPanel rp = new JPanel();
        JLabel n = new JLabel(name);
        Color backColor = new Color(229, 218, 218);
        ColorRoundTextView m = null;

        if (id.equals(clientId))
            backColor = new Color(229, 149, 0);

        rp.setLayout(new BorderLayout());

        if (messageType.equals("text")) {
            m = new ColorRoundTextView(reformText(c), backColor, Color.BLACK);
            rp.add(m, BorderLayout.CENTER);
            m.addMouseListener(new MouseCustomAdapter() {
                @Override
                public void shortActionPerformed(MouseEvent e) {
                    return;
                }

                public void longActionPerformed(MouseEvent e) {
                    if (id.equals(clientId))
                        return;
                    new ReactionMenu(chatroomClient, reactionLb, msgId);
                }
            });

        } else if (messageType.equals("image")) {
            ImageIcon icon = new ImageIcon(c);
            JLabel imgLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
            rp.add(imgLabel, BorderLayout.CENTER);
        } else {
            JButton fileButton = new JButton("Download " + new File(c).getName());
            fileButton.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setSelectedFile(new File(fileButton.getText().replace("Download ", "")));
                int returnValue = fileChooser.showSaveDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        Files.copy(new File(c).toPath(), selectedFile.toPath());
                        JOptionPane.showMessageDialog(this, "File downloaded successfully!");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Failed to download file.");
                    }
                }
            });
            rp.add(fileButton, BorderLayout.CENTER);
        }

        p.setBackground(null);
        p.setOpaque(false);
        wp.setBackground(null);
        wp.setOpaque(false);
        rp.setOpaque(false);

        reactionLb.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        if (!id.equals(clientId)) {
            bb = mc.setImageButton(id, 45);
            p.setLayout(new FlowLayout(FlowLayout.LEFT));
            wp.add(bb, BorderLayout.WEST);
            rp.add(n, BorderLayout.NORTH);
            rp.add(reactionLb, BorderLayout.EAST);
        } else {
            p.setLayout(new FlowLayout(FlowLayout.RIGHT));
        }

        wp.add(rp, BorderLayout.CENTER);

        Dimension ppd = new Dimension(TOTALWIDTH,
                (int) p.getPreferredSize().getHeight());

        p.add(wp);
        p.add(rp);
        p.setMaximumSize(ppd);
        p.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        messageP.add(p, gbc);

        gbc.gridy = gy;
        gbc.weighty = 1.0;
        messageP.add(Box.createVerticalGlue(), gbc);

        rp.addMouseListener(new MouseCustomAdapter() {
            @Override
            public void shortActionPerformed(MouseEvent e) {
                return;
            }

            public void longActionPerformed(MouseEvent e) {
                if (id.equals(clientId))
                    return;
                new ReactionMenu(chatroomClient, reactionLb, msgId);
            }
        });

        if (m != null && clientId.equals("admin")) {
            m.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        MessageDeleteMenu deleteMenu = new MessageDeleteMenu(ChatroomClient.this, msgId, p);
                        deleteMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            });
        }

        bb.addActionListener(e -> new MiniProfileView(id, false));
    }

    public void setNewReaction(int newReact, JLabel c, int msgId) {
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

    public void sendMessageToChat(String imagePath) {
        try {
            DataPost dp = new DataPost();
            String[] data = {clientId, imagePath, String.valueOf(roomId), "image"};
            dp.setChat(data);
            oos.writeObject(dp);
            oos.flush();

            Thread.sleep(100);
            dao.insertMessage(roomId, clientId, imagePath, "image");
        } catch (Exception e1) {
            System.out.println(e1.getMessage());
        }
    }

    public void sendFile(File file) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            String fileType = Files.probeContentType(file.toPath());
            if (fileType == null) {
                fileType = "application/octet-stream"; // 기본 MIME 타입
            }

            String messageType = fileType.startsWith("image/") ? "image" : "file";

            // 파일 데이터를 읽어와서 byte[]로 변환
            byte[] fileData = Files.readAllBytes(file.toPath());

            // DB 연결
            dao.joinAcces();  // joinAcces()를 사용하여 DB 연결 설정
            conn = dao.conn;  // 연결된 Connection 객체를 사용
            String sql = "INSERT INTO files (user_id, chatroom_id, file, file_path, file_type, uploaded_at) VALUES (?, ?, ?, ?, ?, NOW())";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, clientId);
            pstmt.setInt(2, roomId);
            pstmt.setBytes(3, fileData);
            pstmt.setString(4, file.getAbsolutePath());
            pstmt.setString(5, messageType);
            pstmt.executeUpdate();

            // 파일 경로와 메타데이터를 사용하여 채팅 메시지를 전송
            DataPost dp = new DataPost();
            dp.setChat(new String[]{clientId, file.getAbsolutePath(), String.valueOf(roomId), messageType});
            oos.writeObject(dp);
            oos.flush();

            // 메시지 데이터를 데이터베이스에 저장
            dao.insertMessage(roomId, clientId, file.getAbsolutePath(), messageType);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                dao.closeAcces();  // DB 연결 종료
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void refreshMessages() {
        // TODO Auto-generated method stub

    }

    public ChatRoomDao getDao() {
        // TODO Auto-generated method stub
        return dao;
    }

    public static void main(String[] args) {
//        ChatroomClient c = new ChatroomClient("aaa", 2);
//        ChatroomClient c = new ChatroomClient("aaa", 1);
    }
}
