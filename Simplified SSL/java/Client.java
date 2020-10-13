
import java.io.FileInputStream;
import java.util.Properties;

import rsa.PrivateKey;
import rsa.PublicKey;
import security.CryptoSocket;
import security.Hash;
import security.RSA;

public class Client {
	private CryptoSocket socket;

	public Client(String host, int port, String name) throws Exception {
		Properties users = new Properties();
		
		FileInputStream fileInputStream = new FileInputStream(name + ".txt");
		
		users.load(fileInputStream);
		
		fileInputStream.close();
		
		String company = users.getProperty("company");
		
		PublicKey localPublicKey = new PublicKey(users.getProperty("server.public_key").getBytes());
		
		PrivateKey localPrivateKey = new PrivateKey(users.getProperty("private_key").getBytes());
		
		byte pattern = (byte) Integer.parseInt(users.getProperty("pattern"));
		
		int nDataBytes = Integer.parseInt(users.getProperty("ndatabytes"));
		
		int nCheckBytes = Integer.parseInt(users.getProperty("ncheckbytes"));
		
		int k = Integer.parseInt(users.getProperty("k"));
		
		Hash hash = new Hash(nDataBytes, nCheckBytes, pattern, k);
		
		byte[] encryptedCompany = RSA.cipher(company.getBytes(), localPrivateKey);
		
		byte[] oneTimeKey = security.OneTimeKey.newKey(nDataBytes + nCheckBytes + 1);
		
		byte[] encryptedOneTimeKey = RSA.cipher(oneTimeKey, localPublicKey);
		
		byte[] encryptedId = RSA.cipher(name.getBytes(), localPublicKey);
		

		
		
		// Open Server Connection
		
		
		
		socket = new CryptoSocket(host, port, encryptedId, encryptedCompany, encryptedOneTimeKey, oneTimeKey, hash);
	}

// Transfering Data	
	
	public void execute() throws Exception {
		int c, k = 0, i = 0;
		
		
		while ((c = System.in.read()) != -1) {
			
			
			
			socket.getOutputStream().write(c);

			if ((char) c == '\n' || (char) c == '\r')
				socket.getOutputStream().flush();
			++k;
		}
		socket.getOutputStream().flush();
	
		
		
		while ((c = socket.getInputStream().read()) != -1) {
			System.out.write(c);
			if (++i == k)
				break;
		}
		System.out.println();
		System.out.println();	
		System.out.println();	
		
		System.out.println("wrote " + i + " bytes");
		
		
		socket.close();
	}

	public static void main(String[] argv) throws Exception {
		if (argv.length != 3) {
			
			System.out.println("java SimpleClient <host> <port> <name>");
			
			//System.exit(1);
		}
		String host = argv[0];
		
		int port = Integer.parseInt(argv[1]);
		
		String name = argv[2];
		
		new Client(host, port, name).execute();
		
	}
}
