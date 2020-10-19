package security;

import java.io.IOException;
import java.io.InputStream;
import java.io.FilterInputStream;

public class CryptoInputStream extends FilterInputStream {
    protected Hash H;
    protected byte[] K;
    protected byte[] buffer;
    protected int pointer;
    
    public CryptoInputStream(final InputStream inputStream, final byte[] k, final Hash h) {
        super(inputStream);
        this.H = h;
        this.K = k;
        this.pointer = 0;
    }
    
    public int available() throws IOException {
        return super.available() / this.H.getPacketSize() * this.H.getNumberOfDataBytes();
    }
    
    public int read() throws IOException {
        if (this.pointer == 0) {
            int n = 0;
            final byte[] array = new byte[this.H.getPacketSize()];
            int i = 0;
            while (i < this.H.getPacketSize()) {
                final int read = super.in.read();
                if (read == -1) {
                    if (i == 0) {
                        return -1;
                    }
                    throw new IOException("Error in reading data");
                }
                else {
                    array[n++] = (byte)read;
                    ++i;
                }
            }
            try {
                this.buffer = this.H.unpack(OneTimeKey.xor(array, this.K));
            }
            catch (RuntimeException ex) {
                System.out.println(ex);
            }
            catch (Exception ex2) {
                throw new IOException("error in reading");
            }
        }
        final byte b = this.buffer[this.pointer];
        this.pointer = (this.pointer + 1) % this.buffer.length;
        return b;
    }
    
    public int read(final byte[] array, final int n, final int n2) throws IOException {
        if (array == null) {
            throw new NullPointerException("Empty Buffer");
        }
        final int packetSize = this.H.getPacketSize();
        final int numberOfDataBytes = this.H.getNumberOfDataBytes();
        final int n3 = n / numberOfDataBytes;
        final int n4 = n % numberOfDataBytes;
        int n5 = (n2 + n4) / packetSize;
        if ((n2 + n4) % packetSize != 0) {
            ++n5;
        }
        final byte[] array2 = new byte[n5 * packetSize];
        final int n6 = n3 * packetSize;
        final int n7 = n5 * packetSize;
        try {
            if (super.available() < n7) {
                return 0;
            }
            final int read = super.in.read(array2, n6, n7);
            if (read == -1) {
                return read;
            }
            final byte[] unpack = this.H.unpack(OneTimeKey.xor(array2, this.K));
            System.arraycopy(unpack, 0, array, 0, unpack.length);
            return read / packetSize * numberOfDataBytes;
        }
        catch (Exception ex) {
            System.out.println("Error in decrypting");
            return 0;
        }
    }
    
    public long skip(final long n) throws IOException {
        for (long n2 = 0L; n2 < n; ++n2) {
            if (this.read() == -1) {
                return n2;
            }
        }
        return n;
    }
}
