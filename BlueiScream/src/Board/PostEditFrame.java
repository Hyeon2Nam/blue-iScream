package Board;

import javax.swing.*;
import comments.CDataconn;
import comments.Comment;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class PostEditFrame extends JFrame {
    private JTextField titleField;
    private JTextArea contentArea;
    private Post post;
    private BoardMain boardMain;
    private String writerId;
    private String clientId;

    // 댓글 관련 필드 추가
    private DefaultListModel<String> commentListModel;
    private JList<String> commentList;
    private JButton addCommentButton;
    private JButton viewCommentsButton; // 댓글 확인 버튼

    public PostEditFrame(Post post, BoardMain boardMain) {
        this.post = post;
        this.boardMain = boardMain;
        this.writerId = writerId;
        this.clientId = clientId;
        setTitle("게시물 상세보기");
        setSize(800, 600);
        initialize();
    }


	private void initialize() {
		setSize(800, 600);
        setLayout(new BorderLayout());

        // 제목 패널
        JPanel titlePanel = new JPanel(new BorderLayout());
        titleField = new JTextField(post.getTitle());
        titleField.setEditable(false); // 처음에는 수정 불가
        titlePanel.add(new JLabel("제목:"), BorderLayout.WEST);
        titlePanel.add(titleField, BorderLayout.CENTER);
        add(titlePanel, BorderLayout.NORTH);

        // 내용 패널
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentArea = new JTextArea(post.getContent());
        contentArea.setLineWrap(true);
        contentArea.setEditable(false); // 처음에는 수정 불가
        JScrollPane scrollPane = new JScrollPane(contentArea);
        contentPanel.add(new JLabel("내용:"), BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);

        // 댓글 패널
        JPanel commentPanel = new JPanel(new BorderLayout());
        commentListModel = new DefaultListModel<>();
        commentList = new JList<>(commentListModel);
        JScrollPane commentScrollPane = new JScrollPane(commentList);
        commentPanel.add(new JLabel("댓글:"), BorderLayout.NORTH);
        commentPanel.add(commentScrollPane, BorderLayout.CENTER);
        add(commentPanel, BorderLayout.SOUTH);

        // 버튼 패널
        JPanel buttonPanel = new JPanel();

        // 댓글 쓰기 버튼 추가
        addCommentButton = new JButton("댓글 쓰기");
        addCommentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCommentFrame();
            }
        });
        buttonPanel.add(addCommentButton);

        // 댓글 확인 버튼 추가
        viewCommentsButton = new JButton("댓글 확인");
        viewCommentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewComments();
            }
        });
        buttonPanel.add(viewCommentsButton);

        add(buttonPanel, BorderLayout.PAGE_END);

        loadComments(); // 댓글 로드
        setVisible(true);
    }

    // 게시물 작성자 확인 메서드
    private boolean isUserPostOwner() {
        // 여기에 현재 로그인한 사용자의 ID와 post.getUserId()를 비교하는 로직 추가 // 예시: return currentUserId.equals(post.getUserId());
        return true; // 이 부분을 실제 로직으로 변경
    }

    // 수정 모드 활성화 메서드
    private void enableEditing() {
        titleField.setEditable(true);
        contentArea.setEditable(true);
    }

    // 댓글을 데이터베이스에서 불러오는 메서드
    private void loadComments() {
        try {
            List<Comment> comments = CDataconn.getCommentsForPost(post.getPostId());
            
            for (Comment comment : comments) {
                if (!comment.isDeleted()) {  
                    String displayText = comment.getUserName() + ": " + comment.getContent();
                    commentListModel.addElement(displayText);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // 댓글 확인을 위한 새로운 창 열기
    private void viewComments() {
        // 댓글을 로드하여 commentListModel을 갱신
        loadComments();
        commentList.setModel(commentListModel);  // 댓글 목록 갱신
        commentList.revalidate();  // UI 갱신
        commentList.repaint();  // UI 갱신
        setVisible(true);  // 현재 창을 다시 표시하여 변경 사항을 반영
        
        // 댓글을 표시하기 위한 새로운 프레임 생성
        JFrame commentViewFrame = new JFrame("댓글 확인");
        commentViewFrame.setSize(400, 300);
        commentViewFrame.setLayout(new BorderLayout());

        // commentListModel로 JList 생성
        JList<String> commentViewList = new JList<>(commentListModel);
        JScrollPane commentViewScrollPane = new JScrollPane(commentViewList);

        commentViewFrame.add(commentViewScrollPane, BorderLayout.CENTER);

        // 프레임 표시
        commentViewFrame.setVisible(true);
    }


    private void openCommentFrame() {
        JFrame commentFrame = new JFrame("댓글 쓰기");
        commentFrame.setSize(400, 300);
        commentFrame.setLayout(new BorderLayout());

        JTextArea commentArea = new JTextArea();
        JButton submitButton = new JButton("댓글 달기");

        commentFrame.add(new JScrollPane(commentArea), BorderLayout.CENTER);
        commentFrame.add(submitButton, BorderLayout.SOUTH);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String content = commentArea.getText();
                if (content.isEmpty()) {
                    JOptionPane.showMessageDialog(commentFrame, "댓글 내용을 입력하세요.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
                try {
                    CDataconn.addComment(post.getPostId(), 1, content, currentTimestamp); // userId는 실제 사용자 ID로 대체
                    loadComments();
                    commentFrame.dispose();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(commentFrame, "댓글 추가 실패: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        commentFrame.setVisible(true);
    }

    private void savePost() {
        String newTitle = titleField.getText();
        String newContent = contentArea.getText();
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        // 파일 ID가 비어있을 경우 null로 설정
        Integer fileId = (post.getFile() != null && !post.getFile().isEmpty()) ? Integer.parseInt(post.getFile()) : null;

        // 게시물 업데이트
        Dataconn.updatePost(post.getPostId(), post.getUserId(), post.getChatroomId(), newContent, newTitle,
                            new Timestamp(post.getCreatedAt().getTime()), post.isDelete(), currentTimestamp,
                            fileId, post.getIsNotice());
        JOptionPane.showMessageDialog(this, "게시물 업데이트 완료!");
        boardMain.loadPosts(true);
        dispose();
    }

}
