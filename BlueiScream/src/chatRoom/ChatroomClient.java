package chatRoom;

import components.ColorRoundButton;
import components.ColorRoundTextView;
import components.PinkPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Console;
import java.net.MulticastSocket;
import java.util.List;

public class ChatroomClient extends JFrame {
    private JTextArea textAreaDisplay;
    private JTextField inputMessage;
    private ColorRoundButton sendBtn;
    private JButton emoticonBtn;
    private JButton moreContentsBtn;
    private MulticastSocket socket;
    private ChatRoomDao dao;
    private static final int PORT = 5000;
    private static final String HOST = "192.168.40.33";
    private String clientId;
    private String chatroomName;
    private JPanel messageP;
    private int roomId;
    private boolean isFirst;
    private final Color BGC = Color.lightGray;

    public ChatroomClient(String clientId, int roomId) {
        super(clientId + "'s room");
        this.clientId = clientId;
        this.roomId = roomId;


        int totalWidth = 400;
        dao = new ChatRoomDao();
        isFirst = true;

        setSize(totalWidth, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.white);

        // header
        PinkPanel headerP = new PinkPanel();
        // 임시
        chatroomName = "임시 타이틀";
        JLabel titleLb = new JLabel(chatroomName);
        titleLb.setFont(new Font(titleLb.getFont().getFontName(), titleLb.getFont().getStyle(), 20));
        headerP.setSize(totalWidth, 70);
        headerP.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        headerP.add(titleLb);
        add(headerP, BorderLayout.NORTH);

        // message view
        messageP = new JPanel();
        messageP.setBackground(Color.white);
        messageP.setLayout(new BoxLayout(messageP, BoxLayout.Y_AXIS));
        messageP.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        JScrollPane scroll = new JScrollPane(messageP);
        add(scroll, BorderLayout.CENTER);

        // bottom (input field, emoji etc...)
        PinkPanel lineP = new PinkPanel();
        lineP.setSize(totalWidth, 10);

        JPanel buttonP = new JPanel();
        buttonP.setBackground(Color.white);
        buttonP.setSize(totalWidth, 100);
        buttonP.setLayout(new FlowLayout(FlowLayout.CENTER));

        moreContentsBtn = new JButton("+");
        inputMessage = new JTextField(18);
        emoticonBtn = new JButton("^^");
        sendBtn = new ColorRoundButton("send", new Color(255, 214, 214), Color.white, 10);

        inputMessage.setText("input message");
        inputMessage.setForeground(Color.lightGray);

        buttonP.add(moreContentsBtn);
        buttonP.add(inputMessage);
        buttonP.add(emoticonBtn);
        buttonP.add(sendBtn);

        JPanel bottomP = new JPanel();
        bottomP.setSize(totalWidth, 110);
        bottomP.setBackground(Color.white);
        bottomP.setLayout(new BorderLayout());
        bottomP.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        bottomP.add(lineP, BorderLayout.NORTH);
        bottomP.add(buttonP, BorderLayout.CENTER);

        add(bottomP, BorderLayout.SOUTH);

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

        sendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String c = inputMessage.getText();

                if (c.isEmpty() || isFirst)
                    return;

                makeMessageView(c, clientId, clientId);
                dao.insertMessage(roomId, clientId, c, "text");
                inputMessage.setText("");
                validate();
                repaint();
            }
        });

        setVisible(true);
    }

    public void makeReadyMadeMessages() {
        List<Messages> msgs = dao.loadMessages(roomId);

        for (Messages msg : msgs) {
            String name = dao.getUserName(msg.getUserId());
            makeMessageView(msg.getContent(), msg.getUserId(), name);
        }

        validate();
        repaint();
    }

    public void makeMessageView(String c, String id, String name) {
        JButton bb = new JButton(" ");
        JPanel p = new JPanel();
        JPanel wp = new JPanel();
        JPanel rp = new JPanel();
        JLabel n = new JLabel(name);
        ColorRoundTextView m = new ColorRoundTextView(c, BGC, Color.BLACK);

        p.setBackground(null);
        wp.setBackground(null);
        rp.setBackground(null);

        rp.setLayout(new BorderLayout());

        if (!id.equals(clientId)) {
            p.setLayout(new FlowLayout(FlowLayout.LEFT));
            wp.add(bb, BorderLayout.WEST);
            rp.add(n, BorderLayout.NORTH);
        } else
            p.setLayout(new FlowLayout(FlowLayout.RIGHT));

        rp.add(m, BorderLayout.CENTER);
        wp.add(rp, BorderLayout.CENTER);
        p.add(wp);
        messageP.add(p);
    }

    public static void main(String[] args) {
        ChatroomClient c = new ChatroomClient("aaa", 1);
        c.makeReadyMadeMessages();
    }
}
