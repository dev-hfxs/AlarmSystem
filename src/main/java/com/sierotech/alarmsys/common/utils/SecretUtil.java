package com.sierotech.alarmsys.common.utils;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

public class SecretUtil {

	   private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//默认的加密算法
	   

	   public static String encrypt(String data,Key key) throws Exception{
	       byte[] encryptData =  encrypt(data.getBytes("UTF-8"), key,DEFAULT_CIPHER_ALGORITHM);
	       String encryptStr = HexBin.encode(encryptData);
	       return encryptStr;
	   }
	   
	   public static byte[] encrypt(byte[] data,Key key) throws Exception{
	       return encrypt(data, key,DEFAULT_CIPHER_ALGORITHM);
	   }

	   public static byte[] encrypt(byte[] data,Key key,String cipherAlgorithm) throws Exception{
	       //实例化
	       Cipher cipher = Cipher.getInstance(cipherAlgorithm);
	       //使用密钥初始化，设置为加密模式
	       cipher.init(Cipher.ENCRYPT_MODE, key);
	       //执行操作
	       return cipher.doFinal(data);
	   }


	   public static String decrypt(String data,Key key) throws Exception{
		   byte[] strByte = HexBin.decode(data);
		   byte[] decryptData = decrypt(strByte, key,DEFAULT_CIPHER_ALGORITHM);
		   
	       return new String(decryptData,"UTF-8");
	   }

	   
	   public static byte[] decrypt(byte[] data,Key key) throws Exception{
	       return decrypt(data, key,DEFAULT_CIPHER_ALGORITHM);
	   }

	   public static byte[] decrypt(byte[] data,Key key,String cipherAlgorithm) throws Exception{
	       //实例化
	       Cipher cipher = Cipher.getInstance(cipherAlgorithm);
	       //使用密钥初始化，设置为解密模式
	       cipher.init(Cipher.DECRYPT_MODE, key);
	       //执行操作
	       return cipher.doFinal(data);
	   }

	   private static String  showByteArray(byte[] data){
	       if(null == data){
	           return null;
	       }
	       StringBuilder sb = new StringBuilder("{");
	       for(byte b:data){
	           sb.append(b).append(",");
	       }
	       sb.deleteCharAt(sb.length()-1);
	       sb.append("}");
	       return sb.toString();
	   }
	   
	   public static void main(String[] args) throws Exception {
//	       Key k = KeyUtil.generateKey();
		   Key k = KeyUtil.getDefaultKey();
	       String data ="AESsssss!!!!!!!!!!!!!!!!!!!数据!@##$$%%^^&&**(())__++==";
	       System.out.println("加密前数据: string:"+data);
	       System.out.println("加密前数据: byte[]:"+showByteArray(data.getBytes()));
	       System.out.println();
	       //byte加密(示例：字符串转成byte)
	       byte[] encryptData = encrypt(data.getBytes(), k);//数据加密
	       System.out.println("加密后数据: byte[]:"+showByteArray(encryptData));
	       byte[] decryptData = decrypt(encryptData, k);//数据解密
	       System.out.println("解密后数据: byte[]:"+showByteArray(decryptData));
	       System.out.println(" 解密后数据: string:"+new String(decryptData));
	       
	       //直接字符串加密
	       String encryptStr = encrypt(data, k);//数据加密
	       String decryptStr = decrypt(encryptStr, k);//数据解密
	       String strEncryptData = HexBin.encode(encryptData);
	       System.out.println("加密后数据2: encryptStr:"+encryptStr);
	       System.out.println("解密后数据2: string:"+decryptStr);
	   }
}