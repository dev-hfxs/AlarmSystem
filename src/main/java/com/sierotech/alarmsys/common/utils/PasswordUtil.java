/**
* <p>版权所有:(C)2018-2022 天津航峰希萨科技有限公司 </p>
* @创建人: lwm
* @创建日期: 2018年4月15日
* @修改人: 
* @修改日期：
* @描述: 
 */
package com.sierotech.alarmsys.common.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @JDK版本: 1.7
 * @创建人: lwm
 * @创建日期：2018年4月15日 
 * @功能描述: 密码工具类 
 */
public class PasswordUtil {

	public static String encrypt(String pwd) {
		return DigestUtils.md5Hex(pwd);
	}
	

	public static boolean validPassword(String password,String encryptValue) {
		if(null == password || encryptValue == null) {
			return false;
		}
		return DigestUtils.md5Hex(password).equals(encryptValue);
	}
	
	public static void main(String[] args) {
		System.out.println(PasswordUtil.encrypt("82889738"));
	}
}
