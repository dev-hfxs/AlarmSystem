<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>周界报警系统</title>
<%@ include file="/common/res.jsp" %>
<script type="text/javascript">  
	
	$(function() {
		var fullName = '${loginUser.full_name}';
		
		//获取用户的菜单
		$.ajax( {
		    url:'<%=path%>/comm/queryForList.do',
		    data:{
		    	'sqlId':'alarm-user-getUserMenusByUserId',
		    	'userId':'${loginUser.id}'
		    },
		    type:'post',
		    async:false,
		    dataType:'json',
		    success:function(data) {
		    	if(data != null){
		    		userMenus = data;
		    		//展示用户的功能模块
		    		var isFirstMenu = true;
		    		$.each(data,function(i,item){
						if(item.parent_id == 'ROOT'){
							$("#nav-container").append('<li class="navitem"><div class="menu-item" itemId="' + item.id + '"><a>' + item.menu_name + '</a></div></li>');
							if(isFirstMenu == true){
								isFirstMenu = false;
								var curUrl = '<%=path%>/' + item.menu_url;
								if(item.menu_url != null && item.menu_url.length > 0){
									//
								}else{
									curUrl = '<%=path%>/system/layout/bank.jsp';
								}
								$('#frmMain').attr('src',curUrl);
							}
						}
					});
		    		$('.menu-item').bind(
		    				"click",function() {
		    					var itemId = $(this).attr('itemId');
		    					$.each(userMenus,function(i,item){
		    		    			if(item.id == itemId){
		    							var curUrl = '<%=path%>' + item.menu_url;
		    							$('#frmMain').attr('src',curUrl);
		    						}
		    					});
			    	});
		    	}
		    },
		    error : function(data) {
		    	$.messager.alert('异常',data.responseText);
	        }
		});
		
	    //设置菜单鼠标事件的样式
		$('.menu-item').bind("mouseover", function() {
			$(this).addClass('menu-item-over');
		});	
		$('.menu-item').bind("mouseout", function() {
			$(this).removeClass('menu-item-over');
		});
		
		$('.bt-logout,.bt-reset-pwd').bind("mouseover", function() {
			$(this).addClass('bt-mouseover');
		});

		$('.bt-logout,.bt-reset-pwd').bind("mouseout", function() {
			$(this).removeClass('bt-mouseover');
		});
		
		$('.bt-logout').bind("click", function() {
			$.post("<%=path%>/auth/logout.do", function(result){window.location="<%=path%>/system/login/login.jsp";},"json");
		});
		
		$('.bt-reset-pwd').bind("click", function() {
			var content = '<iframe name="popContent" id="popContent" src="<%=path%>/auth/user/passwordReset.jsp" width="100%" height="80%" frameborder="0" scrolling="no"></iframe>';
			var boarddiv = '<div id="msgwindow" title="用户修改密码" ></div>'// style="overflow:hidden;"可以去掉滚动条
			$(document.body).append(boarddiv);
			var win = $('#msgwindow').dialog({
				content : content,
				width : '480',
				height : '320',
				modal : true,
				title : '用户修改密码',
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
		});
		
		$('.login-info').text(fullName);
	});
	 
	
</script>
<style>
.layout-split-west {
    border-right: 1px solid #E6EEF8;
}
.anchorBL{
	display:none;
}
</style>
</head>
<body class="easyui-layout">
	<div id="main-header" data-options="region:'north',border:false"
		style="width: 100%; height: 50px;">
		<div class="header-container" style="width: 100%;">
		    <div class="logo-text div-float">
		    	<div style="position:absolute;z-index:2">
				<img src="<%=path%>/style/default/image/logo48.ico" height="36px" width="36px" style="position:relative;left:5px;bottom:3px;"/>
				</div>
				<div style="z-index:1;float:left">
				  <img src="<%=path%>/style/default/image/logo-alarm.png" height="50px" width="250px" style="position:relative;left:35px;bottom:8px;"/>
				</div>
			</div>
			<div style="float:left;height:50px;width:60%">
					<ul id="nav-container" >
					<!-- 
					<li class="navitem"><div class="menu-item"><a >电子地图</a></div></li>
					<li class="navitem"><div class="menu-item"><a>历史报警</a></div></li>
					<li class="navitem"><div class="menu-item"><a>设备管理</a></div></li>
					-->
					</ul>
			</div>
			<div style="float:left;height:50px;width:20%">
					<div class="login-info"></div>
					<div class="bt-reset-pwd">修改密码</div>
					<div class="bt-logout">注销</div>
			</div>
		</div>
	</div>
	<div id="main-right" data-options="region:'center'" style="width: 100%;height:100%;overflow:hidden;">
		<iframe height="100%" width="100%" frameBorder="0" id="frmMain"	name="frmMain" src="" />
	</div>
</body>
</html>
