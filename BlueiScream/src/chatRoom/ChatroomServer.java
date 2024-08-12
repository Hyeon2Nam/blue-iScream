package chatRoom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatroomServer {
    private static ConcurrentHashMap<Socket, ObjectOutputStream> clients = new ConcurrentHashMap<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(20);

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("server is running...");

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                oos.flush();
                clients.put(clientSocket, oos);
                pool.execute(new ChatroomServer.ClientHandler(clientSocket, oos));
            } catch (IOException e) {
                System.out.println("Error accepting connection: " + e.getMessage());
            }
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private ObjectInputStream ois;
        private ObjectOutputStream oos;

        ClientHandler(Socket socket, ObjectOutputStream oos) {
            this.socket = socket;
            this.oos = oos;
            try {
                this.ois = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                System.out.println("Error setting up stream reader: " + e.getMessage());
            }
        }

        @Override
        public void run() {
            try {
                Object inputObject;
                while ((inputObject = ois.readObject()) != null) {
                    DataPost dp = (DataPost) inputObject;
                    if (inputObject instanceof DataPost) {
                        DataPost receivedData = (DataPost) inputObject;
                        broadcast(receivedData);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error reading from client: " + e.getMessage());
            } finally {
                clients.remove(socket);
                try {
                    ois.close();
                    oos.close();
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Error closing resources: " + e.getMessage());
                }
            }
        }



        private void broadcast(DataPost data) throws IOException {
            for (ObjectOutputStream client : clients.values()) {
                client.writeObject(data);
                client.flush();
            }
        }
    }
}
