package project2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;
import project2.KeyGen;

public class DigitalSignature {
	
	byte[] d, m1, m2;
	BigInteger bi;
	KeyGen g;
	
	List<BigInteger> x;
	
	public byte[] appendArray(byte[] p, byte[] q) {
		byte[] r = new byte[p.length + q.length];
		System.arraycopy(p, 0, r, 0, p.length);
		System.arraycopy(q, 0, r, p.length, q.length);
		return r;
	}
	
	public byte[] remove(byte[] list){
		int counter = 0;

        for(int k = 0; k < list.length ; k++)
        {

            if(list[k]!=0){ 
                break;
            }
            counter++;
        }
        byte [] o = new byte[list.length-counter];
        for(int k = 0; k<(list.length-counter);k++) {
            o[k] = list[k+counter];
        }
        return o; 
	}
	
	public BigInteger convertbig(byte[] array){
		BigInteger big = new BigInteger(1, array);
			
		
		big = big.modPow(x.get(0), x.get(1));
		return big;
	}
	
	public byte[] convertdigest(BigInteger signature){
		
		BigInteger big = signature.modPow(x.get(0), x.get(1));
		byte[] d = big.toByteArray();
		
		return d;
	}
	
	public void sign(byte[] m, BigInteger big){
		try(
				OutputStream output = new FileOutputStream("test.txt.signed");
				ObjectOutputStream output2 = new ObjectOutputStream(output);){
				
			output2.writeObject(big);
				
				output.write(m);
				output.flush();
				output.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
	}

public void sender(File file1) throws Exception{
		
		g = new KeyGen();
		MessageDigest message = MessageDigest.getInstance("MD5");
		
		try(FileInputStream input = new FileInputStream(file1);){
			int index;
			byte[] m = new byte[0];
			
			while((index = input.read()) != -1){
				byte[] b1 = {(byte) index};
				m = appendArray(m, b1);
			}
			String new_message = new String(m);
			m1 = new_message.getBytes();
			
			message.update(m1);
			d = message.digest();
			
			x = g.readKeyFile("privkey.rsa");
			if(x.isEmpty()){
				System.out.println("Function now exiting...");
			}
			else{
				
				bi = convertbig(d);
			
				sign(m, bi);
			}
			
		}catch(Exception e){ // catches the exception
			System.out.println("There was a problem signing the message!");
		}
		
	}
	public void receiver(File signature) throws Exception{
		
		g = new KeyGen();
		MessageDigest message = MessageDigest.getInstance("MD5");
		byte[] reverse;
		
		try(InputStream input = new FileInputStream(signature);
			ObjectInputStream output = new ObjectInputStream(input);){
			
			BigInteger big;
			
			byte[] m = new byte[0];
			int index;
			
			big = (BigInteger) output.readObject();
			while((index = input.read()) != -1){
				byte[] b1 = {(byte) index};
				
				m = appendArray(m, b1);
			}
			String new_message = new String(m);
			m2 = new_message.getBytes();

			input.close();
			
			x = g.readKeyFile("pubkey.rsa");
			
			if(x.isEmpty()){
				System.out.println("Function now exiting...");
			}
			else{
			
				reverse = convertdigest(big);
				byte[] updated = remove(reverse);
				
				
				message.update(m2);

				byte[] d = message.digest();
				System.out.println("The message received is " + (MessageDigest.isEqual(d, updated)? "valid!": "invalid!"));
			}
		}
		catch(Exception e){
			System.out.println("The file does not exist or it cannot be read!");
		}
	}
	
	public static void main(String args[]) throws Exception{
		
	}
}
