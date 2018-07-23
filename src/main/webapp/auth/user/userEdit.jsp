<%@ page pageEncoding="UTF-8"%>
<%
	String userId = request.getParameter("id");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>修改用户</title>
<%@ include file="/common/res.jsp" %>
</head>
<body>
	<div style="margin: 20px 0;"></div>
	<div class="easyui-panel"
		style="width: 100%; max-width: 460px; padding: 30px 60px; border-width:0" >
		<form id="ff" method="post" >
			<div style="margin-bottom: 20px;width: 100%">
				<input class="easyui-textbox" id="userName" name="userName" style="width: 100%"
					data-options="label:'用户名 :',required:true,validType:['email','length[10,50]']">
			</div>
			<div style="margin-bottom: 20px">
				<input class="easyui-textbox" id="fullName" name="fullName" style="width: 100%"
					data-options="label:'姓 名 :',required:true,validType:'length[2,15]'">
			</div>
			<div style="margin-bottom:20px">
				<select class="easyui-combobox" id="sex" name="sex" label="性 别 :" style="width:100%;" panelHeight="auto"><option value="M">男</option><option value="F">女</option></select>
			</div>
			<div style="margin-bottom: 20px">
				<input class="easyui-datebox" id="birthday" name="birthday" label="出生日期 :" labelPosition="left" style="width:100%;">
			</div>
			<div style="margin-bottom: 20px">
				<input type="hidden" id="orgId" name="orgId">
				<input class="easyui-textbox" id="orgName" name="orgName" style="width: 100%">
			</div>
			<div style="margin-bottom: 20px">
				<input class="easyui-textbox" id="contactNumber" name="contactNumber" style="width: 100%"
					data-options="label:'联系电话 :',required:true" validtype='telNum'">
			</div>
		</form>
	</div>
	
	<script>
	var userNameExist = false;
		// 初始化组织树
		$.ajax( {
		    url:'<%=path%>/org/mgr/getOrgTreeValidData.do',
		    data:{
		    	'parentId':'ROOT'
		    },
		    type:'post',
		    async:false,
		    dataType:'json',
		    success:function(repObj) {
		    	if(repObj.returnCode == "success"){
		    		$('#orgName').combotree({ 	
		                data: repObj.data,
		                valueField:'id',
		                textField:'orgName',
		                required: false,
		                editable: false,
		                label:'所属组织'
		            });
		    	}else{
		    		$.messager.alert('提示', repObj.msg);
		    	}
		    },
		    error : function(repObj) {
		    	$.messager.alert('异常', repObj.responseText);
	        }
		});
	
		$(function() {
			//获取用户
			$.ajax( {
			    url:'<%=path%>/comm/queryForList.do',
			    data:{
			    	'sqlId':'alarm-user-getUserById',
			    	'userId':'<%=userId%>'
			    },
			    type:'post',
			    async:false,
			    dataType:'json',
			    success:function(data) {
			    	if(data!=null && data.length > 0){
			    		var userObj = data[0];
			    		$("#id").val(userObj.id);
			    		$("#userName").textbox('setValue',userObj.user_name);
			    		$("#fullName").textbox('setValue',userObj.full_name);
			    		$('#sex').combobox('select',userObj.sex);
			    		$("#birthday").textbox('setValue',userObj.birthday);
			    		$("#contactNumber").textbox('setValue',userObj.contact_number);
			    		$('#orgName').combotree('setValue', userObj.org_id);
			    	}
			    },
			    error : function(data) {
			    	$.messager.alert('异常',data.responseText);
		        }
			});
			
			$("#userName").textbox({  
			    onChange: function(value) {
			    	$.ajax( {
					    url:'<%=path%>/user/mgr/checkUser.do',
					    data:{
					    	'id':'<%=userId%>',
					    	'nameColumn':'userName',
					    	'userName':$(this).val()
					    },
					    type:'post',
					    async:false,
					    dataType:'json',
					    success:function(data) {
					    	if(data.userExist != null && data.userExist !='true'){
					    		userNameExist = false;
					    	}else{
					    		userNameExist = true;
					    		$.messager.alert('提示','用户名已存在，请修改用户名!');
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
		    phoneNum: { //验证手机号   
		        validator: function(value, param){
		         return /^1[3-8]+\d{9}$/.test(value);
		        },
		        message: '请输入正确的手机号码。'
		    },
		    telNum:{ //既验证手机号，又验证座机号
		      validator: function(value, param){ 
		          return /((0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$)|(^((\d3)|(\d{3}\-))?(1[358]\d{9})$)/.test(value);
		         },
		         message: '请输入正确的电话号码。' 
		    }
		});
		
		function okResponse(){
			if($("#ff").form('validate') == false){
				$.messager.alert('输入错误','请检查输入项!');
				return false;
			}
			var orgId = $('#orgName').combotree('getValue');
			if(orgId == null){
				$.messager.alert('输入错误','请选择用户所属组织!');
				return false;
			}
			
			//检查用户名是否已存在			
			if(userNameExist == true){
				$.messager.alert('提示','用户名已存在，请修改用户名!');
				return;
			}
			
			// 提交保存
			$.ajax( {
			    url:'<%=path%>/user/mgr/update.do',
			    data:{
			    	'id':'<%=userId%>',
			    	'userName':$("#userName").val(),
			    	'fullName':$("#fullName").val(),
			    	'sex':$("#sex").val(),
			    	'birthday':$("#birthday").val(),
			    	'contactNumber':$("#contactNumber").val(),
			    	'orgId':orgId
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