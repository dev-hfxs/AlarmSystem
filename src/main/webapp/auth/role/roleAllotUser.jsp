<%@ page pageEncoding="UTF-8"%>
<% String roleId = request.getParameter("id"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>角色用户</title>
<%@ include file="/common/res.jsp" %>
<style>
</style>
</head>
<body>
<!--  -->
<div id="navTab" class="easyui-tabs" data-options="fit:true">
	<div title="角色已分配用户" style="padding:0px">
		<table id="dg" class="easyui-datagrid"  
				data-options="singleSelect:true,rownumbers:true,pageSize:20,fit:true,url:'<%=path%>/comm/queryForPage.do',pagination:true,method:'post',toolbar:'#tb', multiSort:true">
			<thead>
				<tr>
					<th data-options="field:'user_name',width:150">用户名称</th>
					<th data-options="field:'full_name',width:120">姓名</th>
					<th data-options="field:'sex',width:60,formatter:fmSex">性别</th>
					<th data-options="field:'org_name',width:150">所属组织</th>
					<th data-options="field:'id',width:80,align:'center',formatter:showButton">操作</th>
				</tr>
			</thead>
		</table>
		<div id="tb" style="padding:2px 5px;">
			&nbsp;<input id="inpKey" class="easyui-textbox"  prompt="用户名" style="width:150px">
			<a href="#" class="easyui-linkbutton" onclick="doSearch()" iconCls="icon-search">查询&nbsp;&nbsp;</a>
		</div>
	</div>
	<div title="角色未分配用户" style="padding:0px">
		<table id="dg2" class="easyui-datagrid"  
				data-options="singleSelect:true,rownumbers:true,pageSize:20,fit:true,url:'<%=path%>/comm/queryForPage.do',pagination:true,method:'post',toolbar:'#tb2', multiSort:true">
			<thead>
				<tr>
					<th data-options="field:'user_name',width:150">用户名称</th>
					<th data-options="field:'full_name',width:120">姓名</th>
					<th data-options="field:'sex',width:60,formatter:fmSex">性别</th>
					<th data-options="field:'org_name',width:150">所属组织</th>
					<th data-options="field:'id',width:80,align:'center',formatter:showButton2">操作</th>
				</tr>
			</thead>
		</table>
		<div id="tb2" style="padding:2px 5px;">
			&nbsp;<input id="inpKey2" class="easyui-textbox"  prompt="用户名" style="width:150px">
			<a href="#" class="easyui-linkbutton" onclick="doSearch2()" iconCls="icon-search">查询&nbsp;&nbsp;</a>
		</div>
	</div>
</div>
<script>
$(function() {
	var queryParams = $('#dg').datagrid('options').queryParams;
	$('#dg').datagrid('options').pageNumber = 1;
	queryParams.sqlId = 'alarm-role-getRoleAllotUsers';
	queryParams.roleId = '<%=roleId%>';
	$('#dg').datagrid('reload');
	$('.pagination-page-list').hide();
	
	$('#navTab').tabs({
		onSelect: function(title,index){
			if(index == 0){
				var dgParams = $('#dg').datagrid('options').queryParams;
				$('#dg').datagrid('options').pageNumber = 1;
				dgParams.sqlId = 'alarm-role-getRoleAllotUsers';
				dgParams.roleId = '<%=roleId%>';
				$('#dg').datagrid('reload');
			}
            if(index == 1){
            	var dg2Params = $('#dg2').datagrid('options').queryParams;
            	$('#dg2').datagrid('options').pageNumber = 1;
				dg2Params.sqlId = 'alarm-role-getRoleNotAllotUsers';
				dg2Params.roleId = '<%=roleId%>';
				$('#dg2').datagrid('reload');
			}
		}
	});
});

function fmSex(val,row){
	if (val == 'F'){
		return '<span>女</span>';
	} else if (val =='M'){
		return '<span>男</span>';
	}else{
		return '';
	}
}

function showButton(val,row){
	var columnItem = '';
	columnItem =  columnItem + '<span><a href="javascript:void(0)" class="easyui-linkbutton" onclick="doDelete(\''+val+'\')" style="width:80px;">取消分配</a></span>&nbsp;&nbsp;';
	
	return columnItem;
}

function showButton2(val,row){
	var columnItem = '';
	columnItem =  columnItem + '<span><a href="javascript:void(0)" class="easyui-linkbutton" onclick="doAllot(\''+val+'\')" style="width:80px;">分 配</a></span>&nbsp;&nbsp;';
	
	return columnItem;
}

function doDelete(val){
	$.messager.confirm('确认', '确认取消分配该用户?', function(r){
		if(r){
			$.ajax( {
			    url:'<%=path%>/role/mgr/unAllotUser.do',
			    data:{
			    	'roleId':'<%=roleId%>',
			    	'userId':val,
			    },
			    type:'post',
			    async:false,
			    dataType:'json',
			    success:function(data) {
			    	$.messager.alert('提示', '操作成功!', 'info', function(){
			    		$('#dg').datagrid('loadData',{total:0,rows:[]});
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

function doAllot(val){
	$.ajax( {
		    url:'<%=path%>/role/mgr/allotUser.do',
		    data:{
		    	'roleId':'<%=roleId%>',
		    	'userId':val,
		    },
		    type:'post',
		    async:false,
		    dataType:'json',
		    success:function(data) {
		    	$.messager.alert('提示', '操作成功!', 'info', function(){
		    		$('#dg2').datagrid('loadData',{total:0,rows:[]});
		    		$('#dg2').datagrid('reload');
	    		});
		    },
		    error : function(data) {
		    	$.messager.alert('异常',data.responseText);
	        }
	});
}

function doSearch(){
	var charKey = $("#inpKey" ).val();
	var qParams = $('#dg').datagrid('options').queryParams;
	qParams.sqlId = 'alarm-role-getRoleAllotUsers';
	qParams.roleId = '<%=roleId%>';
	qParams.userName = charKey;
	$('#dg').datagrid('loadData',{total:0,rows:[]});
	$('#dg').datagrid('reload');
}

function doSearch2(){
	var charKey2 = $("#inpKey2" ).val();
	var qParams2 = $('#dg2').datagrid('options').queryParams;
	qParams2.sqlId = 'alarm-role-getRoleNotAllotUsers';
	qParams2.userName = charKey2;
	qParams2.roleId = '<%=roleId%>';
	$('#dg2').datagrid('loadData',{total:0,rows:[]});
	$('#dg2').datagrid('reload');
}

</script>
</body>
</html>