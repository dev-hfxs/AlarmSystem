<%@ page pageEncoding="UTF-8"%>
<%
	String alarmId = request.getParameter("id");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>处理结论填写</title>
<%@ include file="/common/res.jsp" %>
<style>
.textbox-label{
	display:none;
}
</style>
</head>
<body>
	<div style="margin: 10px 0;"></div>
	<div class="easyui-panel"
		style="width: 100%; padding: 10px 10px; border-width:0" >
		<form id="ff" method="post" >
			<div style="margin-bottom:0px;width: 100%">
				<table>
					<tr><td width="100px">报警信息:</td><td colspan="3" width="500px"><div id="textAlarmDesc"></div></td></tr>
					<tr><td width="100px">位置信息:</td><td colspan="3"><div id="textPosDesc"></div></td></tr>
					<tr><td width="100px">报警设备:</td><td  width="200px"><div id="textDeviceType"></div></td><td width="100px">NFC号:</td><td  width="200px"><div id="textNFCNum"></div></td></tr>
					<tr><td width="100px">报警类型:</td><td><div id="textAlarmType"></div></td><td width="80px">报警时间:</td><td><div id="textAlarmDate"></div></td></tr>
					<tr><td width="100px">经度:</td><td><div id="textLongitude"></div></td><td >纬度:</td><td><div id="textLatitude"></div></td></tr>
					<tr><td width="100px">入侵人员特征:</td><td colspan="3" width="500px"><input class="easyui-textbox" id="personFeature" name="personFeature" multiline="true" style="width:100%;height:40px;"
				data-options="label:'',validType:'length[0,200]'"></td></tr>
					<tr><td width="100px">报警图片:</td><td colspan="3" width="500px"><input id="alarmImage" name="alarmImage" style="width:200px"></input><input type="hidden" name="alarmId" value="<%=alarmId%>"></input></td></tr>
					<tr><td width="100px">报警原因:</td><td><input type="text" id="alarmReason" name="alarmReason"></input></td><td>&nbsp;出警人:</td><td><input class="easyui-textbox" id="outPolicePerson" name="outPolicePerson" style="width: 100%"
					data-options="label:'',validType:'length[0,20]'"></td></tr>
					<tr><td width="100px">处置方法:</td><td colspan="3"><input class="easyui-textbox" id="processMethod" name="processMethod" style="width: 80%"
					data-options="label:'',validType:'length[0,20]'"></td></tr>
				    
					<tr><td width="100px">处置结果:</td>
						<td width="500px" colspan="3">
						<input class="easyui-textbox" id="processResult" name="processResult" multiline="true" style="width:80%;height:80px;"
				data-options="label:'',validType:'length[0,200]'">
				        </td></tr>
				</table>
			</div>
		</form>
	</div>
	
	<script>
	var alarmReasons = [{'name':'1','text':'人为翻越','selected':true},{ 'name':'2', 'text':'人为敲打'},{ 'name':'3', 'text':'飞鸟敲打'},{ 'name':'4', 'text':'树枝敲打'},{ 'name':'5', 'text':'风雨天气'}];
	var imageValid = false;
	
		$(function() {
			
			$('#alarmReason').combobox({
			    data: alarmReasons,
			    valueField:'name',
			    textField:'text',
			    label:'',
			    panelHeight:'auto',
			    width:'100%'
			});
			
			$("#alarmImage").filebox({prompt:'jpg | png',
				accept:'image/jpeg,image/png',
				buttonText:'选择',
				onChange:function(){
					//验证文件类型
					var filePath = $(this).filebox('getValue');
					var dotPos=filePath.lastIndexOf(".");
					var nameLength=filePath.length;
					var suffix=filePath.substring(dotPos+1,nameLength);
					if(suffix ==="jpg" || suffix ==="png"){
						
					}else{
						$.messager.alert('提示','文件类型有误.');
						imageValid = false;
						return;
					}
					imageValid = true;
					//上传图片
					$.ajax({
					     url: "<%=path%>/file/upload/alarmImage.do",
					     type: 'POST',
					     cache: false,
					     data: new FormData($('#ff')[0]),
					     dataType:"json",
					     processData: false,
					     contentType: false,
					     beforeSend: function(){
					     },
					     success : function(data) {
					     }
					 });
				}
			});
			//获取报警信息
			$.ajax( {
			    url:'<%=path%>/comm/queryForList.do',
			    data:{
			    	'sqlId':'alarm-alarminfo-getAlarminfoById',
			    	'alarmId':'<%=alarmId%>'
			    },
			    type:'post',
			    async:false,
			    dataType:'json',
			    success:function(resultObj) {
			    	var alarmObj = resultObj[0];
			    	$("#textAlarmDesc").text(alarmObj.alarm_desc);
			    	$("#textPosDesc").text(alarmObj.pos_desc);
			    	if(alarmObj.device_type == 'P'){
			    		$("#textDeviceType").text('处理器');
			    	}else if(alarmObj.device_type == 'D'){
			    		$("#textDeviceType").text('探测器');
			    	}
			    	
			    	$("#textNFCNum").text(alarmObj.nfc_number);
			    	if(alarmObj.alarm_type == '1'){
			    		$("#textAlarmType").text('不在线');
			    	}else if(alarmObj.alarm_type == '2'){
			    		$("#textAlarmType").text('感知');
			    	}
			    	$("#textAlarmDate").text(alarmObj.alarm_date);
			    	$("#textLongitude").text(alarmObj.longitude);
			    	$("#textLatitude").text(alarmObj.latitude);	
			    },
			    error : function(resultObj) {
			    	
		        }
			});
		});
		
		function okResponse(){
			
			var processMethod = $("#processMethod").val();
			if(processMethod == null || processMethod == ''){
				$.messager.alert('提示','请填写处理方法.');
				return false;
			}
			var processResult = $("#processResult").val();
			if(processResult == null || processResult == ''){
				$.messager.alert('提示','请填写处理结果.');
				return false;
			}
			
			var personFeature = $("#personFeature").val();
			var alarmReason = $("#alarmReason").val();
			var outPolice = $("#outPolice").val();
			var alarmImage = $("[type='file'][class='textbox-value']").first().val();
			if(alarmImage.length > 0){
				alarmImage = alarmImage.substring(alarmImage.lastIndexOf("\\") + 1);
			}
			if(imageValid == false){
				$.messager.alert('提示','文件类型有误.');
				return;
			}
			// 提交保存
			$.ajax( {
			    url:'<%=path%>/alarm/mgr/process.do',
			    data:{
			    	'alarmId':'<%=alarmId%>',
			    	'processMethod':processMethod,
			    	'processResult':processResult,
			    	'personFeature':personFeature,
			    	'alarmImage':alarmImage,
			    	'alarmReason':alarmReason,
			    	'outPolice':outPolice
			    },
			    type:'post',
			    async:false,
			    dataType:'json',
			    success:function(data) {
			    	if(data.returnCode == "success"){
			    		$.messager.alert('提示', '操作成功!', 'info', function(){
			    			parent.processCallBack();
			    		});
			    	}else{
			    		//$.messager.alert('提示',data.msg);
			    	}
			    },
			    error : function(data) {
			    	//$.messager.alert('异常',data.responseText);
		        }
			});
		}
		
	</script>
</body>
</html>