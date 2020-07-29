package com.wsproject.clientsvr.util;

import java.io.UnsupportedEncodingException;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** 
 * AES 암호화 유틸
 * @author mslim
 *
 */
@Component
public class AESUtil {
	
	private static String key;
	private static String iv;
	private static Key keySpec;
	
	@Value("${encrypt.key}")
	public void init(String value) {
		try {
			key = value;
			iv = key.substring(0, 16);
			byte[] keyBytes = new byte[16];
			byte[] b = key.getBytes("UTF-8");
			int len = b.length;
			
			if (len > keyBytes.length) {
				len = keyBytes.length;
			}
			
			System.arraycopy(b, 0, keyBytes, 0, len);
			keySpec = new SecretKeySpec(keyBytes, "AES");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String encrypt(String str) {
		try {
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
			byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
			String enStr = new String(Base64.encodeBase64(encrypted));
			return enStr;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String decrypt(String str) {
		try {
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
			byte[] byteStr = Base64.decodeBase64(str.getBytes());
			return new String(c.doFinal(byteStr), "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
