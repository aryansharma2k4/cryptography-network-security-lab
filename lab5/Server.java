import java.io.*;
import java.net.*;

public class Server {

    private static final int PORT = 5000;
    private static final long P = 353;
    private static final long G = 3;

    public static void main(String[] args) throws Exception {

        ServerSocket ss = new ServerSocket(PORT);
        System.out.println("Server started. Waiting for connections...");

        Socket a = ss.accept();
        System.out.println("Alice connected");

        Socket b = ss.accept();
        System.out.println("Bob connected");

        Socket m = ss.accept();
        System.out.println("Attacker connected");

        BufferedReader inA = new BufferedReader(
                new InputStreamReader(a.getInputStream()));
        PrintWriter outA = new PrintWriter(
                a.getOutputStream(), true);

        BufferedReader inB = new BufferedReader(
                new InputStreamReader(b.getInputStream()));
        PrintWriter outB = new PrintWriter(
                b.getOutputStream(), true);

        BufferedReader inM = new BufferedReader(
                new InputStreamReader(m.getInputStream()));
        PrintWriter outM = new PrintWriter(
                m.getOutputStream(), true);

        String params = P + "," + G;
        outA.println(params);
        outB.println(params);
        outM.println(params);

        long aPub = Long.parseLong(inA.readLine());
        long bPub = Long.parseLong(inB.readLine());

        outM.println(aPub);
        outM.println(bPub);

        long fakeA = Long.parseLong(inM.readLine());
        long fakeB = Long.parseLong(inM.readLine());

        outA.println(fakeA);
        outB.println(fakeB);

        System.out.println("MITM established. Public keys replaced.");

        while (true) {

            String msgA = inA.readLine();
            outM.println("A:" + msgA);
            String modA = inM.readLine();
            outB.println(modA);

            String msgB = inB.readLine();
            outM.println("B:" + msgB);
            String modB = inM.readLine();
            outA.println(modB);
        }
    }
}
