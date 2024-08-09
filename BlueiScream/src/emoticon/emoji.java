package emoticon;

import javax.swing.*;
import chatRoom.ChatroomClient;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class emoji extends JFrame {

    private DefaultListModel<EmoticonItem> listModel;
    private ChatroomClient chatClient;

    public emoji(ChatroomClient client) {
        this.chatClient = client;
        initializeUI();
        loadEmoticons();
    }

    private void initializeUI() {
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        listModel = new DefaultListModel<>();
        JList<EmoticonItem> list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setCellRenderer(new EmoticonListCellRenderer());
        JScrollPane scrollPane = new JScrollPane(list);
        add(scrollPane, BorderLayout.CENTER);

        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                EmoticonItem selectedItem = list.getSelectedValue();
                if (selectedItem != null && chatClient != null) {
                    chatClient.sendMessageToChat(selectedItem.getImagePath());
                    dispose();
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void loadEmoticons() {
        File dir = new File("emojiimg");
        File[] files = dir.listFiles((d, name) -> name.endsWith(".png") || name.endsWith(".jpg"));
        if (files != null) {
            for (File file : files) {
                listModel.addElement(new EmoticonItem("", file.getAbsolutePath()));
            }
        }
    }

    class EmoticonItem {
        private String text;
        private String imagePath;

        public EmoticonItem(String text, String imagePath) {
            this.text = text;
            this.imagePath = imagePath;
        }

        public String getText() {
            return text;
        }

        public String getImagePath() {
            return imagePath;
        }
    }

    class EmoticonListCellRenderer extends JPanel implements ListCellRenderer<EmoticonItem> {
        private JLabel label;

        public EmoticonListCellRenderer() {
            setLayout(new BorderLayout());
            label = new JLabel();
            add(label, BorderLayout.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends EmoticonItem> list, EmoticonItem value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value != null) {
                label.setText(value.getText());
                ImageIcon icon = new ImageIcon(value.getImagePath());
                Image img = icon.getImage();
                Image resizedImage = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                ImageIcon resizedIcon = new ImageIcon(resizedImage);
                label.setIcon(resizedIcon);
            }
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            return this;
        }
    }
}
