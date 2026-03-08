import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        int port = 65432;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port + "...");
            
            try (Socket socket = serverSocket.accept();
                 ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
                
                System.out.println("Client connected!");
                long[] words = (long[]) ois.readObject();
                
                System.out.println("\n--- Received SHA-512 Message Schedule (W0 - W15) ---");
                for (int i = 0; i < words.length; i++) {
                    System.out.printf("W%-2d: 0x%016x%n", i, words[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
