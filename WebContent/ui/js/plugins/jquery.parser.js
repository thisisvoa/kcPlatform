/**
 * ========================================================
 * 
 * 页面渲染器，页面载入时候查找样式定义为easyui-为开头的ui组件，对组件进行初始化。
 * 若组件关联js未载入，则动态载入js
 * 
 * ========================================================
 */
(function($) {
	$.parser = {
		auto : true,
		/**
		 * 渲染完，让页面可见
		 * @param context
		 */
		onComplete : function(context) {
			
		},
		plugins : ["linkbutton","combobox","datePicker","textinput", "textarea","autocomplete","menu",
		           "checkbox","radiobox","fileupload","combo","combotree","spinner","numberbox","htmleditor",
		           "numberspinner","timespinner","button",
		           "layout","panel","accordion","tabs","fieldset","toolbar","menubutton","grid","tree","validate","treeSelector"],
		/**
		 * 用于渲染页面
		 * @param context
		 */
		parse : function(context) {
			var aa = [];
			for ( var i = 0; i < $.parser.plugins.length; i++) {
				var name = $.parser.plugins[i];
				var r = $(".easyui-" + name, context);
				if (r.length) {
					if (r[name]) {
						r[name]();
					} else {
						aa.push( {
							name : name,
							jq : r
						});
					}
				}
			}
			if (aa.length && window.easyloader) {
				var names = [];
				for ( var i = 0; i < aa.length; i++) {
					names.push(aa[i].name);
				}
				easyloader.load(names, function() {
					for ( var i = 0; i < aa.length; i++) {
						var name = aa[i].name;
						var jq = aa[i].jq;
						jq[name]();
						
					}
					$.parser.onComplete.call($.parser, context);
				});
			} else {
				$.parser.onComplete.call($.parser, context);
			}
		},
		parseOptions : function(target, opt) {
			var t = $(target);
			var options = {};
			var s = $.trim(t.attr("data-options"));
			if (s) {
				var start = s.substring(0, 1);
				var end = s.substring(s.length - 1, 1);
				if (start != "{") {
					s = "{" + s;
				}
				if (end != "}") {
					s = s + "}";
				}
				options = (new Function("return " + s))();
			}
			if (opt) {
				var p = {};
				for ( var i = 0; i < opt.length; i++) {
					var pp = opt[i];
					if (typeof pp == "string") {
						if (pp == "width" || pp == "height" || pp == "left"
								|| pp == "top") {
							p[pp] = parseInt(target.style[pp]) || undefined;
						} else {
							p[pp] = t.attr(pp);
						}
					} else {
						for ( var _c in pp) {
							var _d = pp[_c];
							if (_d == "boolean") {
								p[_c] = t.attr(_c) ? (t.attr(_c) == "true")
										: undefined;
							} else {
								if (_d == "number") {
									p[_c] = t.attr(_c) == "0" ? 0
											: parseFloat(t.attr(_c))
													|| undefined;
								}
							}
						}
					}
				}
				$.extend(options, p);
			}
			return options;
		}
	};
	
	$.fn._outerWidth = function(width) {
		if (width == undefined) {
			if (this[0] == window) {
				return this.width() || document.body.clientWidth;
			}
			return this.outerWidth() || 0;
		}
		return this.each(function() {
			if (!$.support.boxModel && $.browser.msie) {
				$(this).width(width);
			} else {
				var nav = $(this).outerWidth() - $(this).width();
				if(nav<0){
					nav = 0;
				}
				$(this).width(width - nav);
			}
		});
	};
	$.fn._outerHeight = function(height) {
		if (height == undefined) {
			if (this[0] == window) {
				return this.height() || document.body.clientHeight;
			}
			return this.outerHeight() || 0;
		}
		return this
				.each(function() {
					if (!$.support.boxModel && $.browser.msie) {
						$(this).height(height);
					} else {
						$(this).height(height- 
								($(this).outerHeight() - $(this).height()));
					}
				});
	};
	$(function() {
		if (!window.easyloader && $.parser.auto) {
			$.parser.parse();
		}
	});
})(jQuery);
