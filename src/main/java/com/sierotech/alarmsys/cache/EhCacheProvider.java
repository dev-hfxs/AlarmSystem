package com.sierotech.alarmsys.cache;

import java.util.HashMap;
import java.util.Map;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EhCacheProvider implements CacheProvider{
	static final Logger log = LoggerFactory.getLogger(EhCacheProvider.class);
	private static CacheProvider cacheProvider = null;
	private CacheManager cacheManager = null;
	private Map<String,com.sierotech.alarmsys.cache.ObjCache> cachePoolMap  = new HashMap<String,com.sierotech.alarmsys.cache.ObjCache>();

	public static CacheProvider getInstance(){
		if(cacheProvider == null){
			cacheProvider =  new EhCacheProvider();
			try {
				cacheProvider.start();
			} catch (CacheException e) {
				log.error("缓存管理对象初始化失败.");
				e.printStackTrace();
			}
		}
		return cacheProvider;
	}
	
	public <K, V> ObjCache buildCache(String cacheName,Class<K> keyType, Class<V> valueType,CacheConfig config) throws CacheException
	{
		Cache<K,V> newCache = this.cacheManager.createCache(cacheName,CacheConfigurationBuilder.newCacheConfigurationBuilder(keyType, valueType, ResourcePoolsBuilder.heap(1024*1024)).build());
		if(newCache == null){
			throw new CacheException("["+cacheName+"]缓存对象创建失败");
		}
		EhCache ehCache = new EhCache(cacheName,newCache);
		cachePoolMap.put(cacheName, ehCache);
		return ehCache;
	}
	
	public ObjCache getCache(String cacheName) throws CacheException{
		ObjCache tempCache = cachePoolMap.get(cacheName);
		if(tempCache == null){
			throw new CacheException("["+cacheName+"]缓存对象不存在");
		}else{
			return tempCache;
		}
	}
	
	/**
	 * start the cahche withe default enchache.xml config in class path
	 */
    public void start() throws CacheException
    {
    	this.cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build();
		cacheManager.init();
	}
   
    /**
     * stop the ehcache 
     */
	public void stop()
	{
		this.cacheManager.close();
	}
}