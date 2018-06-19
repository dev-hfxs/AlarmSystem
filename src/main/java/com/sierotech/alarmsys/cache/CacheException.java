package com.sierotech.alarmsys.cache;


public class CacheException extends Exception{
	private static final long serialVersionUID = 3289349638588802932L;
	
	public CacheException(String msg)
	{
		  super(msg);
	}
	
	public CacheException(Exception e)
	{
		  super(e);
	}
}

