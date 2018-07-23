<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>电子地图</title>
<%@ include file="/common/res.jsp" %>
<script type="text/javascript"	src="<%=path%>/js/baidumapv2/baidumap_offline_v2_load.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/js/baidumapv2/css/baidu_map_v2.css"/>
<script type="text/javascript">  
	$(function() {
		
		// 百度地图API功能
		var map = new BMap.Map("allmap");    // 创建Map实例
		map.centerAndZoom(new BMap.Point(116.48754182669397,39.996433116750666), 16);  // 初始化地图,设置中心点坐标和地图级别
		//map.addControl(new BMap.MapTypeControl());   //添加地图类型控件 离线只支持电子地图，卫星/三维不支持
		//map.setCurrentCity("北京");          // 设置地图显示的城市 离线地图不支持！！
		map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
	    map.addControl(new BMap.NavigationControl());   //缩放按钮
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
<body class="easyui-layout">
	<div id="allmap" style="width: 100%x; height:100%;">
	</div>
</body>
</html>
