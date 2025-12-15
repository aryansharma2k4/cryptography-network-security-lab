import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 5000;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to Encryption Server.");

            while (true) {
                System.out.println("1. Caesar Cipher");
                System.out.println("2. Monoalphabetic Cipher");
                System.out.println("3. Playfair Cipher");
                System.out.println("4. Hill Cipher");
                System.out.println("5. Vigenère Cipher");
                System.out.println("6. One-Time Pad");
                System.out.println("7. Rail Fence Cipher");
                System.out.println("8. Exit");
                System.out.print("Select an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); 

                if (choice == 8) {
                    output.writeUTF("EXIT");
                    break;
                }

                System.out.print("Enter Plaintext: ");
                String plaintext = scanner.nextLine();
                String key = "";
                String type = "";
                String ciphertext = "";

                switch (choice) {
                    case 1:
                        type = "CAESAR";
                        System.out.print("Enter Shift (Integer): ");
                        key = scanner.nextLine();
                        ciphertext = CryptoUtils.encryptCaesar(plaintext, key);
                        break;
                    case 2:
                        type = "MONO";
                        System.out.print("Enter 26-char shuffled alphabet Key (or enter for default): ");
                        String k = scanner.nextLine();
                        key = k.length() == 26 ? k : "QWERTYUIOPASDFGHJKLZXCVBNM";
                        ciphertext = CryptoUtils.encryptMono(plaintext, key);
                        break;
                    case 3:
                        type = "PLAYFAIR";
                        System.out.print("Enter Keyword: ");
                        key = scanner.nextLine();
                        ciphertext = CryptoUtils.encryptPlayfair(plaintext, key);
                        break;
                    case 4:
                        type = "HILL";
                        System.out.print("Enter 4-letter Key string (e.g., GYBN): ");
                        key = scanner.nextLine();
                        ciphertext = CryptoUtils.encryptHill(plaintext, key);
                        break;
                    case 5:
                        type = "VIGENERE";
                        System.out.print("Enter Keyword: ");
                        key = scanner.nextLine();
                        ciphertext = CryptoUtils.encryptVigenere(plaintext, key);
                        break;
                    case 6:
                        type = "OTP";
                        System.out.print("Enter Random Key (must be length >= text): ");
                        key = scanner.nextLine();
                        if (key.length() < plaintext.length()) {
                            System.out.println("Error: Key too short.");
                            continue;
                        }
                        key = key.substring(0, plaintext.length());
                        ciphertext = CryptoUtils.encryptVigenere(plaintext, key);
                        break;
                    case 7:
                        type = "RAILFENCE";
                        System.out.print("Enter Depth (Integer): ");
                        key = scanner.nextLine();
                        ciphertext = CryptoUtils.encryptRailFence(plaintext, key);
                        break;
                    default:
                        System.out.println("Invalid option.");
                        continue;
                }

                System.out.println("Your Plaintext:        " + plaintext);
                System.out.println("Generated Ciphertext:  " + ciphertext);
                System.out.println("Sending to server for decryption...");

                output.writeUTF(type + "::" + key + "::" + ciphertext);
                
                String response = input.readUTF();
                System.out.println("Server Status: " + response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}