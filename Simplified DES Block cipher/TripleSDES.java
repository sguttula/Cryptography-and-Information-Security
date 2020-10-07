
public class TripleSDES {

	public static void main(String[] args){
		
//		System.out.println("*****************************************************************");
//		System.out.println("         Part 2 Triple SDES Table");
//		System.out.println("*****************************************************************");
//		System.out.println("  Raw Key 1   |   Raw Key 2   |   Plain Text   |  Cipher Text   ");
//		System.out.println("*****************************************************************");
		EncryptTripleSDES();
		DecryptTripleSDES();
//		System.out.println("*****************************************************************");
	}

	private static void DecryptTripleSDES(){
		byte[][] encryptDemoByteKey1 = new byte[4][];
		byte[][] encryptDemoByteKey2 = new byte[4][];
		byte[][] encryptDemoBytePlainText = new byte[4][];
		byte[][] encryptDemoByteCipherText = new byte[4][];

		encryptDemoByteKey1[0] = new byte[]{ 1, 0, 0, 0, 1, 0, 1, 1, 1, 0 };
		encryptDemoByteKey1[1] = new byte[]{ 1, 0, 1, 1, 1, 0, 1, 1, 1, 1 };
		encryptDemoByteKey1[2] = new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		encryptDemoByteKey1[3] = new byte[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

		encryptDemoByteKey2[0] = new byte[]{ 0, 1, 1, 0, 1, 0, 1, 1, 1, 0 };
		encryptDemoByteKey2[1] = new byte[]{ 0, 1, 1, 0, 1, 0, 1, 1, 1, 0 };
		encryptDemoByteKey2[2] = new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		encryptDemoByteKey2[3] = new byte[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

		encryptDemoByteCipherText[0] = new byte[]{ 1, 1, 1, 0, 0, 1, 1, 0 };
		encryptDemoByteCipherText[1] = new byte[]{ 0, 1, 0, 1, 0, 0, 0, 0 };
		encryptDemoByteCipherText[2] = new byte[]{ 1, 0, 0, 0, 0, 0, 0, 0 };
		encryptDemoByteCipherText[3] = new byte[]{ 1, 0, 0, 1, 0, 0, 1, 0 };

		for(int i = 0; i < 4; i++){
			encryptDemoBytePlainText[i] = TripleSDES.Decrypt(encryptDemoByteKey1[i], encryptDemoByteKey2[i], encryptDemoByteCipherText[i]);
			showText(encryptDemoByteKey1[i]);
			System.out.print("       ");
			showText(encryptDemoByteKey2[i]);
			System.out.print("       ");
			showText(encryptDemoBytePlainText[i]);
			System.out.print("       ");
			showText(encryptDemoByteCipherText[i]);
			System.out.println();
		}
	}

	private static void EncryptTripleSDES(){
		byte[][] encryptDemoByteKey1 = new byte[4][];
		byte[][] encryptDemoByteKey2 = new byte[4][];
		byte[][] encryptDemoBytePlainText = new byte[4][];
		byte[][] encryptDemoByteCipherText = new byte[4][];

		encryptDemoByteKey1[0] = new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		encryptDemoByteKey1[1] = new byte[]{ 1, 0, 0, 0, 1, 0, 1, 1, 1, 0 };
		encryptDemoByteKey1[2] = new byte[]{ 1, 0, 0, 0, 1, 0, 1, 1, 1, 0 };
		encryptDemoByteKey1[3] = new byte[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

		encryptDemoByteKey2[0] = new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		encryptDemoByteKey2[1] = new byte[]{ 0, 1, 1, 0, 1, 0, 1, 1, 1, 0 };
		encryptDemoByteKey2[2] = new byte[]{ 0, 1, 1, 0, 1, 0, 1, 1, 1, 0 };
		encryptDemoByteKey2[3] = new byte[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

		encryptDemoBytePlainText[0] = new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0 };
		encryptDemoBytePlainText[1] = new byte[]{ 1, 1, 0, 1, 0, 1, 1, 1 };
		encryptDemoBytePlainText[2] = new byte[]{ 1, 0, 1, 0, 1, 0, 1, 0 };
		encryptDemoBytePlainText[3] = new byte[]{ 1, 0, 1, 0, 1, 0, 1, 0 };

		for(int i = 0; i < 4; i++){
			encryptDemoByteCipherText[i] = TripleSDES.Encrypt(encryptDemoByteKey1[i], encryptDemoByteKey2[i], encryptDemoBytePlainText[i]);
			showText(encryptDemoByteKey1[i]);
			System.out.print("       ");
			showText(encryptDemoByteKey2[i]);
			System.out.print("       ");
			showText(encryptDemoBytePlainText[i]);
			System.out.print("       ");
			showText(encryptDemoByteCipherText[i]);
			System.out.println();
		}
	}

	public static byte[] Encrypt( byte[] rawkey1, byte[] rawkey2, byte[] plaintext ){
		return SDES.Encrypt(rawkey1, SDES.Decrypt(rawkey2, SDES.Encrypt(rawkey1, plaintext)));
	}
	public static byte[] Decrypt( byte[] rawkey1, byte[] rawkey2, byte[] ciphertext ){
		return SDES.Decrypt(rawkey1, SDES.Encrypt(rawkey2, SDES.Decrypt(rawkey1, ciphertext)));
	}
	public static void showText(byte[] bytes){
		for(int i = 0; i < bytes.length; i++)
			System.out.print(bytes[i]);
	}
}
