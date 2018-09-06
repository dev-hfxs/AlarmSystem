<%@ page pageEncoding="UTF-8"%>
<% String pageNum = request.getParameter("pageNum"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>摄像机维护</title>
<%@ include file="/common/res.jsp" %>
<script type="text/javascript"	src="<%=path%>/js/ztree/js/jquery.ztree.all.min.js"></script>
<link rel="stylesheet" href="<%=path%>/js/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="<%=path%>/js/json2.js"></script>
<script type="text/javascript" src="<%=path%>/js/Map.js"></script>
<style>
.panel-tool{
	display:none;
}
</style>
</head>
<body>
<!--  -->
<div  class="easyui-layout"  data-options="fit:true">
	<div  data-options="region:'north',title:'摄像机列表'" style="width: 100%;height:60%;overflow:hidden;">
		<table id="dg" class="easyui-datagrid"  
			data-options="singleSelect:true,rownumbers:true,pageSize:20 ,fit:true,url:'<%=path%>/comm/queryForPage.do',pagination:true,method:'post',toolbar:'#tb', multiSort:true">
			<thead>
			<tr>
				<th data-options="field:'camera_num',width:100">摄像机编号</th>
				<th data-options="field:'model',width:180">摄像机型号</th>
				<th data-options="field:'ip',width:100">摄像机IP</th>
				<th data-options="field:'web_port',width:80">web端口</th>
				<th data-options="field:'device_port',width:80">设备端口</th>
				<th data-options="field:'recorder_ip',width:100">录像机IP</th>
				<th data-options="field:'recorder_port',width:80">录像机端口</th>
				<th data-options="field:'pos_desc',width:120">位置描述</th>
				<th data-options="field:'longitude',width:90">经度</th>
				<th data-options="field:'latitude',width:90">纬度</th>
				<th data-options="field:'camera_user_name',width:100">摄像机登录名</th>
				<th data-options="field:'camera_password',width:100">摄像机密码</th>
				<th data-options="field:'recorder_user_name',width:100">录像机登录名</th>
				<th data-options="field:'recorder_password',width:100">录像机密码</th>
				<th data-options="field:'channel_id',width:80">录像机通道</th>
				<th data-options="field:'id',width:150,align:'center',formatter:showButtons">操作</th>
			</tr>
			</thead>
		</table>
		<div id="tb" style="padding:2px 5px;">
			<a href="#" class="easyui-linkbutton" onclick="addCamera()" iconCls="icon-add">添加摄像机&nbsp;&nbsp;</a> &nbsp;&nbsp;&nbsp;&nbsp;
			<input id="inpKey" class="easyui-textbox"  prompt="摄像机编号" style="width:150px">
			<a href="#" class="easyui-linkbutton" onclick="doSearch()" iconCls="icon-search">查询&nbsp;&nbsp;</a>
		</div>
	</div>
	<div  data-options="region:'center',title:'摄像机预置点列表'" style="width: 100%;height:40%;overflow:hidden;">
		<table id="dg2" class="easyui-datagrid"  
			data-options="singleSelect:true,rownumbers:true,pageSize:10,pageList: [10, 20, 30],fit:true,url:'<%=path%>/comm/queryForPage.do',pagination:true,method:'post',toolbar:'#tb2', multiSort:true">
			<thead>
			<tr>
				<th data-options="field:'camera_num',width:100">摄像机编号</th>
				<th data-options="field:'preset_num',width:180">预置点编号</th>
				<th data-options="field:'preset_desc',width:120">预置点描述</th>
				<th data-options="field:'id',width:150,align:'center',formatter:showButtons2">操作</th>
			</tr>
			</thead>
		</table>
		<div id="tb2" style="padding:2px 5px;">
			<a href="#" class="easyui-linkbutton" onclick="addPreset()" iconCls="icon-add">添加预置点&nbsp;&nbsp;</a> &nbsp;&nbsp;&nbsp;&nbsp;
		</div>
	</div>
</div>
<script>
var curCameraId= '';

$(function() {
	
	$('#dg').datagrid({
		onClickRow:function(rowIndex,rowData){
			curCameraId = rowData.id;
			refreshPreset();
		}
	});
	
	$('#dg').datagrid({
		onLoadSuccess:function(data){
			if(data!=null && data.rows !=null && data.rows.length >0){
				refreshDetector();
			}
		}
	});
	
	var pageNum = "<%=pageNum%>";
	var queryParams = $('#dg').datagrid('options').queryParams;
	queryParams.sqlId = 'alarm-camera-getAll';
	if(pageNum != null && pageNum != 'null' && pageNum != ''){
		$('#dg').datagrid('options').pageNumber = pageNum;
	}
	$('#dg').datagrid('reload');
	$('.pagination-page-list').hide();
});

function showButtons(val,row){
	var columnItem = '';
	columnItem =  columnItem + '<span><a href="javascript:void(0)" class="easyui-linkbutton" onclick="updateCamera(\''+val+'\')" style="width:80px;">修 改</a></span>&nbsp;&nbsp;';
	columnItem =  columnItem + '<span><a href="javascript:void(0)" class="easyui-linkbutton" onclick="deleteCamera(\''+val+'\')" style="width:80px;">删 除</a></span>&nbsp;&nbsp;';
	return columnItem;
}

function showButtons2(val,row){
	var columnItem = '';
	columnItem =  columnItem + '<span><a href="javascript:void(0)" class="easyui-linkbutton" onclick="updatePreset(\''+val+'\')" style="width:80px;">修 改</a></span>&nbsp;&nbsp;';
	columnItem =  columnItem + '<span><a href="javascript:void(0)" class="easyui-linkbutton" onclick="deletePreset(\''+val+'\')" style="width:80px;">删 除</a></span>&nbsp;&nbsp;';
	return columnItem;
}

function addCamera(){
	var content = '<iframe name="popContent" id="popContent" src="<%=path%>/device/camera/cameraAdd.jsp" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>';
	var boarddiv = '<div id="msgwindow" title="添加摄像机" style="overflow:hidden;" ></div>'// style="overflow:hidden;"可以去掉滚动条
	$(document.body).append(boarddiv);
	var win = $('#msgwindow').dialog({
		content : content,
		width : '640',
		height : '480',
		modal : true,
		title : '添加摄像机',
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
			$(this).dialog('destroy');// 后面可以关闭后的事件
		}
	});
	win.dialog('open');
	win.window('center');
}

