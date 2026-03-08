import java.util.Random;

public class HellmanUtils {

    public static long modExp(long b, long e, long m) {

        long r = 1;
        b = b % m;

        while (e > 0) {

            if ((e & 1) == 1) {
                r = (r * b) % m;
            }

            e = e >> 1;
            b = (b * b) % m;
        }

        return r;
    }

    public static long generatePrivateKey() {
        Random rd = new Random();
        return rd.nextInt(200) + 2;
    }

    public static long generatePublicKey(long g, long pr, long p) {
        return modExp(g, pr, p);
    }

    public static long computeSharedSecret(long pub, long pr, long p) {
        return modExp(pub, pr, p);
    }

    public static String encrypt(String msg, long k) {

        StringBuilder sb = new StringBuilder();
        int sh = (int) (k % 26);

        for (char c : msg.toCharArray()) {

            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                sb.append((char) ((c - base + sh) % 26 + base));
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    public static String decrypt(String msg, long k) {

        StringBuilder sb = new StringBuilder();
        int sh = (int) (k % 26);

        for (char c : msg.toCharArray()) {

            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                sb.append((char) ((c - base - sh + 26) % 26 + base));
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }
}
