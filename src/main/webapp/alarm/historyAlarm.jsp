<%@ page pageEncoding="UTF-8"%>
<% String pageNum = request.getParameter("pageNum"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>历史报警</title>
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
		<div title="历史报警信息" style="padding:0px">
			<table id="dg" class="easyui-datagrid"  
			data-options="singleSelect:true,rownumbers:true,pageSize:30 ,fit:true,url:'<%=path%>/comm/queryForPage.do',pagination:true,method:'post',toolbar:'#tb', multiSort:true">
			<thead>
				<tr>
					<th data-options="field:'alarm_desc',width:400">报警信息</th>
					<th data-options="field:'pos_desc',width:300">位置描述</th>
					<th data-options="field:'nfc_number',width:110">设备NFC号</th>
					<th data-options="field:'alarm_date',width:130">报警日期</th>
					<th data-options="field:'confirm_date',width:130">确认日期</th>
					<th data-options="field:'full_name',width:130">确认人</th>
					<th data-options="field:'is_valid',width:70,formatter:fmIsValid">是否有效</th>
					<th data-options="field:'status',width:120,formatter:fmStatus">状态</th>
					<th data-options="field:'remark',width:200">备注</th>
					<!-- 
					<th data-options="field:'id',width:150,align:'center',formatter:showButtons">操作</th>
					-->
				</tr>
			</thead>
			</table>
			<div id="tb" style="padding:2px 5px;">
			    <div style="float:left;margin-left:10px;"><input type="text" id="searchField" name="searchField"></input></div><div id="divSearchText" style="float:left;margin-left:10px;margin-right:10px;"><input type="text" id="searchValue" name="searchValue"></input></div><div id="divSearchDate" style="float:left;margin-left:10px;margin-right:10px;"><input id="beginDate" class="easyui-datebox" label="开始日期:" labelPosition="left" style="width:200px;">&nbsp;<input  id="endDate" class="easyui-datebox" label="结束日期:" labelPosition="left" style="width:200px;"></div>
				&nbsp;<a href="#" class="easyui-linkbutton" onclick="doSearch()" iconCls="icon-search">查询&nbsp;&nbsp;</a>
			</div>
		</div>
</div>
<script>
var searchFields = [{'name':'status','text':'处理状态','selected':true},{ 'name':'alarm_type', 'text':'报警类型'},{ 'name':'alarm_date', 'text':'报警日期'},{ 'name':'confirm_date', 'text':'确认日期'}];
var statusOption = [{'name':'N','text':'未处理','selected':true},{ 'name':'I', 'text':'待处理'},{ 'name':'F', 'text':'已处理'}];
var alarmTypeOption = [{'name':'1','text':'不在线','selected':true},{ 'name':'2', 'text':'感知'}];
var curUserId = '${loginUser.id}';

$(function() {
	var pageNum = "<%=pageNum%>";
	var queryParams = $('#dg').datagrid('options').queryParams;
	queryParams.sqlId = 'alarm-alarminfo-getHistoryAlarm';
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
	    	if("status" == text){
	    		$('#searchValue').combobox({
	    		    data: statusOption,
	    		    valueField:'name',
	    		    textField:'text',
	    		    label:'&nbsp;&nbsp;&nbsp;&nbsp;查询值',
	    		    panelHeight:'auto',
	    		    width:'160px'
	    		 });
	    		$('#divSearchText').show();
	    		$('#divSearchDate').hide();
	    	}else if("alarm_type" == text){
	    		$('#searchValue').combobox({
	    		    data: alarmTypeOption,
	    		    valueField:'name',
	    		    textField:'text',
	    		    label:'&nbsp;&nbsp;&nbsp;&nbsp;查询值',
	    		    panelHeight:'auto',
	    		    width:'160px'
	    		 });
	    		$('#divSearchText').show();
	    		$('#divSearchDate').hide();
	    	}else if("alarm_date" == text){
	    		$('#divSearchText').hide();
	    		$('#divSearchDate').show();
	    	}else if("confirm_date" == text){
	    		$('#divSearchText').hide();
	    		$('#divSearchDate').show();
	    	}
	    	$('.textbox-label-before').css('text-align','right');
	    }
	});
	
	$('#searchValue').combobox({
	    data: statusOption,
	    valueField:'name',
	    textField:'text',
	    label:'&nbsp;&nbsp;&nbsp;&nbsp;查询值',
	    panelHeight:'auto',
	    width:'160px'
	 });
	$('#divSearchText').show();
	$('#divSearchDate').hide();
	
	$('.textbox-label-before').css('text-align','right');
});

