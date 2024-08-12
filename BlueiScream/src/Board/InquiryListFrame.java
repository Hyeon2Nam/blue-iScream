package Board;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

public class InquiryListFrame extends JFrame {
    private JList<String> inquiryList;
    private DefaultListModel<String> inquiryListModel;

    public InquiryListFrame() {
        setTitle("문의사항 목록");
        setSize(400, 300);
        setLocationRelativeTo(null);

        inquiryListModel = new DefaultListModel<>();
        inquiryList = new JList<>(inquiryListModel);

        JScrollPane scrollPane = new JScrollPane(inquiryList);
        add(scrollPane, BorderLayout.CENTER);

        loadInquiries(); // 문의사항 목록 로드

        inquiryList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = inquiryList.getSelectedIndex();
                    if (selectedIndex != -1) {
                        String selectedInquiry = inquiryListModel.getElementAt(selectedIndex);
                        JOptionPane.showMessageDialog(InquiryListFrame.this, selectedInquiry, "문의사항 상세", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        setVisible(true);
    }

    private void loadInquiries() {
        try {
            List<String> inquiries = Dataconn.getAllInquiries(); // 문의사항 목록 불러오기
            inquiryListModel.clear();
            for (String inquiry : inquiries) {
                inquiryListModel.addElement(inquiry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "문의사항을 불러오는 중 오류가 발생했습니다.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
