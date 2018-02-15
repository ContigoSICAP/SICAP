/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mantenimiento;

import javax.crypto.Cipher;  
import javax.crypto.SecretKey;  
import javax.crypto.KeyGenerator;  
// KEY SPECIFICATIONS  
import java.security.spec.KeySpec;  
import java.security.spec.AlgorithmParameterSpec;  
import javax.crypto.spec.PBEKeySpec;  
import javax.crypto.SecretKeyFactory;  
import javax.crypto.spec.PBEParameterSpec;  
// EXCEPTIONS  
import java.security.InvalidAlgorithmParameterException;  
import java.security.NoSuchAlgorithmException;  
import java.security.InvalidKeyException;  
import java.security.spec.InvalidKeySpecException;  
import javax.crypto.NoSuchPaddingException;  
import javax.crypto.BadPaddingException;  
import javax.crypto.IllegalBlockSizeException;  
import java.io.UnsupportedEncodingException;  
import java.io.IOException;  


/**
 *
 * @author Alex
 */
public class StringEncrypter {
    Cipher ecipher;
    Cipher dcipher;  
/**  
72      * Constructor used to create this object.  Responsible for setting  
73      * and initializing this object's encrypter and decrypter Chipher instances  
74      * given a Secret Key and algorithm.  
75      * @param key        Secret Key used to initialize both the encrypter and  
76      *                   decrypter instances.  
77      * @param algorithm  Which algorithm to use for creating the encrypter and  
8      *                   decrypter instances.  
79      */ 
    StringEncrypter(SecretKey key, String algorithm) {
        try {
            ecipher = Cipher.getInstance(algorithm);
            dcipher = Cipher.getInstance(algorithm);
            ecipher.init(Cipher.ENCRYPT_MODE, key);
            dcipher.init(Cipher.DECRYPT_MODE, key);
        } catch (NoSuchPaddingException e) {
            System.out.println("EXCEPTION: NoSuchPaddingException");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("EXCEPTION: NoSuchAlgorithmException");
        } catch (InvalidKeyException e) {
            System.out.println("EXCEPTION: InvalidKeyException");
        }
    }  
/**  
96      * Constructor used to create this object.  Responsible for setting  
97      * and initializing this object's encrypter and decrypter Chipher instances  
98      * given a Pass Phrase and algorithm.  
99      * @param passPhrase Pass Phrase used to initialize both the encrypter and  
100      *                   decrypter instances.  
101      */ 
    StringEncrypter(String passPhrase) {  
// 8-bytes Salt  
        byte[] salt = {
            (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
            (byte)0x56, (byte)0x34, (byte)0xE3, (byte)0x03
        };  
// Iteration count  
        int iterationCount = 19;
        try {
            KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
            ecipher = Cipher.getInstance(key.getAlgorithm());
            dcipher = Cipher.getInstance(key.getAlgorithm());  
// Prepare the parameters to the cipthers
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        } catch (InvalidAlgorithmParameterException e) {
            System.out.println("EXCEPTION: InvalidAlgorithmParameterException");
        } catch (InvalidKeySpecException e) {
            System.out.println("EXCEPTION: InvalidKeySpecException");
        } catch (NoSuchPaddingException e) {
            System.out.println("EXCEPTION: NoSuchPaddingException");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("EXCEPTION: NoSuchAlgorithmException");
        } catch (InvalidKeyException e) {
            System.out.println("EXCEPTION: InvalidKeyException");
        }
    }  
/**  
141      * Takes a single String as an argument and returns an Encrypted version  
142      * of that String.  
143      * @param str String to be encrypted  
144      * @return <code>String</code> Encrypted version of the provided String  
145      */
    public String encrypt(String str) {
        try {
            // Encode the string into bytes using utf-8
            byte[] utf8 = str.getBytes("UTF8");  
// Encrypt  

            byte[] enc = ecipher.doFinal(utf8);  
// Encode bytes to base64 to get a string  

            return new sun.misc.BASE64Encoder().encode(enc);
        } catch (BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (IOException e) {
        }
        return null;
    }  
/**  
166      * Takes a encrypted String as an argument, decrypts and returns the  
167      * decrypted String.  
168      * @param str Encrypted String to be decrypted  
169      * @return <code>String</code> Decrypted version of the provided String  
170      */
    public String decrypt(String str) {
        try {  
// Decode base64 to get bytes
            byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);  
// Decrypt  
            byte[] utf8 = dcipher.doFinal(dec);  
// Decode using utf-8
            return new String(utf8, "UTF8");
        } catch (BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (IOException e) {
        }
        return null;
    }  
/**  
93      * The following method is used for testing the String Encrypter class.  
194      * This method is responsible for encrypting and decrypting a sample  
195      * String using several symmetric temporary Secret Keys.  
196      */
    public static void testUsingSecretKey() {
        try {
            System.out.println();
            System.out.println("+----------------------------------------+");
            System.out.println("|  -- Test Using Secret Key Method --    |");
            System.out.println("+----------------------------------------+");
            System.out.println();
            String secretString = "Cadena a enviar mediante URL co totales a 12";  
// Generate a temporary key for this example. In practice, you would  
// save this key somewhere. Keep in mind that you can also use a  
// Pass Phrase.
            SecretKey desKey       = KeyGenerator.getInstance("DES").generateKey();
            SecretKey blowfishKey  = KeyGenerator.getInstance("Blowfish").generateKey();
            SecretKey desedeKey    = KeyGenerator.getInstance("DESede").generateKey();  
// Create encrypter/decrypter class
            StringEncrypter desEncrypter = new StringEncrypter(desKey, desKey.getAlgorithm());
            StringEncrypter blowfishEncrypter = new StringEncrypter(blowfishKey, blowfishKey.getAlgorithm());
            StringEncrypter desedeEncrypter = new StringEncrypter(desedeKey, desedeKey.getAlgorithm());  
// Encrypt the string
            String desEncrypted       = desEncrypter.encrypt(secretString);
            String blowfishEncrypted  = blowfishEncrypter.encrypt(secretString);
            String desedeEncrypted    = desedeEncrypter.encrypt(secretString);  
// Decrypt the string
            String desDecrypted       = desEncrypter.decrypt(desEncrypted);
            String blowfishDecrypted  = blowfishEncrypter.decrypt(blowfishEncrypted);
            String desedeDecrypted    = desedeEncrypter.decrypt(desedeEncrypted);  
// Print out values
            System.out.println(desKey.getAlgorithm() + " Encryption algorithm");
            System.out.println("    Original String  : " + secretString);
            System.out.println("    Encrypted String : " + desEncrypted);
            System.out.println("    Decrypted String : " + desDecrypted);
            System.out.println();
            System.out.println(blowfishKey.getAlgorithm() + " Encryption algorithm");
            System.out.println("    Original String  : " + secretString);
            System.out.println("    Encrypted String : " + blowfishEncrypted);
            System.out.println("    Decrypted String : " + blowfishDecrypted);
            System.out.println();
            System.out.println(desedeKey.getAlgorithm() + " Encryption algorithm");
            System.out.println("    Original String  : " + secretString);
            System.out.println("    Encrypted String : " + desedeEncrypted);
            System.out.println("    Decrypted String : " + desedeDecrypted);
            System.out.println();
        } catch (NoSuchAlgorithmException e) {}
    }  
/**  
254      * The following method is used for testing the String Encrypter class.  
255      * This method is responsible for encrypting and decrypting a sample  
256      * String using using a Pass Phrase.  
257      */
    public static void testUsingPassPhrase() {
        System.out.println();
        System.out.println("+----------------------------------------+");
        System.out.println("|  -- Test Using Pass Phrase Method --   |");
        System.out.println("+----------------------------------------+");
        System.out.println();
        String secretString = "Attack at dawn!";
        String passPhrase   = "My Pass Phrase";  
// Create encrypter/decrypter class
        StringEncrypter desEncrypter = new StringEncrypter(passPhrase);  
// Encrypt the string
        String desEncrypted       = desEncrypter.encrypt(secretString);  
// Decrypt the string
        String desDecrypted       = desEncrypter.decrypt(desEncrypted);  
// Print out values
        System.out.println("PBEWithMD5AndDES Encryption algorithm");
        System.out.println("    Original String  : " + secretString);
        System.out.println("    Encrypted String : " + desEncrypted);
        System.out.println("    Decrypted String : " + desDecrypted);
        System.out.println();
    }  

}
