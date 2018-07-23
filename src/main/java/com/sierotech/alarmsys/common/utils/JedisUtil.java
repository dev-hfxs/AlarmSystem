/**
* <p>版权所有:(C)2018-2022 天津航峰希萨科技有限公司 </p>
* @创建人: lwm
* @创建日期: 2018年7月23日
* @修改人: 
* @修改日期：
* @描述: 
 */
package com.sierotech.alarmsys.common.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
* @JDK版本: 1.7
* @创建人: lwm
* @创建日期：2018年7月23日
* @功能描述: Jedis操作
 */
public class JedisUtil {
	private static JedisPool pool;
	
	public static synchronized void poolInit(String host, int port) {
		if(pool == null) {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(20);
			config.setMaxWaitMillis(1000);
	        // 设置空间连接
	        config.setMaxIdle(10);
	        
	        // 创建连接池
	        pool = new JedisPool(config, host, port);
		}
	}
	
	public static Jedis getJedis() {
		if(pool == null) {
			return null;
		}
		try {
			return pool.getResource();
		}catch(Exception e) {
			return null;
		}
    }
	
	public static void releaseJedis(Jedis jedis) {
	    pool.returnResource(jedis);
	}
		
}
