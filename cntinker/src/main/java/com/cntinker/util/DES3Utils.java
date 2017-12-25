package com.cntinker.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * 三重加密 3DES也作 Triple DES,
 */
public class DES3Utils {
private static final String ALGORITHM = "DESede";
    
    //默认为 DESede/ECB/PKCS5Padding
    private static final String CIPHER_TRANSFORMAT = "DESede/ECB/PKCS5Padding";
         
    public static String encryptToBase64(String plainText, String key, String encodeing) throws Exception {
        SecretKey deskey = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher c1 = Cipher.getInstance(CIPHER_TRANSFORMAT);
        c1.init(Cipher.ENCRYPT_MODE, deskey);
        byte[] result = c1.doFinal(plainText.getBytes(encodeing));     
        return Base64.encodeBase64String(result);
     }
   
    public static String decryptFromBase64(String base64, String key, String encodeing) throws Exception {
        SecretKey deskey = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher c1 = Cipher.getInstance(CIPHER_TRANSFORMAT);
        c1.init(Cipher.DECRYPT_MODE, deskey);
        byte[] result = c1.doFinal(Base64.decodeBase64(base64));      
        return new String(result, encodeing);
    }
	public static void main(String args[]) {
		
	}
}