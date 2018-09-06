<%@ page pageEncoding="UTF-8"%>
<% 
	String path = request.getContextPath();
	String errorInfo = (String)request.getAttribute("errorInfo");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>访问错误</title>
<script type="text/javascript" src="<%=path%>/js/jquery/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="<%=path%>/js/json2.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/js/easyui/themes/icon.css">
<script type="text/javascript" src="<%=path%>/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=path%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/style/default/main.css">
<script>
$(function() {
	//$("#msgContent").html("<%=errorInfo%>");
});
</script>
 </head>
 <body>
 	<div id="msgContent" class="msg-content">&nbsp;<%=errorInfo%></div>
 </body>
 </html>