
package security;

import java.io.EOFException;
import java.net.Socket;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Properties;
import java.net.ServerSocket;

public class SSLServerSocket extends ServerSocket {
    protected PrivateKey sKR;
    protected Properties prop;
    
    public SSLServerSocket(final int n, final int n2, final InetAddress inetAddress, final PrivateKey skr, final Properties prop) throws IOException {
        super(n, n2, inetAddress);
        this.sKR = skr;
        this.prop = prop;
    }
    
    public SSLServerSocket(final int n, final int n2, final PrivateKey skr, final Properties prop) throws IOException {
        super(n, n2);
        this.sKR = skr;
        this.prop = prop;
    }
    
    public SSLServerSocket(final int n, final PrivateKey skr, final Properties prop) throws IOException {
        super(n);
        this.sKR = skr;
        this.prop = prop;
    }
    
    public Socket accept() throws IOException {
        final Socket accept = super.accept();
        byte[] array;
        Hash hash;
        try {
            final Object[] handshake = this.handshake(accept);
            array = (byte[])handshake[0];
            hash = (Hash)handshake[1];
        }
        catch (Exception ex) {
            throw new IOException(ex.toString());
        }
        return (Socket)new SSLSocket(accept, array, hash);
    }
    
    protected byte[] getGreetingToken(final Socket socket) throws IOException {
        int n = 128;
        byte[] array = new byte[n];
        int n2 = 0;
        int n3;
        while ((n3 = socket.getInputStream().read()) != 33) {
            if (n3 == -1) {
                throw new EOFException("Unexpected ended Greeting");
            }
            if (n3 == 92 && (n3 = socket.getInputStream().read()) == -1) {
                throw new EOFException("Unexpected ended Greeting");
            }
            if (n2 == n) {
                n += n / 2 + 1;
                final byte[] array2 = new byte[n];
                System.arraycopy(array, 0, array2, 0, n2);
                array = array2;
            }
            array[n2++] = (byte)n3;
        }
        final byte[] array3 = new byte[n2];
        System.arraycopy(array, 0, array3, 0, n2);
        return array3;
    }
    
    protected Object[] handshake(final Socket socket) throws Exception {
        int read;
        while ((read = socket.getInputStream().read()) != 33) {
            if (read == -1) {
                throw new EOFException("Unfinished Greeting");
            }
        }
        final byte[] greetingToken = this.getGreetingToken(socket);
        final byte[] greetingToken2 = this.getGreetingToken(socket);
        final byte[] greetingToken3 = this.getGreetingToken(socket);
        final String s = new String(RSA.cipher(greetingToken, (Key)this.sKR));
        final String property = this.prop.getProperty(String.valueOf(s) + ".public_key");
        if (property == null) {
            throw new Exception("Unknow User: " + s);
        }
        final String s2 = new String(RSA.cipher(greetingToken2, (Key)new PublicKey(property.getBytes())));
        if (!s2.equals(this.prop.getProperty(String.valueOf(s) + ".company"))) {
            throw new Exception("Wrong company (" + s + ':' + s2 + ")");
        }
        return new Object[] { RSA.cipher(greetingToken3, (Key)this.sKR), new Hash(Integer.parseInt(this.prop.getProperty(String.valueOf(s) + ".ndatabytes")), Integer.parseInt(this.prop.getProperty(String.valueOf(s) + ".ncheckbytes")), (byte)Integer.parseInt(this.prop.getProperty(String.valueOf(s) + ".pattern")), Integer.parseInt(this.prop.getProperty(String.valueOf(s) + ".k"))) };
    }
}
