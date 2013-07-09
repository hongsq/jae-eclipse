/**
 * 
 */
package com.jae.eclipse.core.util;

import java.math.BigInteger;
import java.security.MessageDigest;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * @author hongshuiqiao
 * 
 */
public class DataEncryptUtil {
	public static final String KEY_SHA = "SHA";
	public static final String KEY_MD5 = "MD5";
	/**
	 * MAC算法可选以下多种算法:
	 * HmacMD5
	 * HmacSHA1
	 * HmacSHA256
	 * HmacSHA384
	 * HmacSHA512
	 */
	public static final String KEY_MAC_MD5 = "HmacMD5";
	public static final String KEY_MAC_SHA1 = "HmacSHA1";
	public static final String KEY_MAC_SHA256 = "HmacSHA256";
	public static final String KEY_MAC_SHA384 = "HmacSHA384";
	public static final String KEY_MAC_SHA512 = "HmacSHA512";

	/**
	 * BASE64解密
	 * 
	 * @param data	需要解密的密码字符串
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptBASE64(String data) throws Exception {
		return Base64.decodeBase64(data);
//		return (new BASE64Decoder()).decodeBuffer(data);
	}

	/**
	 * BASE64加密
	 * 
	 * @param data	需要加密的字符数组
	 * @return
	 * @throws Exception
	 */
	public static String encryptBASE64(byte[] data) throws Exception {
		return Base64.encodeBase64String(data);
//		return (new BASE64Encoder()).encodeBuffer(data);
	}

	/**
	 * MD5加密
	 * 
	 * @param data	需要加密的字符数组
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptMD5(byte[] data) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
		md5.update(data);
		return md5.digest();
	}

	/**
	 * SHA加密
	 * 
	 * @param data	需要加密的字符数组
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptSHA(byte[] data) throws Exception {
		MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
		sha.update(data);
		return sha.digest();
	}

	/**
	 * 初始化HMAC密钥
	 * @param algorithm
	 * @return
	 * @throws Exception
	 */
	public static String initMacKey(String algorithm) throws Exception {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
		SecretKey secretKey = keyGenerator.generateKey();
		return encryptBASE64(secretKey.getEncoded());
	}

	/**
	 * HMAC加密
	 * 
	 * @param data	密匙加密过的字符数组
	 * @param key	需要加密的字符串
	 * @return
	 * @throws Exception
	 */
//	public static byte[] encryptHMAC(byte[] data, String key) throws Exception {
	public static byte[] encryptHMAC(String algorithm, byte[] data, byte[] key) throws Exception {
//		SecretKey secretKey = new SecretKeySpec(decryptBASE64(key), KEY_MAC);
		SecretKey secretKey = new SecretKeySpec(key, algorithm);
		Mac mac = Mac.getInstance(secretKey.getAlgorithm());
		mac.init(secretKey);
		return mac.doFinal(data);
	}

	public static void main(String[] args) {
		String inputStr = "简单加密";
		System.err.println("原文:\n" + inputStr);
		byte[] inputData = inputStr.getBytes();
		String code;
		try {
			code = DataEncryptUtil.encryptBASE64(inputData);
			System.err.println("BASE64加密后:\n" + code);

			byte[] output = DataEncryptUtil.decryptBASE64(code);
			String outputStr = new String(output);
			System.err.println("BASE64解密后:\n" + outputStr);
			System.err.println("验证base64加密解密一致性" + inputStr.equals(outputStr));

			String key = DataEncryptUtil.initMacKey(KEY_MAC_MD5);
			System.err.println("Mac密钥:\n" + key);
////			DataEncryptUtil.encryptHMAC(inputData, key);
//			BigInteger mac = new BigInteger(DataEncryptUtil.encryptHMAC(KEY_MAC_MD5, inputData, key));
//			System.err.println("HMAC:\n" + mac.toString(16));

			BigInteger md5 = new BigInteger(DataEncryptUtil.encryptMD5(inputData));
			System.err.println("MD5:\n" + md5.toString(16));

			BigInteger sha = new BigInteger(DataEncryptUtil.encryptSHA(inputData));
			System.err.println("SHA:\n" + sha.toString(32));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
