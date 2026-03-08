import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter role (alice/bob/attacker): ");
        String r = sc.nextLine();

        Socket s = new Socket("localhost", 5000);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(s.getInputStream()));
        PrintWriter out = new PrintWriter(
                s.getOutputStream(), true);

        String[] pms = in.readLine().split(",");
        long p = Long.parseLong(pms[0]);
        long g = Long.parseLong(pms[1]);

        if (r.equalsIgnoreCase("attacker")) {

            long prA = HellmanUtils.generatePrivateKey();
            long prB = HellmanUtils.generatePrivateKey();

            long puA = HellmanUtils.generatePublicKey(g, prA, p);
            long puB = HellmanUtils.generatePublicKey(g, prB, p);

            long realAPub = Long.parseLong(in.readLine());
            long realBPub = Long.parseLong(in.readLine());

            out.println(puA);
            out.println(puB);

            long keyWithAlice = HellmanUtils.computeSharedSecret(realAPub, prA, p);
            long keyWithBob = HellmanUtils.computeSharedSecret(realBPub, prB, p);

            System.out.println("Key with Alice: " + keyWithAlice);
            System.out.println("Key with Bob: " + keyWithBob);

            while (true) {

                String packet = in.readLine();

                if (packet.startsWith("A:")) {

                    String enc = packet.substring(2);
                    System.out.println("\nEncrypted from Alice: " + enc);

                    String dec = HellmanUtils.decrypt(enc, keyWithAlice);
                    System.out.println("Decrypted: " + dec);

                    String mod = "[HACKED] " + dec;
                    System.out.println("Modified: " + mod);

                    String reEnc = HellmanUtils.encrypt(mod, keyWithBob);
                    System.out.println("Sent to Bob: " + reEnc);

                    out.println(reEnc);
                }

                else if (packet.startsWith("B:")) {

                    String enc = packet.substring(2);
                    System.out.println("\nEncrypted from Bob: " + enc);

                    String dec = HellmanUtils.decrypt(enc, keyWithBob);
                    System.out.println("Decrypted: " + dec);

                    String mod = "[HACKED] " + dec;
                    System.out.println("Modified: " + mod);

                    String reEnc = HellmanUtils.encrypt(mod, keyWithAlice);
                    System.out.println("Sent to Alice: " + reEnc);

                    out.println(reEnc);
                }
            }
        }

        else {

            long pr = HellmanUtils.generatePrivateKey();
            long pu = HellmanUtils.generatePublicKey(g, pr, p);

            out.println(pu);

            long rcv = Long.parseLong(in.readLine());
            long key = HellmanUtils.computeSharedSecret(rcv, pr, p);

            System.out.println("Shared key: " + key);

            try {
                while (true) {

                    System.out.print("Enter message: ");
                    String msg = sc.nextLine();

                    String enc = HellmanUtils.encrypt(msg, key);
                    out.println(enc);

                    String rep = in.readLine();
                    String dec = HellmanUtils.decrypt(rep, key);

                    System.out.println("Received: " + dec);
                }

            } finally {
                s.close();
                sc.close();
            }
        }
    }
}
