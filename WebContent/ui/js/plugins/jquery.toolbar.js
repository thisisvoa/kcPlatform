/********************************
 *
 * 工具栏组件
 * 
 **********************************/
(function($) {
	
	function create(jq){
		$(jq).addClass("toolbar");
		$(jq).children("a").not(".easyui-menubutton").each(function(){
			$(this).linkbutton({plain:true});
		});
		$(jq).bind("_resize", function() {
			if(!$(this).is(":hidden")){
				setSize(this);
			 }
		});
		setSize(jq);
	}
	
	function setSize(jq){
		var options = $.data(jq, "toolbar").options;
		if(options.fit==true){
			var parent = $(jq).parent();
			$(jq)._outerWidth(parent.width()-1);
		}
	}
	$.fn.toolbar = function(options, param) {
		if (typeof options == "string") {
			return $.fn.toolbar.methods[options](this, param);
		}
		options = options || {};
		return this.each(function() {
			var lb = $.data(this, "toolbar");
			if (lb) {
				$.extend(lb.options, options);
			} else {
				$.data(this, "toolbar", {
					options : $.extend( {}, $.fn.toolbar.defaults,
							$.fn.toolbar.parseOptions(this), options)
				});
				$(this).removeAttr("disabled");
			}
			create(this);
		});
	};
	
	/**
	 * 置为可用
	 */
	function enable(jq, id){
		if(id==undefined){
			$(jq).children().each(function(){
				$(this).linkbutton('enable');
			});
		}else{
			$(jq).find("a[id="+id+"]").linkbutton('enable');
		}
	}
	
	/**
	 * 置为不可用
	 */
	function disable(jq, id){
		if(id==undefined){
			$(jq).children().each(function(){
				$(this).linkbutton('disable');
			});
		}else{
			$(jq).find("a[id="+id+"]").linkbutton('disable');
		}
	}
	
	/**
	 * 隐藏
	 */
	function hide(jq, id){
		$(jq).find("a[id="+id+"]").hide();
	}
	
	/**
	 * 显示
	 */
	function show(jq, id){
		$(jq).find("a[id="+id+"]").show();
	}
	
	$.fn.toolbar.methods = {
		options : function(jq) {
			return $.data(jq[0], "toolbar").options;
		},
		enable : function(jq, id) {
			return jq.each(function() {
				enable(this, id);
			});
		},
		disable : function(jq, id) {
			return jq.each(function() {
				disable(this, id);
			});
		},
		hide : function(jq,id){
			return jq.each(function() {
				hide(this, id);
			});
		},
		show : function(jq,id){
			return jq.each(function() {
				show(this, id);
			});
		}
	};
	
	$.fn.toolbar.parseOptions = function(target) {
		var t = $(target);
		return {
			id : t.attr("id"),
			disabled : (t.attr("disabled") ? true : undefined),
			plain : (t.attr("plain") ? t.attr("plain") == "true" : undefined),
			text : $.trim(t.html()),
			iconCls : (t.attr("icon") || t.attr("iconCls")),
			fit:t.attr("fit")=='false'?false:undefined
		};
	};
	$.fn.toolbar.defaults = {
		id : null,
		disabled : false,
		text : "",
		iconCls : null,
		fit:true
	};
})(jQuery);