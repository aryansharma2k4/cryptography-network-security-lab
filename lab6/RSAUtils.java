public class RSAUtils {

    public static int gcd(int a, int b) {
        System.out.println("\nComputing GCD(" + a + ", " + b + ")");
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
            System.out.println("a = " + a + ", b = " + b);
        }
        System.out.println("Final GCD = " + a);
        return a;
    }

    public static int[] extended_euclidean(int a, int b) {
        if (b == 0)
            return new int[]{a, 1, 0};

        int[] vals = extended_euclidean(b, a % b);
        int d = vals[0];
        int x = vals[2];
        int y = vals[1] - (a / b) * vals[2];

        return new int[]{d, x, y};
    }

    public static int mod_inverse(int e, int phi) {
        System.out.println("\nComputing Modular Inverse of " + e + " mod " + phi);
        int[] vals = extended_euclidean(e, phi);
        int gcd = vals[0];
        int x = vals[1];

        if (gcd != 1)
            throw new ArithmeticException("Inverse does not exist.");

        int result = (x % phi + phi) % phi;
        System.out.println("Private exponent d = " + result);
        return result;
    }

    public static long mod_exp(long base, long exponent, long modulus) {
        System.out.println("\nPerforming Modular Exponentiation:");
        System.out.println("Base = " + base + ", Exponent = " + exponent + ", Modulus = " + modulus);

        long result = 1;
        base = base % modulus;

        while (exponent > 0) {
            if ((exponent & 1) == 1) {
                result = (result * base) % modulus;
                System.out.println("Result updated to " + result);
            }
            base = (base * base) % modulus;
            exponent >>= 1;
        }
        return result;
    }

    public static long encrypt(long message, long e, long n) {
        System.out.println("\nEncrypting Message: " + message);
        long cipher = mod_exp(message, e, n);
        System.out.println("Ciphertext = " + cipher);
        return cipher;
    }

    public static long decrypt(long cipher, long d, long n) {
        System.out.println("\nDecrypting Cipher: " + cipher);
        long message = mod_exp(cipher, d, n);
        System.out.println("Decrypted Message = " + message);
        return message;
    }
}