package security;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CryptoSocket extends Socket {
	
	
	protected byte[] oneTimeKey;
	
	protected Hash hash;
	
	protected CryptoInputStream CIn;
	
	protected CryptoOutputStream COut;
	
	protected Socket socket;

	public CryptoSocket(String host, int port, byte[] encryptedId, byte[] encryptedCompany, byte[] encryptedOneTimeKey,
			byte[] oneTimeKey, Hash hash) throws IOException {
		
		
		super(host, port);
		
		this.oneTimeKey = oneTimeKey;
		
		this.hash = hash;
		
		handshake(encryptedId, encryptedCompany, encryptedOneTimeKey);
	}

	public CryptoSocket(Socket socket, byte[] oneTimeKey, Hash hash) throws IOException {
		this.socket = socket;
		this.oneTimeKey = oneTimeKey;
		this.hash = hash;
	}

	protected void handshake(byte[] encryptedId, byte[] encryptedCompany, byte[] encryptedOneTimeKey)
			throws IOException {
		
		
		SSLPacket assembledpacket = new SSLPacket(encryptedId, encryptedCompany, encryptedOneTimeKey);
		ObjectOutputStream oos = new ObjectOutputStream(super.getOutputStream());
		oos.writeObject(assembledpacket);
		oos.flush();
	}

	@Override
	public CryptoInputStream getInputStream() throws IOException {
		if (CIn == null) {
			
			CIn = new CryptoInputStream(socket != null ? socket.getInputStream() : super.getInputStream(),
					oneTimeKey, hash);
		}
		return CIn;
	}

	@Override
	public CryptoOutputStream getOutputStream() throws IOException {
		if (COut == null) {
			
			COut = new CryptoOutputStream(socket != null ? socket.getOutputStream() : super.getOutputStream(),
					oneTimeKey, hash);
		}
		return COut;
	}

	@Override
	public void close() throws IOException {
		if (socket != null) {
			
			socket.close();
		}
		super.close();
	}
}
