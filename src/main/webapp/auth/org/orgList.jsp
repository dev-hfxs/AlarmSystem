<%@ page pageEncoding="UTF-8"%>
<% String pageNum = request.getParameter("pageNum"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>组织管理</title>
<%@ include file="/common/res.jsp" %>
<style>

</style>
</head>
<body>
<div id="navTab" class="easyui-tabs" data-options="fit:true">
		<div title="组织列表" style="padding:0px">
			<table id="dg" class="easyui-datagrid"  
			data-options="singleSelect:true,rownumbers:true,pageSize:30 ,fit:true,url:'<%=path%>/comm/queryForPage.do',pagination:true,method:'post',toolbar:'#tb', multiSort:true">
			<thead>
				<tr>
					<th data-options="field:'org_name',width:130,sortable:true">组织名称</th>
					<th data-options="field:'org_code',width:100,sortable:true">组织编码</th>
					<th data-options="field:'parent_name',width:130,sortable:true">上级组织</th>
					<th data-options="field:'contacts',width:130,sortable:true">联系人</th>
					<th data-options="field:'telephone',width:100">联系电话</th>
					<th data-options="field:'email',width:100">邮箱</th>
					<th data-options="field:'address',width:200">地址</th>
					<th data-options="field:'status',width:60,formatter:showStatusName,sortable:true">状态</th>
					<th data-options="field:'create_date',width:130">创建日期</th>
					<th data-options="field:'id',width:150,align:'center',formatter:showButtons">操作</th>
				</tr>
			</thead>
			</table>
			<div id="tb" style="padding:2px 5px;">
				<a href="#" class="easyui-linkbutton" onclick="doAdd()" iconCls="icon-add">新增组织&nbsp;&nbsp;</a> &nbsp;&nbsp;&nbsp;&nbsp;
				<input id="inpKey" class="easyui-textbox"  prompt="组织名" style="width:150px">
				<a href="#" class="easyui-linkbutton" onclick="doSearch()" iconCls="icon-search">查询&nbsp;&nbsp;</a>
			</div>
		</div>
		<div title="组织树" style="padding:10px">
			<ul class="easyui-tree" id="orgTree" ></ul>
		</div>
</div>
<script>
$(function() {
	var pageNum = "<%=pageNum%>";
	var queryParams = $('#dg').datagrid('options').queryParams;
	queryParams.sqlId = 'alarm-org-getOrgListDataById';
	queryParams.orgId = 'ROOT';
	if(pageNum != null && pageNum != 'null' && pageNum != ''){
		$('#dg').datagrid('options').pageNumber = pageNum;
		
	}
	$('#dg').datagrid('reload');
	$('.pagination-page-list').hide();
	
	$('#navTab').tabs({
		onSelect: function(title,index){
			if(index == 1){
				//加载组织树
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
				    		$('#orgTree').tree({ 	
				                data: repObj.data,
				                valueField:'id',
				                textField:'orgName',
				                required: false,
				                editable: false,
				                label:'所属组织'
				            });
				    	}else{
				    		//$.messager.alert('提示', repObj.msg);
				    	}
				    },
				    error : function(repObj) {
				    	//$.messager.alert('异常', repObj.responseText);
			        }
				});
			}
		}
	});
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
		columnItem = columnItem + '<span><a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:doRecover(\''+val+'\')" style="width:80px;">恢 复</a></span>&nbsp;&nbsp;';
	}else{
		columnItem = '<span><a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:doUpdate(\''+val+'\')" style="width:80px;">修 改</a></span>&nbsp;&nbsp;';
		columnItem = columnItem + '<span><a href="javascript:void(0)" class="easyui-linkbutton" onclick="doDelete(\''+val+'\')" style="width:80px;">删 除</a></span>&nbsp;&nbsp;';
	}
	return columnItem;
}

function doRecover(val){
	$.messager.confirm('确认', '确认恢复该组织?', function(r){
		if(r){
			$.ajax( {
			    url:'<%=path%>/org/mgr/recover.do',
			    data:{
			    	'id':val
			    },
			    type:'post',
			    async:false,
			    dataType:'json',
			    success:function(data) {
			    	if(data.returnCode == "success"){
			    		$('#dg').datagrid('reload');
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
	var content = '<iframe name="popContent" id="popContent" src="<%=path%>/auth/org/orgAdd.jsp" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>';
	var boarddiv = '<div id="msgwindow" title="添加组织" style="overflow:hidden;" ></div>'// style="overflow:hidden;"可以去掉滚动条
	$(document.body).append(boarddiv);
	var win = $('#msgwindow').dialog({
		content : content,
		width : '640',
		height : '480',
		modal : true,
		title : '添加组织',
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
	var curUrl = "<%=path%>/auth/org/orgEdit.jsp?id="+val+"&pageNum="+pageNum + "&pageSize="+pageSize;
	
	var content = '<iframe name="popContent" id="popContent" src="' + curUrl + '" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>';
	var boarddiv = '<div id="msgwindow" title="修改组织" style="overflow:hidden;" ></div>'// style="overflow:hidden;"可以去掉滚动条
	$(document.body).append(boarddiv);
	var win = $('#msgwindow').dialog({
		content : content,
		width : '640',
		height : '480',
		modal : true,
		title : '修改组织',
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
	$.messager.confirm('确认', '确认删除该组织?', function(r){
		if(r){
			$.ajax( {
			    url:'<%=path%>/org/mgr/delete.do',
			    data:{
			    	'id':val
			    },
			    type:'post',
			    async:false,
			    dataType:'json',
			    success:function(data) {
			    	if(data.returnCode == "success"){
			    		$.messager.alert('提示', '删除成功!', 'info', function(){
				    		$('#dg').datagrid('reload');
			    		});
			    	}else{
			    		$.messager.alert('提示', data.msg);
			    	}
			    	
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
	queryParams.sqlId = 'alarm-org-getOrgListDataById';
	queryParams.orgId = 'ROOT';
	queryParams.orgName = charKey;
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