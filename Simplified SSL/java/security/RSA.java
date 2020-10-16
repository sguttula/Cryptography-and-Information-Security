package security;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import rsa.Key;
import rsa.KeyPair;
import rsa.PrivateKey;
import rsa.PublicKey;

public class RSA {
	
	
	
	public static void main(String argv[]) throws Exception {
		
		
		
		List<String> args = Arrays.asList(argv);
		
		if (args.contains("-help")) {
			
			
			System.out.println("java security.RSA -help \n" + "   - this message\n" + "\n" + "java security.RSA -gen [ <text> ]\n" + "   - generate private (KR) and public (KU) keys\n"+ "     and test them on <text> (optional)");
			
			System.exit(0);
			
		// System.out.println("check");
			
			
		} else if (!args.contains("-gen")) {
			System.out.println("java security.RSA -help");
			
			System.exit(0);
		}

		int primeSize = 256;
		if (System.getProperty("prime_size") != null) {
			
			primeSize = Integer.parseInt(System.getProperty("prime_size"));
			if (primeSize < 180 || primeSize > 1024) {
				System.out.println("Prime Size is set to too low or too high! (prime_size <= 180 || prime_size >= 1024)");
				primeSize = 256;
			}
		}
		System.out.println("Prime Size is set to " + primeSize);
		
		KeyPair keyPair = generateKeys(primeSize);
		
		
		System.out.println("KR=" + keyPair.getPrivateKey().toString());
		System.out.println("KU=" + keyPair.getPublicKey().toString());
		if (args.size() != 1) {
			System.out.println("KU(KR(M))="
					+ new String(cipher(cipher(args.get(1), keyPair.getPrivateKey()), keyPair.getPublicKey())));
			System.out.println("KR(KU(M))="
					+ new String(cipher(cipher(args.get(1), keyPair.getPublicKey()), keyPair.getPrivateKey())));
		}
	}

	
//public static BigInteger unsignedByteArrayToBigInteger(byte[] value) {
//		
//		byte[] signedValue = new byte[value.length + 1];
//		
//		
//	}
//	public static KeyPair generateKeys(int primeSize) {
//		Random rnd = new Random();
//			byte[] signedValue = new byte[value.length + 1];
//		
//	}



	public static KeyPair generateKeys(int primeSize) {
		Random rnd = new Random();
		BigInteger p = BigInteger.probablePrime(primeSize, rnd);
		BigInteger q = p.nextProbablePrime();
		BigInteger n = p.multiply(q);
		
		//Generate Key Pairs
		
		//  Multiply (p-1) x (q-1)
		BigInteger tn = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		
		
		
		Random rnd1 = new Random();
		
		
		BigInteger e = BigInteger.probablePrime(tn.bitLength() - 1, rnd1);
		if (!tn.gcd(e).equals(BigInteger.ONE)) {
			System.out.println("tn and e's are not relative prime");
			while (!tn.gcd(e).equals(BigInteger.ONE)) {
				e = BigInteger.probablePrime(tn.bitLength() - 1, rnd1);
			}
		}
		
		BigInteger d = e.modInverse(tn);
//		System.out.println(d);
//		
		
		
		return new KeyPair(new PrivateKey(d, n), new PublicKey(e, n));
	}

	public static BigInteger unsignedByteArrayToBigInteger(byte[] value) {
		
		byte[] signedValue = new byte[value.length + 1];
		
		
		System.arraycopy(value, 0, signedValue, 1, value.length);
		return new BigInteger(signedValue);
	}

	public static byte[] cipher(byte unsignedM[], Key K) throws Exception {
		byte[] signedM = unsignedByteArrayToBigInteger(unsignedM).modPow(K.getKey(), K.getN()).toByteArray();
		if (signedM[0] == 0) {
			
			
			
			byte[] droppedSignedM = new byte[signedM.length - 1];
			System.arraycopy(signedM, 1, droppedSignedM, 0, signedM.length - 1);
			return droppedSignedM;
		}
		return signedM;
	}

	public static byte[] cipher(String s, Key K) throws Exception {
		return cipher(s.getBytes(Charset.forName("UTF-8")), K);
	}
}
