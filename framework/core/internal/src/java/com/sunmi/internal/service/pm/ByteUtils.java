package com.sunmi.internal.service.pm;

import java.io.UnsupportedEncodingException;

public class ByteUtils {

	/**
	 * 打印内容
	 */
	public static String byte2Hex(byte[] raw, int offset, int count) {
		if (raw == null) {
			return null;
		}
		if (offset < 0 || offset > raw.length) {
			offset = 0;
		}
		int end = offset + count;
		if (end > raw.length) {
			end = raw.length;
		}
		StringBuilder hex = new StringBuilder();
		for (int i = offset; i < end; i++) {
			int v = raw[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				hex.append(0);
			}
			hex.append(hv);
			hex.append(" ");
		}
		if (hex.length() > 0) {
			hex.deleteCharAt(hex.length() - 1);
		}
		return hex.toString().toUpperCase();
	}

	/**
	 * 将无符号short转换成int，大端模式(高位在前)
	 */
	public static int unsignedShort2IntBE(byte[] src, int offset) {
		return (src[offset] & 0xff) << 8 | (src[offset + 1] & 0xff);
	}

	/**
	 * 将无符号short转换成int，小端模式(低位在前)
	 */
	public static int unsignedShort2IntLE(byte[] src, int offset) {
		return (src[offset] & 0xff) | (src[offset + 1] & 0xff) << 8;
	}

	/**
	 * 将无符号byte转换成int
	 */
	public static int unsignedByte2Int(byte[] src, int offset) {
		return src[offset] & 0xFF;
	}

	/**
	 * 将int转换成byte数组，大端模式(高位在前)
	 */
	public static byte[] int2BytesBE(int src) {
		byte[] result = new byte[4];
		for (int i = 0; i < 4; i++) {
			result[i] = (byte) (src >> (3 - i) * 8);
		}
		return result;
	}

	/**
	 * 将字节数组转换成int,小端模式(低位在前)
	 */
	public static int unsignedInt2IntLE(byte[] src, int offset) {
		int value = 0;
		for (int i = offset; i < offset + 4; i++) {
			value |= (src[i] & 0xff) << (i - offset) * 8;
		}
		return value;
	}

	/**
	 * 将字节数组转换成int,大端模式(高位在前)
	 */
	public static int unsignedInt2IntBE(byte[] src, int offset) {
		int result = 0;
		for (int i = offset; i < offset + 4; i++) {
			result |= (src[i] & 0xff) << (offset + 3 - i) * 8;
		}
		return result;
	}

	/**
	 * 将命令码转换成16进制字符串(4位)
	 */
	public static String getHexCmd(int cmd) {
		return String.format("0x%04X", cmd);
	}

	/**
	 * 将命令码转换成16进制字符串(2位)
	 */
	public static String getDownloadHexCmd(byte cmd) {
		return String.format("0x%02X", cmd);
	}

	/**
	 * 获取LRC
	 */
	public static byte genLRC(byte[] data, int offset, int len) {
		if (data == null || data.length == 0) {
			return 0;
		}
		if (offset < 0 || offset >= data.length) {
			offset = 0;
		}
		if (len < 0 || len > data.length) {
			len = data.length;
		}
		int end = offset + len;
		if (end > data.length) {
			end = data.length;
		}
		byte lrc = 0;
		for (int i = offset; i < end; i++) {
			lrc ^= data[i];
		}
		return lrc;
	}

	/**
	 * Convert byte[] to hex
	 * string.将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
	 *
	 * @param src
	 *            byte[] data
	 * @return hex string
	 */
	public static String bytes2HexString(byte... src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString().toUpperCase();
	}

	/**
	 * 16进制字符串转byte[]
	 *
	 * @param hexString
	 *            the hex string
	 * @return byte[]
	 */
	public static byte[] hexString2Bytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (char2Byte(hexChars[pos]) << 4 | char2Byte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * Convert char to byte
	 *
	 * @param c
	 *            char
	 * @return byte
	 */
	private static int char2Byte(char c) {
		if (c >= 'a')
			return (c - 'a' + 10) & 0x0f;
		if (c >= 'A')
			return (c - 'A' + 10) & 0x0f;
		return (c - '0') & 0x0f;
	}

	public static byte[] Bcd2Asc(byte[] a) {
		if (a == null) {
			return null;
		}
		if (a.length == 0) {
			return null;
		}

		byte[] b = new byte[a.length * 2];
		for (int i = 0; i < a.length; i++) {
			byte val = (byte) (((a[i] & 0xF0) >> 4) & 0x0F);
			b[i * 2] = (byte) (val > 9 ? val + 'A' - 10 : val + '0');
			val = (byte) (a[i] & 0x0F);
			b[i * 2 + 1] = (byte) (val > 9 ? val + 'A' - 10 : val + '0');
		}

		return b;
	}

	/**
	 * 将String转换为SP字节数组，增加结尾\0符
	 *
	 * @param src
	 *            源字符串
	 * @return 转换后的字节数组
	 */
	public static byte[] string2SPBytes(String src) {
		byte[] in = (src == null ? "".getBytes() : src.getBytes());
		byte[] out = new byte[in.length + 1];// 增加一个结尾符字节
		System.arraycopy(in, 0, out, 0, in.length);
		return out;
	}

	/**
	 * 将SP字节数组转换成字符串
	 *
	 * @param src
	 *            源字节数组，以\0结尾
	 * @return 转换后的字符串
	 */
	public static String spBytes2String(byte[] src) {
		if (src == null || src.length == 0) {
			return "";
		}
		int index = src.length - 1;
		while (index >= 0 && src[index] == 0) {
			index--;
		}
		if (index < 0) {// 所有字节全为0
			return "";
		}
		if (index == src.length - 1) {// src不包含\0符,直接报异常
			throw new IllegalArgumentException("'\\0' not found in param src");
		}

		return new String(src, 0, index + 1);
	}

	/**
	 * 将ASCII 转换为 BCD
	 *
	 * @param ascii
	 * @param asc_len
	 * @return
	 */
	public static byte[] ascii2bcd(byte[] ascii, int asc_len) {
		byte[] bcd = new byte[asc_len / 2];
		int j = 0;
		for (int i = 0; i < (asc_len + 1) / 2; i++) {
			bcd[i] = asc2bcd(ascii[j++]);
			bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc2bcd(ascii[j++])) + (bcd[i] << 4));
		}
		return bcd;
	}

	private static byte asc2bcd(byte asc) {
		byte bcd;

		if ((asc >= '0') && (asc <= '9'))
			bcd = (byte) (asc - '0');
		else if ((asc >= 'A') && (asc <= 'F'))
			bcd = (byte) (asc - 'A' + 10);
		else if ((asc >= 'a') && (asc <= 'f'))
			bcd = (byte) (asc - 'a' + 10);
		else
			bcd = (byte) (asc - 48);
		return bcd;
	}

	public static byte[] asciiStr2Bytes(String ascii) {
		byte[] dat = null;
		try {
			dat = ascii.getBytes("ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return dat;
	}

	public static String hexStr2AsciiStr(String hex) {
		String rec = null;
		try {
			rec = new String(ByteUtils.hexString2Bytes(hex), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return rec;
	}

	/**
	 * 将short转换成byte数组，大端模式(高位在前)
	 */
	public static byte[] short2BytesBE(short src) {
		byte[] result = new byte[2];
		for (int i = 0; i < 2; i++) {
			result[i] = (byte) (src >> (1 - i) * 8);
		}
		return result;
	}
}
