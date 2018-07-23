
function showMessageDialog(url, title, width, height, shadow) {
	var content = '<iframe src="'
			+ url
			+ '" width="100%" height="99%" frameborder="0" scrolling="no"></iframe>';
	var boarddiv = '<div id="msgwindow" title="' + title
			+ '" ></div>'// style="overflow:hidden;"可以去掉滚动条
	$(document.body).append(boarddiv);
	var win = $('#msgwindow').dialog({
		content : content,
		width : width,
		height : height,
		modal : shadow,
		title : title,
		onClose : function() {
			$(this).dialog('destroy');// 后面可以关闭后的事件
		}
	});
	//win.dialog('open');
	//win.window('center');
	win.window('open').window('resize',{top:'60px',left:'100px'});
}

function closeDialog() {
	$("#msgwindow").dialog('destroy');
}

document.oncontextmenu = function(){
	  return false;
}
document.onkeydown = function(){
	  if (event.ctrlKey && window.event.keyCode==67){
	    return false;
	  }
}
document.body.oncopy = function (){
	  return false;
}
