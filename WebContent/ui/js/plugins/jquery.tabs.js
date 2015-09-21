/**
 * jQuery EasyUI 1.2.5
 * 
 * Licensed under the GPL terms To use it on other terms please contact us
 * 
 * Copyright(c) 2009-2011 stworthy [ stworthy@gmail.com ]
 * 
 */
(function($) {
	
	/**
	 * 获取最大滚动宽度
	 */
	function getMaxScrollWidth(jq) {
		var header = $(jq).children("div.tabs-header");
		var tabsWidth = 0;
		$("ul.tabs li", header).each(function() {
			tabsWidth += $(this).outerWidth(true);
		});
		var wrapWidth = header.children("div.tabs-wrap").width();
		var padding = parseInt(header.find("ul.tabs").css("padding-left"));
		return tabsWidth - wrapWidth + padding;
	};
	
	/**
	 * 添加滚动
	 */
	function setScrollers(jq) {
		var opts = $.data(jq, "tabs").options;
		var header = $(jq).children("div.tabs-header");
		var tool = header.children("div.tabs-tool");
		var leftScroller = header.children("div.tabs-scroller-left");
		var rightScroller = header.children("div.tabs-scroller-right");
		var wrap = header.children("div.tabs-wrap");
		var height = ($.boxModel == true ? (header.outerHeight() - (tool
				.outerHeight() - tool.height())) : header.outerHeight());
		if (opts.plain) {
			height -= 2;
		}
		tool.height(height);
		var fullWidth = 0;
		$("ul.tabs li", header).each(function() {
			fullWidth += $(this).outerWidth(true);
		});
		var realWidth = header.width() - tool.outerWidth();
		if (fullWidth > realWidth) {
			leftScroller.show();
			rightScroller.show();
			tool.css("right", rightScroller.outerWidth());
			wrap.css({
				marginLeft : leftScroller.outerWidth(),
				marginRight : rightScroller.outerWidth() + tool.outerWidth(),
				left : 0,
				width : realWidth - leftScroller.outerWidth()
						- rightScroller.outerWidth()
			});
		} else {
			leftScroller.hide();
			rightScroller.hide();
			tool.css("right", 0);
			wrap.css({
				marginLeft : 0,
				marginRight : tool.outerWidth(),
				left : 0,
				width : realWidth
			});
			wrap.scrollLeft(0);
		}
	};
	
	/**
	 * 创建Tabs上的按钮
	 */
	function buildButton(jq) {
		var opts = $.data(jq, "tabs").options;
		var header = $(jq).children("div.tabs-header");
		if (opts.tools) {
			if (typeof opts.tools == "string") {
				$(opts.tools).addClass("tabs-tool").appendTo(header);
				$(opts.tools).show();
			} else {
				header.children("div.tabs-tool").remove();
				var tool = $("<div class=\"tabs-tool\"></div>")
						.appendTo(header);
				for ( var i = 0; i < opts.tools.length; i++) {
					var button = $("<a href=\"javascript:void(0);\"></a>")
							.appendTo(tool);
					button[0].onclick = eval(opts.tools[i].handler
							|| function() {
							});
					button.linkbutton($.extend({}, opts.tools[i], {
						plain : true
					}));
				}
			}
		} else {
			header.children("div.tabs-tool").remove();
		}
	};
	
	/**
	 * 调整宽度
	 */
	function setSize(jq) {
		var opts = $.data(jq, "tabs").options;
		var cc = $(jq);
		if (opts.fit == true) {
			var p = cc.parent();
			opts.width = p.width();
			opts.height = p.height();
		}
		cc.width(opts.width).height(opts.height);
		var header = $(jq).children("div.tabs-header");
		if ($.boxModel == true) {
			header.width(opts.width - (header.outerWidth() - header.width()));
		} else {
			header.width(opts.width);
		}
		setScrollers(jq);
		var panels = $(jq).children("div.tabs-panels");
		var height = opts.height;
		if (!isNaN(height)) {
			if ($.boxModel == true) {
				var panelHeight = panels.outerHeight() - panels.height();
				panels
						.css("height",
								(height - header.outerHeight() - panelHeight)
										|| "auto");
			} else {
				panels.css("height", height - header.outerHeight());
			}
		} else {
			panels.height("auto");
		}
		var width = opts.width;
		if (!isNaN(width)) {
			if ($.boxModel == true) {
				panels.width(width - (panels.outerWidth() - panels.width()));
			} else {
				panels.width(width);
			}
		} else {
			panels.width("auto");
		}
	};
	
	/**
	 * 自适应内容宽度
	 */
	function fitContent(jq) {
		var opts = $.data(jq, "tabs").options;
		var tab = getSelected(jq);
		if (tab) {
			var panels = $(jq).children("div.tabs-panels");
			var width = opts.width == "auto" ? "auto" : panels.width();
			var height = opts.height == "auto" ? "auto" : panels.height();
			tab.panel("resize", {
				width : width,
				height : height
			});
		}
	};
	
	/**
	 * 渲染标签
	 */
	function wrapTabs(jq) {
		var cc = $(jq);
		cc.addClass("tabs-container");
		cc.wrapInner("<div class=\"tabs-panels\"/>");
		$(
				"<div class=\"tabs-header\">"
						+ "<div class=\"tabs-scroller-left\"></div>"
						+ "<div class=\"tabs-scroller-right\"></div>"
						+ "<div class=\"tabs-wrap\">"
						+ "<ul class=\"tabs\"></ul>" + "</div>" + "</div>")
				.prependTo(jq);
		var tabs = [];
		var tp = cc.children("div.tabs-panels");
		tp.children("div[selected]").attr("toselect", "true");
		tp.children("div").each(function() {
			var pp = $(this);
			tabs.push(pp);
			createTab(jq, pp, parseTabOptions(this));
		});
		cc.children("div.tabs-header").find(
				".tabs-scroller-left, .tabs-scroller-right").hover(function() {
			$(this).addClass("tabs-scroller-over");
		}, function() {
			$(this).removeClass("tabs-scroller-over");
		});
		cc.bind("_resize", function(e, param) {
			var opts = $.data(jq, "tabs").options;
			if (opts.fit == true || param) {
				setSize(jq);
				fitContent(jq);
			}
			return false;
		});
		return tabs;
	};
	
	/**
	 * 设置各类属性
	 */
	function setProperties(jq) {
		var opts = $.data(jq, "tabs").options;
		var header = $(jq).children("div.tabs-header");
		var panel = $(jq).children("div.tabs-panels");
		if (opts.plain == true) {
			header.addClass("tabs-header-plain");
		} else {
			header.removeClass("tabs-header-plain");
		}
		if (opts.border == true) {
			header.removeClass("tabs-header-noborder");
			panel.removeClass("tabs-panels-noborder");
		} else {
			header.addClass("tabs-header-noborder");
			panel.addClass("tabs-panels-noborder");
		}
		$(".tabs-scroller-left", header).unbind(".tabs").bind("click.tabs",
				function() {
					var header = $(".tabs-wrap", header);
					var pos = header.scrollLeft() - opts.scrollIncrement;
					header.animate({
						scrollLeft : pos
					}, opts.scrollDuration);
				});
		$(".tabs-scroller-right", header).unbind(".tabs").bind(
				"click.tabs",
				function() {
					var header = $(".tabs-wrap", header);
					var pos = Math.min(header.scrollLeft()
							+ opts.scrollIncrement, getMaxScrollWidth(jq));
					header.animate({
						scrollLeft : pos
					}, opts.scrollDuration);
				});
		var tabs = $.data(jq, "tabs").tabs;
		for ( var i = 0, len = tabs.length; i < len; i++) {
			var tabPanel = tabs[i];
			var tab = tabPanel.panel("options").tab;
			tab.unbind(".tabs").bind("click.tabs", {
				p : tabPanel
			}, function(e) {
				var enable = e.data.p.panel("options").enable;
				if(enable!=false){
					selectTab(jq, getTabIndex(jq, e.data.p));
				}
				
			}).bind(
					"contextmenu.tabs",
					{
						p : tabPanel
					},
					function(e) {
						opts.onContextMenu.call(jq, e, e.data.p
								.panel("options").title);
					});
			tab.find("a.tabs-close").unbind(".tabs").bind("click.tabs", {
				p : tabPanel
			}, function(e) {
				var enable = e.data.p.panel("options").enable;
				if(enable!=false){
					closeTab(jq, getTabIndex(jq, e.data.p));
				}
				
				return false;
			});
		}
	};
	
	/**
	 * 创建标签
	 */
	function createTab(container, pp, options) {
		options = options || {};
		pp.panel($.extend({}, options, {
			border : false,
			noheader : true,
			closed : true,
			doSize : false,
			isSelected:false,
			iconCls : (options.icon ? options.icon : undefined),
			onLoad : function() {
				if (options.onLoad) {
					options.onLoad.call(this, arguments);
				}
				$.data(container, "tabs").options.onLoad.call(container, pp);
			}
		}));
		var opts = pp.panel("options");
		var header = $(container).children("div.tabs-header");
		var tabs = $("ul.tabs", header);
		var tab = $("<li></li>").appendTo(tabs);
		var tabInner = $(
				"<a href=\"javascript:void(0)\" class=\"tabs-inner\"></a>")
				.appendTo(tab);
		var tabLeft = $(
				"<span class=\"tab-inner-left\"></span>")
				.appendTo(tabInner);
		var tabCenter = $(
				"<span class=\"tab-inner-center\"></span>")
				.appendTo(tabLeft);
		
		var tabTitle = $("<span class=\"tabs-title\"></span>").html(opts.title)
				.appendTo(tabCenter);
		var tabIcon = $("<span class=\"tabs-icon\"></span>").appendTo(tabInner);
		if (opts.closable) {
			tabTitle.addClass("tabs-closable");
			$("<a href=\"javascript:void(0)\" class=\"tabs-close\"></a>")
					.appendTo(tab);
		}
		if (opts.iconCls) {
			tabTitle.addClass("tabs-with-icon");
			tabIcon.addClass(opts.iconCls);
		}
		if (opts.tools) {
			var tool = $("<span class=\"tabs-p-tool\"></span>").insertAfter(
					tabInner);
			if (typeof opts.tools == "string") {
				$(opts.tools).children().appendTo(tool);
			} else {
				for ( var i = 0; i < opts.tools.length; i++) {
					var t = $("<a href=\"javascript:void(0)\"></a>").appendTo(
							tool);
					t.addClass(opts.tools[i].iconCls);
					if (opts.tools[i].handler) {
						t.bind("click", eval(opts.tools[i].handler));
					}
				}
			}
			var pr = tool.children().length * 12;
			if (opts.closable) {
				pr += 8;
			} else {
				pr -= 3;
				tool.css("right", "5px");
			}
			tabTitle.css("padding-right", pr + "px");
		}
		
		//tab不可用
		if(opts.enable == false){
			tab.addClass("tabs-disabled");
		}
		opts.tab = tab;
	};
	
	/**
	 * 添加标签接口
	 */
	function addTab(jq, options) {
		var opts = $.data(jq, "tabs").options;
		var tabs = $.data(jq, "tabs").tabs;
		var pp = $("<div></div>").appendTo($(jq).children("div.tabs-panels"));
		tabs.push(pp);
		createTab(jq, pp, options);
		opts.onAdd.call(jq, options.id);
		setScrollers(jq);
		setProperties(jq);
		selectTab(jq, tabs.length - 1);
	};
	
	/**
	 * 更新标签
	 */
	function update(jq, param) {
		var selectHis = $.data(jq, "tabs").selectHis;
		var pp = param.tab;
		var title = pp.panel("options").title;
		pp.panel($.extend({}, param.options, {
			iconCls : (param.options.icon ? param.options.icon : undefined)
		}));
		var opts = pp.panel("options");
		var tab = opts.tab;
		tab.find("span.tabs-icon").attr("class", "tabs-icon");
		tab.find("a.tabs-close").remove();
		tab.find("span.tabs-title").html(opts.title);
		tab.removeClass("tabs-disabled");
		
		if (opts.closable) {
			tab.find("span.tabs-title").addClass("tabs-closable");
			$("<a href=\"javascript:void(0)\" class=\"tabs-close\"></a>")
					.appendTo(tab);
		} else {
			tab.find("span.tabs-title").removeClass("tabs-closable");
		}
		if (opts.iconCls) {
			tab.find("span.tabs-title").addClass("tabs-with-icon");
			tab.find("span.tabs-icon").addClass(opts.iconCls);
		} else {
			tab.find("span.tabs-title").removeClass("tabs-with-icon");
		}
		if(opts.enable == false){
			tab.addClass("tabs-disabled");
		}
		
		if (title != opts.title) {
			for ( var i = 0; i < selectHis.length; i++) {
				if (selectHis[i] == title) {
					selectHis[i] = opts.title;
				}
			}
		}
		setProperties(jq);
		$.data(jq, "tabs").options.onUpdate.call(jq, opts.id);
	};
	
	
	/**
	 * 设置某个Tab不可用
	 */
	function disableTab(container, tab){
		var tb = null;
		if(typeof tab == 'string' || typeof tab == "number" ){
			tb = getTab(container, tab);
		}else{
			tb = tab;
		}
		if(tb==null){
			return;
		}
		var opts = tb.panel("options");
		opts.enable = false;
		var t = opts.tab;
		t.addClass("tabs-disabled");
	};
	
	/**
	 * 设置某个Tab可用
	 */
	function enableTab(container, tab){
		var tb = null;
		if(typeof tab == 'string' || typeof tab == "number" ){
			tb = getTab(container, tab);
		}else{
			tb = tab;
		}
		if(tb==null){
			return;
		}
		var opts = tb.panel("options");
		opts.enable = true;
		var t = opts.tab;
		t.removeClass("tabs-disabled");
	};
	
	/**
	 * 关闭Tab
	 */
	function closeTab(container, tabId) {
		var opts = $.data(container, "tabs").options;
		var tabs = $.data(container, "tabs").tabs;
		var selectHis = $.data(container, "tabs").selectHis;
		if (!exists(container, tabId)) {
			return;
		}
		var tab = getTab(container, tabId);
		var title = tab.panel("options").title;
		var id = tab.panel("options").id;
		if (opts.onBeforeClose.call(container, id) == false) {
			return;
		}
		var tab = getTab(container, tabId, true);
		var tId = tab.panel("options").id;
		tab.panel("options").tab.remove();
		tab.panel("destroy");
		opts.onClose.call(container, tId);
		setScrollers(container);
		
		
		for ( var i = 0; i < selectHis.length; i++) {
			if (selectHis[i] == tId) {
				selectHis.splice(i, 1);
				i--;
			}
		}
		var lastTab = selectHis.pop();
		if (lastTab && exists(container,lastTab)) {
			selectTab(container, lastTab);
		} else {
			if (tabs.length) {
				selectTab(container, 0);
			}
		}
	};

	/**
	 * 获取tab
	 */
	function getTab(container, tabId, close) {
		var tabs = $.data(container, "tabs").tabs;
		if (typeof tabId == "number") {
			if (tabId < 0 || tabId >= tabs.length) {
				return null;
			} else {
				var tab = tabs[tabId];
				if (close) {
					tabs.splice(tabId, 1);
				}
				return tab;
			}
		}
		for ( var i = 0; i < tabs.length; i++) {
			var tab = tabs[i];
			if (tab.panel("options").id == tabId) {
				if (close) {
					tabs.splice(i, 1);
				}
				return tab;
			}
		}
		return null;
	};
	
	/**
	 * 获取tab的序号
	 */
	function getTabIndex(jq, tab) {
		var tabs = $.data(jq, "tabs").tabs;
		for ( var i = 0; i < tabs.length; i++) {
			if (tabs[i][0] == $(tab)[0]) {
				return i;
			}
		}
		return -1;
	};
	
	/**
	 * 获取当前选中的tab
	 */
	function getSelected(jq) {
		var tabs = $.data(jq, "tabs").tabs;
		for ( var i = 0; i < tabs.length; i++) {
			var tab = tabs[i];
			if (tab.panel("options").closed == false) {
				return tab;
			}
		}
		return null;
	};
	
	/**
	 * 初始化选中的tab
	 */
	function initSelectTab(jq) {
		var tabs = $.data(jq, "tabs").tabs;
		for ( var i = 0; i < tabs.length; i++) {
			if (tabs[i].attr("toselect") == "true") {
				selectTab(jq, i);
				return;
			}
		}
		if (tabs.length) {
			selectTab(jq, 0);
		}
	};
	
	/**
	 * 选中某个Tab
	 */
	function selectTab(jq, tabId) {
		var opts = $.data(jq, "tabs").options;
		var tabs = $.data(jq, "tabs").tabs;
		var selectHis = $.data(jq, "tabs").selectHis;
		if (tabs.length == 0) {
			return;
		}
		
		var tab = getTab(jq, tabId);
		if (!tab) {
			return;
		}
		var tId = tab.panel("options").id;
		var url = tab.panel("options").url;
		var isSelected = tab.panel("options").isSelected;
		var selected = getSelected(jq);
		if (selected) {
			selected.panel("close");
			selected.panel("options").tab.removeClass("tabs-selected");
		}
		tab.panel("open");
		selectHis.push(tab.panel("options").id);
		var tabHead = tab.panel("options").tab;
		tabHead.addClass("tabs-selected");
		var wrap = $(jq).find(">div.tabs-header div.tabs-wrap");
		var leftPos = tabHead.position().left + wrap.scrollLeft();
		var left = leftPos - wrap.scrollLeft();
		var right = left + tabHead.outerWidth();
		if (left < 0 || right > wrap.innerWidth()) {
			var pos = Math.min(leftPos - (wrap.width() - tabHead.width()) / 2,
					getMaxScrollWidth(jq));
			wrap.animate({
				scrollLeft : pos
			}, opts.scrollDuration);
		} else {
			var pos = Math.min(wrap.scrollLeft(), getMaxScrollWidth(jq));
			wrap.animate({
				scrollLeft : pos
			}, opts.scrollDuration);
		}
		if(url && isSelected==false){
			var iframe = $("<iframe id=\""+tId+"_frm\" name=\""+tId+"_frm\" style=\"width:100%;height:100%;\" "+
				"src=\"\" frameBorder=0 style=\"padding:0px;margin:0px\" ></iframe>").appendTo(tab.panel("body"));
			iframe.attr("src",url);
		}
		tab.panel("options").isSelected = true;
		fitContent(jq);
		opts.onSelect.call(jq, tId);
		
	};
	/**
	 * 刷新Tab
	 * @param opt:两个属性 id:刷新的tab的Id,url:为新的url 若为空则刷新旧url
	 */
	function refreshTab(jq, opt){
		var tab = getTab(jq, opt.id);
		if (!tab) {
			return;
		}
		var tId = tab.panel("options").id;
		var url = tab.panel("options").url;
		var newUrl = opt.url;
		if(newUrl){
			if(tab.panel("options").closed){
				tab.panel("options").url = newUrl;
				tab.panel("options").isSelected =false;
			}else{
				tab.find("#"+tId+"_frm").attr("src", newUrl);
				tab.panel("options").url = newUrl;
			}
		}else{
			if(tab.panel("options").closed){
				tab.panel("options").isSelected =false;
			}else{
				tab.find("#"+tId+"_frm").attr("src", url);
			}
		}
	}
	/**
	 * 是否存在某个tab
	 */
	function exists(container, tabId) {
		return getTab(container, tabId) != null;
	};
	
	/**
	 * 翻译单个Tab上设置的属性
	 */
	function parseTabOptions(target){
		var t = $(target);
		return {
			id : t.attr("id"),
			enable : (t.attr("enable")=='false' ? false:true),
			url : (t.attr("url"))
		};
	};
	/**
	 * 渲染tab
	 */
	$.fn.tabs = function(options, param) {
		if (typeof options == "string") {
			return $.fn.tabs.methods[options](this, param);
		}
		options = options || {};
		return this.each(function() {
			var state = $.data(this, "tabs");
			var opts;
			if (state) {
				opts = $.extend(state.options, options);
				state.options = opts;
			} else {
				$.data(this, "tabs", {
					options : $.extend({}, $.fn.tabs.defaults, $.fn.tabs
							.parseOptions(this), options),
					tabs : wrapTabs(this),
					selectHis : []
				});
			}
			buildButton(this);
			setProperties(this);
			setSize(this);
			initSelectTab(this);
		});
	};
	
	/**
	 * 提供的api方法
	 */
	$.fn.tabs.methods = {
		options : function(jq) {
			return $.data(jq[0], "tabs").options;
		},
		tabs : function(jq) {
			return $.data(jq[0], "tabs").tabs;
		},
		resize : function(jq) {
			return jq.each(function() {
				setSize(this);
				fitContent(this);
			});
		},
		refresh:function(jq,opt){
			return jq.each(function() {
				refreshTab(this,opt);
			});
		},
		add : function(jq, options) {
			return jq.each(function() {
				addTab(this, options);
			});
		},
		close : function(jq, tabId) {
			return jq.each(function() {
				closeTab(this, tabId);
			});
		},
		getTab : function(jq, tabId) {
			return getTab(jq[0], tabId);
		},
		getTabIndex : function(jq, tab) {
			return getTabIndex(jq[0], tab);
		},
		getSelected : function(jq) {
			return getSelected(jq[0]);
		},
		select : function(jq, tabId) {
			return jq.each(function() {
				selectTab(this, tabId);
			});
		},
		exists : function(jq, tabId) {
			return exists(jq[0], tabId);
		},
		update : function(jq, param) {
			return jq.each(function() {
				update(this, param);
			});
		},
		disableTab : function(jq, tab){
			return disableTab(jq[0], tab);
		},
		
		enableTab : function(jq, tab){
			return enableTab(jq[0], tab);
		}
	};
	
	$.fn.tabs.parseOptions = function(target) {
		var t = $(target);
		return {
			width : (parseInt(target.style.width) || undefined),
			height : (parseInt(target.style.height) || undefined),
			fit : (t.attr("fit") ? t.attr("fit") == "true" : undefined),
			border : (t.attr("border") ? t.attr("border") == "true" : undefined),
			plain : (t.attr("plain") ? t.attr("plain") == "true" : undefined),
			tools : t.attr("tools"),
			onLoad:t.attr("onLoad")?eval(t.attr("onLoad")):undefined,
			onSelect:t.attr("onSelect")?eval(t.attr("onSelect")):undefined,
			onBeforeClose:t.attr("onBeforeClose")?eval(t.attr("onBeforeClose")):undefined,
			onClose:t.attr("onClose")?eval(t.attr("onClose")):undefined,
			onAdd:t.attr("onAdd")?eval(t.attr("onAdd")):undefined,
			onUpdate:t.attr("onUpdate")?eval(t.attr("onUpdate")):undefined,
			onContextMenu:t.attr("onContextMenu")?eval(t.attr("onContextMenu")):undefined
		};
	};
	
	
	$.fn.tabs.defaults = {
		width : "auto",
		height : "auto",
		plain : false,
		fit : false,
		border : true,
		tools : null,
		scrollIncrement : 100,
		scrollDuration : 400,
		onLoad : function(panel) {
		},
		onSelect : function(id) {
		},
		onBeforeClose : function(id) {
		},
		onClose : function(id) {
		},
		onAdd : function(id) {
		},
		onUpdate : function(id) {
		},
		onContextMenu : function(e, title) {
		}
	};
})(jQuery);
