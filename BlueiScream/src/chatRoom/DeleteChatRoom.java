package chatRoom;

import components.ColorRoundButton;
import components.Header;
import login.UserDao;
import utils.MakeComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeleteChatRoom extends JFrame {
    private ChatRoomDao chatDao;
    private final int TOTALWIDTH = 400;
    private String clientId;
    private JPanel centerP;
    private Set<String> selectedItems;
    private MakeComponent mc;

    public DeleteChatRoom(String clientId) {
        this.clientId = clientId;
        chatDao = new ChatRoomDao();
        mc = new MakeComponent();

        setSize(TOTALWIDTH, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        centerP = new JPanel(new BorderLayout());

        createHeader();
        createChatRoomList();

        JPanel buttonP = new JPanel();
        buttonP.setBackground(Color.white);
        ColorRoundButton submit = new ColorRoundButton("나가기", new Color(229, 149, 0), Color.white, 30);
        buttonP.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        buttonP.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonP.add(submit);

        add(buttonP, BorderLayout.SOUTH);

        setVisible(true);

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int size = selectedItems.size();

                if (size < 0)
                    return;

                chatDao.deleteRoom(selectedItems, clientId);
                dispose();
            }
        });
    }

    private void createHeader() {
        Header header = new Header("채팅방 나가기");
        add(header, BorderLayout.NORTH);
    }

    private void createChatRoomList() {
        DefaultListModel<JPanel> listModel = makeRoomList();
        JList<JPanel> panelList = new JList<>(listModel);

        panelList.setCellRenderer(new CreateChatRoom.PanelListRenderer());
        panelList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scroll = new JScrollPane(panelList);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        centerP.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerP.setBackground(Color.white);
        centerP.add(scroll, BorderLayout.CENTER);

        add(centerP, BorderLayout.CENTER);

        selectedItems = new HashSet<>();
        panelList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = panelList.locationToIndex(e.getPoint());
                if (index != -1) {
                    JPanel panel = listModel.getElementAt(index);

                    Component labelComp = panel.getComponent(0);
                    String labelText = "";
                    if (labelComp instanceof JLabel) {
                        JLabel label = (JLabel) labelComp;
                        labelText = label.getText();
                    }

                    Component checkBoxComp = panel.getComponent(3);
                    if (checkBoxComp instanceof JCheckBox) {
                        JCheckBox checkBox = (JCheckBox) checkBoxComp;
                        boolean isSelected = !checkBox.isSelected();
                        checkBox.setSelected(isSelected);

                        if (isSelected)
                            selectedItems.add(labelText);
                        else
                            selectedItems.remove(labelText);

                        panelList.repaint(panelList.getCellBounds(index, index));
                    }
                }
            }
        });
    }

    private DefaultListModel<JPanel> makeRoomList() {
        DefaultListModel<JPanel> model = new DefaultListModel();
        List<ChatRoom> rooms = chatDao.getChatRoomList(clientId);

        for (ChatRoom u : rooms) {
            JPanel p = new JPanel(new BorderLayout());
            JButton b = new JButton(mc.resizeIcon("images/defaultRoomIcon.png", 40));
            JLabel lb = new JLabel(u.getChatroomName());
            JLabel id = new JLabel(String.valueOf(u.getChatroomId()));
            JCheckBox cb = new JCheckBox();

            b.setFocusable(false);
            b.setBackground(Color.white);
            b.setBorder(BorderFactory.createEmptyBorder());

            b.setBackground(Color.white);
            id.setForeground(new Color(0, 0, 0, 0));
            p.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 5));
            lb.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 5));
            cb.setBackground(null);

            p.add(id, BorderLayout.NORTH);
            p.add(b, BorderLayout.WEST);
            p.add(lb, BorderLayout.CENTER);
            p.add(cb, BorderLayout.EAST);

            model.addElement(p);
        }

        return model;
    }

    public static void main(String[] args) {
        new DeleteChatRoom("aaa");
    }

    static class PanelListRenderer implements ListCellRenderer<JPanel> {
        @Override
        public Component getListCellRendererComponent(
                JList<? extends JPanel> list, JPanel value,
                int index, boolean isSelected, boolean cellHasFocus) {
            value.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            value.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            return value;
        }
    }
}
