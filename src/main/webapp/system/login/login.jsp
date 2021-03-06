<%@ page pageEncoding="UTF-8"%>
<% 
	String path = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>周界报警系统登录</title>
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-control" content="no-cache">
<meta http-equiv="Cache" content="no-cache">
<script type="text/javascript" src="<%=path%>/js/jquery/jquery-3.3.1.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/js/easyui/themes/icon.css">
<script type="text/javascript" src="<%=path%>/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=path%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<style>
body{
	background-image:url("<%=path%>/style/default/image/changcheng.jpg");
	background-repeat:no-repeat;
}
.msg-content{ position:relative; top:10px; width:100%; color:red; text-align:left; font-size:14px;}

.panel-title{font-size:14px}

.textbox-label{font-size:14px}
.textbox-label{
	width:80px;
}
.clear{ clear:both;}
</style>
</head>
<body>
<div style="width:400px;height:360px;margin-left:auto;margin-right:auto;margin-top:180px;filter:alpha(Opacity=5);-moz-opacity:0.5;opacity: 0.5;">
	<div class="easyui-panel" style="width:400px;height:260px;max-width:400px;padding:15px 15px;border-radius: 15px 15px 15px 15px;">
		<form id="ff" class="easyui-form" method="post">		
			<div style="margin-bottom:20px">
				<img src="<%=path%>/style/default/image/logo48.png" height="36px" width="36px" style="position:relative;float:left;"/> <div style="float:left;position:relative;top:8px;left:10px;font-size:16px;color:blue;">周 界 报 警 系 统 登 录</div><div class="clear"></div>
			</div>
			<div style="margin-bottom:20px;margin-left:30px;">
				<input class="easyui-textbox" id="userName" name="userName" style="width:80%;height:28px;" data-options="label:'用户名:',required:true,validType:['email','length[10,50]']">
			</div>
			<div style="margin-bottom:20px;margin-left:30px;">
				<input class="easyui-passwordbox" id="password" name="password" prompt="Password" iconWidth="28" style="width:80%;height:28px;" data-options="label:'密 码:',showEye:false,required:true,validType:'length[8,20]'">
			</div>
		</form>
		<div style="text-align:center;padding:5px 0">
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="doLogin()" style="width:80px;height:32px">登 录</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="doClear()" style="width:80px;height:32px">重 置</a>
		</div>
		<div id="msgContent" class="msg-content">&nbsp;</div>
	</div>
<div>
	<script>
		function doLogin(){
			if($("#ff").form('validate') == false){
				$.messager.alert('输入错误','请检查输入项!');
				return false;
			}
			
			$.ajax( {
			    url:'<%=path%>/auth/login.do',
			    data:{
			    	'userName':$("#userName").val(),
			    	'password':$("#password").val()	    	
			    },
			    type:'post',
			    async:false,
			    dataType:'json',
			    success:function(data) {
			        if (data.returnCode == "success") {
					  	$("#msgContent").html("&nbsp;&nbsp;正在登录...");
						window.location="<%=path%>/system/layout/main.jsp";
			    	}else {
					  	$("#msgContent").html(data.msg);
					}
			    },
			    error : function(data) {
			    	$.messager.alert('异常',data.responseText);
		        }
			});
		}
		
		function doClear(){
			$('#ff').form('clear');
		}
	</script>
</body>
</html>