
public class SDES_Encoding 
{
	
	public static void main(String[] args)
	{
		Part1();
	}
	private static void Part1(){
		System.out.println("***** Part 1 *******");

		byte[] key = { 0, 1, 1, 1, 0, 0, 1, 1, 0, 1 };
		String message = "CRYPTOGRAPHY";
		byte[] plaintext = CASCII.Convert(message);
		byte[] ciphertext = SDES.Encrypt(key, plaintext);

		System.out.println("Message:");
		System.out.println(message);
		System.out.println();

		System.out.println("plaintext CASCII bits:");
		Message_Encoding.printArray(plaintext);
		System.out.println();

		System.out.println("ciphertext CASCII bits: ");
		Message_Encoding.printArray(ciphertext);
		System.out.println();

		String output = CASCII.toString(ciphertext);
		System.out.println("Encrypted message:");
		System.out.println(output);
		System.out.println();

		byte[] decrypt = SDES.Decrypt(key, ciphertext);
		System.out.print("Decrypt text:");
		Message_Encoding.printArray(decrypt);
		System.out.println();

		String output2 = CASCII.toString(decrypt);
		System.out.println("Decrypt message");
		System.out.println(output2);
		System.out.println();
	}



}
