import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Random;

public class BankClient {

    public static BigInteger getHash(String message) {
        byte[] data = message.getBytes();
        int[] h = { 0x67452301, 0xEFCDAB89, 0x98BADCFE, 0x10325476, 0xC3D2E1F0 };
        for (int i = 0; i < data.length; i++) {
            h[i % 5] = Integer.rotateLeft(h[i % 5] ^ data[i], 5);
            h[(i + 1) % 5] += h[i % 5] * 31;
        }
        byte[] out = new byte[20];
        for (int i = 0; i < 5; i++) {
            out[i * 4] = (byte) (h[i] >>> 24);
            out[i * 4 + 1] = (byte) (h[i] >>> 16);
            out[i * 4 + 2] = (byte) (h[i] >>> 8);
            out[i * 4 + 3] = (byte) h[i];
        }
        return new BigInteger(1, out);
    }

    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 65433;

        try {
            Random random = new Random();
            
            BigInteger q = BigInteger.probablePrime(160, random);
            BigInteger p;
            BigInteger pMinus1;
            do {
                BigInteger k = new BigInteger(512 - 160, random);
                pMinus1 = k.multiply(q);
                p = pMinus1.add(BigInteger.ONE);
            } while (!p.isProbablePrime(50));
            
            BigInteger hNum = new BigInteger(512 - 2, random).add(BigInteger.TWO);
            BigInteger g = hNum.modPow(pMinus1.divide(q), p);
            
            BigInteger x;
            do {
                x = new BigInteger(160, random);
            } while (x.compareTo(BigInteger.ZERO) <= 0 || x.compareTo(q) >= 0);
            
            BigInteger y = g.modPow(x, p);

            String transactionMsg = "TRANSFER $50,000 TO ACCOUNT #987654321";
            System.out.println("Transaction prepared: '" + transactionMsg + "'");

            BigInteger hm = getHash(transactionMsg);
            
            BigInteger k;
            do {
                k = new BigInteger(160, random);
            } while (k.compareTo(BigInteger.ZERO) <= 0 || k.compareTo(q) >= 0);
            
            BigInteger r = g.modPow(k, p).mod(q);
            BigInteger kInv = k.modInverse(q);
            BigInteger s = kInv.multiply(hm.add(x.multiply(r))).mod(q);

            try (Socket socket = new Socket(host, port);
                 ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
                
                oos.writeObject(p);
                oos.writeObject(q);
                oos.writeObject(g);
                oos.writeObject(y);
                
                oos.writeObject(transactionMsg);
                oos.writeObject(r);
                oos.writeObject(s);
                
                System.out.println("Transaction and manual DSA signature sent to the bank server.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
