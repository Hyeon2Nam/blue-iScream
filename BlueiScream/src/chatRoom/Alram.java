package chatRoom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Alram extends JFrame {
    private static Alram alram;

    private String clientId;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private ChatRoomDao dao;
    private int activeRoom;
    private List<Integer> alramOffRooms;

    public Alram(String client) {
        if (alram == null) {
            alram = this;
        }

        this.clientId = client;
        dao = new ChatRoomDao();

        setAlramOffRooms();
        setupNetwirking();
    }

    private void setupNetwirking() {
        try {
            socket = new Socket("114.70.127.232", 5000);
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();

            Thread listenerThread = new Thread(this::listenForMessages);
            listenerThread.start();
        } catch (IOException e) {
            System.out.println("서버와 연결할 수 없습니다.");
            e.printStackTrace();
        }
    }

    private void listenForMessages() {
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            DataPost receivedDataPost;

            while ((receivedDataPost = (DataPost) ois.readObject()) != null) {
                String[] chat = receivedDataPost.getChat();
                int msgId;
                String user = chat[0];
                int roomId = Integer.parseInt(chat[2]);

                if (activeRoom == roomId || user.equals(clientId) || alramOffRooms.contains(roomId))
                    continue;

                makeAlram(user, dao.getChatRoomName(roomId), chat[1], 2000);
            }
        } catch (IOException e) {
            System.err.println("Error reading from the server: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Close not found: " + e.getMessage());
        }
    }

    public void setAlramOffRooms() {
        alramOffRooms = dao.getAlramOffRooms(clientId);
    }

    public void setActiveRoom(int roomId) {
        this.activeRoom = roomId;
    }

    public static void makeAlram(String id, String roomName, String msg, int duration) {
        // JDialog 생성
        JDialog dialog = new JDialog();
        dialog.setTitle(roomName);
        dialog.setSize(300, 150); // 원하는 크기로 설정
        dialog.setLayout(new BorderLayout());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // 메시지 레이블 생성
        JLabel label = new JLabel("[" + id + "] : " + msg, SwingConstants.CENTER);
        dialog.add(label, BorderLayout.CENTER);

        // 화면 사이즈 가져오기
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // 다이얼로그 사이즈 가져오기
        Dimension dialogSize = dialog.getSize();

        // 우측 하단 모서리에 다이얼로그 위치 설정
        int x = screenSize.width - dialogSize.width;
        int y = screenSize.height - dialogSize.height;
        dialog.setLocation(x, y);

        // 타이머 설정: duration 밀리초 후에 알림창 닫기
        Timer timer = new Timer(duration, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        timer.setRepeats(false); // 타이머가 한 번만 실행되도록 설정
        timer.start(); // 타이머 시작

        dialog.setVisible(true); // 알림창 표시
    }
}
