package Board;

import comments.Comment;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CommentViewFrame extends JFrame {
    private DefaultListModel<String> commentListModel;
    private JList<String> commentList;

    public CommentViewFrame(List<Comment> comments) {
        setTitle("댓글 목록");
        setSize(400, 300);
        setLayout(new BorderLayout());

        // 댓글 모델 초기화
        commentListModel = new DefaultListModel<>();
        for (Comment comment : comments) {
            if (!comment.isDeleted()) {
                String displayText = comment.getUserName() + ": " + comment.getContent();
                commentListModel.addElement(displayText);
            }
        }

        // JList와 스크롤 패널 설정
        commentList = new JList<>(commentListModel);
        JScrollPane commentScrollPane = new JScrollPane(commentList);
        add(commentScrollPane, BorderLayout.CENTER);

        // 프레임 표시
        setVisible(true);
    }
}
