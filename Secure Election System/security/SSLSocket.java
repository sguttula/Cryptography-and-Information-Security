// Decompiled using: fernflower
// Took: 61ms

package security;

import java.io.IOException;
import java.net.InetAddress;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.Socket;

public class SSLSocket extends Socket {
    protected byte[] key;
    protected Hash hash;
    protected InputStream cryptoIn;
    protected OutputStream cryptoOut;
    protected Socket socket;
    
    public SSLSocket(final String s, final int n, final InetAddress inetAddress, final int n2, final byte[] array, final byte[] array2, final byte[] array3, final byte[] key, final Hash hash) throws IOException {
        super(s, n, inetAddress, n2);
        this.handshake(array, array2, array3);
        this.key = key;
        this.hash = hash;
    }
    
    public SSLSocket(final String s, final int n, final byte[] array, final byte[] array2, final byte[] array3, final byte[] key, final Hash hash) throws IOException {
        super(s, n);
        this.handshake(array, array2, array3);
        this.key = key;
        this.hash = hash;
    }
    
    public SSLSocket(final InetAddress inetAddress, final int n, final InetAddress inetAddress2, final int n2, final byte[] array, final byte[] array2, final byte[] array3, final byte[] key, final Hash hash) throws IOException {
        super(inetAddress, n, inetAddress2, n2);
        this.handshake(array, array2, array3);
        this.key = key;
        this.hash = hash;
    }
    
    public SSLSocket(final InetAddress inetAddress, final int n, final byte[] array, final byte[] array2, final byte[] array3, final byte[] key, final Hash hash) throws IOException {
        super(inetAddress, n);
        this.handshake(array, array2, array3);
        this.key = key;
        this.hash = hash;
    }
    
    public SSLSocket(final Socket socket, final byte[] key, final Hash hash) throws IOException {
        super();
        this.socket = socket;
        this.key = key;
        this.hash = hash;
    }
    
    public void close() throws IOException {
        if (this.socket == null) {
            super.close();
            return;
        }
        this.socket.close();
    }
    
    public InputStream getCryptedInputStream() throws IOException {
        if (this.socket == null) {
            return super.getInputStream();
        }
        return super.getInputStream();
    }
    
    public InputStream getInputStream() throws IOException {
        if (this.cryptoIn == null) {
            this.cryptoIn = (InputStream)new CryptoInputStream((this.socket != null) ? this.socket.getInputStream() : super.getInputStream(), this.key, this.hash);
        }
        return this.cryptoIn;
    }
    
    public OutputStream getOutputStream() throws IOException {
        if (this.cryptoOut == null) {
            this.cryptoOut = (OutputStream)new CryptoOutputStream((this.socket != null) ? this.socket.getOutputStream() : super.getOutputStream(), this.key, this.hash);
        }
        return this.cryptoOut;
    }
    
    protected void handshake(final byte[] array, final byte[] array2, final byte[] array3) throws IOException {
        int n = 0;
        for (int i = 0; i < array.length; ++i) {
            if (array[i] == 33 || array[i] == 92) {
                ++n;
            }
        }
        for (int j = 0; j < array2.length; ++j) {
            if (array2[j] == 33 || array2[j] == 92) {
                ++n;
            }
        }
        for (int k = 0; k < array3.length; ++k) {
            if (array3[k] == 33 || array3[k] == 92) {
                ++n;
            }
        }
        final byte[] array4 = new byte[array.length + array2.length + array3.length + n + 4];
        int n2 = 1;
        array4[0] = 33;
        for (int l = 0; l < array.length; ++l) {
            if (array[l] == 33 || array[l] == 92) {
                array4[n2++] = 92;
            }
            array4[n2++] = array[l];
        }
        array4[n2++] = 33;
        for (int n3 = 0; n3 < array2.length; ++n3) {
            if (array2[n3] == 33 || array2[n3] == 92) {
                array4[n2++] = 92;
            }
            array4[n2++] = array2[n3];
        }
        array4[n2++] = 33;
        for (int n4 = 0; n4 < array3.length; ++n4) {
            if (array3[n4] == 33 || array3[n4] == 92) {
                array4[n2++] = 92;
            }
            array4[n2++] = array3[n4];
        }
        array4[n2] = 33;
        super.getOutputStream().write(array4);
        super.getOutputStream().flush();
    }
    
    public String toString() {
        return "Cryto(" + this.socket + ')';
    }
}