function updateCamera(val){
	var queryParams = $('#dg').datagrid('options').queryParams;
	var options = $("#dg" ).datagrid("getPager" ).data("pagination" ).options;
    var pageNum = options.pageNumber;
    var pageSize = options.pageSize;
	var curUrl = "<%=path%>/device/camera/cameraEdit.jsp?id="+val+"&pageNum="+pageNum + "&pageSize="+pageSize;
	
	var content = '<iframe name="popContent" id="popContent" src="' + curUrl + '" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>';
	var boarddiv = '<div id="msgwindow" title="修改摄像机" style="overflow:hidden;" ></div>'// style="overflow:hidden;"可以去掉滚动条
	$(document.body).append(boarddiv);
	var win = $('#msgwindow').dialog({
		content : content,
		width : '640',
		height : '480',
		modal : true,
		title : '修改摄像机',
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
			$(this).dialog('destroy');// 后面可以关闭后的事件
		}
	});
	win.dialog('open');
	win.window('center');
}

function addPreset(){
	var content = '<iframe name="popContent" id="popContent" src="<%=path%>/device/camera/presetAdd.jsp?cameraId=' + curCameraId + '" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>';
	var boarddiv = '<div id="msgwindow" title="添加摄像机预置位" style="overflow:hidden;" ></div>'// style="overflow:hidden;"可以去掉滚动条
	$(document.body).append(boarddiv);
	var win = $('#msgwindow').dialog({
		content : content,
		width : '480',
		height : '300',
		modal : true,
		title : '添加摄像机预置位',
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
			$(this).dialog('destroy');// 后面可以关闭后的事件
		}
	});
	win.dialog('open');
	win.window('center');
}

function updatePreset(val){
	var curUrl = "<%=path%>/device/camera/presetEdit.jsp?id="+val;	
	var content = '<iframe name="popContent" id="popContent" src="' + curUrl + '" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>';
	var boarddiv = '<div id="msgwindow" title="修改摄像机预置位" style="overflow:hidden;" ></div>'// style="overflow:hidden;"可以去掉滚动条
	$(document.body).append(boarddiv);
	var win = $('#msgwindow').dialog({
		content : content,
		width : '480',
		height : '300',
		modal : true,
		title : '修改摄像机预置位',
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
			$(this).dialog('destroy');// 后面可以关闭后的事件
		}
	});
	win.dialog('open');
	win.window('center');
}

function deleteCamera(val){
	$.messager.confirm('确认', '确认删除该摄像机?', function(r){
		if(r){
			$.ajax( {
			    url:'<%=path%>/camera/mgr/deleteCamera.do',
			    data:{
			    	'id':val
			    },
			    type:'post',
			    async:false,
			    dataType:'json',
			    success:function(data) {
			    	$.messager.alert('提示', '删除成功!', 'info', function(){
			    		$('#dg').datagrid('loadData',{total:0,rows:[]});
			    		$('#dg').datagrid('reload');
			    		$('#dg2').datagrid('loadData',{total:0,rows:[]});
			    		$('#dg2').datagrid('reload');
		    		});
			    },
			    error : function(data) {
			    	$.messager.alert('异常',data.responseText);
		        }
			});
		}
	});
}

function deletePreset(val){
	$.messager.confirm('确认', '确认删除预置位?', function(r){
		if(r){
			$.ajax( {
			    url:'<%=path%>/camera/mgr/deletePreset.do',
			    data:{
			    	'id':val
			    },
			    type:'post',
			    async:false,
			    dataType:'json',
			    success:function(data) {
			    	$.messager.alert('提示', '删除成功!', 'info', function(){
			    		$('#dg2').datagrid('loadData',{total:0,rows:[]});
			    		$('#dg2').datagrid('reload');
		    		});
			    },
			    error : function(data) {
			    	$.messager.alert('异常',data.responseText);
		        }
			});
		}
	});
}

function doSearch(){
	var charKey = $("#inpKey" ).val();
	var queryParams = $('#dg').datagrid('options').queryParams;
	queryParams.sqlId = 'alarm-camera-getAll';
	queryParams.cameraNum = charKey;
	$('#dg').datagrid('loadData',{total:0,rows:[]});
	$('#dg').datagrid('reload');
}

function refreshPreset(){
	var queryParams = $('#dg2').datagrid('options').queryParams;
	queryParams.sqlId = 'alarm-camera-getPresetsByCameraId';
	queryParams.cameraId = curCameraId;
	$('#dg2').datagrid('loadData',{total:0,rows:[]});
	$('#dg2').datagrid('reload');	
}

function closeDialogAndRefresh(){
	$('#msgwindow').dialog('destroy');
	$('#dg').datagrid('reload');
}

function closeDialogAndRefresh2(){
	$('#msgwindow').dialog('destroy');
	$('#dg2').datagrid('reload');
}

function closeDialog(){
	$('#msgwindow').dialog('destroy');
}
</script>
</body>
</html>