<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>报警动态</title>
<%@ include file="/common/res.jsp" %>
<script type="text/javascript"	src="<%=path%>/js/baidumapv2/baidumap_offline_v2_load.js"></script>
<script type="text/javascript" src="<%=path%>/js/Map.js"></script>
<script type="text/javascript" src="<%=path%>/js/DateUtil.js"></script>
<script type="text/javascript">
var alarmsArray = new Array();
var lastLoadIndex = 0;

var alarmRedIcon = new BMap.Icon("<%=path%>/style/default/image/alarm-marker-red.ico", new BMap.Size(32,32));
var alarmYellowIcon = new BMap.Icon("<%=path%>/style/default/image/alarm-marker-yellow.ico", new BMap.Size(32,32));
var alarmGreenIcon = new BMap.Icon("<%=path%>/style/default/image/alarm-marker-green.ico", new BMap.Size(32,32));
var map;
var alarmsMap = new Map();

var lastLoadIndex = 0;
	$(function() {
		var tilesRootPath = 'http://localhost:8080/mapImgSrv';    //地图瓦片所在的文件夹
	    var imgType = ".jpg";    //格式

	    var tileLayer = new BMap.TileLayer();
	    tileLayer.getTilesUrl = function (tileCoord, zoom) {
	    	var tilesPath = "";
	        var x = (tileCoord.x + "").replace(/-/gi, "M");
	        var y = (tileCoord.y + "").replace(/-/gi, "M");
			// zoom从0开始,0表示1级
			if(zoom < 12){
			    //12级
				tilesPath = tilesRootPath + '/wmap12/tiles/';
			}else if( zoom > 11 && zoom < 15){
			    //13级-15级
				tilesPath =  tilesRootPath + '/chinaMap15/tiles/';
			}else if( zoom == 15){
				tilesPath =  tilesRootPath + '/xjMap16/tiles/';
			}else if( zoom == 16){
				tilesPath =  tilesRootPath + '/xjMap17/tiles/';
			}else if( zoom == 17){
				tilesPath =  tilesRootPath + '/xjMap18/tiles/';
			}
			
	        var tilesUrl = tilesPath + zoom + '/' + x + '/' + y + imgType;
	        return tilesUrl;
	    }
	    var tileMapType = new BMap.MapType('tileMapType', tileLayer);
	    
		map = new BMap.Map("allmap", { mapType: tileMapType,minZoom:7,maxZoom:17 });    // 创建Map实例		
		map.centerAndZoom(new BMap.Point(116.376592,39.889669), 11);  // 初始化地图,设置中心点坐标和地图级别
		map.enableScrollWheelZoom(false);     			//设置鼠标滚轮缩放
	    map.addControl(new BMap.NavigationControl());   //缩放按钮
	    // 右下角，添加比例尺
	    var scaleControl = new BMap.ScaleControl({anchor: BMAP_ANCHOR_BOTTOM_RIGHT});
	    map.addControl(scaleControl);
	    
	    initLoadAlarms();
	    
	    //refreshMap();
	});
	
	function alarmMarkerClick(e){
		//var p = e.target;
		//marker的位置是" + p.getPosition().lng + "," + p.getPosition().lat 
		
	}
	
	function showPosTip(alarmId){
		var alarmObj = alarmsMap.get(alarmId);
		if(alarmObj != null){
			//移动至目标位置
			var point = new BMap.Point(alarmObj.longitude, alarmObj.latitude);
			map.panTo(point);
			//弹出位置信息窗口
			var opts = {
					width : 250,     // 信息窗口宽度
					height: 80,     // 信息窗口高度
					title : "报警位置信息" , // 信息窗口标题
					enableMessage:true //设置允许信息窗发送短息
				};
			//var content = alarmObj.alarm_desc;
			var content = '经纬度[' + alarmObj.longitude + ',' + alarmObj.latitude + '];位置描述['+alarmObj.pos_desc + '].';
			var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象 
		    map.openInfoWindow(infoWindow,point); //开启信息窗口
		}
	}
	
	function closeDialog(){
		$('#msgwindow').dialog('destroy');
	}
	
	function fmStatus(val,row){
		var columnItem = '';
		if(val == 'N'){
			columnItem =  columnItem + '<span>未处理</span>';
		}else if(val == 'I'){
			columnItem =  columnItem + '<span>待处理</span>';
		}
		
		return columnItem;
	}
	
	function fmAlarmDesc(val,row){
		var columnItem = '<span title="'+ val+'">'+ val +'</span>';		
		return columnItem;
	}
	
	function showButtons(val,row){
		var columnItem = '';
		columnItem = '<span><a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:showPosTip(\''+val+'\')" style="width:80px;">位置</a></span>&nbsp;&nbsp;';
		if(row.status == 'N'){
			columnItem = columnItem + '<span><a href="javascript:void(0)" class="easyui-linkbutton" onclick="doConfirm(\''+val+'\')" style="width:80px;">确警</a></span>&nbsp;&nbsp;';
		}
		
		return columnItem;
	}

	function doConfirm(alarmId){
		var content = '<iframe name="popContent" id="popContent" src="<%=path%>/alarm/alarmConfirm.jsp?id=' + alarmId + '" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>';
		var boarddiv = '<div id="msgwindow" title="报警信息确认" style="overflow:hidden;"></div>'// style="overflow:hidden;"可以去掉滚动条
		$(document.body).append(boarddiv);
		var win = $('#msgwindow').dialog({
			content : content,
			width : '640',
			height : '480',
			modal : true,
			title : '报警信息确认',
			buttons: [{
				text:'&nbsp;&nbsp;&nbsp;&nbsp;确定&nbsp;&nbsp;&nbsp;&nbsp;',
				handler:function(){
					$("#popContent")[0].contentWindow.okResponse();
				}
			},{
				text:'&nbsp;&nbsp;&nbsp;&nbsp;取消&nbsp;&nbsp;&nbsp;&nbsp;',
				handler:function(){
					$('#msgwindow').dialog('destroy');
				}
			}],
			onClose : function() {
				$(this).dialog('destroy');
			}
		});
		win.dialog('open');
		win.window('center');
	}
	
	function confirmCallBack(){
		closeDialog();
		initLoadAlarms();
	}
	
	function refreshParts(){
		if(alarmsArray != null && alarmsArray.length > lastLoadIndex){
			var lastAlarm = alarmsArray[alarmsArray.length - 1 ];
    		for(var i=lastLoadIndex; i<alarmsArray.length; i++){
    			var alarmObj = alarmsArray[i];
    			alarmsMap.put(alarmObj.id, alarmObj);
    			
    			var alarmRecord = {};
    			alarmRecord['alarm_desc'] = alarmObj.alarm_desc;
    			alarmRecord['pos_desc'] = alarmObj.pos_desc;
    			alarmRecord['alarm_date'] = alarmObj.alarm_date;
    			alarmRecord['status'] = alarmObj.status;
    			alarmRecord['id'] = alarmObj.id;
    			// 报警动态信息列表，增加报警记录
    			$('#dg').datagrid('insertRow',{index: 0, row: alarmRecord});
    			
    			// 地图添加标记
    			var pt = new BMap.Point(alarmObj.longitude, alarmObj.latitude);
    			var alarmIcon = alarmRedIcon;
    			if(alarmObj.status == 'I'){
    				alarmIcon = alarmGreenIcon;
    			}else if(alarmObj.status == 'N'){
    				var compareResult = isPastTime(alarmObj.alarm_date);
    				if(compareResult == 1){
						// 报警日期大于一天
     				    alarmIcon = alarmYellowIcon;
    				}
    			}
    			var alarmMarker = new BMap.Marker(pt,{icon:alarmIcon});
    			var alarmLabel = new BMap.Label(alarmObj.alarm_desc, {offset:new BMap.Size(28,-10)});
    			alarmMarker.setLabel(alarmLabel);
    			alarmMarker.addEventListener("click", alarmMarkerClick);
    			map.addOverlay(alarmMarker);
    		}
    		lastLoadIndex = alarmsArray.length;
    		// 移动到 lastAlarm 最新报警消息的坐标
    		map.panTo(new BMap.Point(lastAlarm.longitude, lastAlarm.latitude));
    	}
		setTimeout("refreshParts()", 3500);
	}
	
	function initLoadAlarms(){
		// todo 如果是处理角色的用户登入，此处需修改为只获取分配给该用户的报警消息
		$.ajax( {
		    url:'<%=path%>/alarm/getAlarms4Observer.do',
		    data:{},
		    type:'post',
		    async:false,
		    dataType:'json',
		    success:function(resultObj) {
		    	console.log(resultObj.length);
		    	var allAlarms = resultObj;
		    	lastLoadIndex = 0;
		    	map.clearOverlays();
		    	$('#dg').datagrid('loadData',{total:0,rows:[]});		    	
		    	if(allAlarms != null && allAlarms.length > 0){
		    		alarmsArray = new Array();
		    		for(var i=0; i<allAlarms.length; i++){
		    			alarmsArray.push(allAlarms[i]);
		    		}
		    	}
		    	refreshParts();
		    	setTimeout("refreshAlarms()", 3000);
		    },
		    error : function(resultObj) {
		    	//
	        }
		});
	}
	
	function refreshAlarms(){
		$.ajax( {
		    url:'<%=path%>/alarm/getNewAlarms.do',
		    data:{},
		    type:'post',
		    async:false,
		    dataType:'json',
		    success:function(resultObj) {
		    	
		    	var newAlarms = resultObj;
		    	if(newAlarms != null && newAlarms.length > 0){
		    		for(var i=0; i<newAlarms.length; i++){
		    			alarmsArray.push(newAlarms[i]);
		    		}
		    		//$.merge(alarmsArray, newAlarms);
		    	}
		    	setTimeout("refreshAlarms()", 3000);
		    },
		    error : function(resultObj) {
		    	//
		    	setTimeout("refreshAlarms()", 3000);
	        }
		});
	}
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
	<div data-options="region:'west',split:true,title:'报警动态信息'"	style="width: 430px;height:100%; padding: 0px;">
		<div id="alarmInfo" style="overflow: hidden;height:100%">
			<table id="dg" class="easyui-datagrid"  data-options="singleSelect:true,url:'',rownumbers:true,pageSize:30,height:'100%',pagination:false,method:'post',multiSort:true">
		    <thead>
			<tr>
			    <th data-options="field:'alarm_desc',width:110, formatter:fmAlarmDesc">报警描述</th>
				<th data-options="field:'alarm_date',width:130">报警时间</th>
				<th data-options="field:'status',width:50, formatter:fmStatus">状态</th>
				<th data-options="field:'id', width:80, formatter:showButtons">操作</th>
			</tr>
			</thead>
			</table>
		</div>
	</div>
	<div  data-options="region:'center'" style="width: 100%;height:100%;overflow:hidden;">
		<div id="allmap" style="width: 100%x; height:100%;">
		</div>
	</div>
</body>
</html>
