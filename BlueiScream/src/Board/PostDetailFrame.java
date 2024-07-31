package Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.sql.Timestamp;

public class PostDetailFrame extends JFrame {
    private JTextField titleField;
    private JTextArea contentArea;
    private JButton saveButton;
    private JButton cancelButton;
    private Post post;
    private BoardMain boardMain;

    public PostDetailFrame(Post post, BoardMain boardMain) {
        this.post = post;
        this.boardMain = boardMain;
        setTitle("Edit Post");
        initialize();
    }

    private void initialize() {
        setSize(500, 500);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        titleField = new JTextField(post.getTitle());
        contentArea = new JTextArea(post.getContent());
        contentArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(contentArea);

        saveButton = new JButton("저장");
        cancelButton = new JButton("취소");

        panel.add(new JLabel("제목:"+ Font.PLAIN, 20));
        panel.add(titleField);
        panel.add(new JLabel("내용:"));
        panel.add(scrollPane);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                savePost();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void savePost() {
        String newTitle = titleField.getText();
        String newContent = contentArea.getText();
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        try {
            // Update the post with the new data
            Dataconn.updatePost(post.getPostId(), post.getUserId(), post.getChatroomId(), newContent, newTitle, new Timestamp(post.getCreatedAt().getTime()), post.isDelete(), currentTimestamp, Integer.parseInt(post.getFile()), post.isNotice());
            JOptionPane.showMessageDialog(this, "게시물 업데이트 완료!!");
            boardMain.loadPosts();
            dispose();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "게시물 업데이트 실패: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
