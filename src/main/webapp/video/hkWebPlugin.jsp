<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
	String path = request.getContextPath();
%>
<!doctype html>
<html>
<head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Cache-Control" content="no-cache, must-revalidate" />
    <meta http-equiv="Expires" content="0" />
    <script type="text/javascript"	src="<%=path%>/js/jquery/jquery-3.3.1.min.js"></script>
    <link rel="stylesheet" type="text/css"	href="<%=path%>/js/easyui/themes/default/easyui.css">
  <link rel="stylesheet" type="text/css"	href="<%=path%>/js/easyui/themes/icon.css">
  <script type="text/javascript"	src="<%=path%>/js/easyui/jquery.easyui.min.js"></script>
  <script type="text/javascript"	src="<%=path%>/js/json2.js"></script>
  <script type="text/javascript"	src="<%=path%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
  <link rel="stylesheet" type="text/css"	href="<%=path%>/style/default/hkVideo.css">
</head>
<body class="easyui-layout">
	<div data-options="region:'west'"	style="width: 680px;height:100p%; padding: 0px;">
	    <div class="easyui-panel" title="报警信息及关联摄像头" data-options="fit:true">
				<table id="dg" class="easyui-datagrid"  
				data-options="singleSelect:true,rownumbers:true,pageSize:30,fit:true,url:'<%=path%>/comm/queryForPage.do',pagination:true,method:'post', multiSort:true">
				<thead>
				<tr>
				    <th data-options="field:'alarm_desc',width:320">报警描述</th>
					<th data-options="field:'alarm_date',width:130">报警时间</th>
					<th data-options="field:'status',width:50,formatter:fmStatus">状态</th>
					<th data-options="field:'id', width:100,formatter:fmCamera">关联摄像头</th>
				</tr>
				</thead>
				</table>
		</div>
	</div>
	<div data-options="region:'center'" style="width: 100p%;height:100%;overflow:hidden;">
	    <div id="divPlugin" class="plugin"></div>
	    <div id="divNoPlugin" class="plugin" style="display:none">
	    	<div>
	    		<br>
				提示：插件未安装，请先安装。视频预览、回放需要浏览器支持插件,推荐使用IE浏览器。<br>
				          请根据浏览器版本下载相应的插件，然后解压安装。
				<br>&nbsp;&nbsp;&nbsp;&nbsp;<a href="<%=path%>/res/WebComponentsKit_win32.rar">海康摄像头web访问插件-32位</a>
				<br>&nbsp;&nbsp;&nbsp;&nbsp;<a href="<%=path%>/res/WebComponentsKit_win64.rar">海康摄像头web访问插件-64位</a>
			</div>
	    </div>
	    <div class="easyui-panel" title="摄像头信息" style="width:600px;height:150px;padding:10px;">
			<table cellpadding="0" cellspacing="3" border="0">
	            <tr>
	                <td class="tt">摄像头位置描述:</td>
	                <td><input id="posDesc" type="text" class="txt" value="" disabled="disabled"/></td>
	                <td></td>
	                <td></td>
	            </tr>
	            <tr>
	                <td class="tt">IP地址:</td>
	                <td><input id="cameraIp" type="text" class="txt" value="" disabled="disabled"/></td>
	                <td class="tt"></td>
	                <td><input id="webPort" type="hidden"  value="" />
	                    <input id="devicePort" type="hidden"  value="" />	                    
	                	<input id="cameraUserName" type="hidden" value="" />
	                	<input id="cameraPassword" type="hidden" value="" />
	                	<input id="recorderIp" type="hidden" value="" />
	                	<input id="recorderPort" type="hidden"  value="" />
	                	<input id="recorderUserName" type="hidden" value="" />
	                	<input id="recorderPassword" type="hidden" value="" />
	                </td>
	            </tr>
	            <tr>
	                <td class="tt">对应录像机通道:</td>
	                <td><input id="channelId" type="text" class="txt" value="" disabled="disabled"/></td>
	                <td></td>
	                <td></td>
	            </tr>
	            <tr>
	                <td class="tt">预置点号:</td>
	                <td colspan="3"><input id="presetNum" type="text" class="txt" disabled="disabled"/>
	                	<!-- 
	                    <input type="button" class="btn" value="切换预置点" onclick="clickGoPreset();" />
	                     -->
	                </td>
	            </tr>
	            <tr>
	                <td colspan="4">
	                	<!-- 
	                    <input type="button" class="btn" value="连接" onclick="clickLogin();" />
	                    <input type="button" class="btn" value="断开" onclick="clickLogout();" />
	                     -->
	                </td>
	            </tr>
	        </table>
		</div>
		<div class="easyui-panel" title="回放操作" style="width:600px;height:150px;padding:10px;">
			<table width="100%" cellpadding="0" cellspacing="3" border="0">
	            <tr>
	                <td class="tt">开始时间:</td>
	                <td>
	                    <input id="startTime" type="text" class="txt" disabled="disabled" />
	                </td>
	            </tr>
	            <tr>
	                <td class="tt">结束时间:</td>
	                <td>
	                    <input id="endTime" type="text" class="txt" disabled="disabled"/>
	                </td>
	            </tr>
	            <tr>
	                <td colspan="2">
	                    <button  id="btStartPlayBack" class="easyui-linkbutton" onclick="startPlayBack()" >开始回放</button>
	                    <button  id="btStopPlayBack" class="easyui-linkbutton" onclick="stopPlayBack()" >停止回放</button>
	                </td>
	            </tr>
	        </table>
		</div>
	    <div class="easyui-panel" title="预览操作" style="border:0;width:600px;height:150px;padding:10px;">
	    	<table cellpadding="0" cellspacing="3" border="0" class="left">
	            <tr>
	                <td>
	                	<button  id="btStartRealPlay" class="easyui-linkbutton" onclick="startRealPlay()" >开始预览</button>
	                    <button  id="btStopRealPlay" class="easyui-linkbutton" onclick="stopRealPlay()" >停止预览</button>
	                </td>
	            </tr>
	            <!-- 
	            <tr>
	                <td>
	                    <input type="button" class="btn" value="左上" onmousedown="mouseDownPTZControl(5);" onmouseup="mouseUpPTZControl();" />
	                    <input type="button" class="btn" value="上" onmousedown="mouseDownPTZControl(1);" onmouseup="mouseUpPTZControl();" />
	                    <input type="button" class="btn" value="右上" onmousedown="mouseDownPTZControl(7);" onmouseup="mouseUpPTZControl();" />
	                </td>
	            </tr>
	            <tr>
	                <td>
	                    <input type="button" class="btn" value="左" onmousedown="mouseDownPTZControl(3);" onmouseup="mouseUpPTZControl();" />
	                    <input type="button" class="btn" value="自动" onclick="mouseDownPTZControl(9);" />
	                    <input type="button" class="btn" value="右" onmousedown="mouseDownPTZControl(4);" onmouseup="mouseUpPTZControl();" />
	                </td>
	            </tr>
	            <tr>
	                <td>
	                    <input type="button" class="btn" value="左下" onmousedown="mouseDownPTZControl(6);" onmouseup="mouseUpPTZControl();" />
	                    <input type="button" class="btn" value="下" onmousedown="mouseDownPTZControl(2);" onmouseup="mouseUpPTZControl();" />
	                    <input type="button" class="btn" value="右下" onmousedown="mouseDownPTZControl(8);" onmouseup="mouseUpPTZControl();" />
	                </td>
	            </tr>
	            -->
	        </table>
	    </div>
	 </div>
