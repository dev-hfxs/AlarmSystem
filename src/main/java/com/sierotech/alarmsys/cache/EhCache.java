package com.sierotech.alarmsys.cache;

import org.ehcache.Cache;

public class EhCache implements ObjCache{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8729902570767506379L;

	private String cacheName = "";
	
	private Cache cache = null;
	
	/**
	 * @param cacheName
	 * @param cache
	 */
	public EhCache(String cacheName, Cache cache) {
		super();
		this.cacheName = cacheName;
		this.cache = cache;
	}

	public Object get(Object key) throws CacheException{
		return cache.get(key);
	}
	
	public void update(Object key, Object value) throws CacheException {
		put( key, value );
	}

	public void put(Object key, Object value) throws CacheException {
		cache.put(key, value);
	}
	public void remove(Object key) throws CacheException {
		cache.remove(key);
	}

	public void clear() throws CacheException {
		cache.clear();
	}
	
	public String getCacheName(){
		return this.cacheName;
	}
}