function fmIsValid(val,row){
	if (val == 'Y'){
		return '<span>有效</span>';
	} else if (val =='N'){
		columnItem = '<span>无效</span>';
		if(row.confirm_person == curUserId){
			columnItem = columnItem + '&nbsp;&nbsp;<span><a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:doRecover(\''+row.id +'\')" style="width:80px;">恢复</a></span>&nbsp;&nbsp;';
		}
		return columnItem;
	}else{
		return '';
	}
}

function fmStatus(val,row){
	if (val == 'I'){
		return '<span>待处理</span>';
	} else if (val =='N'){
		return '<span>未处理</span>';
	} else if (val =='F'){
		var columnItem = '<span>已处理</span>';
		if(row.is_valid == 'Y'){
			columnItem = columnItem + '&nbsp;&nbsp;<span><a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:doView(\''+row.id +'\')" style="width:80px;">处理信息</a></span>&nbsp;&nbsp;';
		}
		return columnItem;
	}else{
		return '';
	}
}

function doRecover(alarmId){
	$.messager.confirm('确认', '确认恢复报警状态为有效?', function(r){
		if(r){
			$.ajax( {
			    url:'<%=path%>/alarm/mgr/recoverValid.do',
			    data:{
			    	'alarmId':alarmId
			    },
			    type:'post',
			    async:false,
			    dataType:'json',
			    success:function(data) {
			    	if(data.returnCode == 'success'){
			    		$.messager.alert('提示', '恢复成功!', 'info', function(){
				    		$('#dg').datagrid('reload');
			    		});
			    	}else{
			    		$.messager.alert('异常',data.msg);
			    	}			    	
			    },
			    error : function(data) {
			    	$.messager.alert('异常',data.responseText);
		        }
			});
		}
	});
}

function doView(val){
	var curUrl = "<%=path%>/alarm/viewProcessResult.jsp?id="+val;
	var content = '<iframe name="popContent" id="popContent" src="' + curUrl + '" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>';
	var boarddiv = '<div id="msgwindow" title="处理信息" style="overflow:hidden;" ></div>'// style="overflow:hidden;"可以去掉滚动条
	$(document.body).append(boarddiv);
	var win = $('#msgwindow').dialog({
		content : content,
		width : '640',
		height : '480',
		modal : true,
		title : '处理信息',
		buttons: [{
			text:'&nbsp;&nbsp;&nbsp;&nbsp;确定&nbsp;&nbsp;&nbsp;&nbsp;',
			handler:function(){
				$('#msgwindow').dialog('destroy');
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

function doSearch(){
	var text = $("#searchField").combobox('getValue');
    if(text == null || text == ''){
    	$.messager.alert('提示', '请选择查询字段.');
    	return;
    }
	var queryParams = $('#dg').datagrid('options').queryParams;
	queryParams.sqlId = 'alarm-alarminfo-getHistoryAlarm';
	if(text == 'status'){
		var searchValue = $("#searchValue").combobox('getValue');
		queryParams.status = searchValue;
	}
	if(text == 'alarm_type'){
		var searchValue = $("#searchValue").combobox('getValue');
		queryParams.alarmType = searchValue;
	}
	
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