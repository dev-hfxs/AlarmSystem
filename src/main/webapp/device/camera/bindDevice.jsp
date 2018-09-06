<%@ page pageEncoding="UTF-8"%>
<% 
String deviceId = request.getParameter("deviceId"); 
String deviceType = request.getParameter("deviceType"); 
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>设备关联摄像机</title>
<%@ include file="/common/res.jsp" %>
<style>
.textbox-label{
	width:120px;
}
</style>
</head>
<body>
	<div style="margin: 10px 0;"></div>
	<div class="easyui-panel"
		style="width: 100%; max-width: 460px; padding: 10px 60px; border-width:0" >
		<form id="ff" method="post" >
			<div style="margin-bottom: 10px;width: 100%">
				<input id="camera" name="camera" >
			</div>
			
			<div style="margin-bottom: 10px;width: 100%">
				<input id="preset" name="preset" >
			</div>
		</form>
	</div>
	
	<script>
	var hasBind = 'N';
	
	$(function() {
		$('#camera').combogrid({
			width:'300px',
			panelWidth: '300px',
			panelMinWidth: '200px',
		    mode: 'remote',
		    url: '<%=path%>/comm/queryForList.do?sqlId=alarm-camera-getAll',
		    method: 'get',
		    idField: 'id',
		    textField: 'model',
		    label: '摄像机:',
			labelPosition: 'left',
		    columns: [[
		        {field:'model',title:'摄像机型号',width:100},
		        {field:'ip',title:'IP',width:100},
		        {field:'pos_desc',title:'位置描述',width:180}
		    ]],
		    onChange:function(){
		    	var curCameraId = $(this).combobox('getValue');
		    	$('#preset').combogrid({
					width:'300px',
					panelWidth: '300px',
					panelMinWidth: '200px',
				    mode: 'remote',
				    url: '<%=path%>/comm/queryForList.do?sqlId=alarm-camera-getPresetsByCameraId&cameraId='+curCameraId,
				    method: 'get',
				    idField: 'id',
				    textField: 'preset_num',
				    label: '预置位:',
					labelPosition: 'left',
				    columns: [[
				        {field:'preset_num',title:'预置位编号',width:80},
				        {field:'preset_desc',title:'预置位描述',width:180}
				    ]]
				});
		    },
		    onLoadSuccess:function(){
		    	// 获取该设备是否已设置关联摄像机
				$.ajax( {
				    url:'<%=path%>/comm/queryForList.do',
				    data:{
				    	'sqlId':'alarm-camera-getCameraDeployByDeviceId',
				    	'deviceId':'<%=deviceId%>'
				    },
				    type:'post',
				    async:false,
				    dataType:'json',
				    success:function(data) {
				    	if(data!=null && data.length > 0){
				    		hasBind = 'Y';
				    		var cameraDeployObj = data[0];
				    		$("#camera").combogrid('setValue', cameraDeployObj.camera_id);
				    		$('#preset').combogrid({
									width:'300px',
									panelWidth: '300px',
									panelMinWidth: '200px',
								    mode: 'remote',
								    url: '<%=path%>/comm/queryForList.do?sqlId=alarm-camera-getPresetsByCameraId&cameraId='+cameraDeployObj.camera_id,
								    method: 'get',
								    idField: 'id',
								    textField: 'preset_num',
								    label: '预置位:',
									labelPosition: 'left',
								    columns: [[
								        {field:'preset_num',title:'预置位编号',width:80},
								        {field:'preset_desc',title:'预置位描述',width:180}
								    ]]
							});
				    		$("#preset").combogrid('setValue', cameraDeployObj.preset_id);
				    	}else{
				    		$('#preset').combogrid({
								width:'300px',
								panelWidth: '300px',
								panelMinWidth: '200px',
							    mode: 'local',
							    data:[],
							    idField: 'id',
							    textField: 'preset_num',
							    label: '预置位:',
								labelPosition: 'left',
							    columns: [[
							        {field:'preset_num',title:'预置位编号',width:80},
							        {field:'preset_desc',title:'预置位描述',width:180}
							    ]]
							});
				    	}
				    },
				    error : function(data) {
				    	//$.messager.alert('异常',data.responseText);
			        }
				});
		    }
		});
		
	});
		
	function okResponse(){
		var cameraId = $('#camera').combogrid('getValue');
		var presetId = $('#preset').combogrid('getValue');
		if(cameraId == null){
			$.messager.alert('提示', '请选择摄像机.');
			return;
		}
		if(presetId == null){
			$.messager.alert('提示', '请选择摄像机预置位.');
			return;
		}
		// 提交保存
		$.ajax( {
		    url:'<%=path%>/camera/mgr/bindDevice.do',
		    data:{
		    	'deviceId':'<%=deviceId%>',
		    	'deviceType':'<%=deviceType%>',
		    	'cameraId':cameraId,
		    	'presetId':presetId,
		    	'hasBind':hasBind
		    },
		    type:'post',
		    async:false,
		    dataType:'json',
		    success:function(data) {
		    	if(data.returnCode == "success"){
		    		$.messager.alert('提示', '设置成功!', 'info', function(){
		    			//
		    			parent.closeDialog();
		    		});
		    	}else{
		    		$.messager.alert('提示',data.msg);
		    	}
		    },
		    error : function(data) {
		    	$.messager.alert('异常',data.responseText);
	        }
		});
	}
	</script>
</body>
</html>