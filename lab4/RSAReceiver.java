import java.io.*;
import java.net.*;
import java.math.BigInteger;
import java.util.Scanner;


public class RSAReceiver {
    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        
        System.out.print("Prime P: ");
        BigInteger valP = input.nextBigInteger();
        System.out.print("Prime Q: ");
        BigInteger valQ = input.nextBigInteger();
        
        BigInteger n = valP.multiply(valQ);
        BigInteger phi = (valP.subtract(BigInteger.ONE)).multiply(valQ.subtract(BigInteger.ONE));
        
        BigInteger e;
        while (true) {
            System.out.print("Exponent E (coprime to " + phi + "): ");
            e = input.nextBigInteger();
            if (e.gcd(phi).equals(BigInteger.ONE)) break;
            System.out.println("Invalid E, try again.");
        }
        
        BigInteger d = e.modInverse(phi);
        
        ServerSocket listener = new ServerSocket(9999);
        System.out.println("Receiver active on port 9999...");
        Socket link = listener.accept();
        
        ObjectOutputStream outStream = new ObjectOutputStream(link.getOutputStream());
        ObjectInputStream inStream = new ObjectInputStream(link.getInputStream());


        outStream.writeObject(e);
        outStream.writeObject(n);


        BigInteger[] dataIncoming = (BigInteger[]) inStream.readObject();
        
        StringBuilder outputText = new StringBuilder();
        System.out.print("Inbound Cipher: ");
        for (BigInteger block : dataIncoming) {
            System.out.print(block + " ");
            BigInteger raw = CryptCore.performDecryption(block, d, n);
            outputText.append((char) raw.intValue());
        }


        System.out.println("\nFinal Result: " + outputText.toString());


        link.close();
        listener.close();
    }
}
