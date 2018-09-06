<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>添加摄像机</title>
<%@ include file="/common/res.jsp" %>
<style>
.textbox-label{
	width:100px;
}
</style>
</head>
<body>
	<div style="margin: 10px 0;"></div>
	<div class="easyui-panel"
		style="width: 100%; padding: 10px 10px; border-width:0" >
		<form id="ff" method="post" >
		   <table width="600px">
		   		<tr>
		   			<td >
		   				<input class="easyui-combobox" id="classId" name="classId" label="摄像机型号:"  panelHeight="auto" style="width:250px" data-options="url:'<%=path%>/comm/queryForList.do?sqlId=alarm-camera-getAllCameraClass',method:'get',valueField:'id',textField:'model'">
					</td>
		   			<td>
		   				<input class="easyui-textbox" id="cameraNum" name="cameraNum" style="width: 250px" data-options="label:'摄像机编号:',required:true,validType:'length[1,4]'">
					</td>
		   		</tr>
		   		<tr>
		   			<td>
		   				<input class="easyui-textbox" id="longitude" name="longitude" style="width: 250px" data-options="label:'经度 :',validType:'checkLng'">
		   			</td>
		   			<td>
		   				<input class="easyui-textbox" id="latitude" name="latitude" style="width: 250px" data-options="label:'纬度 :',validType:'checkLat'">
		   			</td>
		   		</tr>
		   		<tr>
		   			<td>
		   				<input class="easyui-textbox" id="posDesc" name="posDesc" style="width: 250px" data-options="label:'位置描述:',validType:'length[0,64]'">
		   			</td>
		   			<td>
		   			    <input class="easyui-textbox" id="ip" name="ip" style="width: 250px" data-options="label:'摄像机IP :',required:true, validType:'checkIp'">
		   			</td>
		   		</tr>
		   		
		   		<tr>
		   			<td>
		   				<input class="easyui-numberbox" id="webPort" name="webPort" value="80" style="width: 250px" data-options="label:'摄像机web端口:',required:true">
		   			</td>
		   			<td>
		   				<input class="easyui-numberbox" id="devicePort" name="devicePort" value="8000" style="width: 250px" data-options="label:'设备端口:',required:true">
		   			</td>
		   		</tr>
		   		<tr>
		   			<td>
		   				<input class="easyui-textbox" id="recorderIp" name="recorderIp" style="width: 250px" data-options="label:'录像机IP :',required:true, validType:'checkIp'">
		   			</td>
		   			<td>
		   				<input class="easyui-numberbox" id="recorderPort" name="recorderPort" value="80" style="width: 250px" data-options="label:'录像机web端口:',required:true">
		   			</td>
		   		</tr>
		   		<tr>
		   			<td>
		   				<input class="easyui-textbox" id="cameraUserName" name="cameraUserName" style="width: 250px" data-options="label:'摄像机登录名:',required:true">
		   			</td>
		   			<td>
		   				<input class="easyui-textbox" id="cameraPassword" name="cameraPassword" style="width: 250px" data-options="label:'摄像机密码:',required:true">
		   			</td>
		   		</tr>
		   		<tr>
		   			<td>
		   				<input class="easyui-textbox" id="recorderUserName" name="recorderUserName" style="width: 250px" data-options="label:'录像机登录名:',required:true">
		   			</td>
		   			<td>
		   				<input class="easyui-textbox" id="recorderPassword" name="recorderPassword" style="width: 250px" data-options="label:'录像机密码:',required:true">
		   			</td>
		   		</tr>
		   		<tr>
		   			<td>
		   				<input class="easyui-numberbox" id="channelId" name="channelId"  style="width: 250px" data-options="label:'录像机通道:',required:true">
		   			</td>
		   			<td>
		   			</td>
		   		</tr>
		   </table>
		</form>
	</div>
	
	<script>
	var cameraNumExist = false;
	
	$(function() {
			$("#cameraNum").textbox({  
			    onChange: function(value) {
			    	var objValue = $(this).val();
			    	$.ajax( {
					    url:'<%=path%>/camera/mgr/checkCameraNum.do',
					    data:{
					    	'cameraId':'',
					    	'cameraNum':$(this).val()
					    },
					    type:'post',
					    async:false,
					    dataType:'json',
					    success:function(data) {
					    	if(data.exist != null && data.exist !='true'){
					    		cameraNumExist = false;
					    	}else{
					    		cameraNumExist = true;
					    		$.messager.alert('提示','摄像机编号已存在，请修改编号!');
					    		return;
					    	}
					    },
					    error : function(data) {
					    	$.messager.alert('异常',data.responseText);
				        }
					});
			    }
			});			
		});
		
		$.extend($.fn.validatebox.defaults.rules, {
			checkLng: { //验证经度
		        validator: function(value, param){
		         return  /^-?(((\d|[1-9]\d|1[1-7]\d|0)\.\d{6})|0|180)$/.test(value);
		        },
		        message: '经度整数部分为0-180,小数位保留6位!'
		    },
		    checkLat: { //验证纬度
		        validator: function(value, param){
		         return  /^-?([0-8]?\d{1}\.\d{6}|0|([0-8]?\d{1})\.\d{6}|90)$/.test(value);
		        },
		        message: '纬度整数部分为0-90,小数位保留6位!'
		    },
		    checkIp:{ //验证IP
			      validator: function(value, param){ 
			          return /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/.test(value);
			         },
			         message: '请输入正确的IP.' 
			}
		});
		
		
		function okResponse(){
			if($("#ff").form('validate') == false){
				$.messager.alert('输入错误','请检查输入项!');
				return false;
			}
			if(cameraNumExist == true){
				$.messager.alert('提示','摄像机编号已存在!');
				return false;
			}
			// 提交保存
			$.ajax( {
			    url:'<%=path%>/camera/mgr/addCamera.do',
			    data:{
			    	'classId':$("#classId").val(),
			    	'cameraNum':$("#cameraNum").val(),
			    	'posDesc':$("#posDesc").val(),
			    	'longitude':$("#longitude").val(),
			    	'latitude':$("#latitude").val(),
			    	'ip':$("#ip").val(),
			    	'webPort':$("#webPort").val(),
			    	'devicePort':$("#devicePort").val(),
			    	'cameraUserName':$("#cameraUserName").val(),
			    	'cameraPassword':$("#cameraPassword").val(),
			    	'recorderIp':$("#recorderIp").val(),
			    	'recorderPort':$("#recorderPort").val(),
			    	'recorderUserName':$("#recorderUserName").val(),
			    	'recorderPassword':$("#recorderPassword").val(),
			    	'channelId':$("#channelId").val()
			    },
			    type:'post',
			    async:false,
			    dataType:'json',
			    success:function(data) {
			    	if(data.returnCode == "success"){
			    		$.messager.alert('提示', '添加成功!', 'info', function(){
			    			parentWin.closeDialogAndRefresh();
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