package Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.sql.Timestamp;

public class PostEditFrame extends JFrame {
    private JTextField titleField;
    private JTextArea contentArea;
    private JButton saveButton;
    private JButton cancelButton;
    private Post post;
    private BoardMain boardMain;

    public PostEditFrame(BoardMain boardMain) {
        this.boardMain = boardMain;
        setTitle("새 게시물");
        initialize();
    }

    public PostEditFrame(Post post, BoardMain boardMain) {
        this.post = post;
        this.boardMain = boardMain;
        setTitle("게시물 수정");
        initialize();
    }

    private void initialize() {
        setSize(500, 500);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        titleField = new JTextField();
        contentArea = new JTextArea();
        contentArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(contentArea);

        if (post != null) {
            titleField.setText(post.getTitle());
            contentArea.setText(post.getContent());
        }

        saveButton = new JButton("저장");
        cancelButton = new JButton("취소");

        panel.add(new JLabel("제목:"));
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
            // 파일 생성 (file_path를 필요에 맞게 수정)
            int fileId = Dataconn.createFile("default_file_path");

            if (post == null) {
                // New post
                Dataconn.createPost(1, 1, newContent, newTitle, currentTimestamp, false, currentTimestamp, fileId, false);
                JOptionPane.showMessageDialog(this, "Post created successfully!");
            } else {
                // Edit existing post
                Dataconn.updatePost(post.getPostId(), post.getUserId(), post.getChatroomId(), newContent, newTitle, new Timestamp(post.getCreatedAt().getTime()), post.isDelete(), currentTimestamp, fileId, post.isNotice());
                JOptionPane.showMessageDialog(this, "Post updated successfully!");
            }

            boardMain.loadPosts();
            dispose();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving post: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
