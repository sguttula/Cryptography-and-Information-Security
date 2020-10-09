package project2;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class KeyGen {
	private BigInteger p;  			
	private BigInteger q; 				
	private BigInteger n; 		
	private BigInteger fi_N;    			
	private BigInteger e; 		
	private BigInteger d;					
	
	public KeyGen(){
		
		p = BigInteger.probablePrime(512, new Random());
		q = BigInteger.probablePrime(512, new Random());
		
		while(p.equals(q)){
			q = BigInteger.probablePrime(512, new Random());
		}
		
		// n = p x q
		n = p.multiply(q);
		
		fi_N = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		
		
		e  = BigInteger.probablePrime(512, new Random());
		while(true){
			if(!e.equals(p) && !e.equals(q) && (e.gcd(fi_N)).equals(BigInteger.ONE)){
				break;
			}
			e  = BigInteger.probablePrime(512, new Random());
		}
		
		d = e.modInverse(fi_N);
	}

	public BigInteger getN() {
		return n;
	}

	public BigInteger getfi_N() {
		return fi_N;
	}

	public BigInteger getE() {
		return e;
	}

	public BigInteger getD() {
		return d;
	}

	public List<BigInteger> getPublic(){
		
		List<BigInteger> pubKey = new ArrayList<BigInteger>();
		
		pubKey.add(e);
		pubKey.add(n);
		
		return pubKey;
	}
	
	public List<BigInteger> getPrivate(){
		
		List<BigInteger> privKey = new ArrayList<BigInteger>();
		
		privKey.add(d);
		privKey.add(n);
		
		return privKey;
	}
	
	public void publicKeyToFile(){
		try(OutputStream output = new FileOutputStream("pubkey.rsa");
			ObjectOutputStream output1 = new ObjectOutputStream(output);){
			output1.writeObject(this.getE());
			output1.writeObject(this.getN());
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void privateKeyToFile(){
		try(OutputStream output = new FileOutputStream("privkey.rsa");
			ObjectOutputStream output1 = new ObjectOutputStream(output);){
			output1.writeObject(this.getD());
			output1.writeObject(this.getN());
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public List<BigInteger> readKeyFile(String file){
		List<BigInteger> k = new ArrayList<BigInteger>();
		try(InputStream input = new FileInputStream(file);
			ObjectInputStream input1 = new ObjectInputStream(input);){
			
			k.add( (BigInteger) input1.readObject()  );
			k.add( (BigInteger) input1.readObject()  );
			
		}
		catch(Exception e){
			System.out.println("Cannot perform the function without a key!");
		}
		
		return k;
	}
	
	public static void main(String[] args){
		KeyGen keyGen = new KeyGen();
		
		System.out.println("E: " + keyGen.getE());
		System.out.println("E is prime: " + keyGen.getE().isProbablePrime(500)+ "\n");

		System.out.println("D: " + keyGen.getD());

		System.out.println("N: " + keyGen.getN());
		
		keyGen.publicKeyToFile();
		keyGen.privateKeyToFile();
		
		
		List<BigInteger> pubKey = keyGen.readKeyFile("pubkey.rsa");
		List<BigInteger> privKey = keyGen.readKeyFile("privkey.rsa");
		
		System.out.println("Comparing public E: " + pubKey.get(0).equals(keyGen.getE()));
		System.out.println("Comparing public N: " + pubKey.get(1).equals(keyGen.getN()));
		
		System.out.println();		
		System.out.println("Comparing private D: " + privKey.get(0).equals(keyGen.getD()));
		System.out.println("Comparing private N: " + privKey.get(1).equals(keyGen.getN()));
		
	}
}