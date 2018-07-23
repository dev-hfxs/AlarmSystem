<%@ page pageEncoding="UTF-8"%>
<%
	String roleId = request.getParameter("id");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>修改角色</title>
<%@ include file="/common/res.jsp" %>
</head>
<body>
	<div style="margin: 10px 0;"></div>
	<div class="easyui-panel"
		style="width: 100%; max-width: 460px; padding: 10px 60px; border-width:0" >
		<form id="ff" method="post" >
			<div style="margin-bottom: 20px;width: 100%">
				<input class="easyui-textbox" id="roleName" name="roleName" style="width: 100%"
					data-options="label:'角色名称 :',required:true,validType:'length[3,20]'">
			</div>
			<div style="margin-bottom: 20px;width: 100%">
				<input class="easyui-textbox" id="roleDesc" name="roleDesc" multiline="true" style="width: 100%;height:80px;"
				data-options="label:'角色描述 :',validType:'length[0,200]'">
			</div>
	</div>	
	<script>
	$(function() {
		//获取角色
		$.ajax( {
		    url:'<%=path%>/comm/queryForList.do',
		    data:{
		    	'sqlId':'alarm-role-getRoleById',
		    	'roleId':'<%=roleId%>'
		    },
		    type:'post',
		    async:false,
		    dataType:'json',
		    success:function(data) {
		    	if(data!=null && data.length > 0){
		    		var roleObj = data[0];
		    		$("#roleName").textbox('setValue', roleObj.role_name);
		    		$("#roleDesc").textbox('setValue',userObj.role_desc);
		    	}
		    },
		    error : function(data) {
		    	//$.messager.alert('异常',data.responseText);
	        }
		});
	});
		
	function okResponse(){
		var roleName = $('#roleName').val();
		if(roleName == null){
			$.messager.alert('提示','请输入角色名称!');
			return false;
		}
		
		// 提交保存
		$.ajax( {
		    url:'<%=path%>/role/mgr/update.do',
		    data:{
		    	'roleId':'<%=roleId%>',
		    	'roleName':$("#roleName").val(),
		    	'roleDesc':$("#roleDesc").val()
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
		    		//$.messager.alert('提示',data.msg);
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