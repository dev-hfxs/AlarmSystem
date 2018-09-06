/*百度地图离线API V2 接口文件
 *发布: http://www.xiaoguo123.com/p/baidumap_offline_v2
 *网页中只需要加载此JS文件即可
 *@2016-9-21
 *百度地图图片即瓦片文件请自行下载，或联系我索取(QQ 63659875 验证：百度地图API)
 *9-21修正IE下的错误
 */
var customTilesUrl = 'http://localhost:8080/mapImgSrv';
var customImgext = '.jpg';

var customMinZoom = 7;  //地图最小级别
var customMaxZoom = 17; //地图最大级别
var customInitZoom = 11;//初始加载级别
var customInitCenterPoint = {'longitude':116.376592, 'latitude':39.889669};

var customTileLayer = new BMap.TileLayer();
customTileLayer.getTilesUrl = function(tileCoord, zoom) {
	var tilesPath = "";
    var x = (tileCoord.x + "").replace(/-/gi, "M");
    var y = (tileCoord.y + "").replace(/-/gi, "M");
	// zoom从0开始,0表示1级
	if(zoom < 12){
	    //12级
		tilesPath = customTilesUrl + '/wmap12/tiles/';
	}else if( zoom > 11 && zoom < 15){
	    //13级-15级
		tilesPath =  customTilesUrl + '/chinaMap15/tiles/';
	}else if( zoom == 15){
		tilesPath =  customTilesUrl + '/xjMap16/tiles/';
	}else if( zoom == 16){
		tilesPath =  customTilesUrl + '/xjMap17/tiles/';
	}else if( zoom == 17){
		tilesPath =  customTilesUrl + '/xjMap18/tiles/';
	}
	
    var tilesUrl = tilesPath + zoom + '/' + x + '/' + y + customImgext;
    return tilesUrl;
};

var customTileMapType = new BMap.MapType('tileMapType', customTileLayer);
