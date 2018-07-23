<%@ page pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String forceFlag = request.getParameter("forceFlag");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>用户修改密码</title>
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-control" content="no-cache">
<meta http-equiv="Cache" content="no-cache">
<script type="text/javascript"	src="<%=path%>/js/jquery/jquery-3.3.1.min.js"></script>
<link rel="stylesheet" type="text/css"	href="<%=path%>/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"	href="<%=path%>/js/easyui/themes/icon.css">
<script type="text/javascript"	src="<%=path%>/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"	src="<%=path%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"	src="<%=path%>/js/common.js"></script>
</head>
<body>
	<div style="margin: 20px 0;"></div>
	<div class="easyui-panel"
		style="width: 100%; max-width: 460px; padding: 30px 60px; border-width:0" >
		<form id="ff" method="post" >
			<div style="margin-bottom:20px">
				<input class="easyui-passwordbox" id="oldPwd" name="oldPwd" iconWidth="28" style="width:100%;height:28px;" data-options="label:'原 密 码:',showEye:false,required:true,validType:'length[8,20]'">
			</div>
			<div style="margin-bottom:20px">
				<input class="easyui-passwordbox" id="newPwd" name="newPwd" iconWidth="28" style="width:100%;height:28px;" data-options="label:'新 密 码:',showEye:false,required:true,validType:'length[8,20]'">
			</div>
			<div style="margin-bottom:20px">
				<input class="easyui-passwordbox" id="confirmPwd" name="confirmPwd" iconWidth="28" style="width:100%;height:28px;" data-options="label:'确认新密码:',showEye:false,required:true,validType:'length[8,20]'">
			</div>
		</form>
		<!-- 
		<div style="text-align: center; padding: 5px 0">
			<a href="javascript:void(0)" class="easyui-linkbutton"	onclick="doSubmit()" style="width: 80px">确认</a> &nbsp;&nbsp;
			<a href="javascript:void(0)" class="easyui-linkbutton" 	onclick="doCancel()" style="width: 80px">取消</a>
		</div>
		-->
	</div>
	
	<script>
	
	function okResponse() {		
			if($("#ff").form('validate') == false){
				$.messager.alert('输入错误','请检查输入项!');
				return false;
			}
			var newPwd = $("input[name='newPwd']").val();
			var confirmPwd = $("input[name='confirmPwd']").val();
			if(newPwd != confirmPwd){
				$.messager.alert('提示','两次新密码输入不一致!');
				return false;
			}
			
			// 提交保存			
			$.ajax( {
			    url:'<%=path%>/user/mgr/pwd/change.do',
			    data:{
			    	'id':'${loginUser.id}',
			    	'oldPwd':$("#oldPwd").val(),
			    	'newPwd':$("#newPwd").val()
			    },
			    type:'post',
			    async:false,
			    dataType:'json',
			    success:function(data) {
			    	if(data.returnCode == "success"){
			    		$.messager.alert('提示','操作成功！', 'info', function(){
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