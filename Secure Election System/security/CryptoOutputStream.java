package security;

import java.io.IOException;
import java.io.OutputStream;
import java.io.FilterOutputStream;

public class CryptoOutputStream extends FilterOutputStream {
    protected Hash H;
    protected byte[] K;
    private byte[] buffer;
    private int pointer;
    
    public CryptoOutputStream(final OutputStream outputStream, final byte[] k, final Hash h) {
        super(outputStream);
        this.H = h;
        this.K = k;
        this.buffer = new byte[h.getNumberOfDataBytes()];
        this.pointer = 0;
    }
    
    public void flush() throws IOException {
        if (this.pointer != 0) {
            this.shallowFlush();
        }
        super.flush();
    }
    
    protected void shallowFlush() throws IOException {
        if (this.pointer != 0) {
            this.write(this.buffer, 0, this.pointer);
            this.pointer = 0;
        }
    }
    
    public void write(final int n) throws IOException {
        this.buffer[this.pointer++] = (byte)n;
        if (this.pointer == this.buffer.length) {
            this.pointer = 0;
            this.write(this.buffer, 0, this.buffer.length);
        }
    }
    
    public void write(final byte[] array, final int n, final int n2) throws IOException {
        final byte[] array2 = new byte[n2];
        System.arraycopy(array, n, array2, 0, n2);
        try {
            super.out.write(OneTimeKey.xor(this.H.pack(array2), this.K));
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
