package com.sierotech.alarmsys.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

public class KeyUtil {

private static final String KEY_ALGORITHM = "AES";
	
	private static Key defaultKey = null;

	public static Key generateKey() {
		// 返回生成指定算法密钥生成器的 KeyGenerator 对象
		KeyGenerator kg = null;
		try {
			kg = KeyGenerator.getInstance(KEY_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		// 初始化此密钥生成器，使其具有确定的密钥大小
		// AES 要求密钥长度为 128
		kg.init(128);
		// 生成一个密钥
		SecretKey secretKey = kg.generateKey();
		return secretKey;
	}

	public static void outKeyFile(String filePath) {
		Key key = generateKey();
		outKeyFile(key,filePath);
	}
	
	public static void outKeyFile(Key key, String filePath) {
		String keyencode = HexBin.encode(key.getEncoded());
		File file = new File(filePath);
		try {
			OutputStream outputStream = new FileOutputStream(file);
			outputStream.write(keyencode.getBytes());
			outputStream.close();
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static Key readKeyFile(String filePath) {
		try {
			// 1.读取文件中的密钥
			File file = new File(filePath);
			InputStream inputStream = new FileInputStream(file);// 文件内容的字节流
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream); // 得到文件的字符流
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader); // 放入读取缓冲区
			String readd = "";
			StringBuffer stringBuffer = new StringBuffer();
			while ((readd = bufferedReader.readLine()) != null) {
				stringBuffer.append(readd);
			}
			inputStream.close();
			String keystr = stringBuffer.toString();
			// 读取出来的key是编码之后的 要进行转码
			byte[] keybyte = HexBin.decode(keystr);
			return new SecretKeySpec(keybyte, KEY_ALGORITHM);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Key getDefaultKey(){
		if(defaultKey == null){
//			log.info("key path:{}",KeyUtil.class.getClassLoader().getResource("transDataKey.txt").getPath());
			String dataKeyFile = KeyUtil.class.getClassLoader().getResource("transDataKey.txt").getPath();
//			String dataKeyFile = "D:\\Users\\etpSrc\\A7项目\\server\\eTPServer\\WebContent\\WEB-INF\\classes\\transDataKey.txt";
//			String dataKeyFile = "D:/Users/etpSrc/A7项目/server/eTPServer/WebContent/WEB-INF/classes/transDataKey.txt";
			defaultKey = KeyUtil.readKeyFile(dataKeyFile);
		}
		return defaultKey;
	}
	
	public static void main(String[] args){
//		Key key = generateKey();
//		String path  = ConfigFactory.class.getClassLoader().getResource("transDataKey.txt").getPath();
//		String path = "C:\\Users\\w3d\\Desktop\\db\\transDataKey.txt";
//		outKeyFile(key,path);
	}
}