</body>

<script id="videonode" src="<%=path%>/js/hk/webVideoCtrl.js"></script>
<script>
var curConnIp = '';
var curOperation = '';

//初始化插件
//全局保存当前选中窗口
var g_iWndIndex = 0; //可以不用设置这个变量，有窗口参数的接口中，不用传值，开发包会默认使用当前选择窗口
$(function () {
	var queryParams = $('#dg').datagrid('options').queryParams;
	queryParams.sqlId = 'alarm-camera-getAlarmsCamera';
	
	$('#dg').datagrid('reload');
	//$('#dg').datagrid('loadData',{total:0,rows:[]});
	
	$('#dg').datagrid({
		onClickCell: function(index,field,value){
			if(field == 'id'){
				var rows = $('#dg').datagrid('getRows');
				var curRowData= rows[index];
				if(curRowData.camera_num !=null){
					switchCamera(curRowData);
				}
			}
		}
	});
	
	$('.pagination-page-list').hide();
	
 	// 检查插件是否已经安装过
 	var iRet = WebVideoCtrl.I_CheckPluginInstall();
 	if (-1 == iRet) {
    	alert("您还未安装过插件，请下载WebComponentsKit.exe安装！");
    	$('#divPlugin').hide(); 
    	$('#divNoPlugin').show();
    	$("#btStartPlayBack").attr('disabled',true);
    	$("#btStopPlayBack").attr('disabled',true);
    	$("#btStartRealPlay").attr('disabled',true);
    	$("#btStopRealPlay").attr('disabled',true);
    	
    	$("#btStartPlayBack").css('background','lightgray');
    	$("#btStopPlayBack").css('background','lightgray');
    	$("#btStartRealPlay").css('background','lightgray');
    	$("#btStopRealPlay").css('background','lightgray');
     	return;
 	}

 // 初始化插件参数及插入插件
 WebVideoCtrl.I_InitPlugin(600, 400, {
     bWndFull: false,     //是否支持单窗口双击全屏，默认支持 true:支持 false:不支持
     iPackageType: 2,    //2:PS 11:MP4
     iWndowType: 1,
     bNoPlugin: true,
     cbSelWnd: function (xmlDoc) {
         //g_iWndIndex = parseInt($(xmlDoc).find("SelectWnd").eq(0).text(), 10);
         //var szInfo = "当前选择的窗口编号：" + g_iWndIndex;
         //showCBInfo(szInfo);
     },
     cbDoubleClickWnd: function (iWndIndex, bFullScreen) {
         //var szInfo = "当前放大的窗口编号：" + iWndIndex;
         //if (!bFullScreen) {            
         //    szInfo = "当前还原的窗口编号：" + iWndIndex;
         //}
         //showCBInfo(szInfo);
     },
     cbEvent: function (iEventType, iParam1, iParam2) {
         if (2 == iEventType) {
        	 // 回放正常结束
             //showCBInfo("窗口" + iParam1 + "回放结束！");
         } else if (-1 == iEventType) {
             //showCBInfo("设备" + iParam1 + "网络错误！");
         } else if (3001 == iEventType) {
             //clickStopRecord(g_szRecordType, iParam1);
         }
     },
     cbRemoteConfig: function () {
         //showCBInfo("关闭远程配置库！");
     },
     cbInitPluginComplete: function () {
         WebVideoCtrl.I_InsertOBJECTPlugin("divPlugin");
         // 检查插件是否最新
         //if (-1 == WebVideoCtrl.I_CheckPluginVersion()) {
         //    alert("检测到新的插件版本，双击开发包目录里的WebComponentsKit.exe升级！");
         //    return;
         //}
     }
 });
});

