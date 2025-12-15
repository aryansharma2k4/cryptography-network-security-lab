import java.io.*;
import java.net.*;

public class Server {
    private static final int PORT = 5000;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Waiting for clients on port " + PORT + "...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected.");
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        ) {
            while (true) {
                String request = input.readUTF();
                if (request.equals("EXIT")) {
                    System.out.println("Client sent EXIT command.");
                    break;
                }

                String[] parts = request.split("::");
                if (parts.length < 3) {
                    output.writeUTF("Error: Invalid format");
                    continue;
                }

                String type = parts[0];
                String key = parts[1];
                String ciphertext = parts[2];
                String decryptedText = "";

                System.out.println("\n[" + type + "] Received Ciphertext: " + ciphertext);

                switch (type) {
                    case "CAESAR": decryptedText = CryptoUtils.decryptCaesar(ciphertext, key); break;
                    case "MONO": decryptedText = CryptoUtils.decryptMono(ciphertext, key); break;
                    case "PLAYFAIR": decryptedText = CryptoUtils.decryptPlayfair(ciphertext, key); break;
                    case "HILL": decryptedText = CryptoUtils.decryptHill(ciphertext, key); break;
                    case "VIGENERE": decryptedText = CryptoUtils.decryptVigenere(ciphertext, key); break;
                    case "OTP": decryptedText = CryptoUtils.decryptVigenere(ciphertext, key); break;
                    case "RAILFENCE": decryptedText = CryptoUtils.decryptRailFence(ciphertext, key); break;
                    default: decryptedText = "Unknown Cipher Type";
                }

                System.out.println("       Decrypted Plaintext: " + decryptedText);

                output.writeUTF("Server received and decrypted message successfully.");
            }
        } catch (EOFException e) {
            System.out.println("Client disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}