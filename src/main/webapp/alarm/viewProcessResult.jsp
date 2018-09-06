<%@ page pageEncoding="UTF-8"%>
<%
	String alarmId = request.getParameter("id");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>处理结论查看</title>
<%@ include file="/common/res.jsp" %>
<script type="text/javascript" src="<%=path%>/js/Map.js"></script>
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
					<tr><td width="100px">入侵人员特征:</td><td colspan="3" width="500px"><div id="personFeature"></div></td></tr>
					<tr><td width="100px">报警图片:</td><td colspan="3" width="500px"><div id="alarmImage"></div></td></tr>
					<tr><td width="100px">报警原因:</td><td><div id="alarmReason"></div></td><td>&nbsp;出警人:</td><td><div id="outPolice"></div></td></tr>
					<tr><td width="100px">处置方法:</td><td colspan="3"><div id="processMethod"></div></td></tr>
					<tr><td width="100px">处置结果:</td><td width="500px" colspan="3"><div id="processResult"></div></td></tr>
				</table>
			</div>
		</form>
	</div>
<script>
var alarmReasons = {'1':'人为翻越','2':'人为敲打','3':'飞鸟敲打','4':'树枝敲打','5':'风雨天气'};
		$(function() {
			//获取报警信息
			$.ajax( {
			    url:'<%=path%>/comm/queryForList.do',
			    data:{
			    	'sqlId':'alarm-alarminfo-getAlarmProcessResult',
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
			    	
			    	$("#personFeature").text(alarmObj.person_feature);
			    	$("#alarmImage").html("<a href='<%=path%>/file/download/alarmimage.do?alarmId=<%=alarmId%>&fileName=" + alarmObj.alarm_image + "'>" + alarmObj.alarm_image + "</a>");
			    	$("#alarmReason").text(alarmReasons[alarmObj.alarm_reason]);
			    	$("#processMethod").text(alarmObj.process_method);
			    	$("#processResult").text(alarmObj.process_result);
			    },
			    error : function(resultObj) {
			    	
		        }
			});
		});
		
	</script>
</body>
</html>