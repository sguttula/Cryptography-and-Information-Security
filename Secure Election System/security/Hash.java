package security;

import java.math.BigInteger;

public class Hash {
    private int nDatabytes;
    private int nCheckbytes;
    private byte pattern;
    private int k;
    
    public Hash(final int nDatabytes, final int nCheckbytes, final byte pattern, final int k) {
        super();
        this.nDatabytes = nDatabytes;
        this.nCheckbytes = nCheckbytes;
        this.pattern = pattern;
        this.k = k;
    }
    
    public int getNumberOfDataBytes() {
        return this.nDatabytes;
    }
    
    public int getPacketSize() {
        return this.nDatabytes + this.nCheckbytes + 1;
    }
    
    public static void main(final String[] array) throws Exception {
        if (array.length < 5) {
            System.out.println("java security.Hash <databytes> <checkbytes> <pattern> <k> <text> [ <text> ... ]");
            System.exit(1);
        }
        final int int1 = Integer.parseInt(array[0]);
        final int int2 = Integer.parseInt(array[1]);
        final byte b = (byte)Integer.parseInt(array[2]);
        final int int3 = Integer.parseInt(array[3]);
        for (int i = 4; i < array.length; ++i) {
            final byte[] pack = pack(array[i].getBytes(), int1, int2, b, int3);
            System.out.println("packed Bytes");
            System.out.println(new String(pack));
            System.out.println("unpacked Bytes");
            System.out.println(new String(unpack(pack, int1, int2, b, int3)));
        }
    }
    
    public byte[] pack(final byte[] array) {
        return pack(array, this.nDatabytes, this.nCheckbytes, this.pattern, this.k);
    }
    
    public byte[] pack(final byte[] array, final int n) {
        final byte[] array2 = new byte[n];
        System.arraycopy(array, 0, array2, 0, n);
        return pack(array2, this.nDatabytes, this.nCheckbytes, this.pattern, this.k);
    }
    
    public static byte[] pack(final byte[] array, final int n, final int n2, final byte b, final int n3) {
        if (n > 256) {
            throw new RuntimeException("Maximum Size of databytes is 255.");
        }
        final int length = array.length;
        final int n4 = n + n2 + 1;
        final int n5 = (length % n == 0) ? (length / n) : (length / n + 1);
        final byte[] array2 = new byte[n5 * n4];
        int n6 = 0;
        for (int i = 0; i < n5; ++i) {
            final byte b2 = (byte)(((i + 1) * n > length) ? (length % n) : n);
            array2[i * n4] = b2;
            BigInteger bigInteger = BigInteger.valueOf(0L);
            for (byte b3 = 0; b3 < b2; ++b3) {
                final byte b4 = array[n6];
                ++n6;
                bigInteger = bigInteger.add(BigInteger.valueOf((b & b4) * n3));
                array2[i * n4 + b3 + 1] = b4;
            }
            final BigInteger mod = bigInteger.mod(BigInteger.valueOf((int)Math.pow(2.0, 8 * n2)));
            final int length2 = mod.toByteArray().length;
            for (int j = 0; j < n2; ++j) {
                if (n2 - j > length2) {
                    array2[i * n4 + n + j + 1] = 0;
                }
                else {
                    array2[i * n4 + n + j + 1] = mod.toByteArray()[j - (n2 - length2)];
                }
            }
        }
        return array2;
    }
    
    public byte[] unpack(final byte[] array) throws Exception {
        return unpack(array, this.nDatabytes, this.nCheckbytes, this.pattern, this.k);
    }
    
    public static byte[] unpack(final byte[] array, final int n, final int n2, final byte b, final int n3) throws Exception {
        if (n > 256) {
            throw new RuntimeException("Maximum size of databytes is 256");
        }
        final int length = array.length;
        final int n4 = 1 + n + n2;
        if (length % n4 != 0) {
            throw new Exception("Packet Size is wrong");
        }
        final int n5 = length / n4;
        byte b2 = 0;
        for (int i = 0; i < n5; ++i) {
            b2 += array[i * n4];
        }
        final byte[] array2 = new byte[b2];
        int j = 0;
        int n6 = 0;
        int n7 = 0;
        while (j < n5) {
            final byte b3 = array[j * n4];
            BigInteger bigInteger = BigInteger.valueOf(0L);
            ++n6;
            for (byte b4 = 0; b4 < b3; ++b4) {
                final byte b5 = array[n6];
                ++n6;
                bigInteger = bigInteger.add(BigInteger.valueOf((b5 & b) * n3));
                array2[n7] = b5;
                ++n7;
            }
            if (b3 < n) {
                n6 += n - b3;
            }
            final BigInteger mod = bigInteger.mod(BigInteger.valueOf((int)Math.pow(2.0, 8 * n2)));
            final int length2 = mod.toByteArray().length;
            for (int k = n2 - length2; k < n2; ++k) {
                if (k >= 0 && array[j * n4 + n + k + 1] != mod.toByteArray()[length2 - n2 + k]) {
                    throw new Exception("wrong checksum");
                }
            }
            n6 += n2;
            ++j;
        }
        return array2;
    }
}
