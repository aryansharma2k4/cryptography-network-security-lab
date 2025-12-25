import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            String key = "SECRETKEY"; 
            if(key.length() != 8) key = "12345678";

            Socket socket = new Socket("localhost", 5000);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            Scanner sc = new Scanner(System.in);

            System.out.print("Enter Plaintext Message: ");
            String plainText = sc.nextLine();

            String encryptedMsg = DES.encrypt(plainText, key);
            System.out.println("Encrypted ciphertext: " + encryptedMsg);

            dos.writeUTF(encryptedMsg);
            
            socket.close();
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}