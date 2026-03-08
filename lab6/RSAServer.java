import java.io.*;
import java.net.*;

public class RSAServer {

    public static void main(String[] args) throws Exception {

        int p = 11;
        int q = 13;

        int n = p * q;
        int phi = (p - 1) * (q - 1);

        int e = 7;

        System.out.println("========== RSA KEY GENERATION ==========");
        System.out.println("Chosen primes:");
        System.out.println("p = " + p);
        System.out.println("q = " + q);

        System.out.println("\nn = p * q = " + n);
        System.out.println("phi(n) = (p-1)(q-1) = " + phi);

        RSAUtils.gcd(e, phi);
        int d = RSAUtils.mod_inverse(e, phi);

        System.out.println("\nPublic Key (e, n) = (" + e + ", " + n + ")");
        System.out.println("Private Key (d, n) = (" + d + ", " + n + ")");

        System.out.println("\n========== HOMOMORPHIC PROPERTY TEST ==========");

        int m1 = 4;
        int m2 = 5;

        long c1 = RSAUtils.encrypt(m1, e, n);
        long c2 = RSAUtils.encrypt(m2, e, n);

        long c_mul = (c1 * c2) % n;
        System.out.println("\nCipher Multiplication mod n = " + c_mul);

        long decryptedMul = RSAUtils.decrypt(c_mul, d, n);

        long expected = (m1 * m2) % n;

        System.out.println("\n(m1 * m2) mod n = " + expected);

        if (decryptedMul == expected)
            System.out.println("Homomorphic Property VERIFIED");
        else
            System.out.println("Homomorphic Property NOT VERIFIED");

        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("\nServer waiting on port 5000...");

        Socket socket = serverSocket.accept();
        System.out.println("Client connected.");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

        long receivedCipher = Long.parseLong(in.readLine());

        System.out.println("\nEncrypted message received from client: " + receivedCipher);

        long decryptedMessage = RSAUtils.decrypt(receivedCipher, d, n);

        System.out.println("\nFinal Decrypted Message at Server: " + decryptedMessage);

        socket.close();
        serverSocket.close();
    }
}