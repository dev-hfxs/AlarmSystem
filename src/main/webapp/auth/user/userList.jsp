<%@ page pageEncoding="UTF-8"%>
<% String pageNum = request.getParameter("pageNum"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>用户管理</title>
<%@ include file="/common/res.jsp" %>
<style>

</style>
</head>
<body>
<!--  -->
<div id="dgPanel" class="easyui-panel" data-options="fit:true">
	<table id="dg" class="easyui-datagrid"  
			data-options="title:'用户列表',singleSelect:true,rownumbers:true,pageSize:30 ,fit:true,url:'<%=path%>/comm/queryForPage.do',pagination:true,method:'post',toolbar:'#tb', multiSort:true">
		<thead>
			<tr>
				<th data-options="field:'user_name',width:200,sortable:true">用户名</th>
				<th data-options="field:'full_name',width:100,sortable:true">姓名</th>
				<th data-options="field:'org_name',width:200,sortable:true">所在单位</th>
				<th data-options="field:'contact_number',width:100">联系电话</th>
				<th data-options="field:'status',width:100,formatter:showStatusName,sortable:true">用户状态</th>
				<th data-options="field:'id',width:150,align:'center',formatter:showButtons">操作</th>
			</tr>
		</thead>
	</table>
</div>
<div id="tb" style="padding:2px 5px;">
		<a href="#" class="easyui-linkbutton" onclick="doAdd()" iconCls="icon-add">新增用户&nbsp;&nbsp;</a> &nbsp;&nbsp;&nbsp;&nbsp;
		<input id="inpKey" class="easyui-textbox"  prompt="用户名" style="width:150px">
		<a href="#" class="easyui-linkbutton" onclick="doSearch()" iconCls="icon-search">查询&nbsp;&nbsp;</a>
</div>
<script>
$(function() {
	var pageNum = "<%=pageNum%>";
	var queryParams = $('#dg').datagrid('options').queryParams;
	queryParams.sqlId = 'alarm-user-getDefinedUsers';
	if(pageNum != null && pageNum != 'null' && pageNum != ''){
		//var pageList = $('#dg').datagrid('options').pageList;
		//$('#dg').datagrid('options').pageSize = pageSize;
		$('#dg').datagrid('options').pageNumber = pageNum;
		
	}
	$('#dg').datagrid('reload');
	$('.pagination-page-list').hide();
});

function showStatusName(val,row){
	if (val == 'N'){
		return '<span>新建</span>';
	} else if (val =='R'){
		return '<span>恢复</span>';
	}else if(val =='D'){
		return '<span>删除</span>';
	}else{
		return val;
	}
}

function showButtons(val,row){
	var columnItem = '';
	if(row.status == 'D'){
		columnItem = '<span><a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:doRecover(\''+val+'\')" style="width:80px;">恢 复</a></span>&nbsp;&nbsp;';
	}else{
		columnItem =  columnItem + '<span><a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:doUpdate(\''+val+'\')" style="width:80px;">修 改</a></span>&nbsp;&nbsp;';
		columnItem =  columnItem + '<span><a href="javascript:void(0)" class="easyui-linkbutton" onclick="doDelete(\''+val+'\')" style="width:80px;">删 除</a></span>&nbsp;&nbsp;';
		columnItem =  columnItem + '<span><a href="javascript:void(0)" class="easyui-linkbutton" onclick="doSetPwd(\''+val+'\')" style="width:80px;">重置密码</a></span>';
	}	
	return columnItem;
}

function doSetPwd(val){
	$.messager.confirm('确认', '确认重置用户密码?', function(r){
		if(r){
			$.ajax( {
			    url:'<%=path%>/user/mgr/pwd/reset.do',
			    data:{
			    	'id':val
			    },
			    type:'post',
			    async:false,
			    dataType:'json',
			    success:function(data) {
			    	if(data.returnCode == "success"){
			    		$.messager.alert('提示','用户密码已重置!');
			    	}else{
			    		$.messager.alert('提示',data.msg);
			    	}
			    },
			    error : function(data) {
			    	$.messager.alert('异常',data.responseText);
		        }
			});
		}
	});
}

function doAdd(){
	var content = '<iframe name="popContent" id="popContent" src="<%=path%>/auth/user/userAdd.jsp" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>';
	var boarddiv = '<div id="msgwindow" title="添加用户" style="overflow:hidden;" ></div>'// style="overflow:hidden;"可以去掉滚动条
	$(document.body).append(boarddiv);
	var win = $('#msgwindow').dialog({
		content : content,
		width : '640',
		height : '480',
		modal : true,
		title : '添加用户',
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
	var curUrl = "<%=path%>/auth/user/userEdit.jsp?id="+val+"&pageNum="+pageNum + "&pageSize="+pageSize;
	
	var content = '<iframe name="popContent" id="popContent" src="' + curUrl + '" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>';
	var boarddiv = '<div id="msgwindow" title="修改用户" style="overflow:hidden;" ></div>'// style="overflow:hidden;"可以去掉滚动条
	$(document.body).append(boarddiv);
	var win = $('#msgwindow').dialog({
		content : content,
		width : '640',
		height : '480',
		modal : true,
		title : '修改用户',
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

function doDelete(val){
	$.messager.confirm('确认', '确认删除该用户?', function(r){
		if(r){
			$.ajax( {
			    url:'<%=path%>/user/mgr/delete.do',
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

function doRecover(val){
	$.messager.confirm('确认', '确认恢复该用户?', function(r){
		if(r){
			$.ajax( {
			    url:'<%=path%>/user/mgr/recover.do',
			    data:{
			    	'id':val
			    },
			    type:'post',
			    async:false,
			    dataType:'json',
			    success:function(data) {
			    	$.messager.alert('提示', '恢复成功!', 'info', function(){
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
	queryParams.sqlId = 'alarm-user-getDefinedUsers';
	queryParams.userName = charKey;
	$('#dg').datagrid('loadData',{total:0,rows:[]});
	$('#dg').datagrid('reload');
}

function closeDialogAndRefresh(){
	$('#msgwindow').dialog('destroy');
	$('#dg').datagrid('reload');
}
</script>
</body>
</html>