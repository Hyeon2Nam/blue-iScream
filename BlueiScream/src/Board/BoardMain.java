package Board;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class BoardMain extends JFrame {
    private JList<String> TitleList = new JList<>();
    private JList<String> DateList = new JList<>();
    private DefaultListModel<String> TitleModel = new DefaultListModel<>();
    private DefaultListModel<String> DateModel = new DefaultListModel<>();
    private List<Post> posts = new ArrayList<>();
    private int index;

    public BoardMain() {
        this.setTitle("게시판");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(getOwner());

        InitialScreen is = new InitialScreen();
        setContentPane(is);

        this.setResizable(false);
        this.setSize(500, 500);
        this.setVisible(true);
        
    }

    public void loadPosts() {
        try {
            posts = Dataconn.getAllPosts();
            TitleModel.clear();
            DateModel.clear();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (Post post : posts) {
                TitleModel.addElement(post.getTitle());
                DateModel.addElement(sdf.format(post.getCreatedAt()));
            }
            TitleList.setModel(TitleModel);
            DateList.setModel(DateModel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private class InitialScreen extends JPanel {
        private JButton WriteBtn, DeleteBtn, ModifyBtn;

        public InitialScreen() {
            this.setSize(500, 500);
            this.setLayout(null);
            this.setBackground(new Color(153, 204, 255));

            JLabel label = new JLabel("게시판");
            label.setBounds(20, 20, 450, 50);
            label.setFont(new Font("GOOGLE FONTS", Font.PLAIN, 20));
            label.setForeground(new Color(0, 0, 102));
            label.setHorizontalAlignment(JLabel.CENTER);
            add(label);

            TitleList.setBounds(20, 70, 300, 345);
            DateList.setBounds(320, 70, 150, 345);
            DateList.setEnabled(false);

            add(TitleList);
            add(DateList);

            WriteBtn = new JButton("추가");
            WriteBtn.setBounds(240, 430, 75, 25);
            ModifyBtn = new JButton("수정");
            ModifyBtn.setBounds(320, 430, 75, 25);
            DeleteBtn = new JButton("삭제");
            DeleteBtn.setBounds(400, 430, 70, 25);

            add(WriteBtn);
            add(ModifyBtn);
            add(DeleteBtn);

            loadPosts();

            WriteBtn.addActionListener(e -> {
                if (TitleModel.size() == 17) {
                    JOptionPane.showMessageDialog(null, "더 이상 글을 쓸 수 없습니다.", "Message", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                PostEditFrame s = new PostEditFrame(BoardMain.this);
                s.setVisible(true);
            });

            ModifyBtn.addActionListener(e -> {
                if (!TitleList.isSelectionEmpty()) {
                    index = TitleList.getSelectedIndex();
                    PostEditFrame s = new PostEditFrame(posts.get(index), BoardMain.this);
                    s.setVisible(true);
                }
            });

            DeleteBtn.addActionListener(e -> {
                if (!TitleList.isSelectionEmpty()) {
                    int choice = JOptionPane.showConfirmDialog(null, "정말로 삭제하시겠습니까?", "Delete", JOptionPane.OK_CANCEL_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        int selectedIndex = TitleList.getSelectedIndex();
                        try {
                            Dataconn.deletePost(posts.get(selectedIndex).getPostId());
                            posts.remove(selectedIndex);
                            TitleModel.remove(selectedIndex);
                            DateModel.remove(selectedIndex);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    public static void main(String[] args) {
        new BoardMain();
    }
}
