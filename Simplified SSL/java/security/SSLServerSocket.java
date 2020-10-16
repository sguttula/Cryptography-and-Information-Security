package security;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

import rsa.PrivateKey;
import rsa.PublicKey;

public class SSLServerSocket extends ServerSocket {
	
	
	protected PrivateKey serverPrivateKey;
	
	
	protected Properties users;

	public SSLServerSocket(int port, PrivateKey serverPrivateKey, Properties users) throws IOException {
		super(port);
		
		this.serverPrivateKey = serverPrivateKey;
		
		this.users = users;
	}

	protected Object[] handshake(Socket socket) throws Exception {
		
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		SSLPacket packet;
		if ((packet = (SSLPacket) ois.readObject()) == null) {
			
			throw new IOException("handshake packet format not good");
		}
		byte[] encryptedId = packet.getEncryptedId();
		
		
		
		
		byte[] encryptedCompany = packet.getEncryptedCompany();
		
		byte[] encryptedOneTimeKey = packet.getEncryptedOneTimeKey();
		
		
		
		
		String id = new String(RSA.cipher(encryptedId, serverPrivateKey));
		System.out.println(id);
		
		
		String clientPublicKey = users.getProperty(id + ".public_key");
		if (clientPublicKey == null) {
			
			
			//System.out.println(id);

			throw new Exception("User " + id + " not able to find");
		}
		PublicKey localPublicKey = new PublicKey(clientPublicKey.getBytes());
		
		
		String company = new String(RSA.cipher(encryptedCompany, localPublicKey));
		
		
		System.out.println(company);
		
		
		if (!company.equals(users.getProperty(id + ".company"))) {
			
			
			
		//	System.out.println(company);

			throw new Exception("Company " + company + " is incorrect for user " + id);
		}
		
		int j = Integer.parseInt(users.getProperty(id + ".ndatabytes"));
		
		
		int k = Integer.parseInt(users.getProperty(id + ".ncheckbytes"));
		
		
		byte b = (byte) Integer.parseInt(users.getProperty(id + ".pattern"));
		
		
		int m = Integer.parseInt(users.getProperty(id + ".k"));
		
		Object[] nCryptoParams = new Object[2];
		
		nCryptoParams[0] = RSA.cipher(encryptedOneTimeKey, serverPrivateKey);
		
		nCryptoParams[1] = new Hash(j, k, b, m);
		
		return nCryptoParams;
	}

	public Socket accept() throws IOException {
		
		Socket localSocket = super.accept();
		byte[] oneTimeKey = null;
		Hash hash = null;
		try {
			
			Object[] nCryptoParams = handshake(localSocket);
			
			oneTimeKey = (byte[]) nCryptoParams[0];
			
			hash = (Hash) nCryptoParams[1];
		}
		catch (Exception localException) {
			
			throw new IOException(localException.toString());
		}
		return new CryptoSocket(localSocket, oneTimeKey, hash);
	}
}
