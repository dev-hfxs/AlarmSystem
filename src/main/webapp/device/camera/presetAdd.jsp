<%@ page pageEncoding="UTF-8"%>
<% String cameraId = request.getParameter("cameraId"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>添加摄像机预置位</title>
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
				<input class="easyui-numberbox" id="presetNum" name="presetNum" style="width: 90%"
				data-options="label:'预置位编号:',required:true">
			</div>
			<div style="margin-bottom: 10px;width: 100%">
				<input class="easyui-textbox" id="presetDesc" name="presetDesc" style="width: 90%"
					data-options="label:'预置位描述:',validType:'length[2,20]'">
			</div>
		</form>
	</div>
	
	<script>
		$(function() {
			
			
		});
		
		function okResponse(){
			if($("#ff").form('validate') == false){
				$.messager.alert('输入错误','请检查输入项!');
				return false;
			}
			
			// 提交保存
			$.ajax( {
			    url:'<%=path%>/camera/mgr/addPreset.do',
			    data:{
			    	'cameraId':'<%=cameraId%>',
			    	'presetNum':$("#presetNum").val(),
			    	'presetDesc':$("#presetDesc").val()
			    },
			    type:'post',
			    async:false,
			    dataType:'json',
			    success:function(data) {
			    	if(data.returnCode == "success"){
			    		$.messager.alert('提示', '添加成功!', 'info', function(){
			    			parentWin.closeDialogAndRefresh2();
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