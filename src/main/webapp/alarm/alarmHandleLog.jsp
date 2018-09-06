<%@ page pageEncoding="UTF-8"%>
<% String pageNum = request.getParameter("pageNum"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>处警记录</title>
<%@ include file="/common/res.jsp" %>
<script type="text/javascript" src="<%=path%>/js/DateUtil.js"></script>
<style>
.textbox-label{
width:60px;
}
</style>
</head>
<body>
<div id="navTab" class="easyui-tabs" data-options="fit:true">
		<div title="处警记录" style="padding:0px">
			<table id="dg" class="easyui-datagrid"  
			data-options="singleSelect:true,rownumbers:true,pageSize:30 ,fit:true,url:'<%=path%>/comm/queryForPage.do',pagination:true,method:'post',toolbar:'#tb', multiSort:true">
			<thead>
				<tr>
					<th data-options="field:'alarm_desc',width:400">报警信息</th>
					<th data-options="field:'pos_desc',width:300">位置描述</th>
					<th data-options="field:'alarm_date',width:130">报警时间</th>
					<th data-options="field:'confirm_date',width:130">确认时间</th>
					<th data-options="field:'confirm_person',width:100">确认人</th>
					<th data-options="field:'is_valid',width:110,formatter:fmIsValid">是否有效</th>
					<th data-options="field:'process_date',width:130">处理时间</th>
					<th data-options="field:'process_person',width:100">处理人</th>
					<th data-options="field:'process_method',width:100">处理方法</th>
					<th data-options="field:'process_result',width:180">处理结果</th>
				</tr>
			</thead>
			</table>
			<div id="tb" style="padding:2px 5px;">
			    <input type="text" id="searchField" name="searchField">&nbsp;&nbsp;&nbsp;&nbsp;</input><input id="beginDate" class="easyui-datebox" label="开始日期:" labelPosition="left" style="width:200px;">&nbsp;<input  id="endDate" class="easyui-datebox" label="结束日期:" labelPosition="left" style="width:200px;">
				<a href="#" class="easyui-linkbutton" onclick="doSearch()" iconCls="icon-search">查询&nbsp;&nbsp;</a>
			</div>
		</div>
</div>
<script>
var searchFields = [{ 'name':'alarm_date', 'text':'报警时间'},{ 'name':'confirm_date', 'text':'确认时间'},{ 'name':'confirm_date', 'text':'处理时间'}];

$(function() {
	var pageNum = "<%=pageNum%>";
	var queryParams = $('#dg').datagrid('options').queryParams;
	queryParams.sqlId = 'alarm-alarminfo-getHandleLog';
	if(pageNum != null && pageNum != 'null' && pageNum != ''){
		//var pageList = $('#dg').datagrid('options').pageList;
		//$('#dg').datagrid('options').pageSize = pageSize;
		$('#dg').datagrid('options').pageNumber = pageNum;
	}
	$('#dg').datagrid('reload');
	$('.pagination-page-list').hide();
	
	$('#searchField').combobox({
	    data: searchFields,
	    valueField:'name',
	    textField:'text',
	    label:'查询字段',
	    panelHeight:'auto',
	    width:'160px',
	    onChange:function(){
	    	var text = $(this).combobox('getValue');
	    	var datas = [];
	    	if("box" == text){
	    		//datas = boxFileds;
	    	}else if("processor" == text){
	    		//datas = processorFileds;
	    	}else if("detector" == text){
	    		//datas = detectorFileds;
	    	}
	    }
	});
	$('.textbox-label-before').css('text-align','right');
});

function fmIsValid(val,row){
	if (val == 'Y'){
		return '<span>有效</span>';
	} else if (val =='N'){
		return '<span>无效</span>';
	}else{
		return '';
	}
}
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
	var text = $("#searchField").combobox('getValue');
    if(text == null || text == ''){
    	$.messager.alert('提示', '请选择查询字段.');
    	return;
    }
    var queryParams = $('#dg').datagrid('options').queryParams;
	queryParams.sqlId = 'alarm-alarminfo-getHandleLog';
	
    if(text == 'alarm_date'){
		var beginDate = $("#beginDate").val();
	    var endDate = $("#endDate").val();
	    if(beginDate == null || beginDate == ''){
	    	beginDate = '1990-01-01';
	    }
	    if(endDate == null || endDate == ''){
	    	endDate = new Date().format("yyyy-MM-dd");
	    }
	    queryParams.alarmBeginDate = beginDate;
		queryParams.alarmEndDate = endDate;
	}
	
	if(text == 'confirm_date'){
		var beginDate = $("#beginDate").val();
	    var endDate = $("#endDate").val();
	    if(beginDate == null || beginDate == ''){
	    	beginDate = '1990-01-01';
	    }
	    if(endDate == null || endDate == ''){
	    	endDate = new Date().format("yyyy-MM-dd");
	    }
	    queryParams.confirmBeginDate = beginDate;
		queryParams.confirmEndDate = endDate;
	}
	
	if(text == 'process_date'){
		var beginDate = $("#beginDate").val();
	    var endDate = $("#endDate").val();
	    if(beginDate == null || beginDate == ''){
	    	beginDate = '1990-01-01';
	    }
	    if(endDate == null || endDate == ''){
	    	endDate = new Date().format("yyyy-MM-dd");
	    }
	    queryParams.processBeginDate = beginDate;
		queryParams.processEndDate = endDate;
	}
	
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