package rsa;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

public class PublicKey extends Key {

	public PublicKey(byte aByte[]) throws IOException {
		super.read(aByte);
	}

	public PublicKey(InputStream input) throws IOException {
		super.read(input);
	}

	public PublicKey(BigInteger a1, BigInteger a2) {
		super(a1, a2);
	}

}
