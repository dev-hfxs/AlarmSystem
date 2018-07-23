<%@ page pageEncoding="UTF-8"%>
<% String pageNum = request.getParameter("pageNum"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>处理器维护</title>
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
	<div data-options="region:'west',title:'机箱信息'"
		style="width: 250px;height:300px; padding: 0px;">
		<ul id="deviceTree" class="ztree"></ul>
	</div>
	<div  data-options="region:'center',title:'处理器列表'" style="width: 100%;height:100%;overflow:hidden;">
		<table id="dg" class="easyui-datagrid"  
			data-options="singleSelect:true,rownumbers:true,pageSize:30 ,fit:true,url:'<%=path%>/comm/queryForPage.do',pagination:true,method:'post',toolbar:'#tb', multiSort:true">
			<thead>
			<tr>
				<th data-options="field:'nfc_number',width:120">NFC序列号</th>
				<th data-options="field:'moxa_number',width:120">MOXA序列号</th>
				<th data-options="field:'ip',width:100">IP</th>
				<th data-options="field:'online',width:100">在线状态</th>
				<th data-options="field:'id',width:200,align:'center',formatter:showButtons">操作</th>
			</tr>
			</thead>
		</table>
		<div id="tb" style="padding:2px 5px;">
		<a href="#" class="easyui-linkbutton" onclick="doAdd()" iconCls="icon-add">添加处理器&nbsp;&nbsp;</a> &nbsp;&nbsp;&nbsp;&nbsp;
		<input id="inpKey" class="easyui-textbox"  prompt="NFC序列号" style="width:150px">
		<a href="#" class="easyui-linkbutton" onclick="doSearch()" iconCls="icon-search">查询&nbsp;&nbsp;</a>
		</div>
	</div>
</div>
</div>
<script>
var curBoxId = '';
$(function() {
	
	var setting = {
			check: {
				enable: false,
				chkboxType:{'Y':'ps','N':'s'}
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				onClick: onClick
			}
	};
			
		$.ajax( {
		    url:'<%=path%>/comm/queryForList.do',
		    data:{
		    	'sqlId':'alarm-processor-getBoxsTree'
		    },
		    type:'post',
		    async:false,
		    dataType:'json',
		    success:function(data) {
		    	var treeNodes = data;
				if(treeNodes != null && treeNodes.length > 0){
					for(var i = 0; i < treeNodes.length; i++){
					    var item = treeNodes[i];
					    treeNodes[i]['icon'] = '<%=path%>/style/default/image/box.png';
					    treeNodes[i]['name'] = treeNodes[i]['box_number'];
					}
				}
				
				$.fn.zTree.init($("#deviceTree"), setting, treeNodes);
		    },
		    error : function(data) {
		    	//$.messager.alert('异常',data.responseText);
	        }
		});
	
	//var pageNum = "<%=pageNum%>";
	//var queryParams = $('#dg').datagrid('options').queryParams;
	//queryParams.sqlId = 'alarm-processor-getProcessorsByBoxId';
	//if(pageNum != null && pageNum != 'null' && pageNum != ''){
	//	$('#dg').datagrid('options').pageNumber = pageNum;		
	//}
	//$('#dg').datagrid('reload');
	$('.pagination-page-list').hide();
});

function onClick(event, treeId, treeNode, clickFlag){
	var queryParams = $('#dg').datagrid('options').queryParams;
	queryParams.sqlId = 'alarm-processor-getProcessorsByBoxId';
	queryParams.machineBoxId = treeNode.id;
	curBoxId = treeNode.id;
	
	//if(pageNum != null && pageNum != 'null' && pageNum != ''){
	//	$('#dg').datagrid('options').pageNumber = pageNum;		
	//}
	$('#dg').datagrid('reload');
}

function showButtons(val,row){
	var columnItem = '';
	columnItem =  columnItem + '<span><a href="javascript:void(0)" class="easyui-linkbutton" onclick="doUpdate(\''+val+'\')" style="width:80px;">修 改</a></span>&nbsp;&nbsp;';
	columnItem =  columnItem + '<span><a href="javascript:void(0)" class="easyui-linkbutton" onclick="doDelete(\''+val+'\')" style="width:80px;">删 除</a></span>&nbsp;&nbsp;';
	
	return columnItem;
}


function doAdd(){
	if(curBoxId == null || curBoxId == ''){
		$.messager.alert('提示', '请先选择机箱！');
		return;
	}
	var content = '<iframe name="popContent" id="popContent" src="<%=path%>/device/processor/processorAdd.jsp?boxId=' + curBoxId +'" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>';
	var boarddiv = '<div id="msgwindow" title="添加处理器" style="overflow:hidden;" ></div>'// style="overflow:hidden;"可以去掉滚动条
	$(document.body).append(boarddiv);
	var win = $('#msgwindow').dialog({
		content : content,
		width : '480',
		height : '240',
		modal : true,
		title : '添加处理器',
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
	var curUrl = "<%=path%>/device/processor/processorEdit.jsp?id="+val+"&pageNum="+pageNum + "&pageSize="+pageSize;
	
	var content = '<iframe name="popContent" id="popContent" src="' + curUrl + '" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>';
	var boarddiv = '<div id="msgwindow" title="修改处理器" style="overflow:hidden;" ></div>'// style="overflow:hidden;"可以去掉滚动条
	$(document.body).append(boarddiv);
	var win = $('#msgwindow').dialog({
		content : content,
		width : '480',
		height : '240',
		modal : true,
		title : '修改处理器',
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
	$.messager.confirm('确认', '确认删除该处理器?', function(r){
		if(r){
			$.ajax( {
			    url:'<%=path%>/processor/mgr/delete.do',
			    data:{
			    	'id':val
			    },
			    type:'post',
			    async:false,
			    dataType:'json',
			    success:function(data) {
			    	if(data.returnCode == 'success'){
			    		$.messager.alert('提示', '删除成功!', 'info', function(){
				    		$('#dg').datagrid('reload');
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
	});
}

function doSearch(){
	var charKey = $("#inpKey" ).val();
	var queryParams = $('#dg').datagrid('options').queryParams;
	queryParams.sqlId = 'alarm-processor-getProcessors';
	queryParams.nfcNumber = charKey;
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