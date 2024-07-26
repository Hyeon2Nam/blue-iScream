//package chatRoom;
//
//import javax.swing.*;
//import java.awt.*;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
//import java.io.*;
//import java.net.*;
//
//public class DrawingClient extends JFrame {
//	private DrawingPanel drawingPanel;
//	private JList<String> userList;
//	private JList<String> chatList;
//	private DefaultListModel<String> userListModel;
//	private DefaultListModel<String> chatListModel;
//	private Socket socket;
//	private BufferedReader reader;
//	private PrintWriter writer;
//	private ObjectOutputStream oos;
//	private ObjectInputStream ois;
//
//	public DrawingClient() {
//		super("Network Drawing Client");
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setSize(800, 600); // Adjust the size to accommodate the lists
//		setLayout(new BorderLayout());
//
//		initializeComponents();
//		setupNetworking();
//		setVisible(true);
//	}
//
//	private void initializeComponents() {
//		// Initialize the drawing panel
//		drawingPanel = new DrawingPanel();
//		add(drawingPanel, BorderLayout.CENTER);
//
//		// User list with a model
//		userListModel = new DefaultListModel<>();
//		userList = new JList<>(userListModel);
//		userList.setBorder(BorderFactory.createTitledBorder("Users"));
//
//		// Chat list with a model
//		chatListModel = new DefaultListModel<>();
//		chatList = new JList<>(chatListModel);
//		chatList.setBorder(BorderFactory.createTitledBorder("Chat"));
//
//		// Create a panel to hold both lists
//		JPanel listPanel = new JPanel(new GridBagLayout());
//		GridBagConstraints gbc = new GridBagConstraints();
//		// 좌측 컴포넌트 (넓게)
//		gbc.gridx = 0;
//		gbc.gridy = 0;
//		gbc.gridwidth = 1; // 좌측 컴포넌트가 차지하는 열의 수
//		gbc.gridheight = 2; // 두 행 모두 차지
//		gbc.fill = GridBagConstraints.BOTH;
//		gbc.weightx = 0.5;
//		gbc.weighty = 1; // 높이도 확장
//		gbc.insets = new Insets(10, 10, 10, 5);
//
//		// 우측 상단 컴포넌트
//		gbc.gridx = 1; // 두 번째 열
//		gbc.gridy = 0; // 첫 번째 행
//		gbc.gridheight = 1; // 행 하나 차지
//		gbc.insets = new Insets(10, 0, 25, 10);
//		listPanel.add(new JScrollPane(userList), gbc);
//
//		// 우측 하단 컴포넌트
//		gbc.gridx = 1; // 두 번째 열
//		gbc.gridy = 1; // 두 번째 행
//		gbc.insets = new Insets(5, 5, 10, 10);
//		listPanel.add(new JScrollPane(chatList), gbc);
//
//		JTextField bottomRightField = new JTextField("Bottom Right Field");
//		bottomRightField.setPreferredSize(new Dimension(bottomRightField.getPreferredSize().width, 1)); // 선호 크기로 높이 설정
//
//		gbc.gridx = 1;
//		gbc.gridy = 2;
//		gbc.insets = new Insets(5, 5, 10, 10);
//		listPanel.add(bottomRightField, gbc);
//
//		// Add the list panel to the east
//		add(listPanel, BorderLayout.EAST);
//
//		// ActionListener 추가
//		bottomRightField.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				String text = bottomRightField.getText();
//				System.out.println("chat: " + text); // 콘솔에 텍스트 출력
//				//DrawingClient.this.writer.println("chat: "+text);
//
//				try {
//					DataPost dp = new DataPost();
////					dp.setChat("chat: " + text);text
//					oos.writeObject(dp);
//					oos.flush();
//				} catch(IOException e1) {
//					System.out.println(e1.getMessage());
//				}
//				bottomRightField.setText(""); // 메시지 전송 후 텍스트 필드 클리어
//			}
//		});
//	}
//
//	private void setupNetworking() {
//		try {
//			socket = new Socket("localhost", 5000); // Connect to the server
//			//reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//			//writer = new PrintWriter(socket.getOutputStream(), true);
//			oos = new ObjectOutputStream(socket.getOutputStream());
//			oos.flush();
//			// Listening for messages from the server
//			Thread listenerThread = new Thread(this::listenForMessages);
//			listenerThread.start();
//		} catch (IOException e) {
//			System.out.println("Unable to connect to the server.");
//			e.printStackTrace();
//		}
//	}
//
//	private void listenForMessages() {
//		try {
//			// 객체를 읽어서 적절한 타입으로 캐스팅합니다.
//			ois = new ObjectInputStream(socket.getInputStream());
//			DataPost receivedDataPost;
//			//while ((line = reader.readLine()) != null) {
//			//String line = receivedDataPost.getChat();
//			//String line;
//			//null 체크하기
//            while ((receivedDataPost = (DataPost) ois.readObject()) != null) {
//			    if (receivedDataPost.getChat().startsWith("draw:")) {
//			        String[] parts = receivedDataPost.getChat().substring(5).split(",");
//			        int x1 = Integer.parseInt(parts[0]);
//			        int y1 = Integer.parseInt(parts[1]);
//			        int x2 = Integer.parseInt(parts[2]);
//			        int y2 = Integer.parseInt(parts[3]);
//			        SwingUtilities.invokeLater(() -> drawingPanel.drawLine(x1, y1, x2, y2));
//			    } else if (receivedDataPost.getChat().startsWith("user:")) {
//			        String userName = receivedDataPost.getChat().substring(5);
//			        SwingUtilities.invokeLater(() -> userListModel.addElement(userName));
//			    } else if (receivedDataPost.getChat().startsWith("chat:")) {
//			        String chatMessage = receivedDataPost.getChat().substring(5);
//			        SwingUtilities.invokeLater(() -> chatListModel.addElement(chatMessage));
//			    }
//			}
//		} catch (IOException e) {
//			System.err.println("Error reading from the server: " + e.getMessage());
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			System.out.println("Close not found: "+e.getMessage());
//        }
//    }
//
//	class DrawingPanel extends JPanel {
//		private int prevX, prevY;
//
//		public DrawingPanel() {
//			setPreferredSize(new Dimension(400, 400));
//			setBackground(Color.WHITE);
//			addMouseListener(new MouseAdapter() {
//				@Override
//				public void mousePressed(MouseEvent e) {
//					prevX = e.getX();
//					prevY = e.getY();
//				}
//			});
//			addMouseMotionListener(new MouseMotionAdapter() {
//				@Override
//				public void mouseDragged(MouseEvent e) {
//					int x = e.getX();
//					int y = e.getY();
//					//writer.println("draw:" + prevX + "," + prevY + "," + x + "," + y); // Send line data to server
//					DataPost dp = new DataPost();
//					dp.setChat("draw:" + prevX + "," + prevY + "," + x + "," + y);
//					dp.setImg("test");
//					try {
//						oos.writeObject(dp);
//						oos.flush();
//					} catch (IOException ex) {
//						throw new RuntimeException(ex);
//					}
//					drawLine(prevX, prevY, x, y);
//					prevX = x;
//					prevY = y;
//				}
//			});
//		}
//
//		void drawLine(int x1, int y1, int x2, int y2) {
//			Graphics g = getGraphics();
//			g.drawLine(x1, y1, x2, y2);
//		}
//	}
//
//	public static void main(String[] args) {
//		SwingUtilities.invokeLater(DrawingClient::new);
//	}
//}
