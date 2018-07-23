<%@ page pageEncoding="UTF-8"%>
<% String pageNum = request.getParameter("pageNum"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>机箱维护</title>
<%@ include file="/common/res.jsp" %>
<style>

</style>
</head>
<body>
<!--  -->
<div id="dgPanel" class="easyui-panel" data-options="fit:true">
	<table id="dg" class="easyui-datagrid"  
			data-options="title:'机箱列表',singleSelect:true,rownumbers:true,pageSize:30 ,fit:true,url:'<%=path%>/comm/queryForPage.do',pagination:true,method:'post',toolbar:'#tb', multiSort:true">
		<thead>
			<tr>
				<th data-options="field:'box_number',width:120">机箱编号</th>
				<th data-options="field:'nfc_number',width:120">NFC序列号</th>
				<th data-options="field:'longitude',width:100">经度</th>
				<th data-options="field:'latitude',width:100">纬度</th>
				<th data-options="field:'pos_desc',width:250">位置描述</th>
				<th data-options="field:'id',width:200,align:'center',formatter:showButtons">操作</th>
			</tr>
		</thead>
	</table>
</div>
<div id="tb" style="padding:2px 5px;">
		<a href="#" class="easyui-linkbutton" onclick="doAdd()" iconCls="icon-add">添加机箱&nbsp;&nbsp;</a> &nbsp;&nbsp;&nbsp;&nbsp;
		<input id="inpKey" class="easyui-textbox"  prompt="机箱编号" style="width:150px">
		<a href="#" class="easyui-linkbutton" onclick="doSearch()" iconCls="icon-search">查询&nbsp;&nbsp;</a>
</div>
<script>
$(function() {
	var pageNum = "<%=pageNum%>";
	var queryParams = $('#dg').datagrid('options').queryParams;
	queryParams.sqlId = 'alarm-box-getBoxs';
	if(pageNum != null && pageNum != 'null' && pageNum != ''){
		//var pageList = $('#dg').datagrid('options').pageList;
		//$('#dg').datagrid('options').pageSize = pageSize;
		$('#dg').datagrid('options').pageNumber = pageNum;
		
	}
	$('#dg').datagrid('reload');
	$('.pagination-page-list').hide();
});

function showButtons(val,row){
	var columnItem = '';
	columnItem =  columnItem + '<span><a href="javascript:void(0)" class="easyui-linkbutton" onclick="doUpdate(\''+val+'\')" style="width:80px;">修 改</a></span>&nbsp;&nbsp;';
	columnItem =  columnItem + '<span><a href="javascript:void(0)" class="easyui-linkbutton" onclick="doDelete(\''+val+'\')" style="width:80px;">删 除</a></span>&nbsp;&nbsp;';
	
	return columnItem;
}


function doAdd(){
	var content = '<iframe name="popContent" id="popContent" src="<%=path%>/device/box/boxAdd.jsp" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>';
	var boarddiv = '<div id="msgwindow" title="添加机箱" style="overflow:hidden;" ></div>'// style="overflow:hidden;"可以去掉滚动条
	$(document.body).append(boarddiv);
	var win = $('#msgwindow').dialog({
		content : content,
		width : '560',
		height : '400',
		modal : true,
		title : '添加机箱',
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
	var curUrl = "<%=path%>/device/box/boxEdit.jsp?id="+val+"&pageNum="+pageNum + "&pageSize="+pageSize;
	
	var content = '<iframe name="popContent" id="popContent" src="' + curUrl + '" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>';
	var boarddiv = '<div id="msgwindow" title="修改机箱" style="overflow:hidden;" ></div>'// style="overflow:hidden;"可以去掉滚动条
	$(document.body).append(boarddiv);
	var win = $('#msgwindow').dialog({
		content : content,
		width : '560',
		height : '400',
		modal : true,
		title : '修改机箱',
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
	$.messager.confirm('确认', '确认删除该机箱?', function(r){
		if(r){
			$.ajax( {
			    url:'<%=path%>/box/mgr/delete.do',
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
	queryParams.sqlId = 'alarm-box-getBoxs';
	queryParams.boxNumber = charKey;
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