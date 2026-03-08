import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;

public class BankServer {

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
        int port = 65433;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Bank Secure Server listening on port " + port + "...");
            try (Socket socket = serverSocket.accept();
                 ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
                
                BigInteger p = (BigInteger) ois.readObject();
                BigInteger q = (BigInteger) ois.readObject();
                BigInteger g = (BigInteger) ois.readObject();
                BigInteger y = (BigInteger) ois.readObject();
                
                String message = (String) ois.readObject();
                BigInteger r = (BigInteger) ois.readObject();
                BigInteger s = (BigInteger) ois.readObject();
                
                System.out.println("\nReceived Transaction Request: '" + message + "'");
                
                BigInteger hm = getHash(message);
                
                BigInteger w = s.modInverse(q);
                BigInteger u1 = hm.multiply(w).mod(q);
                BigInteger u2 = r.multiply(w).mod(q);
                
                BigInteger v = g.modPow(u1, p).multiply(y.modPow(u2, p)).mod(p).mod(q);
                
                if (v.equals(r)) {
                    System.out.println("[SUCCESS] Signature verified! The transaction is authentic and unaltered.");
                } else {
                    System.out.println("[ERROR] Signature verification failed! Transaction rejected.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
