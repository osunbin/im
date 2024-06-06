package com.bin.im.common.encrypt;


import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

public class SecureUtil {

	private SecureUtil() {
	}

	/**
	 * Base64编码
	 */
	private static final Base64.Encoder encoder = Base64.getEncoder();

	/**
	 * Base64解码
	 */
	private static final Base64.Decoder decoder = Base64.getDecoder();


	/**
	 * Base64编码，byte[] 转 String
	 * @param bytes byte[]
	 * @return 字符串
	 */
	public static String encodeBytesToString(byte[] bytes){
		return encoder.encodeToString(bytes);
	}

	/**
	 * Base64解码，String 转 byte[]
	 * @param text 字符串
	 * @return byte[]
	 */
	public static byte[] decodeStringToBytes(String text){
		return decoder.decode(text);
	}

	/**
	 * Base64编码，String 转 String
	 * @param text 字符串
	 * @return Base64格式字符串
	 */
	public static String encode(String text){
		return encoder.encodeToString(text.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Base64解码，String 转 String
	 * @param base64Text Base64格式字符串
	 * @return 字符串
	 */
	public static String decode(String base64Text){
		return new String(decoder.decode(base64Text), StandardCharsets.UTF_8);
	}

	// ----------------------- 摘要加密 -----------------------

	/**
	 * md5加密
	 * @param str 指定字符串
	 * @return 加密后的字符串
	 */
	public static String md5(String str) {
		str = (str == null ? "" : str);
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] btInput = str.getBytes();
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			int j = md.length;
			char[] strA = new char[j * 2];
			int k = 0;
			for (byte byte0 : md) {
				strA[k++] = hexDigits[byte0 >>> 4 & 0xf];
				strA[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(strA);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * sha1加密
	 *
	 * @param str 指定字符串
	 * @return 加密后的字符串
	 */
	public static String sha1(String str) {
		try {
			str = (str == null ? "" : str);
			MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
			return getShaHexString(str, messageDigest);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * sha256加密
	 *
	 * @param str 指定字符串
	 * @return 加密后的字符串
	 */
	public static String sha256(String str) {
		try {
			str = (str == null ? "" : str);
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			return getShaHexString(str, messageDigest);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * sha384加密
	 *
	 * @param str 指定字符串
	 * @return 加密后的字符串
	 */
	public static String sha384(String str) {
		try {
			str = (str == null ? "" : str);
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-384");
			return getShaHexString(str, messageDigest);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * sha512加密
	 *
	 * @param str 指定字符串
	 * @return 加密后的字符串
	 */
	public static String sha512(String str) {
		try {
			str = (str == null ? "" : str);
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
			return getShaHexString(str, messageDigest);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * sha (Secure Hash Algorithm)加密 公共方法
	 *
	 * @param str 指定字符串
	 * @param messageDigest 消息摘要
	 * @return 加密后的字符串
	 */
	private static String getShaHexString(String str, MessageDigest messageDigest) {
		messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
		byte[] bytes = messageDigest.digest();
		StringBuilder builder = new StringBuilder();
		String temp;
		for (byte aByte : bytes) {
			temp = Integer.toHexString(aByte & 0xFF); // 获取无符号整数十六进制字符串
			if (temp.length() == 1) {
				builder.append("0"); // 确保每个字节都用两个字符表示
			}
			builder.append(temp);
		}

		return builder.toString();
	}

	/**
	 * md5加盐加密: md5(md5(str) + md5(salt))
	 * @param str 字符串
	 * @param salt 盐
	 * @return 加密后的字符串
	 */
	@Deprecated
	public static String md5BySalt(String str, String salt) {
		return md5(md5(str) + md5(salt));
	}

	/**
	 * sha256加盐加密: sha256(sha256(str) + sha256(salt))
	 * @param str 字符串
	 * @param salt 盐
	 * @return 加密后的字符串
	 */
	@Deprecated
	public static String sha256BySalt(String str, String salt) {
		return sha256(sha256(str) + sha256(salt));
	}

	// ----------------------- 对称加密 AES -----------------------

    /**
     * 默认密码算法
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * AES加密
     *
     * @param key 加密的密钥
     * @param text 需要加密的字符串
     * @return 返回Base64转码后的加密数据
     */
    public static String aesEncrypt(String key, String text) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            byte[] byteContent = text.getBytes(StandardCharsets.UTF_8);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key));
            byte[] result = cipher.doFinal(byteContent);
            return encoder.encodeToString(result);
 		} catch (Exception e) {
 			throw new RuntimeException(e);
 		}
    }

    /**
     * AES解密
     * @param key 加密的密钥
     * @param text 已加密的密文
     * @return 返回解密后的数据
     */
    public static String aesDecrypt(String key, String text) {
       try {
    	   Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
           cipher.init(Cipher.DECRYPT_MODE, getSecretKey(key));
           byte[] result = cipher.doFinal(decoder.decode(text));
           return new String(result, StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }

    /**
     * 生成加密秘钥
     * @param password 秘钥
     * @return SecretKeySpec
	 */
    private static SecretKeySpec getSecretKey(final String password) throws NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(password.getBytes());
        kg.init(128, random);
        SecretKey secretKey = kg.generateKey();
        return new SecretKeySpec(secretKey.getEncoded(), "AES");
    }


	// ----------------------- 非对称加密 RSA -----------------------

	private static final String ALGORITHM = "RSA";

	private static final int KEY_SIZE = 1024;


	// ---------- 5个常用方法

	/**
	 * 生成密钥对
	 * @return Map对象 (private=私钥, public=公钥)
	 * @throws Exception 异常
	 */
	@Deprecated
	public static HashMap<String, String> rsaGenerateKeyPair() throws Exception {

		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
		KeyPair keyPair;

		keyPairGenerator.initialize(KEY_SIZE,
				new SecureRandom(UUID.randomUUID().toString().replaceAll("-", "").getBytes()));
		keyPair = keyPairGenerator.generateKeyPair();

		RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();

		HashMap<String, String> map = new HashMap<>(16);
		map.put("private", encoder.encodeToString(rsaPrivateKey.getEncoded()));
		map.put("public", encoder.encodeToString(rsaPublicKey.getEncoded()));
		return map;
	}

	/**
	 * RSA公钥加密
	 * @param publicKeyString 公钥
	 * @param content 内容
	 * @return 加密后内容
	 */
	@Deprecated
	public static String rsaEncryptByPublic(String publicKeyString, String content) {
		try {
			// 获得公钥对象
			PublicKey publicKey = getPublicKeyFromString(publicKeyString);

			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			// 该密钥能够加密的最大字节长度
			int splitLength = ((RSAPublicKey) publicKey).getModulus().bitLength() / 8 - 11;
			byte[][] arrays = splitBytes(content.getBytes(), splitLength);
			StringBuilder stringBuilder = new StringBuilder();
			for (byte[] array : arrays) {
				stringBuilder.append(bytesToHexString(cipher.doFinal(array)));
			}
			return stringBuilder.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * RSA私钥加密
	 * @param privateKeyString 私钥
	 * @param content 内容
	 * @return 加密后内容
	 */
	@Deprecated
	public static String rsaEncryptByPrivate(String privateKeyString, String content) {
		try {
			PrivateKey privateKey = getPrivateKeyFromString(privateKeyString);

			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			// 该密钥能够加密的最大字节长度
			int splitLength = ((RSAPrivateKey) privateKey).getModulus().bitLength() / 8 - 11;
			byte[][] arrays = splitBytes(content.getBytes(), splitLength);
			StringBuilder stringBuilder = new StringBuilder();
			for (byte[] array : arrays) {
				stringBuilder.append(bytesToHexString(cipher.doFinal(array)));
			}
			return stringBuilder.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * RSA公钥解密
	 * @param publicKeyString 公钥
	 * @param content 已加密内容
	 * @return 解密后内容
	 */
	@Deprecated
	public static String rsaDecryptByPublic(String publicKeyString, String content) {

		try {
			PublicKey publicKey = getPublicKeyFromString(publicKeyString);

			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			// 该密钥能够加密的最大字节长度
			int splitLength = ((RSAPublicKey) publicKey).getModulus().bitLength() / 8;
			byte[] contentBytes = hexStringToBytes(content);
			byte[][] arrays = splitBytes(contentBytes, splitLength);
			StringBuilder stringBuilder = new StringBuilder();
			for (byte[] array : arrays) {
				stringBuilder.append(new String(cipher.doFinal(array)));
			}
			return stringBuilder.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * RSA私钥解密
	 * @param privateKeyString 公钥
	 * @param content 已加密内容
	 * @return 解密后内容
	 */
	@Deprecated
	public static String rsaDecryptByPrivate(String privateKeyString, String content) {
		try {
			PrivateKey privateKey = getPrivateKeyFromString(privateKeyString);

			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			// 该密钥能够加密的最大字节长度
			int splitLength = ((RSAPrivateKey) privateKey).getModulus().bitLength() / 8;
			byte[] contentBytes = hexStringToBytes(content);
			byte[][] arrays = splitBytes(contentBytes, splitLength);
			StringBuilder stringBuilder = new StringBuilder();
			for (byte[] array : arrays) {
				stringBuilder.append(new String(cipher.doFinal(array)));
			}
			return stringBuilder.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	// ---------- 获取*钥

	/** 根据公钥字符串获取 公钥对象 */
	private static PublicKey getPublicKeyFromString(String key)
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		// 过滤掉\r\n
		key = key.replace("\r\n", "");

		// 取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(decoder.decode(key));

		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);

		return keyFactory.generatePublic(x509KeySpec);
	}

	/** 根据私钥字符串获取 私钥对象 */
	private static PrivateKey getPrivateKeyFromString(String key)
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		// 过滤掉\r\n
		key = key.replace("\r\n", "");

		// 取得私钥
		PKCS8EncodedKeySpec x509KeySpec = new PKCS8EncodedKeySpec(decoder.decode(key));

		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);

		return keyFactory.generatePrivate(x509KeySpec);
	}


	// ---------- 一些辅助方法

	/** 根据限定的每组字节长度，将字节数组分组 */
	private static byte[][] splitBytes(byte[] bytes, int splitLength) {

		// bytes与splitLength的余数
		int remainder = bytes.length % splitLength;
		// 数据拆分后的组数，余数不为0时加1
		int quotient = remainder != 0 ? bytes.length / splitLength + 1 : bytes.length / splitLength;
		byte[][] arrays = new byte[quotient][];
		byte[] array;
		for (int i = 0; i < quotient; i++) {
			// 如果是最后一组（quotient-1）,同时余数不等于0，就将最后一组设置为remainder的长度
			if (i == quotient - 1 && remainder != 0) {
				array = new byte[remainder];
				System.arraycopy(bytes, i * splitLength, array, 0, remainder);
			} else {
				array = new byte[splitLength];
				System.arraycopy(bytes, i * splitLength, array, 0, splitLength);
			}
			arrays[i] = array;
		}
		return arrays;
	}

	/** 将字节数组转换成16进制字符串 */
	private static String bytesToHexString(byte[] bytes) {

		StringBuilder sb = new StringBuilder(bytes.length);
		String temp;
		for (byte aByte : bytes) {
			temp = Integer.toHexString(0xFF & aByte);
			if (temp.length() < 2) {
				sb.append(0);
			}
			sb.append(temp);
		}
		return sb.toString();
	}

	/** 将16进制字符串转换成字节数组 */
	private static byte[] hexStringToBytes(String hex) {

		int len = (hex.length() / 2);
		hex = hex.toUpperCase();
		byte[] result = new byte[len];
		char[] chars = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(chars[pos]) << 4 | toByte(chars[pos + 1]));
		}
		return result;
	}

	/** 将char转换为byte */
	private static byte toByte(char c) {

		return (byte) "0123456789ABCDEF".indexOf(c);
	}


}