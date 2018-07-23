<%@ page pageEncoding="UTF-8"%>
<% 
String pageNum = request.getParameter("pageNum"); 
String roleId = request.getParameter("id");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>角色分配功能</title>
<%@ include file="/common/res.jsp" %>
<script type="text/javascript"	src="<%=path%>/js/ztree/js/jquery.ztree.all.min.js"></script>
<link rel="stylesheet" href="<%=path%>/js/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="<%=path%>/js/json2.js"></script>
<script type="text/javascript" src="<%=path%>/js/Map.js"></script>
<style>

</style>
</head>
<body>
<!--  -->
<div id="dgPanel" class="easyui-panel" data-options="fit:true">
	<div title="功能树" style="padding:10px">
			<ul class="ztree" id="funcTree" ></ul>
	</div>
</div>
<script>
var initAllotMenuMap = new Map();

$(function() {
	  var setting = {
			check: {
				enable: true,
				chkboxType:{'Y':'ps','N':'s'}
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				onCheck: zTreeOnCheck
			}
		};
		
		$.ajax( {
		    url:'<%=path%>/comm/queryForList.do',
		    data:{
		    	'sqlId':'alarm-role-getRoleAllotFuncs',
		    	'roleId':'<%=roleId%>'
		    },
		    type:'post',
		    async:false,
		    dataType:'json',
		    success:function(data) {
		    	var treeNodes = data;
				if(treeNodes != null && treeNodes.length > 0){
					for(var i = 0; i < treeNodes.length; i++){
					    var item = treeNodes[i];
					    treeNodes[i]['icon'] = '<%=path%>/style/default/image/wx.png';
					    if(item.checked == true || item.checked == 'true'){
					    	initAllotMenuMap.put(item.id, item.name);
					    }
					}
				}
				
				$.fn.zTree.init($("#funcTree"), setting, treeNodes);
		    },
		    error : function(data) {
		    	//$.messager.alert('异常',data.responseText);
	        }
		});
		
		function zTreeOnCheck(event, treeId, treeNode) {
			//var zTree = $.fn.zTree.getZTreeObj("funcTree");
			//var changedNodes = zTree.getChangeCheckedNodes();
		};
});

function okResponse(){
	var zTree = $.fn.zTree.getZTreeObj("funcTree");
	//var changedNodes = zTree.getChangeCheckedNodes();
	var checkedNodes = zTree.getCheckedNodes();
		var newAllotMenus = new Array();
		var chgAllotMenuMap = new Map();
		
		//识别新增的节点
		for(var i=0; i<checkedNodes.length; i++){
			var menuId = checkedNodes[i].id;
			chgAllotMenuMap.put(menuId, checkedNodes[i].name);
			if(initAllotMenuMap.get(menuId) != null){
				//
			}else{
				var newItem = {id:menuId, action:'A'};
				newAllotMenus[newAllotMenus.length] = newItem;
			}
		}
		
		//识别删除的节点
		for(var i=0; i<initAllotMenuMap.size(); i++){
			var menuId = initAllotMenuMap.getItem(i).key;
			if(chgAllotMenuMap.get(menuId) != null){
				//已分配的还存在则忽略
			}else{
				//不存在则表示删除
				var deleteItem = {id:menuId, action:'D'};
				newAllotMenus[newAllotMenus.length] = deleteItem;
			}
		}
		if(newAllotMenus != null && newAllotMenus.length > 0){
			// 提交保存
			var resultJson = JSON.stringify(newAllotMenus);
			$.ajax( {
			    url:'<%=path%>/role/mgr/allotFunc.do',
			    data:{
			    	'roleId':'<%=roleId%>',
			    	'allotFuncs':resultJson,
			    },
			    type:'post',
			    async:false,
			    dataType:'json',
			    success:function(data) {
			    	$.messager.alert('提示', '操作成功!', 'info', function(){
			    		parentWin.closeDialog();
		    		});
			    },
			    error : function(data) {
			    	$.messager.alert('异常',data.responseText);
		        }
			});
		}else{
			$.messager.alert('提示', '功能设置未改变, 没有需要保存的数据！', 'info', function(){
	    		//
    		});
		}
}
</script>
</body>
</html>