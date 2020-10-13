
import java.io.FileInputStream;
import java.util.Properties;

import rsa.PrivateKey;
import security.SSLServerSocket;
import security.CryptoSocket;

public class Server implements Runnable {
	private SSLServerSocket server;
	private int port;
	private PrivateKey serverPrivateKey;
	private Properties users;

	public Server() throws Exception {
		
		port = 8080;
		String usersFileName = "users.txt";
		
		String privateKeyFileName = "private_key.txt";

		if (System.getProperty("server.port") != null) {
			
			port = Integer.parseInt(System.getProperty("server.port"));
		}
		if (System.getProperty("server.users") != null) {
			
			usersFileName = System.getProperty("server.users");
		}
		if (System.getProperty("private_key") != null) {
			
			privateKeyFileName = System.getProperty("private_key");
		}
		FileInputStream fileInputStream = new FileInputStream(privateKeyFileName);
		
		serverPrivateKey = new PrivateKey(fileInputStream);
		
		fileInputStream.close();
		
		fileInputStream = new FileInputStream(usersFileName);
		
		users = new Properties();
		
		users.load(fileInputStream);
		
		fileInputStream.close();
		
		server = new SSLServerSocket(port, serverPrivateKey, users);
	}

	public class RequestHandler implements Runnable {
		
		private CryptoSocket socket;

		private RequestHandler(CryptoSocket socket) {
			
			this.socket = socket;
		}

		public void run() {
			try {
				
				System.out.println("Connect.");
				int c;
			
				while ((c = socket.getInputStream().read()) != -1) {
					if (c >= 97 && c <= 122) {
						c -= 32;
					} else if (c >= 65 && c <= 90) {
						c += 32;
					}
					
					socket.getOutputStream().write(c);

					if (socket.getInputStream().available() == 0) {
						socket.getOutputStream().flush();
					}
				}
				socket.getOutputStream().flush();
				socket.close();
				
				System.out.println("Disconnect");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void run() {
		try {
			for (;;) {
				new Thread(new Server.RequestHandler((CryptoSocket) server.accept())).run();
			}
			
		} catch (Exception localException) {
			
			System.out.println("Server: " + localException);
		}
	}

	public static void main(String[] argv) throws Exception {
		
		new Server().run();
	}
}