function fmStatus(val,row){
	var columnItem = '';
	if(val == 'N'){
		columnItem =  columnItem + '<span>未处理</span>';
	}else if(val == 'I'){
		columnItem =  columnItem + '<span>待处理</span>';
	}	
	return columnItem;
}

function fmCamera(val,row){
	var columnItem = '';
	if(row.camera_num != null && row.camera_num != ''){
		columnItem =  columnItem + "<span><a href='javascript:void(0)' class='easyui-linkbutton' title='点击切换摄像头'>" + row.model + "-" + row.camera_num + "</a></span>&nbsp;&nbsp;";
	}else{
		columnItem =  columnItem + '<span>无</span>&nbsp;&nbsp;';
	}
	return columnItem;
}

function formatDate(curDate) 
{ 
	 var year = curDate.getFullYear();
	 var month = curDate.getMonth() + 1;
	 if (month >= 1 && month <= 9) {
	    month = "0" + month;
	 }
	 var day = curDate.getDate();	 
	 if (day <= 9) {
	    day = "0" + day;
	 }	 
	 var hours = curDate.getHours();
	 var minutes = curDate.getMinutes();
	 var seconds = curDate.getSeconds();
	 var str = year+"-" + month + "-" + day + " " + hours +":"+ minutes + ":" + seconds;
	 
	 return str;
}

