package aptg.cathaybkeco.util;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

public class EncryptUtil {
	
	/**
	 * AES加密
	 * @param value
	 * @param keyStr
	 * @return String
	 * @throws Exception
	 */
	public static String encryptAES(String value, String keyStr) throws Exception {
		try {
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
	 * AES解密
	 * 
	 * @param key
	 * @param initVector
	 * @param encrypted
	 * @return String
	 */
	public static String decryptAES(String encrypted, String keyStr) throws Exception {
		try {
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
	 * MD5加密
	 * @param str
	 * @return
	 * @throws Exception 
	 */
	public static String encryptMD5(String value) throws Exception {
		StringBuffer hexValue = new StringBuffer();
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] md5Bytes = md5.digest(value.getBytes());		
			for (int i = 0; i < md5Bytes.length; i++) {
				int val = ((int) md5Bytes[i]) & 0xff;
				if (val < 16)
					hexValue.append("0");
				hexValue.append(Integer.toHexString(val));
			}
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		return hexValue.toString();
	}
	
	
	/**
	 * 利用java原生的摘要實現SHA256加密
	 * 
	 * @param str 加密後的報文
	 * @return
	 */
	public static String encryptSHA256(String str) throws Exception {
		String encodeStr = "";
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(str.getBytes("UTF-8"));
			encodeStr = byte2Hex(messageDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encodeStr;
	}

	/**
	 * 將byte轉為16進位制
	 * 
	 * @param bytes
	 * @return
	 */
	private static String byte2Hex(byte[] bytes) {
		StringBuffer stringBuffer = new StringBuffer();
		String temp = null;
		for (int i = 0; i < bytes.length; i++) {
			temp = Integer.toHexString(bytes[i] & 0xFF);
			if (temp.length() == 1) {
				// 1得到一位的進行補0操作
				stringBuffer.append("0");
			}
			stringBuffer.append(temp);
		}
		return stringBuffer.toString();
	}
}
