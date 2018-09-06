<%@ page pageEncoding="UTF-8"%>
<%
	String alarmId = request.getParameter("id");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>重新确认报警</title>
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
					<tr><td width="100px">是否有效:</td><td><input type="text" id="isValid" name="isValid"></input></td><td width="120px"></td><td></td></tr>
				    
					<tr><td width="100px"><div id="labelRemark"  style="display:none">备注:</div><div id="labelHandler">处理人:</div></td>
						<td width="500px" colspan="3">
						<div id="contentRemark" style="width:100%;display:none">
						<input class="easyui-textbox" id="remark" name="remark" multiline="true" style="width:80%;height:80px;"
				data-options="label:'',validType:'length[0,200]'">
				        </div>
				        <div id="contentHandler">
				        	<select class="easyui-combogrid" id="processPerson" style="width:200px">
				        	<!-- 
				            <select class="easyui-combogrid" id="processPerson" style="width:200px" data-options="
								panelWidth: 500,
								panelMinWidth: '50%',
								idField: 'id',
								textField: 'full_name',
								url: '<%=path%>/comm/queryForList.do?sqlId=alarm-user-getAlarmHandlerUsers',
								method: 'get',
								columns: [[
									{field:'full_name',title:'姓名',width:80},
									{field:'user_name',title:'用户名',width:120},
									{field:'org_name',title:'所属组织',width:80,align:'right'}
								]],
								fitColumns: true,
								label: '',
								labelPosition: 'top' ">
						    </select>
						    -->
				        </div>
				        </td></tr>
				</table>
			</div>
		</form>
	</div>
	
	<script>
	var validOptions = [{'name':'Y','text':'有效','selected':true},{ 'name':'N', 'text':'无效'}];
	
		$(function() {
			$('#isValid').combobox({
			    data: validOptions,
			    valueField:'name',
			    textField:'text',
			    label:'',
			    panelHeight:'auto',
			    width:'200px',
			    onChange:function(){
			    	var text = $(this).combobox('getValue');
			    	if("Y" == text){
			    		$("#labelRemark").hide()
			    		$("#labelHandler").show();
			    		$("#contentRemark").hide();
			    		$("#contentHandler").show();
			    	}else if("N" == text){
			    		$("#labelRemark").show();
			    		$("#labelHandler").hide();
			    		$("#contentRemark").show();
			    		$("#contentHandler").hide(); 
			    	}
			    }
			});
			
			
			//获取报警信息
			$.ajax( {
			    url:'<%=path%>/comm/queryForList.do',
			    data:{
			    	'sqlId':'alarm-alarminfo-getAlarmConfirminfoById',
			    	'alarmId':'<%=alarmId%>'
			    },
			    type:'post',
			    async:false,
			    dataType:'json',
			    success:function(resultObj) {
			    	var alarmObj = resultObj[0];			    	
			    	//alert(alarmObj.confirm_person);
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
			    	
			    	//
			    	$('#remark').text(alarmObj.remark);
			    	$('#processPerson').combogrid({
						panelWidth: 500,
						panelMinWidth: '50%',
						idField: 'id',
						textField: 'full_name',
						url: '<%=path%>/comm/queryForList.do?sqlId=alarm-user-getAlarmHandlerUsers',
						method: 'get',
						columns: [[
							{field:'full_name',title:'姓名',width:80},
							{field:'user_name',title:'用户名',width:120},
							{field:'org_name',title:'所属组织',width:80,align:'right'}
						]],
						fitColumns: true,
						label: '',
						labelPosition: 'top',
						onLoadSuccess: function(){
							$('#processPerson').combogrid('setValue', alarmObj.process_person);
						}
					});
			    },
			    error : function(resultObj) {
			    	
		        }
			});
		});
		
		function okResponse(){
			if($("#ff").form('validate') == false){
				$.messager.alert('输入错误','请检查输入项!');
				return false;
			}
			var alarmIsValid = $("#isValid").val();
			var params = {};
			params['alarmId'] = '<%=alarmId%>';
			params['isValid'] = alarmIsValid;
			
			var processPerson = $('#processPerson').combogrid('getValue');
			if(alarmIsValid == 'Y'){
				if(processPerson == null || processPerson == ''){
					$.messager.alert('提示','请选择处警的用户.');
					return false;
				}
				params['processPerson'] = processPerson;
			}
			var remark = $('#remark').val();
			if(alarmIsValid == 'N'){
				if(remark == null || remark == ''){
					$.messager.alert('提示','请填写备注信息.');
					return false;
				}
				params['remark'] = remark;
			}
			// 提交保存
			$.ajax( {
			    url:'<%=path%>/alarm/mgr/reconfirm.do',
			    data:params,
			    type:'post',
			    async:false,
			    dataType:'json',
			    success:function(data) {
			    	if(data.returnCode == "success"){
			    		$.messager.alert('提示', '操作成功!', 'info', function(){
			    			// 刷新表格、地图
			    			parent.confirmCallBack();
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