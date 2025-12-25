import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        try {
            String key = "SECRETKEY"; 
            if(key.length() != 8) key = "12345678"; 

            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Server Waiting for connection...");

            Socket socket = serverSocket.accept();
            System.out.println("Client Connected.");

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String encryptedMsg = dis.readUTF();

            System.out.println("Encrypted Message received: " + encryptedMsg);
            
            String decryptedMsg = DES.decrypt(encryptedMsg, key);
            System.out.println("Decrypted Original Message: " + decryptedMsg);

            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}