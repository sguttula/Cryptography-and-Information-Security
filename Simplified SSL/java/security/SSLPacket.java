package security;

import java.io.Serializable;

public class SSLPacket implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	byte[] encryptedId;
	
	byte[] encryptedCompany;
	
	byte[] encryptedOneTimeKey;

	public SSLPacket(byte[] encryptedId, byte[] encryptedCompany, byte[] encryptedOneTimeKey) {
		this.encryptedId = encryptedId;
		this.encryptedCompany = encryptedCompany;
		this.encryptedOneTimeKey = encryptedOneTimeKey;
	}

	public byte[] getEncryptedId() {
		return encryptedId;
	}

	public void setEncryptedId(byte[] encryptedId) {
		this.encryptedId = encryptedId;
	}

	public byte[] getEncryptedCompany() {
		return encryptedCompany;
	}

	public void setEncryptedCompany(byte[] encryptedCompany) {
		this.encryptedCompany = encryptedCompany;
	}

	public byte[] getEncryptedOneTimeKey() {
		return encryptedOneTimeKey;
	}

	public void setEncryptedOneTimeKey(byte[] encryptedOneTimeKey) {
		this.encryptedOneTimeKey = encryptedOneTimeKey;
	}
}
