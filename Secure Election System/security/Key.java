package security;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
public class Key {
    protected BigInteger key;
    protected BigInteger n;
    private static BigInteger zero;
    
    static {
        Key.zero = BigInteger.ZERO;
    }
    
    public Key() {
        this(Key.zero, Key.zero);
    }
    
    public Key(final BigInteger key, final BigInteger n) {
        super();
        this.key = key;
        this.n = n;
    }
    
    protected BigInteger getKey() {
        return this.key;
    }
    
    protected BigInteger getN() {
        return this.n;
    }
    
    public void read(final InputStream inputStream) throws IOException {
        int read;
        while ((read = inputStream.read()) != 123) {
            switch (read) {
                default: {
                    throw new IOException("Wrong Format");
                }
                case 9:
                case 10:
                case 13:
                case 32: {
                    continue;
                }
            }
        }
        final StringBuffer sb = new StringBuffer(128);
        int read2;
        while ((read2 = inputStream.read()) != 44) {
            if (read2 == -1) {
                throw new EOFException("Unexpected End of File");
            }
            sb.append((char)read2);
        }
        try {
            this.key = new BigInteger(sb.toString());
        }
        catch (NumberFormatException ex) {
            throw new IOException(ex.toString());
        }
        sb.setLength(0);
        int read3;
        while ((read3 = inputStream.read()) != 125) {
            if (read3 == -1) {
                throw new EOFException("Unexpected End of File");
            }
            sb.append((char)read3);
        }
        try {
            this.n = new BigInteger(sb.toString());
        }
        catch (NumberFormatException ex2) {
            throw new IOException(ex2.toString());
        }
    }
    
    public void read(final byte[] array) throws IOException {
        this.read(new ByteArrayInputStream(array));
    }
    
    public String toString() {
        return String.valueOf('{') + this.key.toString() + ',' + this.n.toString() + '}';
    }
}
