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
import java.net.MulticastSocket;
import java.util.List;

public class ChatroomClient extends JFrame {
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
    private final Color BGC = Color.white;
    private JScrollPane scroll;
    private final int TOTALWIDTH = 400;
    private GridBagConstraints gbc;
    private int gy;

    public ChatroomClient(String clientId, int roomId) {
        super(clientId + "'s room");

        gy = 0;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = gy;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;

        this.clientId = clientId;
        this.roomId = roomId;

        dao = new ChatRoomDao();
        isFirst = true;

        setSize(TOTALWIDTH, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.white);

        // header----------------------------------------------------

        PinkPanel headerP = new PinkPanel();
        chatroomName = dao.getChatRoomName(roomId);
        JLabel titleLb = new JLabel(chatroomName);
        titleLb.setFont(new Font(titleLb.getFont().getFontName(), titleLb.getFont().getStyle(), 20));
        headerP.setSize(TOTALWIDTH, 70);
        headerP.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        headerP.add(titleLb);
        add(headerP, BorderLayout.NORTH);

        // message view --------------------------------------------------------

        messageP = new JPanel();
        messageP.setBackground(null);
        messageP.setLayout(new GridBagLayout());
        scroll = new JScrollPane(messageP);
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

                JScrollBar vertical = scroll.getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
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

        JScrollBar vertical = scroll.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    public void makeMessageView(String c, String id, String name) {
        if (messageP.getComponentCount() > 0)
            messageP.remove(messageP.getComponentCount() - 1);

        gbc.gridy = gy++;
        gbc.weighty = 0.0;

        JButton bb = new JButton(" ");
        JPanel pp = new JPanel();
        JPanel p = new JPanel();
        JPanel wp = new JPanel();
        JPanel rp = new JPanel();
        JLabel n = new JLabel(name);
        ColorRoundTextView m = new ColorRoundTextView(reformText(c), BGC, Color.BLACK);

        p.setBackground(null);
        wp.setBackground(null);
        rp.setBackground(null);
        pp.setBackground(null);

        rp.setLayout(new BorderLayout());
        p.setLayout(new FlowLayout(FlowLayout.LEFT));

        if (!id.equals(clientId)) {
            pp.setLayout(new FlowLayout(FlowLayout.LEFT));
            wp.add(bb, BorderLayout.WEST);
            rp.add(n, BorderLayout.NORTH);
        } else {
            pp.setLayout(new FlowLayout(FlowLayout.RIGHT));
        }

        rp.add(m, BorderLayout.CENTER);
        wp.add(rp, BorderLayout.CENTER);

        Dimension wpd = wp.getPreferredSize();
        Dimension rpd = rp.getPreferredSize();
        Dimension ppd = new Dimension(TOTALWIDTH,
                (int) p.getPreferredSize().getHeight());
        double tw = wpd.getWidth() + rpd.getWidth();
        double th = wpd.getWidth() + rpd.getWidth();
        Dimension td = new Dimension((int) tw, (int) th);

        p.add(wp);
        p.add(rp);
        p.setMaximumSize(td);
        pp.add(p);
        pp.setMaximumSize(ppd);
        pp.setMinimumSize(ppd);
        messageP.add(pp, gbc);

        gbc.gridy = gy;
        gbc.weighty = 1.0;
        messageP.add(Box.createVerticalGlue(), gbc);
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
        ChatroomClient c = new ChatroomClient("aaa", 1);
        c.makeReadyMadeMessages();
    }
}
