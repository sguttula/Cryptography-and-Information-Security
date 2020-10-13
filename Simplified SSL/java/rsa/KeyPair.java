package rsa;

public class KeyPair {

	private PrivateKey kR;
	private PublicKey kU;

	public KeyPair(PrivateKey privateKey, PublicKey publicKey) {
		this.kR = privateKey;
		this.kU = publicKey;
	}

	public PublicKey getPublicKey() {
		return kU;
	}

	public PrivateKey getPrivateKey() {
		return kR;
	}

	// Convert key pair to a string and return the string
	public String toString() {
		return "{" + kR + ", " + kU + "}";
	}

}
