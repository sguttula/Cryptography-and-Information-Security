package security;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class OneTimeKey {

	public static void main(String argv[]) throws Exception {
		
		
		if (argv.length != 2) {
			System.out.println("java security.OneTimeKey <key>  <text> [ <text> ... ]");
			
			
		} else {
			
			
			System.out.println("Original Message is " + argv[1]);
			
			byte[] encodedM = xor(argv[1].getBytes(), argv[0].getBytes());
			
			System.out.println("Encoded to " + new String(encodedM));
			
			byte[] decodedM = xor(encodedM, argv[0].getBytes());
			
			byte[] decodedMessage = new byte[argv[1].length()];
			
			System.arraycopy(argv[1].getBytes(), 0, decodedMessage, 0, argv[1].getBytes().length);
			
			System.arraycopy(decodedM, 0, decodedMessage, 0, decodedM.length);
			
			System.out.println("Decoded to " + new String(decodedMessage));
		}
	}

	public static byte[] newKey(Random r, int n) {
		
		byte[] rBytes = new byte[n];
		r.nextBytes(rBytes);
		
		return rBytes;
	}

	public static byte[] newKey(int n) {
		
		Random r = new Random(System.currentTimeMillis());
		
		return newKey(r, n);
	}
	
	
	
	public static void printKey(byte[] b, OutputStream os) throws IOException {
		
		byte[] k = newKey(b.length);
		
		os.write(xor(b, k));
	}

	public static byte[] xor(byte[] messageData, byte[] oneTimeKey) {
		if (messageData.length % oneTimeKey.length != 0) {
			
			throw new RuntimeException("Size of Message is not divisible by key ");
		}
		
		
		byte[] xorMessageData = new byte[messageData.length];
		
		
		System.arraycopy(messageData, 0, xorMessageData, 0, messageData.length);
		int j = 0;
		for (int i = 0; i < messageData.length / oneTimeKey.length; i++) {
			
			for (int k = 0; k < oneTimeKey.length; k++) {
				
				xorMessageData[j] = ((byte) (xorMessageData[j] ^ oneTimeKey[k]));
				// k++;
				j++;
			}
		}
		return xorMessageData;
	}

	
}
