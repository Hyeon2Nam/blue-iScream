package comments;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class CommentFrame extends JFrame {
    private JTextArea commentArea;
    private JButton addButton;
    private JList<String> commentList;
    private DefaultListModel<String> commentListModel;
    private int postId;

    public CommentFrame(int postId) {
        this.postId = postId;
        setTitle("댓글 관리");
        setSize(400, 400);
        setLayout(new BorderLayout());

        commentArea = new JTextArea(5, 20);
        addButton = new JButton("댓글 추가");
        commentListModel = new DefaultListModel<>();
        commentList = new JList<>(commentListModel);

        loadComments();

        add(new JScrollPane(commentList), BorderLayout.CENTER);
        add(commentArea, BorderLayout.SOUTH);
        add(addButton, BorderLayout.EAST);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addComment();
            }
        });

        setVisible(true);
    }

    private void loadComments() {
        try {
            List<Comment> comments = CDataconn.getCommentsForPost(postId);
            commentListModel.clear();
            for (Comment comment : comments) {
            	String displayText = comment.getUserName() + ": " + comment.getContent();
                commentListModel.addElement(displayText);
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addComment() {
        String content = commentArea.getText();
        if (content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "댓글 내용을 입력하세요.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        try {
            CDataconn.addComment(postId, 1, content, currentTimestamp); // userId는 실제 사용자 ID로 대체
            loadComments();
            commentArea.setText("");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "댓글 추가 실패: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
