package chatRoom;

import components.ColorRoundButton;
import components.DarkPanel;
import components.Header;
import components.UnderLineTextField;
import login.User;
import login.UserDao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.util.*;
import java.util.List;

public class CreateChatRoom extends JFrame {
    private ChatRoomDao chatDao;
    private UserDao userDao;
    private final int TOTALWIDTH = 400;
    private String clientId;
    private JPanel centerP;
    private Set<String> selectedItems;
    UnderLineTextField titleField;

    public CreateChatRoom(String clientId) {
        this.clientId = clientId;
        userDao = new UserDao();
        chatDao = new ChatRoomDao();

        setSize(TOTALWIDTH, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        centerP = new JPanel(new BorderLayout());

        createHeader();
        createUserList();

        JPanel buttonP = new JPanel();
        buttonP.setBackground(Color.white);
        ColorRoundButton submit = new ColorRoundButton("만들기", new Color(229, 149, 0), Color.white, 30);
        buttonP.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        buttonP.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonP.add(submit);

        add(buttonP, BorderLayout.SOUTH);

        setVisible(true);

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int size = selectedItems.size();
                int category = 1;

                if (size < 0 || titleField.getText().isEmpty())
                    return;
                else if (size > 1)
                    category = 2;

                selectedItems.add(clientId);

                Timestamp ts = new Timestamp(System.currentTimeMillis());
                chatDao.createNewChatRoom(titleField.getText(), category, ts);

                int roomId = chatDao.getChatRoomId();
                chatDao.insertRoomAndUserInfo(roomId, selectedItems, ts);

                dispose();
            }
        });
    }

    private void createHeader() {
        Header header = new Header("채팅방 생성");
        add(header, BorderLayout.NORTH);

        JPanel titleP = new JPanel(new BorderLayout());
        titleP.setBackground(Color.white);
        JLabel titleLb = new JLabel("채팅방 이름");
        JLabel listLb = new JLabel("추가할 멤버");
        titleField = new UnderLineTextField(Color.gray, Color.BLACK);

        titleLb.setForeground(Color.gray);
        listLb.setForeground(Color.gray);
        listLb.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        titleField.setPreferredSize(new Dimension(TOTALWIDTH - 100, 50));
        titleP.setMaximumSize(new Dimension(TOTALWIDTH, 100));

        titleP.add(titleLb, BorderLayout.NORTH);
        titleP.add(titleField, BorderLayout.CENTER);
        titleP.add(listLb, BorderLayout.SOUTH);

        centerP.add(titleP, BorderLayout.NORTH);
    }

    private void createUserList() {
        DefaultListModel<JPanel> listModel = makeRoomList();
        JList<JPanel> panelList = new JList<>(listModel);

        panelList.setCellRenderer(new PanelListRenderer());
        panelList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scroll = new JScrollPane(panelList);

        centerP.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
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

                    Component labelComp = panel.getComponent(1);
                    String labelText = "";
                    if (labelComp instanceof JLabel) {
                        JLabel label = (JLabel) labelComp;
                        labelText = label.getText();
                    }

                    Component checkBoxComp = panel.getComponent(2);
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
        List<User> users = userDao.getAllUsers();

        for (User u : users) {
            if (u.getUserId().equals(clientId))
                continue;

            JPanel p = new JPanel(new BorderLayout());
            JButton b = new JButton("");
            JLabel lb = new JLabel(u.getUserName());
            JCheckBox cb = new JCheckBox();

            p.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
            lb.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 5));
            cb.setBackground(null);

            p.add(b, BorderLayout.WEST);
            p.add(lb, BorderLayout.CENTER);
            p.add(cb, BorderLayout.EAST);

            model.addElement(p);
        }

        return model;
    }

    public static void main(String[] args) {
        new CreateChatRoom("aaa");
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
