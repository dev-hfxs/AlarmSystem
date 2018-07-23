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
* @功能描述: 坐标转换
 */
public class CoordinateTransform {
	 double x_PI = 3.14159265358979324 * 3000.0 / 180.0;
	    double PI = 3.1415926535897932384626;
	    double a = 6378245.0;
	    double ee = 0.00669342162296594323;

	    /**
	     * 百度坐标系 (BD-09) 与 火星坐标系 (GCJ-02)的转换
	     * 即 百度 转 谷歌、高德
	     * @param bd_lon
	     * @param bd_lat
	     * @returns {*[]}
	     */
	    public Point bd09togcj02(double bd_lon, double bd_lat){
	        double x = bd_lon - 0.0065;
	        double y = bd_lat - 0.006;
	        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_PI);
	        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_PI);
	        double gg_lng = z * Math.cos(theta);
	        double gg_lat = z * Math.sin(theta);
	        Point point=new Point(gg_lng, gg_lat);
	        return point;
	    }

	    /**
	     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换
	     * 即谷歌、高德 转 百度
	     * @param lng
	     * @param lat
	     * @returns {*[]}
	     */
	    public Point gcj02tobd09(double lng, double lat){
	        double z = Math.sqrt(lng * lng + lat * lat) + 0.00002 * Math.sin(lat * x_PI);
	        double theta = Math.atan2(lat, lng) + 0.000003 * Math.cos(lng * x_PI);
	        double bd_lng = z * Math.cos(theta) + 0.0065;
	        double bd_lat = z * Math.sin(theta) + 0.006;
	        Point point=new Point(bd_lng, bd_lat);
	        return point;
	    };

	    /**
	     * WGS84转GCj02
	     * @param lng
	     * @param lat
	     * @returns {*[]}
	     */
	    public Point wgs84togcj02(double lng, double lat){
	        double dlat = transformlat(lng - 105.0, lat - 35.0);
	        double dlng = transformlng(lng - 105.0, lat - 35.0);
	        double radlat = lat / 180.0 * PI;
	        double magic = Math.sin(radlat);
	        magic = 1 - ee * magic * magic;
	        double sqrtmagic = Math.sqrt(magic);
	        dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * PI);
	        dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * PI);
	        double mglat = lat + dlat;
	        double mglng = lng + dlng;
	        Point point=new Point(mglng, mglat);
	        return point;
	    };

	    /**
	     * GCJ02 转换为 WGS84
	     * @param lng
	     * @param lat
	     * @returns {*[]}
	     */
	    public Point gcj02towgs84(double lng, double lat){
	        double dlat = transformlat(lng - 105.0, lat - 35.0);
	        double dlng = transformlng(lng - 105.0, lat - 35.0);
	        double radlat = lat / 180.0 * PI;
	        double magic = Math.sin(radlat);
	        magic = 1 - ee * magic * magic;
	        double sqrtmagic = Math.sqrt(magic);
	        dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * PI);
	        dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * PI);
	        double mglat = lat + dlat;
	        double mglng = lng + dlng;
	        Point point=new Point(mglng, mglat);
	        return point;
	    };


	    private double transformlat(double lng,double lat){
	        double ret= -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
	        ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
	        ret += (20.0 * Math.sin(lat * PI) + 40.0 * Math.sin(lat / 3.0 * PI)) * 2.0 / 3.0;
	        ret += (160.0 * Math.sin(lat / 12.0 * PI) + 320 * Math.sin(lat * PI / 30.0)) * 2.0 / 3.0;
	        return ret;
	    }

	    private double transformlng(double lng,double lat){
	        double ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
	        ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
	        ret += (20.0 * Math.sin(lng * PI) + 40.0 * Math.sin(lng / 3.0 * PI)) * 2.0 / 3.0;
	        ret += (150.0 * Math.sin(lng / 12.0 * PI) + 300.0 * Math.sin(lng / 30.0 * PI)) * 2.0 / 3.0;
	        return ret;
	    }

	    
	    public static void main(String[] args) {
	    	CoordinateTransform ts = new CoordinateTransform();
	    	Point afterPo = ts.wgs84togcj02(116.474858, 39.989288);
	    	System.out.println(afterPo.getLng() + ":" + afterPo.getLat());
	    	
	    	Point bdPo = ts.gcj02tobd09(afterPo.getLng(), afterPo.getLat());
	    	System.out.println(bdPo.getLng() + ":" + bdPo.getLat());
	    	
	    }
}
