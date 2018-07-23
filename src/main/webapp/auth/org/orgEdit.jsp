<%@ page pageEncoding="UTF-8"%>
<%
	String orgId = request.getParameter("id");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>修改组织</title>
<%@ include file="/common/res.jsp" %>
</head>
<body>
	<div style="margin: 20px 0;"></div>
	<div class="easyui-panel"
		style="width: 100%; max-width: 460px; padding: 10px 60px; border-width:0" >
		<form id="ff" method="post" >
			<div style="margin-bottom: 20px;width: 100%">
				<input class="easyui-textbox" id="orgName" name="orgName" style="width: 100%"
					data-options="label:'组织名称 :',required:true,validType:'length[2,50]'">
			</div>
			<div style="margin-bottom: 20px">
				<input class="easyui-textbox" id="orgCode" name="orgCode" style="width: 100%"
					data-options="label:'组织编码 :',required:true,validType:'length[2,15]'">
			</div>
			<div style="margin-bottom: 20px">
				<input class="easyui-textbox" id="parentId" name="parentId" style="width: 100%">
			</div>
			
			<div style="margin-bottom: 20px">
				<input class="easyui-textbox" id="contacts" name="contacts" style="width: 100%"
					data-options="label:'联系人 :', validType:'length[0,10]'">
			</div>
			<div style="margin-bottom: 20px">
				<input class="easyui-textbox" id="telephone" name="telephone" style="width: 100%"
					data-options="label:'联系电话:', validType:'telNum'">
			</div>
			<div style="margin-bottom: 20px">
				<input class="easyui-textbox" id="email" name="email" style="width: 100%"
					data-options="label:'联系邮件:', validType:'email'">
			</div>
			<div style="margin-bottom: 20px">
				<input class="easyui-textbox" id="address" name="address" style="width: 100%"
					data-options="label:'地址:', validType:'length[0,20]'">
			</div>
			<div style="margin-bottom: 20px">
				<input class="easyui-numberbox" id="orderNum" name="orderNum" style="width: 100%"
					data-options="label:'序号:', validType:'length[0,3]'">
			</div>
		</form>
	</div>
	
	<script>
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
		    		$('#parentId').combotree({ 	
		                data: repObj.data,
		                valueField:'id',
		                textField:'org_name',
		                required: false,
		                editable: false,
		                label:'上级组织'
		            });
		    	}else{
		    		//$.messager.alert('提示', repObj.msg);
		    	}
		    },
		    error : function(repObj) {
		    	//$.messager.alert('异常', repObj.responseText);
	        }
		});
	
		$(function() {
			//获取组织
			$.ajax( {
			    url:'<%=path%>/comm/queryForList.do',
			    data:{
			    	'sqlId':'alarm-org-getOrgById',
			    	'orgId':'<%=orgId%>'
			    },
			    type:'post',
			    async:false,
			    dataType:'json',
			    success:function(data) {
			    	if(data!=null && data.length > 0){
			    		var orgObj = data[0];
			    		$("#orgName").textbox('setValue', orgObj.org_name);
			    		$("#orgCode").textbox('setValue', orgObj.org_code);
			    		$('#parentId').combotree('setValue', orgObj.parent_id);
			    		$("#contacts").textbox('setValue', orgObj.contacts);
			    		$("#telephone").textbox('setValue', orgObj.telephone);
			    		$("#email").textbox('setValue', orgObj.email);
			    		$("#address").textbox('setValue', orgObj.address);
			    		$("#orderNum").textbox('setValue', orgObj.order_num);
			    	}
			    },
			    error : function(data) {
			    	//$.messager.alert('异常',data.responseText);
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
			var parentId = $('#parentId').combotree('getValue');
			if(parentId == null){
				$.messager.alert('输入错误','请选择上级组织!');
				return false;
			}
			
			// 提交保存
			$.ajax( {
			    url:'<%=path%>/org/mgr/update.do',
			    data:{
			    	'orgId':'<%=orgId%>',
			    	'orgName':$("#orgName").val(),
			    	'orgCode':$("#orgCode").val(),
			    	'parentId':parentId,
			    	'contacts':$("#contacts").val(),
			    	'telephone':$("#telephone").val(),
			    	'email':$("#email").val(),
			    	'address':$("#address").val(),
			    	'orderNum':$("#orderNum").val()
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