/**
* <p>版权所有:(C)2018-2022 天津航峰希萨科技有限公司 </p>
* @创建人: lwm
* @创建日期: 2018年7月10日
* @修改人: 
* @修改日期：
* @描述: 
 */
package com.sierotech.alarmsys.map;

/**
* @JDK版本: 1.7
* @创建人: lwm
* @创建日期：2018年7月10日
* @功能描述: 
 */
public class Point {
	private double lng ;
    private double lat ;
	
	public Point(double lng, double lat) {
		super();
		this.lng = lng;
		this.lat = lat;
	}

	/**
	 * @return the lng
	 */
	public double getLng() {
		return lng;
	}

	/**
	 * @param lng the lng to set
	 */
	public void setLng(double lng) {
		this.lng = lng;
	}

	/**
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * @param lat the lat to set
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}

	
	@Override
	public String toString() {
		return "Point [lng=" + lng + ", lat=" + lat + "]";
	}	
	
}
