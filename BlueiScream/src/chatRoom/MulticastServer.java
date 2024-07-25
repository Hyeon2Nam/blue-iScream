package chatRoom;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastServer {
    private MulticastSocket socket;
    private InetAddress group;
    private static final int PORT = 5000;
    private static final String HOST = "239.255.255.250"; // 멀티캐스트 주소

    public MulticastServer() {
        try {
            group = InetAddress.getByName(HOST);
            socket = new MulticastSocket(PORT);
            socket.setTimeToLive(0); // 같은 로컬 네트워크 내에서만
            socket.joinGroup(group);
            System.out.println("Multicast Server running...");

            while (true) {
                byte[] buffer = new byte[256];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received from client: " + received);

                // 여기에서 멀티캐스트 그룹에 다시 전송하거나 기타 작업을 수행할 수 있습니다.
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.leaveGroup(group);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new MulticastServer();
    }
}