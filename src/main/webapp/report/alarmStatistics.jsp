<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>报警频次统计</title>
<%@ include file="/common/res.jsp" %>
<script type="text/javascript"	src="<%=path%>/js/ztree/js/jquery.ztree.all.min.js"></script>
<link rel="stylesheet" href="<%=path%>/js/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="<%=path%>/js/json2.js"></script>
<script type="text/javascript" src="<%=path%>/js/echarts.min.js"></script>
<style>
.panel-tool{
	display:none;
}
.left
{
    float:left;
}
.chart-panel
{
    width:640px;
    height:480px;
}
</style>
</head>
<body>
<!--  -->
<div  class="easyui-layout"  data-options="fit:true">
	<div id="areaChart" class="left chart-panel">
	</div>
	<div id="timeChart" class="left chart-panel">		
	</div>
</div>
<script>
var areaChart = echarts.init(document.getElementById('areaChart'));
$.ajax( {
    url:'<%=path%>/report/getAreaAlarmCount.do',
    data:{},
    type:'post',
    async:false,
    dataType:'json',
    success:function(data) {
    	if(data!=null){
    		var xData = new Array();
    		var yData = new Array();
    		
    		for (var sk in data){
    			xData[xData.length] = sk;
    			yData[yData.length] = data[sk];    			
    		}
    		// 指定图表的配置项和数据
    		var areaChartOption = {
    		    title: {
    		        text: '报警区域Top10'
    		    },
    		    tooltip: {},
    		    legend: {
    		        data:['报警次数']
    		    },
    		    xAxis: {
    		        data: xData
    		    },
    		    yAxis: {},
    		    series: [{
    		        name: '报警次数',
    		        type: 'bar',
    		        data: yData,
    		        barWidth:25
    		    }]
    		};
    		// 使用刚指定的配置项和数据显示图表。
    		areaChart.setOption(areaChartOption);
    	}
    },
    error : function(data) {
    	//$.messager.alert('异常',data.responseText);
    }
});

var timeChart = echarts.init(document.getElementById('timeChart'));
$.ajax( {
    url:'<%=path%>/report/getMonthAlarmCount.do',
    data:{},
    type:'post',
    async:false,
    dataType:'json',
    success:function(data) {
    	if(data!=null){
    		var xData = new Array();
    		var yData = new Array();
    		
    		for (var i=0; i <data.length; i++ ){
    			xData[xData.length] = (i+1)+'月';
    			yData[yData.length] = data[i];	
    		}
    		//指定图表的配置项和数据
    		timeChartOption = {
	   			title: {
	   			        text: '时间分布(月份)'
	   			},
	   			tooltip: {},
	   				legend: {
	   			        data:['报警次数']
	   			},
	   			xAxis: {
	   			        type: 'category',
	   			        boundaryGap : false,
	   			        offset:1,
	   			        data: xData
	   			 },
	   			 yAxis: {
	   			        type: 'value'
	   			 },
	   			 series: [{
	   			 name:'报警次数',
   			        data: yData,
   			        type: 'line'
   			    }]
   			};

    		//使用刚指定的配置项和数据显示图表。
    		timeChart.setOption(timeChartOption);
    	}
    },
    error : function(data) {
    	//$.messager.alert('异常',data.responseText);
    }
});

</script>
</body>
</html>