// Decompiled using: fernflower
// Took: 212ms

package security;

import java.math.BigInteger;
import java.io.IOException;
import java.io.InputStream;

public class PublicKey extends Key {
    public PublicKey(final InputStream inputStream) throws IOException {
        super();
        this.read(inputStream);
    }
    
    protected PublicKey(final BigInteger bigInteger, final BigInteger bigInteger2) {
        super(bigInteger, bigInteger2);
    }
    
    public PublicKey(final byte[] array) throws IOException {
        super();
        this.read(array);
    }
}
