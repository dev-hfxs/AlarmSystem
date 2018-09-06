<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>缓存管理</title>
<%@ include file="/common/res.jsp" %>
<script type="text/javascript"	src="<%=path%>/js/ztree/js/jquery.ztree.all.min.js"></script>
<link rel="stylesheet" href="<%=path%>/js/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="<%=path%>/js/json2.js"></script>
<script type="text/javascript" src="<%=path%>/js/Map.js"></script>
<style>
.panel-tool{
	display:none;
}
</style>
</head>
<body>
<!--  -->
<div  class="easyui-layout"  data-options="fit:true">
	<div  data-options="region:'center',title:'缓存管理'" style="width: 100%;height:100%;overflow:hidden;padding-left:20px;padding-top:20px;">
		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="refreshCache()" style="width:120px;height:28px">刷新基础数据缓存</a>
	</div>
</div>
<script>
function refreshCache(){
	$.ajax( {
	    url:'<%=path%>/cache/mgr/refresh.do',
	    data:{},
	    type:'post',
	    async:false,
	    dataType:'json',
	    success:function(data) {
	        if (data.returnCode == "success") {
	        	$.messager.alert('提示','操作成功!');
	    	}else {
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