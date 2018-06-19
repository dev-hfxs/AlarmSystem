package com.sierotech.alarmsys.common.utils;
import java.util.UUID;

/**
 * 
 * 
 * 
 *<p>Company: </p>
 *@version 1.0
 */
public class UUIDGenerator {
	 /**
	   * get uuid;
	   * @return
	   */
	  public static String getUUID()
	  {
		  UUID uuid = UUID.randomUUID();
		  String uuidStr = uuid.toString();
		  return uuidStr.replace("-", "");
	  }
}
