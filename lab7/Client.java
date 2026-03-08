import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class Client {

    public static long[] preprocessSHA512(String message) {
        byte[] msgBytes = message.getBytes();
        long origLenBits = (long) msgBytes.length * 8;

        ByteBuffer buffer = ByteBuffer.allocate(128);
        buffer.put(msgBytes);
        buffer.put((byte) 0x80);
        
        while (buffer.position() < 112) {
            buffer.put((byte) 0x00);
        }
        
        buffer.putLong(0L);
        buffer.putLong(origLenBits);
        
        buffer.flip();
        long[] wWords = new long[16];
        for (int i = 0; i < 16; i++) {
            wWords[i] = buffer.getLong();
        }
        
        return wWords;
    }

    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 65432;

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter a short message to process: ");
            String message = scanner.nextLine();

            long[] words = preprocessSHA512(message);

            try (Socket socket = new Socket(host, port);
                 ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
                
                oos.writeObject(words);
                System.out.println("Message schedule sent to the server successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
