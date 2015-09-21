/**
 * 页面全局变量
 */
PAGE_CONFIG = {
	onParsed:[]//在渲染页面完毕后执行的回调
};

/**
 * 用于页面上注册渲染完的回调
 */
$parsed = function(func){
	PAGE_CONFIG.onParsed.push(func);
};

/**
 * 页面加载完后，加载parser.js插件并渲染界面
 */
$(function() {
	//渲染完后执行的回调函数
	easyloader.load('parser', function(context) {
		jQuery.parser.onComplete = function(){
			$(document.body).css("visibility","visible");
			var callbacks = PAGE_CONFIG.onParsed;
			for(var i=0;i<callbacks.length;i++){
				callbacks[i]();
			}
			
		};
		jQuery.parser.parse();//渲染方法
	});
});

