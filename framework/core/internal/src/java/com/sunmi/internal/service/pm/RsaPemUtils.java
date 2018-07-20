package com.sunmi.internal.service.pm;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;

/**
 * Created by KenMa on 2017/10/27.
 */

public class RsaPemUtils {

	private static final String modulus = "C4B14E4FD6282BD62E5A54614BD18CCA8419FCD42D98FE7D103E2A7B829B703D45F227E1AFCD2FA527F3A127BB5AC522E1DB18F1CCCAA81B65E0D81B716B2D7C9EE62019308F6E19087BF0FB9F3B398C077F9CC67C45FD7AA410B551CDE96BEA9406490E5A6AB257FD65DD9A8E2A90825477A1300213169830D4582CC6D326CB232C13BBA263ADE3DD5ECD98DE6C74D6792E580C383293D02DBD4B15E4703F583BEC27C2C5A6499360D8BFE7F35D64564736804DC6DBCFA88EF7E8D4546E9C7BE85187ED185625A024CA524E6331D15078350DEB454A19EB2365A4275C9EE86B7E8105BBB9AB296E8D35EAA9E75A358CDDB69B2DCBF04CD7C892252AF3F9537F";
	private static final String publicExponent = "010001";

	public static RSAPublicKey getPublicKey(String modulus, String exponent) {
		try {
			BigInteger b1 = new BigInteger(modulus, 16);
			BigInteger b2 = new BigInteger(exponent, 16);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {
		try {
			BigInteger b1 = new BigInteger(modulus, 16);
			BigInteger b2 = new BigInteger(exponent, 16);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static final String TRANSFORMATION_RSA_None_PKCS1Padding = "RSA/None/PKCS1Padding";
	private static final String TRANSFORMATION_RSA_None_NoPadding = "RSA/None/NoPadding";

	/**
	 * @param data
	 * @param key
	 * @param cipherMode
	 *            {@link Cipher#DECRYPT_MODE}
	 * @return
	 * @throws Exception
	 */
	public static String calcRsaKey(String data, Key key, int cipherMode, String transformation) throws Exception {
		Cipher cipher = Cipher.getInstance(transformation);
		cipher.init(cipherMode, key);
		return ByteUtils.bytes2HexString(cipher.doFinal(ByteUtils.hexString2Bytes(data)));
	}

	public static byte[] calcRsaKey(byte[] data, Key key, int cipherMode, String transformation) throws Exception {
		Cipher cipher = Cipher.getInstance(transformation);
		cipher.init(cipherMode, key);
		return cipher.doFinal(data);
	}

	public static String decryptByPublicKey(String data, RSAPublicKey publicKey, String transformation)
			throws Exception {
		return calcRsaKey(data, publicKey, Cipher.DECRYPT_MODE, transformation);
	}

	public static byte[] decryptByPublicKey(byte[] data, RSAPublicKey publicKey, String transformation)
			throws Exception {
		return calcRsaKey(data, publicKey, Cipher.DECRYPT_MODE, transformation);
	}

	public static String encryptByPrivateKey(String data, RSAPrivateKey privateKey, String transformation)
			throws Exception {
		return calcRsaKey(data, privateKey, Cipher.ENCRYPT_MODE, transformation);
	}

	public static boolean verifyApkSign(byte[] hashMessage, byte[] signData) {
		RSAPublicKey publicKey = getPublicKey(modulus, publicExponent);
		try {
			byte[] hashBytes = decryptByPublicKey(signData, publicKey, TRANSFORMATION_RSA_None_PKCS1Padding);
			if (Arrays.equals(hashMessage, hashBytes)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}