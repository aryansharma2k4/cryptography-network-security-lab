import java.io.*;
import java.net.*;
import java.math.BigInteger;
import java.util.Scanner;


public class RSASender {
    public static void main(String[] args) throws Exception {
        Scanner console = new Scanner(System.in);
        Socket connection = new Socket("localhost", 9999);
        
        ObjectInputStream inputSource = new ObjectInputStream(connection.getInputStream());
        ObjectOutputStream outputTarget = new ObjectOutputStream(connection.getOutputStream());


        BigInteger keyE = (BigInteger) inputSource.readObject();
        BigInteger keyN = (BigInteger) inputSource.readObject();
        System.out.println("Connected. Modulus: " + keyN);


        System.out.print("Message to send: ");
        String text = console.nextLine();
        byte[] characters = text.getBytes();


        BigInteger[] encryptedData = new BigInteger[characters.length];
        for (int i = 0; i < characters.length; i++) {
            BigInteger m = BigInteger.valueOf(characters[i]);
            encryptedData[i] = CryptCore.performEncryption(m, keyE, keyN);
        }


        outputTarget.writeObject(encryptedData);
        System.out.println("Transmission complete.");


        connection.close();
    }
}
