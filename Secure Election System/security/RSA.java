package security;

import java.util.Random;
import java.math.BigInteger;

public class RSA {
    public RSA() {
        super();
    }
    
    public static byte[] cipher(final String s, final Key key) throws Exception {
        return cipher(s.getBytes(), key);
    }
    
    public static byte[] cipher(final byte[] array, final Key key) throws Exception {
        final byte[] array2 = new byte[array.length + 1];
        array2[0] = 0;
        for (int i = 0; i < array.length; ++i) {
            array2[i + 1] = array[i];
        }
        final byte[] byteArray = new BigInteger(array2).modPow(key.getKey(), key.getN()).toByteArray();
        if (byteArray[0] != 0) {
            return byteArray;
        }
        final byte[] array3 = new byte[byteArray.length - 1];
        System.arraycopy(byteArray, 1, array3, 0, byteArray.length - 1);
        return array3;
    }
    
    public static KeyPair generateKeys(final BigInteger bigInteger, final BigInteger bigInteger2) {
        final BigInteger one = BigInteger.ONE;
        final BigInteger multiply = bigInteger.multiply(bigInteger2);
        final BigInteger multiply2 = bigInteger.subtract(one).multiply(bigInteger2.subtract(one));
        final BigInteger relativePrime = relativePrime(multiply2);
        return new KeyPair(new PrivateKey(relativePrime.modInverse(multiply2), multiply), new PublicKey(relativePrime, multiply));
    }
    
    public static void main(final String[] array) throws Exception {
        if (array.length >= 1) {
            if (array[0].equals("-help")) {
                System.out.println("java security.RSA -help ");
                System.out.println("   - this message");
                System.out.println();
                System.out.println("java security.RSA -gen [ <text> ]");
                System.out.println("   - generate private (KR) and public (KU) keys");
                System.out.println("     and test them on <text> (optional)");
                System.out.println();
                return;
            }
            if (array[0].equals("-gen") && array.length <= 2) {
                final String property = System.getProperty("prime_size");
                final String property2 = System.getProperty("prime_certainty");
                int int1;
                if (property == null) {
                    int1 = 256;
                }
                else {
                    int1 = Integer.parseInt(property);
                }
                int int2;
                if (property2 == null) {
                    int2 = 5;
                }
                else {
                    int2 = Integer.parseInt(property2);
                }
                final KeyPair generateKeys = generateKeys(new BigInteger(int1, int2, new Random()), new BigInteger(int1, int2, new Random()));
                System.out.println(generateKeys);
                if (array.length == 2) {
                    final byte[] bytes = array[1].getBytes();
                    final byte[] cipher = cipher(bytes, generateKeys.getPublicKey());
                    final byte[] cipher2 = cipher(bytes, generateKeys.getPrivateKey());
                   // System.out.println("Cipher txtxtx: "+cipher);
                    System.out.println("KU(KR(M))=" + new String(cipher(cipher, generateKeys.getPrivateKey())));
                    System.out.println("KR(KU(M))=" + new String(cipher(cipher2, generateKeys.getPublicKey())));
                }
                return;
            }
        }
        System.out.println("java security.RSA -help");
    }
    
    private static BigInteger relativePrime(final BigInteger bigInteger) {
        final Random random = new Random();
        final int length = bigInteger.toByteArray().length;
        BigInteger mod;
        do {
            final byte[] array = new byte[length];
            random.nextBytes(array);
            mod = new BigInteger(array).abs().mod(bigInteger);
        } while (bigInteger.gcd(mod).compareTo(BigInteger.ONE) != 0);
        return mod;
    }
}
