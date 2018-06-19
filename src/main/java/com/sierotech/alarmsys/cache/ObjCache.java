package com.sierotech.alarmsys.cache;


public interface ObjCache extends java.io.Serializable{

	/**
	 * get objtec by key
	 * @param key
	 * @return
	 * @throws CacheException
	 */
	public Object get(Object key) throws CacheException;
	
	/**
	 * put object into cache
	 * @param key
	 * @param value
	 * @throws CacheException
	 */
	public void put(Object key, Object value) throws CacheException;
	
	/**
	 * update cache
	 * @param key
	 * @param value
	 * @throws CacheException
	 */
	public void update(Object key, Object value) throws CacheException;

	/**
	 * remove object by key
	 * @param key
	 * @throws CacheException
	 */
	public void remove(Object key) throws CacheException;
	
	/**
	 * clear cache
	 * @throws CacheException
	 */
	public void clear() throws CacheException;
	
		/**
	 * get cache name
	 * @return
	 */
	public String getCacheName();
	

}