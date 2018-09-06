<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>报警频次统计</title>
<%@ include file="/common/res.jsp" %>
<script type="text/javascript">  
	
	$(function() {
		setSubMenuAction();
		
		$(".panel-header.accordion-header").each(function(index,element){
		    if(index ==0){
		    	//alert($(this).text());
		    	$(this).click()
		    }
		});
	});
	 
	function setSubMenuAction(){
		$('.panel-header.accordion-header').bind(
				"click",
				function() {
					$(this).siblings().removeClass('accordion-header-selected')
							.end().addClass('accordion-header-selected');
					var menuUrl = $(this).attr('itemUrl');
					var curUrl = menuUrl;
					
					$('#frmConfig').attr('src', curUrl);
				});
	
		$('.panel-header.accordion-header').bind("mouseover", function() {
			$(this).addClass('accordion-header-mouseover');
		});
	
		$('.panel-header.accordion-header').bind("mouseout", function() {
			$(this).removeClass('accordion-header-mouseover');
		});
	}
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
	<div id="main-left"
		data-options="region:'west',split:true,title:'警情统计'"
		style="width: 200px;height:300px; padding: 0px;">
		<div id="subMenu" style="overflow: hidden;height:100%;">
			<div class="panel-header accordion-header" itemUrl="<%=path%>/report/alarmStatistics.jsp">
				<div class="panel-title">
					<img src="<%=path%>/style/default/image/func.png" /><a>&nbsp;&nbsp;报警频次统计</a>
				</div>
			</div>
		</div>
	</div>

	<div id="main-right" data-options="region:'center'" style="width: 100%;height:100%;overflow:hidden;">
		<iframe height="100%" width="100%" frameBorder="0" id="frmConfig"	name="frmConfig" src="" />
	</div>
</body>
</html>
