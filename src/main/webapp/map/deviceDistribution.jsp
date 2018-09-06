<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>报警动态</title>
<%@ include file="/common/res.jsp" %>
<script type="text/javascript"	src="<%=path%>/js/baidumapv2/baidumap_offline_v2_load.js"></script>
<script type="text/javascript"	src="<%=path%>/js/baidumapv2/baidumap_config.js"></script>
<script type="text/javascript" src="<%=path%>/js/Map.js"></script>
<script type="text/javascript" src="<%=path%>/js/DateUtil.js"></script>
<script type="text/javascript" src="<%=path%>/js/coordinateTransform.js"></script>
<script type="text/javascript">
var boxIcon = new BMap.Icon("<%=path%>/style/default/image/box-marker.ico", new BMap.Size(32,32));
var detectorIcon = new BMap.Icon("<%=path%>/style/default/image/detector-marker.png", new BMap.Size(32,32));
var map;

	$(function() {
		var bdInitPosition = wgsToBd(customInitCenterPoint.latitude, customInitCenterPoint.longitude);
	    map = new BMap.Map("allmap", { mapType: customTileMapType, minZoom:customMinZoom, maxZoom:customMaxZoom});    // 创建Map实例		
		map.centerAndZoom(new BMap.Point(bdInitPosition.lon, bdInitPosition.lat), customInitZoom);  // 初始化地图,设置中心点坐标和地图级别
		map.enableScrollWheelZoom(false);     			//设置鼠标滚轮缩放
	    map.addControl(new BMap.NavigationControl());   //缩放按钮
	    // 右下角，添加比例尺
	    var scaleControl = new BMap.ScaleControl({anchor: BMAP_ANCHOR_BOTTOM_RIGHT});
	    map.addControl(scaleControl);
	    
	    //加载设备
	   //获取机箱
		$.ajax( {
		    url:'<%=path%>/comm/queryForList.do',
		    data:{
		    	'sqlId':'alarm-box-getBoxs'
		    },
		    type:'post',
		    async:false,
		    dataType:'json',
		    success:function(data) {
		    	if(data!=null && data.length > 0){
		    		$.each(data,function(i,item){
		    			//转换wgs坐标为百度坐标
		    			var bdPosition = wgsToBd(item.latitude, item.longitude);
		    			// 地图添加标记
		    			var pt = new BMap.Point(bdPosition.lon, bdPosition.lat);
		    			var alarmMarker = new BMap.Marker(pt,{icon:boxIcon});
		    			var alarmLabel = new BMap.Label(item.box_number, {offset:new BMap.Size(28,-10)});
		    			alarmMarker.setLabel(alarmLabel);
		    			map.addOverlay(alarmMarker);
		    		});
		    		//获取探测器
		    		$.each(data,function(i,item){
		    			var boxId = item.id;
		    			$.ajax( {
		    			    url:'<%=path%>/comm/queryForList.do',
		    			    data:{
		    			    	'sqlId':'alarm-detector-getDetectorsByBoxId',
		    			    	'boxId': boxId
		    			    },
		    			    type:'post',
		    			    async:false,
		    			    dataType:'json',
		    			    success:function(detectors) {
		    			    	if(detectors!=null && detectors.length > 0){
		    			    		$.each(detectors,function(i,item){
			    			    		//转换wgs坐标为百度坐标
			    		    			var bdPosition = wgsToBd(item.latitude, item.longitude);
			    		    			// 地图添加标记
			    		    			var pt = new BMap.Point(bdPosition.lon, bdPosition.lat);
			    		    			var alarmMarker = new BMap.Marker(pt,{icon:detectorIcon});
			    		    			var alarmLabel = new BMap.Label(item.nfc_number, {offset:new BMap.Size(28,-10)});
			    		    			alarmMarker.setLabel(alarmLabel);
			    		    			map.addOverlay(alarmMarker);
		    			    		});
		    			    	}
		    			    },error : function(data) {
		    			    	//$.messager.alert('异常',data.responseText);
		    		        }
		    			});
		    		});
		    	}
		    },
		    error : function(data) {
		    	//$.messager.alert('异常',data.responseText);
	        }
		});
	});
	
</script>
<style>
.layout-split-west {
    border-right: 1px solid #E6EEF8;
}
.anchorBL{
	display:none;
}
</style>
</head>
<body>
<div  class="easyui-layout"  data-options="fit:true">
	<div  data-options="region:'center'" style="width: 100%;height:100%;overflow:hidden;">
		<div id="allmap" style="width: 100%x; height:100%;">
		</div>
	</div>
</body>
</html>
