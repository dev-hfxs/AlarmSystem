package com.sierotech.alarmsys.cache;

@SuppressWarnings("serial")
public class CacheConfig implements java.io.Serializable{
    private String     cacheName;
    private int        maxNum;
    private int        minNum;
    private String     policy;
    private int        lifespan;
    private long       dayFiexInvalidTime = 0;
    private boolean    turnOff = false;
    private boolean    distributedCacheSupported = false;
    private boolean    isShareCrossWebApp = false;
    private int        type =0;  //0:obj;1:app;

	/**
	 * @return the cacheName
	 */
	public String getCacheName() {
		return cacheName;
	}
	/**
	 * @param cacheName the cacheName to set
	 */
	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}
	/**
	 * @return the maxNum
	 */
	public int getMaxNum() {
		return maxNum;
	}
	/**
	 * @param maxNum the maxNum to set
	 */
	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}
	/**
	 * @return the minNum
	 */
	public int getMinNum() {
		return minNum;
	}
	/**
	 * @param minNum the minNum to set
	 */
	public void setMinNum(int minNum) {
		this.minNum = minNum;
	}
	/**
	 * @return the policy
	 */
	public String getPolicy() {
		return policy;
	}
	/**
	 * @param policy the policy to set
	 */
	public void setPolicy(String policy) {
		this.policy = policy;
	}
	/**
	 * @return the lifespan
	 */
	public int getLifespan() {
		return lifespan;
	}
	/**
	 * @param lifespan the lifespan to set
	 */
	public void setLifespan(int lifespan) {
		this.lifespan = lifespan;
	}
	/**
	 * @return the distributedCacheSupported
	 */
	public boolean isDistributedCacheSupported() {
		return distributedCacheSupported;
	}
	/**
	 * @param distributedCacheSupported the distributedCacheSupported to set
	 */
	public void setDistributedCacheSupported(boolean distributedCacheSupported) {
		this.distributedCacheSupported = distributedCacheSupported;
	}
	/**
	 * @return the turnOff
	 */
	public boolean isTurnOff() {
		return turnOff;
	}
	/**
	 * @param turnOff the turnOff to set
	 */
	public void setTurnOff(boolean turnOff) {
		this.turnOff = turnOff;
	}
	/**
	 * @return the dayFiexInvalidTime
	 */
	public long getDayFiexInvalidTime() {
		return dayFiexInvalidTime;
	}
	/**
	 * @param dayFiexInvalidTime the dayFiexInvalidTime to set
	 */
	public void setDayFiexInvalidTime(long dayFiexInvalidTime) {
		this.dayFiexInvalidTime = dayFiexInvalidTime;
	}
	/**
	 * @return the isShareCrossWebApp
	 */
	public boolean isShareCrossWebApp() {
		return isShareCrossWebApp;
	}
	/**
	 * @param isShareCrossWebApp the isShareCrossWebApp to set
	 */
	public void setShareCrossWebApp(boolean isShareCrossWebApp) {
		this.isShareCrossWebApp = isShareCrossWebApp;
	}
	/**
	
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	
}
