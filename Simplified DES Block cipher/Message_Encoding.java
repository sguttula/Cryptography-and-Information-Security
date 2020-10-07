

public class Message_Encoding {
	
	public static void main(String[] args){
		
		Part2();
		Part3(); // This takes a little while to finish
	}

	
	private static void Part2(){
		System.out.println("***** Part 2 *******");

		String encrypted = "1011011001111001001011101111110000111110100000000001110111010001111011111101101100010011000000101101011010101000101111100011101011010111100011101001010111101100101110000010010101110001110111011111010101010100001100011000011010101111011111010011110111001001011100101101001000011011111011000010010001011101100011011110000000110010111111010000011100011111111000010111010100001100001010011001010101010000110101101111111010010110001001000001111000000011110000011110110010010101010100001000011010000100011010101100000010111000000010101110100001000111010010010101110111010010111100011111010101111011101111000101001010001101100101100111001110111001100101100011111001100000110100001001100010000100011100000000001001010011101011100101000111011100010001111101011111100000010111110101010000000100110110111111000000111110111010100110000010110000111010001111000101011111101011101101010010100010111100011100000001010101110111111101101100101010011100111011110101011011";
		byte[] key = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		byte[] plaintext;
		byte[] ciphertext = toByteArray(encrypted);
		


		for(int i = 0; i < 1024; i++){
			plaintext = SDES.Decrypt(key, ciphertext);
			String str = CASCII.toString(plaintext);
		
			if(str.contains("THE") && followsPunctuationRules(str)){
				System.out.print("key: ");
				printArray(key);
				System.out.println(str);
			}
			
			incrementKey(key);
		}
	}


	private static void Part3(){
		System.out.println(" \n ***** Part 3 *******");
		String encrypted = "00011111100111111110011111101100111000000011001011110010101010110001011101001101000000110011010111111110000000001010111111000001010010111001111001010101100000110111100011111101011100100100010101000011001100101000000101111011000010011010111100010001001000100001111100100000001000000001101101000000001010111010000001000010011100101111001101111011001001010001100010100000";

		byte[] key1 = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		byte[] key2 = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		byte[] plaintext;
		byte[] ciphertext = toByteArray(encrypted);


		for(int i = 0; i < 1024; i++){
			for(int j = 0; j < 1024; j++){
				plaintext = TripleSDES.Decrypt(key1, key2, ciphertext);
				
				String str = CASCII.toString(plaintext);
				
				// only print strings that contain the substring "THE" and follow some of the sample letter periodicidies
				if(str.contains("THE") && followsPunctuationRules(str)){
					System.out.print("key1: ");
					printArray(key1);
					System.out.print("  key2: ");
					printArray(key2);
					System.out.println(str);
				}
				incrementKey(key1);
			}
			incrementKey(key2);
		}
	}
	
	private static boolean followsPunctuationRules(String str){
		for(int i = 0; i < str.length() - 1; i++){
			char c = str.charAt(i);
			if(c == '.' || c == '?' || c == ',' || c == ':'){
				if(str.charAt(i + 1) != ' ')
					return false;
			}
		}
		return true;
	}
	
	/*
	private static boolean followsCharacterRules(String str){
		return countChars(str, 'E') >= countChars(str, 'T') 
				&& countChars(str, 'T') >= countChars(str, 'S') 
				&& countChars(str, 'S') >= countChars(str, 'L') 
				&& countChars(str, 'L') >= countChars(str, 'F') 
				&& countChars(str, 'F') >= countChars(str, 'X') 
				&& countChars(str, 'F') >= countChars(str, 'Z') 
				&& countChars(str, 'A') >= countChars(str, 'C');
	}
	


	private static int countChars(String str, char c){
		int count = 0;
		
		for(int i = 0; i < str.length(); i++){
			if(str.charAt(i) == c)
				count++;
		}
		return count;
	}*/

	public static void printArray(byte[] array){
		for(int i = 0; i < array.length; i++)
			System.out.print(array[i] + " ");
	}


	private static byte[] toByteArray (String message){
		byte[] temp = new byte[message.length()];
		for(int i = 0; i < message.length(); i++){
			temp[i] = (message.charAt(i) == '1') ? (byte)1 : (byte)0;
		}
		return temp;
	}

	private static void incrementKey (byte[] key){
		if (key[9] == 0) {
			key[9] = 1;
			return;
		}
		else{
			key[9] = 0;
		}

		if (key[8] == 0) {
			key[8] = 1;
			return;
		}
		else{
			key[8] = 0;
		}


		if (key[7] == 0) {
			key[7] = 1;
			return;
		}
		else{
			key[7] = 0;
		}

		if (key[6] == 0) {
			key[6] = 1;
			return;
		}
		else{
			key[6] = 0;
		}

		if (key[5] == 0) {
			key[5] = 1;
			return;
		}
		else{
			key[5] = 0;
		}

		if (key[4] == 0) {
			key[4] = 1;
			return;
		}
		else{
			key[4] = 0;
		}

		if (key[3] == 0) {
			key[3] = 1;
			return;
		}
		else{
			key[3] = 0;
		}

		if (key[2] == 0) {
			key[2] = 1;
			return;
		}
		else{
			key[2] = 0;
		}

		if (key[1] == 0) {
			key[1] = 1;
			return;
		}
		else{
			key[1] = 0;
		}

		if (key[0] == 0) {
			key[0] = 1;
			return;
		}
		else{
			key[0] = 0;
		}
	}

}
