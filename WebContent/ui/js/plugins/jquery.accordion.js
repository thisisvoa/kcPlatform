(function($) {
	function resize(jq) {
		var options = $.data(jq, "accordion").options;
		var panels = $.data(jq, "accordion").panels;
		var cc = $(jq);
		if (options.fit == true) {
			var p = cc.parent();
			p.addClass("panel-noscroll");
			if (p[0].tagName == "BODY") {
				$("html").addClass("panel-fit");
			}
			options.width = p.width();
			options.height = p.height();
		}
		if (options.width > 0) {
			cc._outerWidth(options.width);
		}
		var height = "auto";
		var headerHeight = "auto";
		if (options.height > 0) {
			cc._outerHeight(options.height);
			headerHeight = panels.length ? panels[0].panel("header").css("height", "")
					._outerHeight() : "auto";
			height = cc.height() - (panels.length - 1) * headerHeight;
		}
		for ( var i = 0; i < panels.length; i++) {
			var panel = panels[i];
			var header = panel.panel("header");
			header._outerHeight(headerHeight);
			panel.panel("resize", {
				width : cc.width(),
				height : height
			});
		}
	};
	
	function getSelected(jq) {
		var panels = $.data(jq, "accordion").panels;
		for ( var i = 0; i < panels.length; i++) {
			var panel = panels[i];
			if (panel.panel("options").collapsed == false) {
				return panel;
			}
		}
		return null;
	};
	
	function getPanelIndex(jq, target) {
		var panels = $.data(jq, "accordion").panels;
		for ( var i = 0; i < panels.length; i++) {
			if (panels[i][0] == $(target)[0]) {
				return i;
			}
		}
		return -1;
	};
	
	function getPanel(jq, title, splice) {
		var panels = $.data(jq, "accordion").panels;
		if (typeof title == "number") {
			if (title < 0 || title >= panels.length) {
				return null;
			} else {
				var panel = panels[title];
				if (splice) {
					panels.splice(title, 1);
				}
				return panel;
			}
		}
		for ( var i = 0; i < panels.length; i++) {
			var panel = panels[i];
			if (panel.panel("options").title == title) {
				if (splice) {
					panels.splice(i, 1);
				}
				return panel;
			}
		}
		return null;
	};
	
	function renderCls(jq) {
		var options = $.data(jq, "accordion").options;
		var cc = $(jq);
		if (options.border) {
			cc.removeClass("accordion-noborder");
		} else {
			cc.addClass("accordion-noborder");
		}
	};
	
	function _1a(jq) {
		var cc = $(jq);
		cc.addClass("accordion");
		var panels = [];
		cc.children("div").each(function() {
			var opt = $.extend({}, $.parser.parseOptions(this), {
				selected : ($(this).attr("selected") ? true : undefined)
			});
			var pp = $(this);
			panels.push(pp);
			bindEvent(jq, pp, opt);
		});
		cc.bind("_resize", function(e, param) {
			var options = $.data(jq, "accordion").options;
			if (options.fit == true || param) {
				resize(jq);
			}
			return false;
		});
		return {
			accordion : cc,
			panels : panels
		};
	}
	;
	function bindEvent(jq, pp, opts) {
		pp.panel($.extend({}, opts,
				{
					collapsible : false,
					minimizable : false,
					maximizable : false,
					closable : false,
					doSize : false,
					collapsed : true,
					headerCls : "accordion-header",
					bodyCls : "accordion-body",
					onBeforeExpand : function() {
						var selected = getSelected(jq);
						if (selected) {
							var header = $(selected).panel("header");
							header.removeClass("accordion-header-selected");
							header.find(".accordion-collapse").triggerHandler(
									"click");
						}
						var header = pp.panel("header");
						header.addClass("accordion-header-selected");
						header.find(".accordion-collapse").removeClass(
								"accordion-expand");
					},
					onExpand : function() {
						var options = $.data(jq, "accordion").options;
						options.onSelect.call(jq, pp.panel("options").title, getPanelIndex(
								jq, this));
					},
					onBeforeCollapse : function() {
						var header = pp.panel("header");
						header.removeClass("accordion-header-selected");
						header.find(".accordion-collapse").addClass(
								"accordion-expand");
					}
				}));
		var header = pp.panel("header");
		var t = $(
				"<a class=\"accordion-collapse accordion-expand\" href=\"javascript:void(0)\"></a>")
				.appendTo(header.children("div.panel-tool"));
		t.bind("click", function(e) {
			var animate = $.data(jq, "accordion").options.animate;
			stop(jq);
			if (pp.panel("options").collapsed) {
				pp.panel("expand", animate);
			} else {
				pp.panel("collapse", animate);
			}
			return false;
		});
		header.click(function() {
			$(this).find(".accordion-collapse").triggerHandler("click");
			return false;
		});
	};
	
	function select(jq, target) {
		var panel = getPanel(jq, target);
		if (!panel) {
			return;
		}
		var selected = getSelected(jq);
		if (selected && selected[0] == panel[0]) {
			return;
		}
		panel.panel("header").triggerHandler("click");
	};
	
	function selectDefault(jq) {
		var panels = $.data(jq, "accordion").panels;
		for ( var i = 0; i < panels.length; i++) {
			if (panels[i].panel("options").selected) {
				selectPanel(i);
				return;
			}
		}
		if (panels.length) {
			selectPanel(0);
		}
		function selectPanel(target) {
			var options = $.data(jq, "accordion").options;
			var animate = options.animate;
			options.animate = false;
			select(jq, target);
			options.animate = animate;
		};
	};

	function stop(jq) {
		var panels = $.data(jq, "accordion").panels;
		for ( var i = 0; i < panels.length; i++) {
			panels[i].stop(true, true);
		}
	};
	
	function add(jq, opt) {
		var options = $.data(jq, "accordion").options;
		var panels = $.data(jq, "accordion").panels;
		if (opt.selected == undefined) {
			opt.selected = true;
		}
		stop(jq);
		var pp = $("<div></div>").appendTo(jq);
		panels.push(pp);
		bindEvent(jq, pp, opt);
		resize(jq);
		options.onAdd.call(jq, opt.title, panels.length - 1);
		if (opt.selected) {
			select(jq, panels.length - 1);
		}
	};
	
	function remove(jq, target) {
		var options = $.data(jq, "accordion").options;
		var panels = $.data(jq, "accordion").panels;
		stop(jq);
		var panel = getPanel(jq, target);
		var title = panel.panel("options").title;
		var index = getPanelIndex(jq, panel);
		if (options.onBeforeRemove.call(jq, title, index) == false) {
			return;
		}
		var panel = getPanel(jq, target, true);
		if (panel) {
			panel.panel("destroy");
			if (panels.length) {
				resize(jq);
				var select = getSelected(jq);
				if (!select) {
					select(jq, 0);
				}
			}
		}
		options.onRemove.call(jq, title, index);
	};
	
	$.fn.accordion = function(options, param) {
		if (typeof options == "string") {
			return $.fn.accordion.methods[options](this, param);
		}
		options = options || {};
		return this.each(function() {
			var data = $.data(this, "accordion");
			var opts;
			if (data) {
				opts = $.extend(data.options, options);
				data.opts = opts;
			} else {
				opts = $.extend({}, $.fn.accordion.defaults, $.fn.accordion
						.parseOptions(this), options);
				var r = _1a(this);
				$.data(this, "accordion", {
					options : opts,
					accordion : r.accordion,
					panels : r.panels
				});
			}
			renderCls(this);
			resize(this);
			selectDefault(this);
		});
	};
	$.fn.accordion.methods = {
		options : function(jq) {
			return $.data(jq[0], "accordion").options;
		},
		panels : function(jq) {
			return $.data(jq[0], "accordion").panels;
		},
		resize : function(jq) {
			return jq.each(function() {
				resize(this);
			});
		},
		getSelected : function(jq) {
			return getSelected(jq[0]);
		},
		getPanel : function(jq, target) {
			return getPanel(jq[0], target);
		},
		getPanelIndex : function(jq, target) {
			return getPanelIndex(jq[0], target);
		},
		select : function(jq, target) {
			return jq.each(function() {
				select(this, target);
			});
		},
		add : function(jq, opt) {
			return jq.each(function() {
				add(this, opt);
			});
		},
		remove : function(jq, target) {
			return jq.each(function() {
				remove(this, target);
			});
		}
	};
	$.fn.accordion.parseOptions = function(target) {
		return $.extend({}, $.parser.parseOptions(target, [ "width", "height", {
			fit : "boolean",
			border : "boolean",
			animate : "boolean"
		} ]));
	};
	$.fn.accordion.defaults = {
		width : "auto",
		height : "auto",
		fit : false,
		border : true,
		animate : true,
		onSelect : function(_4f, _50) {
		},
		onAdd : function(_51, _52) {
		},
		onBeforeRemove : function(_53, _54) {
		},
		onRemove : function(_55, _56) {
		}
	};
})(jQuery);
