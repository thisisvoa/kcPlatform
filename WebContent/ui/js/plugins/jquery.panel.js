(function($) {
	function removeNode(target) {
		target.each(function() {
			$(this).remove();
			if ($.browser.msie) {
				this.outerHTML = "";
			}
		});
	};
	/**
	 * 设置宽度和高度
	 */
	function setSize(target, param) {
		var options = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		var header = panel.children("div.panel-header");
		var pbody = panel.children("div.panel-body");
		if (param) {
			if (param.width) {
				options.width = param.width;
			}
			if (param.height) {
				options.height = param.height;
			}
			if (param.left != null) {
				options.left = param.left;
			}
			if (param.top != null) {
				options.top = param.top;
			}
		}
		if (options.fit == true) {
			var p = panel.parent();
			p.addClass("panel-noscroll");
			if (p[0].tagName == "BODY") {
				$("html").addClass("panel-fit");
			}
			options.width = p.width();
			options.height = p.height();
		}
		panel.css({
			left : options.left,
			top : options.top
		});
		if (!isNaN(options.width)) {
			panel._outerWidth(options.width);
		} else {
			panel.width("auto");
		}
		header.add(pbody)._outerWidth(panel.width());
		if (!isNaN(options.height)) {
			panel._outerHeight(options.height);
			pbody._outerHeight(panel.height() - header._outerHeight());
		} else {
			pbody.height("auto");
		}
		panel.css("height", "");
		options.onResize.apply(target, [ options.width, options.height ]);
		pbody.children("div.layout,div.tabs-container,div.panel,div.accordion,div.toolbar").triggerHandler("_resize");
		pbody.find("table.grid").triggerHandler("_resize");//改变内部grid大小
	};
	
	function movePanel(target, opts) {
		var options = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		if (opts) {
			if (opts.left != null) {
				options.left = opts.left;
			}
			if (opts.top != null) {
				options.top = opts.top;
			}
		}
		panel.css({
			left : options.left,
			top : options.top
		});
		options.onMove.apply(target, [ options.left, options.top ]);
	};
	function wrapPanel(target) {
		$(target).addClass("panel-body");
		var panel = $("<div class=\"panel\"></div>").insertBefore(target);
		panel[0].appendChild(target);
		panel.bind("_resize", function() {
			var options = $.data(target, "panel").options;
			if (options.fit == true) {
				setSize(target);
			}
			return false;
		});
		return panel;
	};
	function addHeader(jq) {
		var options = $.data(jq, "panel").options;
		var panel = $.data(jq, "panel").panel;
		if (options.tools && typeof options.tools == "string") {
			panel.find(">div.panel-header>div.panel-tool .panel-tool-a")
					.appendTo(options.tools);
		}
		removeNode(panel.children("div.panel-header"));
		if (options.title && !options.noheader) {
			var header = $("<div class=\"panel-header\"><div class=\"panel-title\">"
							+ options.title + "</div></div>").prependTo(panel);
			if (options.iconCls) {
				header.find(".panel-title").addClass("panel-with-icon");
				$("<div class=\"panel-icon\"></div>").addClass(options.iconCls)
						.appendTo(header);
			}
			var panelTool = $("<div class=\"panel-tool\"></div>").appendTo(header);
			panelTool.bind("click", function(e) {
				e.stopPropagation();
			});
			if (options.tools) {
				if (typeof options.tools == "string") {
					$(options.tools).children()
							.each(function() {
										$(this).addClass(
												$(this).attr("iconCls"))
												.addClass("panel-tool-a")
												.appendTo(panelTool);
									});
				} else {
					for ( var i = 0; i < options.tools.length; i++) {
						var t = $("<a href=\"javascript:void(0)\"></a>")
								.addClass(options.tools[i].iconCls).appendTo(panelTool);
						if (options.tools[i].handler) {
							t.bind("click", eval(options.tools[i].handler));
						}
					}
				}
			}
			if (options.collapsible) {
				$("<a class=\"panel-tool-collapse\" href=\"javascript:void(0)\"></a>")
						.appendTo(panelTool).bind("click", function() {
							if (options.collapsed == true) {
								expandPanel(jq, true);
							} else {
								collapsePanel(jq, true);
							}
							return false;
						});
			}
			if (options.minimizable) {
				$("<a class=\"panel-tool-min\" href=\"javascript:void(0)\"></a>")
						.appendTo(panelTool).bind("click", function() {
							minimizePanel(jq);
							return false;
						});
			}
			if (options.maximizable) {
				$("<a class=\"panel-tool-max\" href=\"javascript:void(0)\"></a>")
						.appendTo(panelTool).bind("click", function() {
							if (options.maximized == true) {
								restorePanel(jq);
							} else {
								maximizePanel(jq);
							}
							return false;
						});
			}
			if (options.closable) {
				$("<a class=\"panel-tool-close\" href=\"javascript:void(0)\"></a>")
						.appendTo(panelTool).bind("click", function() {
							closePanel(jq);
							return false;
						});
			}
			panel.children("div.panel-body").removeClass("panel-body-noheader");
		} else {
			panel.children("div.panel-body").addClass("panel-body-noheader");
		}
	};
	function loadData(target) {
		var data = $.data(target, "panel");
		if (data.options.href && (!data.isLoaded || !data.options.cache)) {
			data.isLoaded = false;
			destory(target);
			var pbody = data.panel.find(">div.panel-body");
			if (data.options.loadingMessage) {
				pbody.html($("<div class=\"panel-loading\"></div>").html(
						data.options.loadingMessage));
			}
			$.ajax({
				url : data.options.href,
				cache : false,
				success : function(_1f) {
					pbody.html(data.options.extractor.call(target, _1f));
					if ($.parser) {
						$.parser.parse(pbody);
					}
					data.options.onLoad.apply(target, arguments);
					data.isLoaded = true;
				}
			});
		}
	};
	function destory(target) {
		var t = $(target);
		t.find(".combo-f").each(function() {
			$(this).combo("destroy");
		});
		t.find(".m-btn").each(function() {
			$(this).menubutton("destroy");
		});
		t.find(".s-btn").each(function() {
			$(this).splitbutton("destroy");
		});
	};
	
	function init(target) {
		$(target).find("div.panel:visible,div.accordion:visible,div.tabs-container:visible,div.layout:visible")
				.each(function() {
					$(this).triggerHandler("_resize", [ true ]);
				});
	};
	
	function openPanel(target, forceOpen) {
		var options = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		if (forceOpen != true) {
			if (options.onBeforeOpen.call(target) == false) {
				return;
			}
		}
		panel.show();
		options.closed = false;
		options.minimized = false;
		options.onOpen.call(target);
		if (options.maximized == true) {
			options.maximized = false;
			maximizePanel(target);
		}
		if (options.collapsed == true) {
			options.collapsed = false;
			collapsePanel(target);
		}
		if (!options.collapsed) {
			loadData(target);
			init(target);
		}
	};
	
	function closePanel(target, forceClose) {
		var options = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		if (forceClose != true) {
			if (options.onBeforeClose.call(target) == false) {
				return;
			}
		}
		panel.hide();
		options.closed = true;
		options.onClose.call(target);
	};
	
	function destroyPanel(target, forceDestroy) {
		var opts = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		if (forceDestroy != true) {
			if (opts.onBeforeDestroy.call(target) == false) {
				return;
			}
		}
		destory(target);
		removeNode(panel);
		opts.onDestroy.call(target);
	};
	
	function collapsePanel(target, animate) {
		var options = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		var pbody = panel.children("div.panel-body");
		var header = panel.children("div.panel-header")
				.find("a.panel-tool-collapse");
		if (options.collapsed == true) {
			return;
		}
		pbody.stop(true, true);
		if (options.onBeforeCollapse.call(target) == false) {
			return;
		}
		header.addClass("panel-tool-expand");
		if (animate == true) {
			pbody.slideUp("normal", function() {
				options.collapsed = true;
				options.onCollapse.call(target);
			});
		} else {
			pbody.hide();
			options.collapsed = true;
			options.onCollapse.call(target);
		}
	};
	
	function expandPanel(target, animate) {
		var options = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		var pbody = panel.children("div.panel-body");
		var tool = panel.children("div.panel-header")
				.find("a.panel-tool-collapse");
		if (options.collapsed == false) {
			return;
		}
		pbody.stop(true, true);
		if (options.onBeforeExpand.call(target) == false) {
			return;
		}
		tool.removeClass("panel-tool-expand");
		if (animate == true) {
			pbody.slideDown("normal", function() {
				options.collapsed = false;
				options.onExpand.call(target);
				loadData(target);
				init(target);
			});
		} else {
			pbody.show();
			options.collapsed = false;
			options.onExpand.call(target);
			loadData(target);
			init(target);
		}
	};
	function maximizePanel(target) {
		var opts = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		var tool = panel.children("div.panel-header").find("a.panel-tool-max");
		if (opts.maximized == true) {
			return;
		}
		tool.addClass("panel-tool-restore");
		if (!$.data(target, "panel").original) {
			$.data(target, "panel").original = {
				width : opts.width,
				height : opts.height,
				left : opts.left,
				top : opts.top,
				fit : opts.fit
			};
		}
		opts.left = 0;
		opts.top = 0;
		opts.fit = true;
		setSize(target);
		opts.minimized = false;
		opts.maximized = true;
		opts.onMaximize.call(target);
	};
	function minimizePanel(target) {
		var opts = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		panel.hide();
		opts.minimized = true;
		opts.maximized = false;
		opts.onMinimize.call(target);
	};
	function restorePanel(target) {
		var opts = $.data(target, "panel").options;
		var panle = $.data(target, "panel").panel;
		var tool = panle.children("div.panel-header").find("a.panel-tool-max");
		if (opts.maximized == false) {
			return;
		}
		panle.show();
		tool.removeClass("panel-tool-restore");
		var original = $.data(target, "panel").original;
		opts.width = original.width;
		opts.height = original.height;
		opts.left = original.left;
		opts.top = original.top;
		opts.fit = original.fit;
		setSize(target);
		opts.minimized = false;
		opts.maximized = false;
		$.data(target, "panel").original = null;
		opts.onRestore.call(target);
	};
	
	function renderBody(target) {
		var options = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		var header = $(target).panel("header");
		var pbody = $(target).panel("body");
		panel.css(options.style);
		panel.addClass(options.cls);
		if (options.border) {
			header.removeClass("panel-header-noborder");
			pbody.removeClass("panel-body-noborder");
		} else {
			header.addClass("panel-header-noborder");
			pbody.addClass("panel-body-noborder");
		}
		header.addClass(options.headerCls);
		pbody.addClass(options.bodyCls);
		if (options.id) {
			$(target).attr("id", options.id);
		} else {
			$(target).attr("id", "");
		}
	};
	function setTitle(target, title) {
		$.data(target, "panel").options.title = title;
		$(target).panel("header").find("div.panel-title").html(title);
	};
	var TO = false;
	var resized = true;
	$(window).unbind(".panel")
			.bind("resize.panel",
					function() {
						if (!resized) {
							return;
						}
						if (TO !== false) {
							clearTimeout(TO);
						}
						TO = setTimeout(
								function() {
									resized = false;
									var layout = $("body.layout");
									if (layout.length) {
										layout.layout("resize");
									} else {
										$("body").children("div.panel,div.accordion,div.tabs-container,div.layout")
												 .triggerHandler("_resize");
									}
									resized = true;
									TO = false;
								}, 50);
					});
	$.fn.panel = function(options, param) {
		if (typeof options == "string") {
			return $.fn.panel.methods[options](this, param);
		}
		options = options || {};
		return this.each(function() {
			var data = $.data(this, "panel");
			var opts;
			if (data) {
				opts = $.extend(data.options, options);
			} else {
				opts = $.extend({}, $.fn.panel.defaults, $.fn.panel
						.parseOptions(this), options);
				$(this).attr("title", "");
				data = $.data(this, "panel", {
					options : opts,
					panel : wrapPanel(this),
					isLoaded : false
				});
			}
			if (opts.content) {
				$(this).html(opts.content);
				if ($.parser) {
					$.parser.parse(this);
				}
			}
			addHeader(this);
			renderBody(this);
			if (opts.doSize == true) {
				data.panel.css("display", "block");
				setSize(this);
			}
			if (opts.closed == true || opts.minimized == true) {
				data.panel.hide();
			} else {
				openPanel(this);
			}
		});
	};
	$.fn.panel.methods = {
		options : function(jq) {
			return $.data(jq[0], "panel").options;
		},
		panel : function(jq) {
			return $.data(jq[0], "panel").panel;
		},
		header : function(jq) {
			return $.data(jq[0], "panel").panel.find(">div.panel-header");
		},
		body : function(jq) {
			return $.data(jq[0], "panel").panel.find(">div.panel-body");
		},
		setTitle : function(jq, title) {
			return jq.each(function() {
				setTitle(this, title);
			});
		},
		open : function(jq, param) {
			return jq.each(function() {
				openPanel(this, param);
			});
		},
		close : function(jq, param) {
			return jq.each(function() {
				closePanel(this, param);
			});
		},
		destroy : function(jq, param) {
			return jq.each(function() {
				destroyPanel(this, param);
			});
		},
		refresh : function(jq, url) {
			return jq.each(function() {
				$.data(this, "panel").isLoaded = false;
				if (url) {
					$.data(this, "panel").options.href = url;
				}
				loadData(this);
			});
		},
		resize : function(jq, param) {
			return jq.each(function() {
				setSize(this, param);
			});
		},
		move : function(jq, param) {
			return jq.each(function() {
				movePanel(this, param);
			});
		},
		maximize : function(jq) {
			return jq.each(function() {
				maximizePanel(this);
			});
		},
		minimize : function(jq) {
			return jq.each(function() {
				minimizePanel(this);
			});
		},
		restore : function(jq) {
			return jq.each(function() {
				restorePanel(this);
			});
		},
		collapse : function(jq, param) {
			return jq.each(function() {
				collapsePanel(this, param);
			});
		},
		expand : function(jq, param) {
			return jq.each(function() {
				expandPanel(this, param);
			});
		}
	};
	$.fn.panel.parseOptions = function(target) {
		var t = $(target);
		return $.extend({}, $.parser.parseOptions(target, [ "id", "width",
				"height", "left", "top", "title", "iconCls", "cls",
				"headerCls", "bodyCls", "tools", "href", {
					cache : "boolean",
					fit : "boolean",
					border : "boolean",
					noheader : "boolean"
				}, {
					collapsible : "boolean",
					minimizable : "boolean",
					maximizable : "boolean"
				}, {
					closable : "boolean",
					collapsed : "boolean",
					minimized : "boolean",
					maximized : "boolean",
					closed : "boolean"
				} ]), {
			loadingMessage : (t.attr("loadingMessage") != undefined ? t
					.attr("loadingMessage") : undefined)
		});
	};
	$.fn.panel.defaults = {
		id : null,
		title : null,
		iconCls : null,
		width : "auto",
		height : "auto",
		left : null,
		top : null,
		cls : null,
		headerCls : null,
		bodyCls : null,
		style : {},
		href : null,
		cache : true,
		fit : false,
		border : true,
		doSize : true,
		noheader : false,
		content : null,
		collapsible : false,
		minimizable : false,
		maximizable : false,
		closable : false,
		collapsed : false,
		minimized : false,
		maximized : false,
		closed : false,
		tools : null,
		href : null,
		loadingMessage : "Loading...",
		extractor : function(_67) {
			var _68 = /<body[^>]*>((.|[\n\r])*)<\/body>/im;
			var _69 = _68.exec(_67);
			if (_69) {
				return _69[1];
			} else {
				return _67;
			}
		},
		onLoad : function() {
		},
		onBeforeOpen : function() {
		},
		onOpen : function() {
		},
		onBeforeClose : function() {
		},
		onClose : function() {
		},
		onBeforeDestroy : function() {
		},
		onDestroy : function() {
		},
		onResize : function(_6a, _6b) {
		},
		onMove : function(_6c, top) {
		},
		onMaximize : function() {
		},
		onRestore : function() {
		},
		onMinimize : function() {
		},
		onBeforeCollapse : function() {
		},
		onBeforeExpand : function() {
		},
		onCollapse : function() {
		},
		onExpand : function() {
		}
	};
})(jQuery);
