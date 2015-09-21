(function($) {
	function show(el, type, speed, timeout) {
		var win = $(el).window("window");
		if (!win) {
			return;
		}
		switch (type) {
			case null:
				win.show();
				break;
			case "slide":
				win.slideDown(speed);
				break;
			case "fade":
				win.fadeIn(speed);
				break;
			case "show":
				win.show(speed);
				break;
		}
		var timer = null;
		if (timeout > 0) {
			timer = setTimeout(function() {
				hide(el, type, speed);
			}, timeout);
		}
		win.hover(function() {
			if (timer) {
				clearTimeout(timer);
			}
		}, function() {
			if (timeout > 0) {
				timer = setTimeout(function() {
					hide(el, type, speed);
				}, timeout);
			}
		});
	};
	function hide(el, type, speed) {
		if (el.locked == true) {
			return;
		}
		el.locked = true;
		var win = $(el).window("window");
		if (!win) {
			return;
		}
		switch (type) {
			case null:
				win.hide();
				break;
			case "slide":
				win.slideUp(speed);
				break;
			case "fade":
				win.fadeOut(speed);
				break;
			case "show":
				win.hide(speed);
				break;
		}
		setTimeout(function() {
			$(el).window("destroy");
		}, speed);
	};
	function createDialog(title, content,opts) {
		var win = $("<div class=\"messager-body\"></div>").appendTo("body");
		win.append(content);
		var opt = $.extend({}, {
			title : title,
			noheader : (title ? false : true),
			width : 350,
			height : "auto",
			modal : true,
			collapsible : false,
			minimizable : false,
			maximizable : false,
			resizable : false,
			onClose : function() {
				setTimeout(function() {
					win.window("destroy");
				}, 100);
			}
		},opts);
		win.window(opt);
		win.window("window").addClass("messager-window");
		win.children("div.messager-button").children("a:first").focus();
		return win;
	};
	
	$.messager = {
		show : function(options) {
			var opts = $.extend( {
				showType : "slide",
				showSpeed : 600,
				width : 250,
				height : 100,
				msg : "",
				title : "",
				timeout : 4000
			}, options || {});
			var win = $("<div class=\"messager-body\"></div>").html(opts.msg).appendTo("body");
			win.window( {
				title : opts.title,
				width : opts.width,
				height : opts.height,
				collapsible : false,
				minimizable : false,
				maximizable : false,
				shadow : false,
				draggable : false,
				resizable : false,
				closed : true,
				iconCls : "icon-dialog",
				onBeforeOpen : function() {
					show(this, opts.showType, opts.showSpeed, opts.timeout);
					return false;
				},
				onBeforeClose : function() {
					hide(this, opts.showType, opts.showSpeed);
					return false;
				}
			});
			win.window("window").css({
				left : "",
				top : "",
				right : 0,
				zIndex : $.fn.window.defaults.zIndex++,
				bottom : -document.body.scrollTop
						- document.documentElement.scrollTop
			});
			win.window("open");
		},
		progress : function(options) {
			var opts = $.extend( {
				title : "",
				msg : "",
				text : undefined,
				interval : 300
			}, options || {});
			var methods = {
				bar : function() {
					return $("body>div.messager-window").find("div.messager-p-bar");
				},
				close : function() {
					var win = $("body>div.messager-window>div.messager-body");
					if (win.length) {
						if (win[0].timer) {
							clearInterval(win[0].timer);
						}
						win.window("close");
					}
				}
			};
			if (typeof options == "string") {
				var method = methods[options];
				return method();
			}
			var pbar = "<div class=\"messager-progress\"><div class=\"messager-p-bar\"></div><div class=\"messager-p-msg\"></div></div>";
			var win = createDialog(opts.title, pbar, {iconCls : "icon-dialog",closable : false});
			win.find("div.messager-p-msg").html(opts.msg);
			var bar = win.find("div.messager-p-bar");
			bar.progressbar( {
				text : opts.text
			});
		}
	};
	$.messager.defaults = {
		ok : "Ok",
		cancel : "Cancel"
	};
})(jQuery);