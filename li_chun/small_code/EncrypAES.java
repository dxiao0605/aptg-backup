package test;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;




public class EncrypAES {
	private static final String keyStr = "aptgbattery"; // 128 bit key
//	private static final String keyStr = "aptgcathaybkeco"; // 128 bit key
//	private static final String initVector = "taiweter1qaz2wsx"; // 16 bytes IV
	
//	private static final String AESkey = "taiweter1234qwer"; // 128 bit key
//	private static final String initVector = "taiweter1qaz2wsx"; // 16 bytes IV
	/**
	 * 加密
	 * 
	 * @param key
	 * @param initVector
	 * @param value
	 * @return String
	 */
	public static String encryptAES(String value) throws Exception {
		try {
//			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
//			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
//			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
//			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
//			byte[] encrypted = cipher.doFinal(value.getBytes());
//			return Base64.encodeBase64String(encrypted);
			
			
			
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(keyStr.getBytes());
            kgen.init(256, random);
            Key  key = kgen.generateKey();
			
			byte[] bytes = value.getBytes("UTF-8");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(bytes);
            return Base64.getEncoder().encodeToString(encrypted);
		} catch (Exception ex) {
			throw new Exception(ex.toString());
		}
	}

	/**
	 * 解密
	 * 
	 * @param key
	 * @param initVector
	 * @param encrypted
	 * @return String
	 */
	public static String decryptAES(String encrypted) throws Exception {
		try {
//			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
//			SecretKeySpec skeySpec = new SecretKeySpec(AESkey.getBytes("UTF-8"), "AES");
//			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
//			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
//			byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));
//			return new String(original);
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(keyStr.getBytes());
            kgen.init(256, random);
            Key  key = kgen.generateKey();
			
            byte[] bytes = Base64.getDecoder().decode(encrypted);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] doFinal = cipher.doFinal(bytes);
            return new String(doFinal, "UTF-8");
		} catch (Exception ex) {
			throw new Exception(ex.toString());
		}
	}
	  
	   /** 
	    * @param args 
	    * @throws NoSuchPaddingException 
	    * @throws NoSuchAlgorithmException 
	    * @throws BadPaddingException 
	    * @throws IllegalBlockSizeException 
	    * @throws InvalidKeyException 
	    */  
	   public static void main(String[] args) throws Exception { 
		   System.out.println("1:加密 2:解密");
		   Scanner sc = new Scanner(System.in);
		   String type = sc.nextLine();  
		   if("2".equals(type)) {
			   System.out.println("請輸入解密字串");
			   Scanner sc2 = new Scanner(System.in);
		       String msg = sc2.nextLine();  
		       String decontent = decryptAES(msg);  
		       System.out.println("解密後:" + decontent);  
		   }else {	   
			   System.out.println("請輸入加密字串");
			   Scanner sc2 = new Scanner(System.in);
		       String msg = sc2.nextLine();  
		       String encontent = encryptAES(msg);  
		       String decontent = decryptAES(encontent);  
		       System.out.println("明文是:" + msg);  
		       System.out.println("AES加密後:" + encontent);  
		       System.out.println("AES解密後:" + decontent);  
		   }
	   }  
}
