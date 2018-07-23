<%@ page pageEncoding="UTF-8"%>
<%
	String processorId = request.getParameter("id");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>修改处理器</title>
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
			<div style="margin-bottom: 20px;width: 100%">
				<input class="easyui-textbox" id="nfcNumber" name="nfcNumber" style="width: 100%"
					data-options="label:'处理器NFC序列号:',required:true,validType:'length[14,14]'">
			</div>
			<div style="margin-bottom: 20px">
				<input class="easyui-textbox" id="moxaNumber" name="moxaNumber" style="width: 100%"
					data-options="label:'MOXANFC序列号:',required:true,validType:'length[14,14]'">
			</div>
			<div style="margin-bottom: 20px">
				<input class="easyui-textbox" id="ip" name="ip" style="width: 100%"
					data-options="label:'IP:',validType:'checkIp'">
			</div>
		</form>
	</div>
	
	<script>
		$(function() {
			//获取处理器
			$.ajax( {
			    url:'<%=path%>/comm/queryForList.do',
			    data:{
			    	'sqlId':'alarm-processor-getProcessorById',
			    	'processorId':'<%=processorId%>'
			    },
			    type:'post',
			    async:false,
			    dataType:'json',
			    success:function(data) {
			    	if(data!=null && data.length > 0){
			    		var processorObj = data[0];
			    		$("#nfcNumber").textbox('setValue', processorObj.nfc_number);
			    		$('#moxaNumber').textbox('setValue', processorObj.moxa_number);
			    		$("#ip").textbox('setValue', processorObj.ip);
			    	}
			    },
			    error : function(data) {
			    	//$.messager.alert('异常',data.responseText);
		        }
			});
			
			$("#nfcNumber").textbox('textbox').bind("keyup", function () { $(this).val($(this).val().toUpperCase());});
			$("#moxaNumber").textbox('textbox').bind("keyup", function () { $(this).val($(this).val().toUpperCase());});
		});
		
		$.extend($.fn.validatebox.defaults.rules, {
			 checkIp : {// 验证IP地址  
			        validator : function(value) {  
			            var reg = /^((1?\d?\d|(2([0-4]\d|5[0-5])))\.){3}(1?\d?\d|(2([0-4]\d|5[0-5])))$/ ;  
			            return reg.test(value);  
			        },  
			        message : 'IP地址格式不正确'
			}
		});
		
		
		function okResponse(){
			if($("#ff").form('validate') == false){
				$.messager.alert('输入错误','请检查输入项!');
				return false;
			}
			
			// 提交保存
			$.ajax( {
			    url:'<%=path%>/processor/mgr/update.do',
			    data:{
			    	'processorId':'<%=processorId%>',
			    	'nfcNumber':$("#nfcNumber").val(),
			    	'moxaNumber':$("#moxaNumber").val(),
			    	'ip':$("#ip").val()
			    },
			    type:'post',
			    async:false,
			    dataType:'json',
			    success:function(data) {
			    	if(data.returnCode == "success"){
			    		$.messager.alert('提示', '修改成功!', 'info', function(){
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