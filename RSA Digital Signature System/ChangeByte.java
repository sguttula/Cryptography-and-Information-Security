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
import project2.DigitalSignature;
import java.util.Scanner;

public class ChangeByte {
	byte[] message;

	public byte[] change_array(byte[] p, byte[] q) {
		byte[] r = new byte[p.length + q.length];
		System.arraycopy(p, 0, r, 0, p.length);
		System.arraycopy(q, 0, r, p.length, q.length);
		return r;
	}


	public void update_byte(File file1) { 
		Scanner s = new Scanner(System.in);
		try(InputStream input = new FileInputStream(file1);
				ObjectInputStream o = new ObjectInputStream(input);){

			int i;
			BigInteger b;
			byte[] m = new byte[0];


			b = (BigInteger) o.readObject();
			while((i = input.read()) != -1){
				byte[] message_1 = {(byte) i};
				
				m = change_array(m, message_1);
			}
			String string = new String(m);
			message = string.getBytes();
			int k = 0;
			
			do {
				System.out.println("what byte do you want to change?");
				k = s.nextInt();
			}
			while(k < 0 || k >= m.length);
			m[k] = 0;

			OutputStream output = new FileOutputStream("test.txt.signed");
			ObjectOutputStream output1 = new ObjectOutputStream(output);
			output1.writeObject(b);
			output.write(m);
			output.flush();
			output.close();
			input.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}

	}

	public static void main(String[] args){
	}
}