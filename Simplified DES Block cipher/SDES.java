package com.jeanchristophe.sdes;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class SDES {
	// 10 bits key
	private final BitSet masterKey;

	//sandboxes for fK function
	private static boolean [][][] S0 = new boolean[4][4][2];
	private static boolean [][][] S1 = new boolean[4][4][2];
	static{
		S0[0][0] = new boolean[]{false,true};
		S0[0][1] = new boolean[]{false,false};
		S0[0][2] = new boolean[]{true,true};
		S0[0][3] = new boolean[]{true,false};
		S0[1][0] = new boolean[]{true,true};
		S0[1][1] = new boolean[]{true,false};
		S0[1][2] = new boolean[]{false,true};
		S0[1][3] = new boolean[]{false,false};
		S0[2][0] = new boolean[]{false,false};
		S0[2][1] = new boolean[]{true,false};
		S0[2][2] = new boolean[]{false,true};
		S0[2][3] = new boolean[]{true,true};
		S0[3][0] = new boolean[]{true,true};
		S0[3][1] = new boolean[]{false,true};
		S0[3][2] = new boolean[]{true,true};
		S0[3][3] = new boolean[]{true,false};

		S1[0][0] = new boolean[]{false,false};
		S1[0][1] = new boolean[]{false,true};
		S1[0][2] = new boolean[]{true,false};
		S1[0][3] = new boolean[]{true,true};
		S1[1][0] = new boolean[]{true,false};
		S1[1][1] = new boolean[]{false,false};
		S1[1][2] = new boolean[]{false,true};
		S1[1][3] = new boolean[]{true,true};
		S1[2][0] = new boolean[]{true,true};
		S1[2][1] = new boolean[]{false,false};
		S1[2][2] = new boolean[]{false,true};
		S1[2][3] = new boolean[]{false,false};
		S1[3][0] = new boolean[]{true,false};
		S1[3][1] = new boolean[]{false,true};
		S1[3][2] = new boolean[]{false,false};
		S1[3][3] = new boolean[]{true,true};
	}

	/**
	 * Public constructor to keep track of the key
	 * and be able to encrypt and decrypt a character.
	 *
	 * @param key
	 * 	Length of the string must be 10 with only 0 and 1 character.
	 */
	public SDES(String userKey) {
		// Check the validity of the key.
		String regex = "[0,1]{10}";
		if (!userKey.matches(regex)) throw new IllegalArgumentException("Wrong key, must be a String representing a 10 bits key with only 0 and 1");

		// Convert the string key to a BitSet
		BitSet key = new BitSet(10);
		for(int i=0; i < 10; i++){
			if(userKey.charAt(i) == '1') key.set(i);
		}
		masterKey = key;
	}

	/**
	 * Usefull to test the code and see the BitSet key.
	 * @return
	 * 	Used clone method to not return the original BitSet and protect it.
	 */
	public BitSet getKey(){
		return (BitSet) this.masterKey.clone();
	}

	/**
	 * P10 permutation.
	 * @param key
	 * 	Original SDES key given by the user.
	 * @return
	 * 	A new key after the permutation
	 *	{0,1,2,3,4,5,6,7,8,9} (original key) to {2,4,1,6,3,9,0,8,7,5} (new key)
	 */
	private BitSet p10(final BitSet key){
		BitSet permutedKey = new BitSet(10);
		changeBit(permutedKey, 0, key.get(2));
		changeBit(permutedKey, 1, key.get(4));
		changeBit(permutedKey, 2, key.get(1));
		changeBit(permutedKey, 3, key.get(6));
		changeBit(permutedKey, 4, key.get(3));
		changeBit(permutedKey, 5, key.get(9));
		changeBit(permutedKey, 6, key.get(0));
		changeBit(permutedKey, 7, key.get(8));
		changeBit(permutedKey, 8, key.get(7));
		changeBit(permutedKey, 9, key.get(5));
		return permutedKey;
	}

	/**
	 * Circular left shift of a BitSet from n bits.
	 * @param key
	 * @param shit
	 * 	The number of bits of the shift (1,2,3, ...)
	 * @return
	 * 	The new key after the shift.
	 */
	private BitSet circularLeftShift(final BitSet key, int shift){
		BitSet s = new BitSet(10);
		// left part of the key
		for (int i=0; i < 5; i++){
			boolean value = key.get(i);
			int shiftedIndex = i - shift;
			if (shiftedIndex < 0){
				if(value){
					s.set(5 + shiftedIndex);
				}else{
					s.clear(5 + shiftedIndex);
				}
			}else{
				if(value){
					s.set(shiftedIndex);
				}else{
					s.clear(shiftedIndex);
				}
			}
		}
		// right part of the key
		for (int i=5; i < 10; i++){
			boolean value = key.get(i);
			int shiftedIndex = i - shift;
			if (shiftedIndex < 5){
				if(value){
					s.set(5+shiftedIndex);
				}else{
					s.clear(5+shiftedIndex);
				}
			}else{
				if(value){
					s.set(shiftedIndex);
				}else{
					s.clear(shiftedIndex);
				}
			}
		}
		return s;
	}

	/**
	 * P8 permutation.
	 * @param key
	 * 	10 bits key
	 * @return
	 * 	A new key of 8 bits after the permutation
	 *	{0,1,2,3,4,5,6,7,8,9} (key) to {5,2,6,3,7,4,9,8} (new key)
	 */
	private BitSet p8(final BitSet key){
		BitSet p8 = new BitSet(8);
		changeBit(p8, 0, key.get(5));
		changeBit(p8, 1, key.get(2));
		changeBit(p8, 2, key.get(6));
		changeBit(p8, 3, key.get(3));
		changeBit(p8, 4, key.get(7));
		changeBit(p8, 5, key.get(4));
		changeBit(p8, 6, key.get(9));
		changeBit(p8, 7, key.get(8));
		return p8;
	}

	/**
	 * Generate the keys for S-DES
	 * The key K of S-DES is a 10-bit vector. K is transformed using P10, yielding a 10-bit vector
	 * In the first round,
	 * Both part of p10 are rotated on the left by one and then transformed using P8, yielding K1
	 * In the second round,
	 * C1 are rotated on the left by two and then transformed using P8, yielding K2
	 *
	 * @return
	 * 	 K1 and K2 are the round keys for the first two rounds, and are used as input of the round functions.
	 */
	private List<BitSet> generateKeys(){
		BitSet p10 = p10(this.getKey());

		BitSet c1 = circularLeftShift(p10, 1);

		BitSet k1 = p8(c1);

		BitSet c2 = circularLeftShift(c1,2);

		BitSet k2 = p8(c2);

		List<BitSet> keys = new ArrayList<>();
		keys.add(k1);
		keys.add(k2);
		return keys;
	}

	/**
	 * ip permutes an 8 bits BitSet.
	 * @param plainText
	 * 	8 bits representing a char.
	 * @return
	 * 	The permuted 8 bits.
	 */
	private BitSet ip(final BitSet plainText){
		BitSet ip = new BitSet(8);
		changeBit(ip, 0, plainText.get(1));
		changeBit(ip, 1, plainText.get(5));
		changeBit(ip, 2, plainText.get(2));
		changeBit(ip, 3, plainText.get(0));
		changeBit(ip, 4, plainText.get(3));
		changeBit(ip, 5, plainText.get(7));
		changeBit(ip, 6, plainText.get(4));
		changeBit(ip, 7, plainText.get(6));
		return ip;
	}

	/**
	 * rip re-permutes an 8 bits p8 permuted BitSet.
	 * @param permutedText
	 * 	permuted 8 bits representing a p8 permutation.
	 * @return
	 * 	The original 8 bits.
	 */
	public BitSet rip(final BitSet permutedText){
		BitSet rip = new BitSet();
		changeBit(rip, 0, permutedText.get(3));
		changeBit(rip, 1, permutedText.get(0));
		changeBit(rip, 2, permutedText.get(2));
		changeBit(rip, 3, permutedText.get(4));
		changeBit(rip, 4, permutedText.get(6));
		changeBit(rip, 5, permutedText.get(1));
		changeBit(rip, 6, permutedText.get(7));
		changeBit(rip, 7, permutedText.get(5));
		return rip;
	}

	/**
	 * Expansion/Permutation
	 * @param input
	 * 	{0,1,2,3}
	 * @return
	 * 	{3,0,1,2,1,2,3,0}
	 */
	public BitSet ep(final BitSet input){
		BitSet ep = new BitSet(8);
		if(input.get(0)){
			ep.set(1);
			ep.set(7);
		}
		if(input.get(1)){
			ep.set(2);
			ep.set(4);
		}
		if(input.get(2)){
			ep.set(3);
			ep.set(5);
		}
		if(input.get(3)){
			ep.set(0);
			ep.set(6);
		}
		return ep;
	}

	private BitSet xor(final BitSet b1, final BitSet b2){
		BitSet x = (BitSet) b1.clone();
		x.xor(b2);
		return x;
	}

	/**
	 * Sandbox transformation function of the SDES.
	 * @param input
	 * 	4 bits BitSet
	 * @param s
	 * 	A representation of a Sandbox
	 * @return
	 * 	2 bits BitSet
	 */
	private BitSet sandbox(final BitSet input, final boolean [][][] s){
		boolean p0, p1, p2, p3;
		p0 = input.get(0);
		p1 = input.get(1);
		p2 = input.get(2);
		p3 = input.get(3);

		int line=0;
		if (!p0 && !p3) line = 0;
		if (!p0 && p3) line = 1;
		if (p0 && !p3) line = 2;
		if (p0 && p3) line = 3;

		int col=0;
		if (!p1 && !p2) col = 0;
		if (!p1 && p2) col = 1;
		if (p1 && !p2) col = 2;
		if (p1 && p2) col = 3;

		BitSet res = new BitSet(2);
		boolean[] value = s[line][col];
		if (value[0]){
			res.set(0);
		}
		if (value[1]){
			res.set(1);
		}
		return res;
	}

	private BitSet p4(final BitSet part1, final BitSet part2){
		BitSet p4 = new BitSet(4);
		changeBit(p4, 0, part1.get(1));
		changeBit(p4, 1, part2.get(1));
		changeBit(p4, 2, part2.get(0));
		changeBit(p4, 3, part1.get(0));
		return p4;
	}

	private BitSet f(BitSet right, BitSet sk){
		BitSet ep = ep(right);
		BitSet xor = xor(ep,sk);
		BitSet s0 = sandbox(xor.get(0, 4), S0);
		BitSet s1 = sandbox(xor.get(4, 8), S1);
		return p4(s0,s1);
	}

	private BitSet fK(BitSet bits, BitSet key){
		BitSet f = f(bits.get(4, 8), key);
		BitSet x = xor(bits.get(0, 4), f);
		BitSet c = new BitSet(8);
		for(int i=0; i<4;i++){
			if(x.get(i)){
				c.set(i);
			}
		}
		for(int i=4; i<8;i++){
			if(bits.get(i)){
				c.set(i);
			}
		}
		return c;
	}

	/**
	 * SW method of SDES.
	 *
	 * @param input
	 * 	The BitSet to invert. This is a private function of the SDES algorithm.
	 * 	The length must be 8 bits.
	 *
	 * @return
	 * 	The inverted BitSet, 4 last bits become the 4 first.
	 */
	private BitSet sw(BitSet input){
		BitSet inverse = new BitSet(8);
		for(int i=0; i<4;i++){
			if(input.get(i)){
				inverse.set(i+4);
			}
		}
		for(int i=4; i<8;i++){
			if(input.get(i)){
				inverse.set(i-4);
			}
		}
		return inverse;
	}

	/**
	 * Public method of SDES object to encrypt a character.
	 *
	 * @param c
	 * 	The character to encrypt
	 * @return
	 * 	The encrypted character.
	 */
	public char encrypt(char c){
		//Generate and get keys
		List<BitSet> keys = generateKeys();
		BitSet k1 = keys.get(0);
		BitSet k2 = keys.get(1);

		// Convert character to binary string
		// Doesn't put the 0 at the left, stop at the last 1.
		String binary = Integer.toBinaryString((int)c);
		if (binary.length() > 8) throw new IllegalArgumentException("Wrong charset, characters must be encoded with 8 bit");

		BitSet b = new BitSet(8);
		int index = 7;
		for(int i = binary.length()-1; i >= 0; i--){
			if (binary.charAt(i) == '1'){
				b.set(index);
			}
			index--;
		}

		BitSet ip = ip(b);
		BitSet fk1 = fK(ip, k1);
		BitSet inverse = sw(fk1);
		BitSet fk2 = fK(inverse, k2);
		BitSet enc = rip(fk2);

		StringBuilder res = new StringBuilder();
		for (int i=0; i<8; i++){
			if (enc.get(i)){
				res.append("1");
			}else{
				res.append("0");
			}
		}

		int i = Integer.parseInt(res.toString(), 2);
		return (char)i;
	}

	/**
	 * Public method of SDES object to decrypt an encoded character.
	 *
	 * @param c
	 * 	The character to decrypt.
	 * @return
	 * 	The original character.
	 */
	public char decrypt(char c){
		List<BitSet> keys = generateKeys();
		BitSet k1 = keys.get(0);
		BitSet k2 = keys.get(1);

		String binary = Integer.toBinaryString((int)c);
		if (binary.length() > 8) throw new IllegalArgumentException("Wrong charset, characters must be encoded with 8 bit");
		BitSet b = new BitSet(8);
		int index = 7;
		for(int i = binary.length()-1; i>=0; i--){
			if (binary.charAt(i) == '1'){
				b.set(index);
			}
			index--;
		}

		BitSet ip = ip(b);
		BitSet fk1 = fK(ip,k2);
		BitSet inverse = sw(fk1);
		BitSet fk2 = fK(inverse, k1);
		BitSet dec = rip(fk2);

		StringBuilder res = new StringBuilder();
		for (int i=0; i<8; i++){
			if (dec.get(i)){
				res.append("1");
			}else{
				res.append("0");
			}
		}
		int i = Integer.parseInt(res.toString(), 2);
		return (char)i;
	}

	/**
	 * Helper function to change bits (boolean values) in a BitSet.
	 *
	 * @param bitset
	 * 	The BitSet that will be modified.
	 * @param index
	 * 	Index of the BitSet value to modify.
	 * @param newValue
	 */
    private void changeBit(BitSet bitset, int index, boolean newValue){
		if (newValue){
			// True
			bitset.set(index);
		}else{
			// False
			bitset.clear(index);
		}
	}

    /**
     * Helper function to print the BitSet with 0 and 1.
     * The BitSet toString() method only print the index of the 1 bit.
     *
     * @param bitSet
     * 	The bitset to print under the form "010101..."
     * @param size
     * 	Useful to print the 0 at the left.
     */
    public void printBitSet(BitSet bitSet, int size){
    	StringBuilder builder = new StringBuilder();
    	for(int i=0; i<size; i++){
    		if (bitSet.get(i)){
    			builder.append("1");
    		}else{
    			builder.append("0");
    		}
    	}
    	System.out.println(builder.toString());
    }
} 
