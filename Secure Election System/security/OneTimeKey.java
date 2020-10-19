
package security;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class OneTimeKey {
    public OneTimeKey() {
        super();
    }
    
    public static void main(final String[] array) throws Exception {
        if (array.length < 2) {
            System.out.println("java security.OneTimeKey <key>  <text> [ <text> ... ]");
            System.exit(1);
        }
        final byte[] bytes = array[0].getBytes();
        for (int i = 1; i < array.length; ++i) {
            System.out.println("original text is " + array[i]);
            final byte[] xor = xor(array[i].getBytes(), bytes);
            System.out.println("encoded to " + new String(xor));
            System.out.println("decoded to " + new String(xor(xor, bytes)));
        }
    }
    
    public static byte[] newKey(final int n) {
        return newKey(new Random(), n);
    }
    
    public static byte[] newKey(final Random random, final int n) {
        final byte[] array = new byte[n];
        random.nextBytes(array);
        return array;
    }
    
    public static void printKey(final byte[] array, final OutputStream outputStream) throws IOException {
        for (int i = 0; i < array.length; ++i) {
            outputStream.write(array[i]);
        }
    }
    
    public static byte[] xor(final byte[] arr1, final byte[] arr2) {
        if (arr1.length % arr2.length != 0) {
            throw new RuntimeException("Length of one-time key is wrong");
        }
        final byte[] arr3 = new byte[arr1.length];
        System.arraycopy(arr1, 0, arr3, 0, arr1.length);
        int n = 0;
        for (int i = 0; i < arr1.length / arr2.length; ++i) {
            for (int j = 0; j < arr2.length; ++j) {
                arr3[n] ^= arr2[j];
                ++n;
            }
        }
        return arr3;
    }
}
