<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>周界报警系统</title>
<%@ include file="/common/res.jsp" %>
<script>
</script>
</head>
<body class="easyui-layout">
	<div id="main-header" data-options="region:'north',border:false"
		style="width: 100%; height: 88px;">
		<div class="header-container" style="width: 100%;">
			<div class="system-info">
				<div class="logo-text div-float">
					航峰<font color="#CC0000">希萨</font> &nbsp;<font color="#FFFFFF">项目管理系统</font>
				</div>
				<div class="login-info div-float">
					用户:&nbsp;<span id="userName"></span>&nbsp;&nbsp;单位:&nbsp;<span id="orgName"></span><br><span id="roleName"></span>
				</div>
				<div class="header-button">
					<div class="bt-reset-pwd">修改密码</div>
					<div class="bt-logout">注销</div>
				</div>
			</div>
			<div class="div-clean"></div>
			<div class="header-tab" id="header-tab">
				
			</div>
		</div>
	</div>

	<div id="main-left"
		data-options="region:'west',split:true,title:'功能列表'"
		style="width: 200px; padding: 0px;">
		<div id="subMenu" style="overflow: hidden;">
		
		</div>
	</div>

	<div id="main-right" data-options="region:'center',title:'当前访问:'">
		<div id="main-container"
			style="height: 100%; width: 100%; overflow: hidden;">
			<iframe height="100%" width="100%" frameBorder="0" id="frmMain"	name="frmMain" src="" />
		</div>
	</div>
</body>
</html>
