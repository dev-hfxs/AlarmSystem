package com.sierotech.alarmsys.cache;


public interface CacheProvider {
	public <K, V> ObjCache buildCache(String cacheName,Class<K> keyType, Class<V> valueType,CacheConfig config) throws CacheException;
	
	public ObjCache getCache(String cacheName) throws CacheException;
	
	public void start() throws CacheException;
	
	public void stop();
}
