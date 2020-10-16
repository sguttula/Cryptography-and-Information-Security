import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class MyRSA {
    
    public static void main(String [] args) throws Exception {
        // generate public and private keys
        KeyPair keyPair = buildKeyPair();
        PublicKey pubKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        
        // sign the message
        byte [] signed = encrypt(pubKey, "chandu");
        
       // System.out.println(new String(signed));  // <<signed message>>
        System.out.println(new String(signed).toString());  // <<signed message>>
        
        
        // verify the message
        byte[] verified = decrypt(privateKey, signed);                                 
        System.out.println(new String(verified));     // This is a secret message
    }
   // public static void savePublicKey(String fileName)
    //{
    	
   // }
    public static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
        final int keySize = 2048;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);      
        return keyPairGenerator.genKeyPair();
    }

    public static byte[] encrypt(PublicKey publicKey, String message) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
        
        byte[] enc= cipher.doFinal(message.getBytes());  
        System.out.println(enc.length);
        return enc;
    }
    
    public static byte[] decrypt(PrivateKey privateKey, byte [] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        
        return cipher.doFinal(encrypted);
    }
}