<%@ page pageEncoding="UTF-8"%>
<% String processorId = request.getParameter("processorId"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>添加探测器</title>
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
				<input class="easyui-textbox" id="detectorSeq" name="detectorSeq" style="width: 90%"
					data-options="label:'探测器编号:',required:true,validType:'checkDCode'">
			</div>
			<div style="margin-bottom: 20px;width: 100%">
				<input class="easyui-textbox" id="nfcNumber" name="nfcNumber" style="width: 90%"
					data-options="label:'探测器NFC序列号:',required:true,validType:'length[14,14]'">
			</div>
			<div style="margin-bottom: 20px">
				<input class="easyui-textbox" id="longitude" name="longitude" style="width: 90%"
					data-options="label:'经度 :',required:true,validType:'checkLng'">
			</div>
			<div style="margin-bottom: 20px">
				<input class="easyui-textbox" id="latitude" name="latitude" style="width: 90%"
					data-options="label:'纬度 :',required:true,validType:'checkLat'">
			</div>
			<div style="margin-bottom: 20px">
				<input class="easyui-textbox" id="posDesc" name="posDesc" style="width: 90%"
					data-options="label:'位置描述:', validType:'length[0,20]'">
			</div>
			<!-- 
			<div style="margin-bottom: 20px;width: 100%">
				<input class="easyui-textbox" id="orderNum" name="orderNum" style="width: 90%"
					data-options="label:'顺序号:',required:true,validType:'length[1,3]'">
			</div>
			 -->
		</form>
	</div>
	
	<script>
		$(function() {
			$("#nfcNumber").textbox('textbox').bind("keyup", function () { $(this).val($(this).val().toUpperCase());});
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
		    checkDCode: { //验证探测器编号
		        validator: function(value, param){
		         return  /^((00[1-9])|(0[1-9][0-9])|(1[0-9][0-9])|(2[0-3][0-9])|240){1}$/.test(value);
		        },
		        message: '探测器编号为001-240的数字组成!'
		    }
		});
		
		
		function okResponse(){
			if($("#ff").form('validate') == false){
				$.messager.alert('输入错误','请检查输入项!');
				return false;
			}
			
			// 提交保存
			$.ajax( {
			    url:'<%=path%>/detector/mgr/add.do',
			    data:{
			    	'nfcNumber':$("#nfcNumber").val(),
			    	'longitude':$("#longitude").val(),
			    	'latitude':$("#latitude").val(),
			    	'processorId':'<%=processorId%>',
			    	'detectorSeq':$("#detectorSeq").val(),
			    	'posDesc':$("#posDesc").val()
			    	
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