function switchCamera(row){
	//if(curConnIp != ''){
	//	//切换时断开已连接的
	//	WebVideoCtrl.I_Logout(curConnIp);
	//}	
	$("#posDesc").val(row.pos_desc);
	$("#cameraIp").val(row.ip);
	$("#channelId").val(row.channel_id);
	$("#presetNum").val(row.preset_num);
	$("#startTime").val(row.alarm_date);	
	$("#webPort").val(row.web_port);
	$("#devicePort").val(row.device_port);
	$("#cameraUserName").val(row.camera_user_name);
	$("#cameraPassword").val(row.camera_password);
	$("#recorderIp").val(row.recorder_ip);
	$("#recorderPort").val(row.recorder_port);
	$("#recorderUserName").val(row.recorder_user_name);
	$("#recorderPassword").val(row.recorder_password);
	
	if(row.alarm_date != null){
		var tempDate = row.alarm_date.replace("-", "/").replace("-", "/");
		var endDate = new Date(tempDate);
		endDate.setMinutes(endDate.getMinutes() + 30);
		var fmtEndDate = formatDate(endDate);
		$("#endTime").val(fmtEndDate);
	}else{
		$("#endTime").val('');
	}
}

function realPlay(cameraIp){
	//获取rtsp端口
    var oPort = WebVideoCtrl.I_GetDevicePort(cameraIp);
	var rtspPort = 554;
	if (oPort != null) {
	   var rtspPort = oPort.iRtspPort;
	}
    //预览
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);
	if(oWndInfo != null){
		WebVideoCtrl.I_Stop({
            success: function () {
            	WebVideoCtrl.I_StartRealPlay(cameraIp, {
                    iRtspPort: rtspPort,
                    iStreamType: 1,
                    iChannelID: 1,
                    bZeroChannel: false,
                    success: function () {
                    	//调整预置点
                    	var presetNum = $("#presetNum").val();
                    	WebVideoCtrl.I_GoPreset(presetNum, {
                             success: function (xmlDoc) { },
                             error: function (status, xmlDoc) { }
                        });
                    	$.messager.alert('提示','开始预览成功！');
                    },
                    error: function (status, xmlDoc) {
                    	$.messager.alert('提示','开始预览失败！');
                    }
                });
            }
        });
	}else{
		WebVideoCtrl.I_StartRealPlay(cameraIp, {
            iRtspPort: rtspPort,
            iStreamType: 1,
            iChannelID: 1,
            bZeroChannel: false,
            success: function () {
            	//调整预置点
            	var presetNum = $("#presetNum").val();
            	WebVideoCtrl.I_GoPreset(presetNum, {
                     success: function (xmlDoc) { },
                     error: function (status, xmlDoc) { }
                });
            	$.messager.alert('提示','开始预览成功！');
            },
            error: function (status, xmlDoc) {
                $.messager.alert('提示','开始预览失败！');
            }
        });
	}
}

