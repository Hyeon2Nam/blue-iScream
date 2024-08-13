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
    private JButton fileButton;
    private JLabel fileLabel;
    private BoardMain boardMain;
    private User currentUser;
    private Post post; // 기존 게시물 수정 모드인지 확인하기 위해 추가
    private String filePath = null;

    // 새 게시물 작성 시 사용하는 생성자
    public PostDetailFrame(BoardMain boardMain, User currentUser) {
        this.boardMain = boardMain;
        this.currentUser = currentUser;
        this.post = null; // 새 게시물 작성이므로 post는 null
        setTitle("새 게시물 작성");
        setSize(800, 600);
        initialize();
    }

    // 기존 게시물 수정 시 사용하는 생성자
    public PostDetailFrame(BoardMain boardMain, User currentUser, Post post) {
        this.boardMain = boardMain;
        this.currentUser = currentUser;
        this.post = post; // 수정할 게시물 정보를 받음
        setTitle("게시물 수정");
        setSize(800, 600);
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        // 제목 패널
        JPanel titlePanel = new JPanel(new BorderLayout());
        titleField = new JTextField();
        
        if (post != null) { // 수정 모드인 경우 기존 게시물의 제목을 설정
            titleField.setText(post.getTitle());
        }
        
        titlePanel.add(new JLabel("제목:"), BorderLayout.WEST);
        titlePanel.add(titleField, BorderLayout.CENTER);
        add(titlePanel, BorderLayout.NORTH);

        // 내용 패널
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentArea = new JTextArea();
        
        if (post != null) { // 수정 모드인 경우 기존 게시물의 내용을 설정
            contentArea.setText(post.getContent());
        }
        
        contentArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(contentArea);
        contentPanel.add(new JLabel("내용:"), BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);

        // 파일 선택 패널
        JPanel filePanel = new JPanel(new BorderLayout());
        fileButton = new JButton("파일 추가");
        fileLabel = new JLabel("선택된 파일 없음");
        filePanel.add(fileButton, BorderLayout.WEST);
        filePanel.add(fileLabel, BorderLayout.CENTER);
        add(filePanel, BorderLayout.SOUTH);

        // 파일 선택 버튼 리스너
        fileButton.addActionListener(e -> selectFile());

        // 버튼 패널
        JPanel buttonPanel = new JPanel();
        saveButton = new JButton("저장");
        cancelButton = new JButton("취소");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.PAGE_END);

        // 저장 버튼 리스너
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                savePost();
            }
        });

        // 취소 버튼 리스너
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    // 파일 선택 메서드
    private void selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            filePath = fileChooser.getSelectedFile().getAbsolutePath();
            fileLabel.setText(fileChooser.getSelectedFile().getName());
        }
    }

    private void savePost() {
        String title = titleField.getText();
        String content = contentArea.getText();
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        try {
            Integer fileId = null;
            if (filePath != null) {
                fileId = Dataconn.createFile(filePath);
            }
            
            if (post == null) {
                // 새 게시물 추가
                Dataconn.createPost(currentUser.getUserId(), 0, content, title, currentTimestamp, false, currentTimestamp, false);
                JOptionPane.showMessageDialog(this, "게시물 작성 완료!");
            } else {
                // 기존 게시물 수정
                Dataconn.updatePost(post.getPostId(), currentUser.getUserId(), post.getChatroomId(), content, title, post.getCreatedAt(), false, currentTimestamp, fileId, post.getIsNotice());
                JOptionPane.showMessageDialog(this, "게시물 수정 완료!");
            }
            boardMain.loadPosts();
            dispose();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "게시물 저장 실패: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
