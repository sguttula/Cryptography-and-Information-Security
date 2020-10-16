package security;

import java.io.Serializable;

public class CryptoPacket implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	byte[] encodedMessage;

	public CryptoPacket(byte[] encodedMessage) {
		
		this.encodedMessage = encodedMessage;
	}

	public byte[] getEncodedMessage() {
		return encodedMessage;
	}

	public void setEncodedMessage(byte[] encodedMessage) {
		this.encodedMessage = encodedMessage;
	}
}
