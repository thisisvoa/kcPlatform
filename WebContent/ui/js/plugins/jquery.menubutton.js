(function($) {
	function init(jq) {
		var options = $.data(jq, "menubutton").options;
		var btn = $(jq);
		btn.removeClass("m-btn-active m-btn-plain-active").addClass("m-btn");
		btn.linkbutton($.extend({}, options, {
			text : options.text + "<span class=\"m-btn-downarrow\">&nbsp;</span>"
		}));
		if(options.menu) {
			$(options.menu).menu(
							{
								onShow : function() {
									btn.addClass((options.plain == true) ? "m-btn-plain-active"
													: "m-btn-active");
								},
								onHide : function() {
									btn.removeClass((options.plain == true) ? "m-btn-plain-active"
													: "m-btn-active");
								}
							});
		}
		setEnable(jq, options.disabled);
	};
	
	function setEnable(jq, enable) {
		var options = $.data(jq, "menubutton").options;
		options.disabled = enable;
		var btn = $(jq);
		if (enable) {
			btn.linkbutton("disable");
			btn.unbind(".menubutton");
		} else {
			btn.linkbutton("enable");
			btn.unbind(".menubutton");
			btn.bind("click.menubutton", function() {
				showMenu();
				return false;
			});
			var T = null;
			btn.bind("mouseenter.menubutton", function() {
				T = setTimeout(function() {
					showMenu();
				}, options.duration);
				return false;
			}).bind("mouseleave.menubutton", function() {
				if (T) {
					clearTimeout(T);
				}
			});
		}
		function showMenu() {
			if (!options.menu) {
				return;
			}
			var left = btn.offset().left;
			if (left + $(options.menu)._outerWidth() + 5 > $(window)._outerWidth()) {
				left = $(window)._outerWidth() - $(options.menu)._outerWidth() - 5;
			}
			$("body>div.menu-top").menu("hide");
			$(options.menu).menu("show", {
				left : left,
				top : btn.offset().top + btn.outerHeight()
			});
			btn.blur();
		};
	};
	
	$.fn.menubutton = function(options, param) {
		if (typeof options == "string") {
			return $.fn.menubutton.methods[options](this, param);
		}
		options = options || {};
		return this.each(function() {
			var data = $.data(this, "menubutton");
			if (data) {
				$.extend(data.options, options);
			} else {
				$.data(this, "menubutton", {
					options : $.extend({}, $.fn.menubutton.defaults,
							$.fn.menubutton.parseOptions(this), options)
				});
				$(this).removeAttr("disabled");
			}
			init(this);
		});
	};
	
	$.fn.menubutton.methods = {
		options : function(jq) {
			return $.data(jq[0], "menubutton").options;
		},
		enable : function(jq) {
			return jq.each(function() {
				setEnable(this, false);
			});
		},
		disable : function(jq) {
			return jq.each(function() {
				setEnable(this, true);
			});
		},
		destroy : function(jq) {
			return jq.each(function() {
				var options = $(this).menubutton("options");
				if (options.menu) {
					$(options.menu).menu("destroy");
				}
				$(this).remove();
			});
		}
	};
	
	$.fn.menubutton.parseOptions = function(target) {
		return $.extend({}, $.fn.linkbutton.parseOptions(target), $.parser
				.parseOptions(target, [ "menu", {
					plain : "boolean",
					duration : "number"
				} ]));
	};
	
	$.fn.menubutton.defaults = $.extend({}, $.fn.linkbutton.defaults, {
		plain : true,
		menu : null,
		duration : 100
	});
})(jQuery);
