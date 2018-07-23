<%@ page pageEncoding="UTF-8"%>
<% String pageNum = request.getParameter("pageNum"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>角色管理</title>
<%@ include file="/common/res.jsp" %>
<style>

</style>
</head>
<body>
<!--  -->
<div id="dgPanel" class="easyui-panel" data-options="fit:true">
	<table id="dg" class="easyui-datagrid"  
			data-options="title:'角色列表',singleSelect:true,rownumbers:true,pageSize:30 ,fit:true,url:'<%=path%>/comm/queryForPage.do',pagination:true,method:'post',toolbar:'#tb', multiSort:true">
		<thead>
			<tr>
				<th data-options="field:'role_name',width:200">角色名称</th>
				<th data-options="field:'role_type',width:100,formatter:showRoleType">角色类型</th>
				<th data-options="field:'sys_flag',width:100,formatter:showSysFlag">系统定义</th>
				<th data-options="field:'role_desc',width:200">角色描述</th>
				<th data-options="field:'id',width:250,align:'center',formatter:showButtons">操作</th>
			</tr>
		</thead>
	</table>
</div>
<div id="tb" style="padding:2px 5px;">
		<a href="#" class="easyui-linkbutton" onclick="doAdd()" iconCls="icon-add">新增角色&nbsp;&nbsp;</a> &nbsp;&nbsp;&nbsp;&nbsp;
		<input id="inpKey" class="easyui-textbox"  prompt="角色名" style="width:150px">
		<a href="#" class="easyui-linkbutton" onclick="doSearch()" iconCls="icon-search">查询&nbsp;&nbsp;</a>
</div>
<script>
$(function() {
	var pageNum = "<%=pageNum%>";
	var queryParams = $('#dg').datagrid('options').queryParams;
	queryParams.sqlId = 'alarm-role-getDefinedRoles';
	if(pageNum != null && pageNum != 'null' && pageNum != ''){
		//var pageList = $('#dg').datagrid('options').pageList;
		//$('#dg').datagrid('options').pageSize = pageSize;
		$('#dg').datagrid('options').pageNumber = pageNum;
		
	}
	$('#dg').datagrid('reload');
	$('.pagination-page-list').hide();
});

function showSysFlag(val,row){
	if (val == 'Y'){
		return '<span>是</span>';
	} else if (val =='N'){
		return '<span>否</span>';
	}else{
		return '';
	}
}

function showRoleType(val,row){
	if (val == 'M'){
		return '<span>管理角色</span>';
	} else if (val =='B'){
		return '<span>业务角色</span>';
	}else{
		return '';
	}
}

function showButtons(val,row){
	var columnItem = '';
	if(row.sys_flag != 'Y'){
		columnItem =  columnItem + '<span><a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:doUpdate(\''+val+'\')" style="width:80px;">修 改</a></span>&nbsp;&nbsp;';
		columnItem =  columnItem + '<span><a href="javascript:void(0)" class="easyui-linkbutton" onclick="doDelete(\''+val+'\')" style="width:80px;">删 除</a></span>&nbsp;&nbsp;';
	}
	
	if(row.role_type == 'M' ){
		//超级管理员可设置管理员角色
		if('u8952c8666964e07a9a285b10d706a61' == '${loginUser.id}' ){
			columnItem =  columnItem + '<span><a href="javascript:void(0)" class="easyui-linkbutton" onclick="allotFunc(\''+val+'\')" style="width:80px;">角色功能</a></span>&nbsp;&nbsp;';
			columnItem =  columnItem + '<span><a href="javascript:void(0)" class="easyui-linkbutton" onclick="allotUser(\''+val+'\')" style="width:80px;">角色用户</a></span>&nbsp;&nbsp;';
		}
	}else{
		columnItem =  columnItem + '<span><a href="javascript:void(0)" class="easyui-linkbutton" onclick="allotFunc(\''+val+'\')" style="width:80px;">角色功能</a></span>&nbsp;&nbsp;';
		columnItem =  columnItem + '<span><a href="javascript:void(0)" class="easyui-linkbutton" onclick="allotUser(\''+val+'\')" style="width:80px;">角色用户</a></span>&nbsp;&nbsp;';
	}
	return columnItem;
}


function doAdd(){
	var content = '<iframe name="popContent" id="popContent" src="<%=path%>/auth/role/roleAdd.jsp" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>';
	var boarddiv = '<div id="msgwindow" title="新增角色" style="overflow:hidden;" ></div>'// style="overflow:hidden;"可以去掉滚动条
	$(document.body).append(boarddiv);
	var win = $('#msgwindow').dialog({
		content : content,
		width : '480',
		height : '320',
		modal : true,
		title : '新增角色',
		buttons: [{
			text:'&nbsp;&nbsp;&nbsp;&nbsp;确定&nbsp;&nbsp;&nbsp;&nbsp;',
			handler:function(){
				$("#popContent")[0].contentWindow.okResponse();
			}
		},{
			text:'&nbsp;&nbsp;&nbsp;&nbsp;取消&nbsp;&nbsp;&nbsp;&nbsp;',
			handler:function(){
				$('#msgwindow').dialog('destroy');
			}
		}],
		onClose : function() {
			$(this).dialog('destroy');// 后面可以关闭后的事件
		}
	});
	win.dialog('open');
	win.window('center');
}

function doUpdate(val){
	var queryParams = $('#dg').datagrid('options').queryParams;
	var options = $("#dg" ).datagrid("getPager" ).data("pagination" ).options;
    var pageNum = options.pageNumber;
    var pageSize = options.pageSize;
	var curUrl = "<%=path%>/auth/role/roleEdit.jsp?id="+val+"&pageNum="+pageNum + "&pageSize="+pageSize;
	
	var content = '<iframe name="popContent" id="popContent" src="' + curUrl + '" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>';
	var boarddiv = '<div id="msgwindow" title="修改角色" style="overflow:hidden;" ></div>'// style="overflow:hidden;"可以去掉滚动条
	$(document.body).append(boarddiv);
	var win = $('#msgwindow').dialog({
		content : content,
		width : '480',
		height : '320',
		modal : true,
		title : '修改角色',
		buttons: [{
			text:'&nbsp;&nbsp;&nbsp;&nbsp;确定&nbsp;&nbsp;&nbsp;&nbsp;',
			handler:function(){
				$("#popContent")[0].contentWindow.okResponse();
			}
		},{
			text:'&nbsp;&nbsp;&nbsp;&nbsp;取消&nbsp;&nbsp;&nbsp;&nbsp;',
			handler:function(){
				$('#msgwindow').dialog('destroy');
			}
		}],
		onClose : function() {
			$(this).dialog('destroy');// 后面可以关闭后的事件
		}
	});
	win.dialog('open');
	win.window('center');
}

function allotFunc(val){
	var curUrl = "<%=path%>/auth/role/roleAllotFunc.jsp?id="+val;
	
	var content = '<iframe name="popContent" id="popContent" src="' + curUrl + '" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>';
	var boarddiv = '<div id="msgwindow" title="角色分配功能" style="overflow:hidden;" ></div>'// style="overflow:hidden;"可以去掉滚动条
	$(document.body).append(boarddiv);
	var win = $('#msgwindow').dialog({
		content : content,
		width : '640',
		height : '480',
		modal : true,
		title : '角色分配功能',
		buttons: [{
			text:'&nbsp;&nbsp;&nbsp;&nbsp;确定&nbsp;&nbsp;&nbsp;&nbsp;',
			handler:function(){
				$("#popContent")[0].contentWindow.okResponse();
			}
		},{
			text:'&nbsp;&nbsp;&nbsp;&nbsp;取消&nbsp;&nbsp;&nbsp;&nbsp;',
			handler:function(){
				$('#msgwindow').dialog('destroy');
			}
		}],
		onClose : function() {
			$(this).dialog('destroy');// 后面可以关闭后的事件
		}
	});
	win.dialog('open');
	win.window('center');
}

function allotUser(val){
	var curUrl = "<%=path%>/auth/role/roleAllotUser.jsp?id="+val;
	
	var content = '<iframe name="popContent" id="popContent" src="' + curUrl + '" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>';
	var boarddiv = '<div id="msgwindow" title="角色分配用户" style="overflow:hidden;" ></div>'// style="overflow:hidden;"可以去掉滚动条
	$(document.body).append(boarddiv);
	var win = $('#msgwindow').dialog({
		content : content,
		width : '640',
		height : '480',
		modal : true,
		title : '角色分配用户',
		onClose : function() {
			$(this).dialog('destroy');// 后面可以关闭后的事件
		}
	});
	win.dialog('open');
	win.window('center');
}

function doDelete(val){
	$.messager.confirm('确认', '确认删除该角色?', function(r){
		if(r){
			$.ajax( {
			    url:'<%=path%>/role/mgr/delete.do',
			    data:{
			    	'id':val
			    },
			    type:'post',
			    async:false,
			    dataType:'json',
			    success:function(data) {
			    	$.messager.alert('提示', '删除成功!', 'info', function(){
			    		$('#dg').datagrid('reload');
		    		});
			    },
			    error : function(data) {
			    	$.messager.alert('异常',data.responseText);
		        }
			});
		}
	});
}

function doSearch(){
	var charKey = $("#inpKey" ).val();
	var queryParams = $('#dg').datagrid('options').queryParams;
	queryParams.sqlId = 'alarm-role-getDefinedRoles';
	queryParams.roleName = charKey;
	$('#dg').datagrid('loadData',{total:0,rows:[]});
	$('#dg').datagrid('reload');
}

function closeDialogAndRefresh(){
	$('#msgwindow').dialog('destroy');
	$('#dg').datagrid('reload');
}

function closeDialog(){
	$('#msgwindow').dialog('destroy');
}
</script>
</body>
</html>