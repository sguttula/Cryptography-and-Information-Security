
package security;

import java.math.BigInteger;
import java.io.IOException;
import java.io.InputStream;

public class PrivateKey extends Key {
    public PrivateKey(final InputStream inputStream) throws IOException {
        super();
        this.read(inputStream);
    }
    
    protected PrivateKey(final BigInteger bigInteger, final BigInteger bigInteger2) {
        super(bigInteger, bigInteger2);
    }
    
    public PrivateKey(final byte[] array) throws IOException {
        super();
        this.read(array);
    }
}
