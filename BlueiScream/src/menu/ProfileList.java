package menu;

import chatRoom.ChatRoomDao;
import chatRoom.ChatRoomListMenu;
import chatRoom.ChatroomClient;
import components.ColorRoundButton;
import components.Header;
import login.User;
import login.UserDao;
import profile.MiniProfileView;
import profile.Profile;
import profile.ProfileDao;
import utils.MakeComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Blob;
import java.util.List;

public class ProfileList extends JFrame {
    private final int TOTALWIDTH = 380;
    private String clientId;
    private MakeComponent mc;
    private ProfileDao dao;
    private UserDao udao;
    private JPanel main;
    private JScrollPane scroll;
    private JPanel profileList;

    public ProfileList(String clientId) {
        this.clientId = clientId;
        mc = new MakeComponent();
        dao = new ProfileDao();
        udao = new UserDao();
    }

    public JPanel makeNewProfilesList() {
        main = new JPanel(new BorderLayout());

        initializeComponents();
        loadProfiles();

        return main;
    }

    public void initializeComponents() {
        dao = new ProfileDao();

        main.setSize(TOTALWIDTH, 800);
        main.setBackground(Color.white);

        createHeader();
        initProfileList();
    }

    private void initProfileList() {
        profileList = new JPanel();
        profileList.setBackground(Color.white);
        profileList.setLayout(new BoxLayout(profileList, BoxLayout.Y_AXIS));
        scroll = new JScrollPane(profileList);
        main.add(scroll, BorderLayout.CENTER);
        scroll.setBorder(BorderFactory.createEmptyBorder());
    }

    private void createHeader() {
        Header headerP = new Header("친구");
        main.add(headerP, BorderLayout.NORTH);
    }

    private void loadProfiles() {
        List<User> pfs = udao.getAllUsers();
        User me = udao.getUser(clientId);
        JPanel lbp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lb = new JLabel("친구" + pfs.size() + "명");

        makeProfileView(clientId, me.getUserName(), dao.getClientProfileImage(clientId));

        lb.setBorder(BorderFactory.createEmptyBorder(20, 10, 0, 0));
        lbp.setBackground(null);
        lbp.setMaximumSize(new Dimension(TOTALWIDTH, lb.getPreferredSize().height + 10));

        lbp.add(lb);
        profileList.add(lbp);

        for (User p : pfs) {
            if (p.getUserId().equals(clientId))
                continue;
            makeProfileView(p.getUserId(), p.getUserName(), dao.getClientProfileImage(p.getUserId()));
        }
    }

    private void makeProfileView(String id, String name, Blob img) {
        JPanel pp = new JPanel(new BorderLayout());
        JLabel imgLb = new JLabel();
        JLabel nameLb = new JLabel(clientId.equals(id) ? "나" : name);
        ImageIcon icon = mc.loadImage(id, 45);
        JButton btn = new ColorRoundButton("정지", new Color(0, 38, 66), Color.white, 13);

        imgLb.setIcon(icon);
        imgLb.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        nameLb.setFont(nameLb.getFont().deriveFont(15.0f));
        btn.setFocusable(false);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        pp.setBackground(Color.white);
        pp.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        pp.add(imgLb, BorderLayout.WEST);
        pp.add(nameLb, BorderLayout.CENTER);
        if (clientId.equals("admin") && !id.equals("admin"))
            pp.add(btn, BorderLayout.EAST);

        pp.setMaximumSize(new Dimension(TOTALWIDTH, (int) pp.getPreferredSize().getHeight()));

        profileList.add(pp);

        // ------------------[event]--------------------

        pp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                pp.setBackground(pp.getBackground().darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                pp.setBackground(Color.white);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                new MiniProfileView(id, id.equals(clientId));
            }
        });

        btn.addActionListener(e -> {
            JDialog d = new JDialog();
            d.add(new Label("작업중......"));
            d.setVisible(true);

            int res = udao.deleteUser(id);
            d.dispose();

            if (res <= 0)
                JOptionPane.showMessageDialog(this, "정지실패");
            else
                JOptionPane.showMessageDialog(this, "정지완료");
        });
    }
}
