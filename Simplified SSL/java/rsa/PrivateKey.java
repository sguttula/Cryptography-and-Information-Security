package rsa;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

public class PrivateKey extends Key {

	public PrivateKey(byte aByte[]) throws IOException {
		super.read(aByte);
	}

	public PrivateKey(InputStream input) throws IOException {
		super.read(input);
	}

	public PrivateKey(BigInteger a1, BigInteger a2) {
		super(a1, a2);
	}

}
