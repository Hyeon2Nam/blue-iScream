package menu;

import chatRoom.Alram;
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
    public static Alram alram;

    public MainMenuView(String clientId) {
        this.clientId = clientId;
        mc = new MakeComponent();

        initializeComponents();
        alram = new Alram(clientId);
        setVisible(true);
    }

    public void initializeComponents() {
        setSize(TOTALWIDTH, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.white);
        setResizable(false);

        mainContentsP = new JPanel(new CardLayout());
        mainContentsP.setBackground(Color.white);

        mainContentsP.add(new ProfileList(clientId).makeNewProfilesList(), "profile");
        mainContentsP.add(new ChatRoomList(clientId).makeNewChatRoomList(), "chat");

        add(mainContentsP, BorderLayout.CENTER);

        // bottom (input field, emoji etc...)------------------------------

        DarkPanel lineP = new DarkPanel();
        lineP.setSize(TOTALWIDTH, 10);

        int size = 40;
        JPanel btnP = new JPanel();
        JPanel BtnWrapper = new JPanel();
        JButton profileBtn = mc.setIconButton("images/profileBtnIcon.png", size);
        JButton chatBtn = mc.setIconButton("images/chatBtnIcon.png", size);
        JButton settingBtn = mc.setIconButton("images/settingBtnIcon.png", size);

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

        profileBtn.addActionListener(e -> card.show(mainContentsP, "profile"));

        chatBtn.addActionListener(e -> card.show(mainContentsP, "chat"));
    }

    public static void main(String[] args) {
        new MainMenuView("111");
//        new MainMenuView("admin");
    }
}
