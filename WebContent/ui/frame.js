/**
 * 系统框架页执行
 */
var SysFrame = {
		sign:"$sysFrame$",//框架页面标志，用于内部的IFrame寻找顶级框架；
		config:{
		}
};
window.SysFrame = SysFrame;

/**
 * 框架页初始化
 */
(function($) {
	/**
	 * 加载弹出框JS
	 */
	easyloader.load("js/zDialog/zDialog.js");
	easyloader.load("js/zDialog/zDrag.js");
})(jQuery);