function startRealPlay(){
	var cameraIp = $("#cameraIp").val();
	var webPort = $("#webPort").val();
	var devicePort = $("#devicePort").val();
	var cameraUserName = $("#cameraUserName").val();
	var cameraPassword = $("#cameraPassword").val();
	
	if(cameraIp.length > 0){
		if(cameraIp == curConnIp){
			//表示已连接
			if(curOperation == 'startRealPlay'){
				$.messager.alert('提示','当前摄像头已在预览.');
				return;
			}else{
				//先断开连接
				WebVideoCtrl.I_Logout(curConnIp);
			}
		}else{
			//先断开连接
			WebVideoCtrl.I_Logout(curConnIp);
		}
		//连接
		var iRet = WebVideoCtrl.I_Login(cameraIp, 1, webPort, cameraUserName, cameraPassword, {
	        success: function (xmlDoc) {
	            //连接成功
	            curConnIp = cameraIp;
	            realPlay(cameraIp);
	        },
	        error: function (status, xmlDoc) {
	            //连接失败
	        	$.messager.alert('提示','摄像头连接失败!');
	        }
	    });
		curOperation = 'startRealPlay';
	}else{
		$.messager.alert('提示','关联摄像头IP未指定.');
	}
}

function stopRealPlay(){
	if(curOperation == 'startRealPlay'){
		var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);
	    if (oWndInfo != null) {
        	WebVideoCtrl.I_Stop({
	            success: function () {
	            	curOperation = 'stopRealPlay';
	            	WebVideoCtrl.I_Logout(curConnIp);
	            	$.messager.alert('提示','停止预览成功！');
	            },
	            error: function () {
	            	$.messager.alert('提示','停止预览失败！');
	            }
        	});
    	}
	}else{
		$.messager.alert('提示','当前没有预览, 不需停止预览.');
	}
}

function playBack(recorderIp, channelID, startTime, endTime){
	var oPort = WebVideoCtrl.I_GetDevicePort(recorderIp);
	var rtspPort = 554;
	if (oPort != null) {
	   var rtspPort = oPort.iRtspPort;
	}	
	WebVideoCtrl.I_StartPlayback(recorderIp, {
        iRtspPort: rtspPort,
        iStreamType: 1,
        iChannelID: channelID,
        szStartTime: startTime,
        szEndTime: endTime,
        success: function () {
            $.messager.alert('提示','开始回放成功！');
            curOperation='startPlayBack'
        },
        error: function (status, xmlDoc) {
            $.messager.alert('提示','开始回放失败！');
        }
    });
}

//开始回放
function startPlayBack(){
	
	var recorderIp = $("#recorderIp").val();
	var recorderPort = $("#recorderPort").val();
	var recorderUserName = $("#recorderUserName").val();
	var recorderPassword = $("#recorderPassword").val();
	
	var channelId = $("#channelId").val();
	var strStartTime = $("#startTime").val();
    var strEndTime = $("#endTime").val();
    
    if(recorderIp.length > 0){
		//if(recorderIp == curConnIp){
		//	//表示已连接, 直接回放
		//	var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);
		//	playBack(recorderIp, channelId, strStartTime, strEndTime);
		//}else{
			//先断开连接
			WebVideoCtrl.I_Logout(curConnIp);
			//连接
			var iRet = WebVideoCtrl.I_Login(recorderIp, 1, recorderPort, recorderUserName, recorderPassword, {
		        success: function (xmlDoc) {
		            //连接成功
		            curConnIp = recorderIp;
		            playBack(recorderIp, channelId, strStartTime, strEndTime);
		        },
		        error: function (status, xmlDoc) {
		        	$.messager.alert('提示','录像机连接失败!');
		        }
		    });
		//}
	}else{
		$.messager.alert('提示','关联摄像头录像机IP未指定.');
	}
}

//停止回放
function stopPlayBack() {
	if(curOperation == 'startPlayBack'){
		var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);
	    if (oWndInfo != null) {
        	WebVideoCtrl.I_Stop({
	            success: function () {
	                $.messager.alert('提示','停止回放成功！');
	            	curOperation = 'stopPlayBack';
	            },
	            error: function () {
	            	$.messager.alert('提示','停止回放失败！');
	            }
        	});
    	}
	}else{
		$.messager.alert('提示','当前没有回放, 不需停止回放.');
	}
}
</script>
</html>