import java.math.BigInteger;


public class CryptCore {
    public static BigInteger calculateGCD(BigInteger x, BigInteger y) {
        return x.gcd(y);
    }


    public static BigInteger performEncryption(BigInteger msg, BigInteger pub, BigInteger mod) {
        return msg.modPow(pub, mod);
    }


    public static BigInteger performDecryption(BigInteger cipher, BigInteger priv, BigInteger mod) {
        return cipher.modPow(priv, mod);
    }
}
