package menu;

import chatRoom.ChatRoomDao;
import chatRoom.ChatRoomListMenu;
import components.DarkPanel;
import components.Header;
import utils.MakeComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuView extends JFrame {
    private String clientId;
    private final int TOTALWIDTH = 400;
    private MakeComponent mc;
    private JPanel mainContentsP;

    public MainMenuView(String clientId) {
        this.clientId = clientId;
        mc = new MakeComponent();

        initializeComponents();
        setVisible(true);
    }

    public void initializeComponents() {
        setSize(TOTALWIDTH, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.white);
        setResizable(false);

        mainContentsP = new JPanel(new CardLayout());
        mainContentsP.setBackground(Color.white);

        mainContentsP.add(new ChatRoomList(clientId).makeNewChatRoomList(), "chat");
        mainContentsP.add(new ProfileList(clientId).makeNewProfilesList(), "profile");

        add(mainContentsP, BorderLayout.CENTER);

        // bottom (input field, emoji etc...)------------------------------

        DarkPanel lineP = new DarkPanel();
        lineP.setSize(TOTALWIDTH, 10);

        int size = 40;
        JPanel btnP = new JPanel();
        JPanel BtnWrapper = new JPanel();
        JButton profileBtn = mc.setIconButton("BlueiScream/images/profileBtnIcon.png", size);
        JButton chatBtn = mc.setIconButton("BlueiScream/images/chatBtnIcon.png", size);
        JButton settingBtn = mc.setIconButton("BlueiScream/images/settingBtnIcon.png", size);

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

        CardLayout card = (CardLayout) mainContentsP.getLayout();

        profileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(mainContentsP, "profile");
            }
        });

        chatBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(mainContentsP, "chat");
            }
        });
    }

    public static void main(String[] args) {
        new MainMenuView("qqq");
    }
}
