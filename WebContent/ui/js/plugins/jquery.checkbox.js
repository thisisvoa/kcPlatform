(function($) {
	/**
	 * 渲染
	 */
	function renderBox(jq){
		var options = $.data(jq, "checkbox").options;
		var warpBox = $("<span></span>").insertBefore($(jq));
		warpBox.addClass("checkwarper");
		
		if(options.disabled){
			if($(jq).attr("checked")){
				warpBox.addClass("checkwarper-checked-disable");
			}else{
				warpBox.addClass("checkwarper-disable");
			}
		}else{
			if($(jq).attr("checked")){
				warpBox.addClass("checkwarper-checked");
			}
		}
		if(options.hidden){
			warpBox.hide();
		}
		$(jq).hide();
		if($(jq).attr("checked")){
			options.initChecked = true;
		}else{
			options.initChecked = false;
		}
		$(jq).addClass("ui-checkbox");
		$.data(jq, "checkbox").warpBox = warpBox;
	};
	
	/**
	 * 绑定事件
	 */
	function bindEvent(jq){
		var options = $.data(jq, "checkbox").options;
		var warpBox = $.data(jq, "checkbox").warpBox;
		
		$(jq).closest("form").bind("reset",function(){
			if(options.initChecked){
				check(jq);
			}else{
				unCheck(jq);
			}
		});
		
		$(warpBox).click(function(e){
			if(!options.disabled){
				clearTempState();//清除一些临时状态
				$(jq).trigger("click");
				
				if($(jq).attr("checked")){
					warpBox.addClass("checkwarper-checked");
				}else{
					warpBox.removeClass("checkwarper-checked");
				}
				
			}
		}).hover(function(){
			if(options.disabled==false){
				if($(jq).attr("checked")){
					warpBox.addClass("checkwarper-checked-hover");
				}else{
					warpBox.addClass("checkwarper-hover");
				}
			}
		},function(){
			if(options.disabled==false){
				warpBox.removeClass("checkwarper-checked-hover");
				warpBox.removeClass("checkwarper-hover");
			}
		}).mousedown(function(){
			if(options.disabled==false){
				if($(jq).attr("checked")){
					warpBox.addClass("checkwarper-checked-click");
				}else{
					warpBox.addClass("checkwarper-click");
				}
			}
		}).mouseup(function(){
			warpBox.removeClass("checkwarper-checked-click");
			warpBox.removeClass("checkwarper-click");
		});
		/**
		 * 清除临时的一些样式状态
		 */
		function clearTempState(){
			warpBox.removeClass("checkwarper-click");
			warpBox.removeClass("checkwarper-checked-click");
			warpBox.removeClass("checkwarper-hover");
			warpBox.removeClass("checkwarper-checked-hover");
		}
	};
	
	/**
	 * 不可用
	 */
	function disable(jq){
		var options = $.data(jq, "checkbox").options;
		var warpBox = $.data(jq, "checkbox").warpBox;
		options.disabled = true;
		warpBox.removeClass("checkwarper-checked");
		if($(jq).attr("checked")){
			warpBox.addClass("checkwarper-checked-disable");
		}else{
			warpBox.addClass("checkwarper-disable");
		}
	};
	
	/**
	 * 可用
	 */
	function enable(jq){
		var options = $.data(jq, "checkbox").options;
		var warpBox = $.data(jq, "checkbox").warpBox;
		options.disabled = false;
		warpBox.removeClass("checkwarper-disable");
		warpBox.removeClass("checkwarper-checked-disable");
		if($(jq).attr("checked")){
			warpBox.addClass("checkwarper-checked");
		}
	};
	
	/**
	 * 显示
	 */
	function show(jq){
		var options = $.data(jq, "checkbox").options;
		var warpBox = $.data(jq, "checkbox").warpBox;
		options.hidden = false;
		warpBox.show();
	};
	
	/**
	 * 隐藏
	 */
	function hide(jq){
		var options = $.data(jq, "checkbox").options;
		var warpBox = $.data(jq, "checkbox").warpBox;
		options.hidden = true;
		warpBox.hide();
	};
	
	/**
	 * 设置某个选项选中
	 */
	function check(jq){
		var options = $.data(jq, "checkbox").options;
		var warpBox = $.data(jq, "checkbox").warpBox;
		$(jq).attr("checked",true);
		if(options.disabled){
			warpBox.addClass("checkwarper-checked-disable");
		}else{
			warpBox.addClass("checkwarper-checked");
		}
	}
	
	/**
	 * 不选中
	 */
	function unCheck(jq){
		var options = $.data(jq, "checkbox").options;
		var warpBox = $.data(jq, "checkbox").warpBox;
		$(jq).removeAttr("checked");
		if(options.disabled){
			warpBox.removeClass("checkwarper-checked-disable");
			warpBox.addClass("checkwarper-disable");
		}else{
			warpBox.removeClass("checkwarper-checked");
		}
	}
	
	$.fn.checkbox = function(options, method){
		if (typeof options == "string") {
			var fn = $.fn.checkbox.methods[options];
			if (fn) {
				return fn(this, method);
			}
		}
		options = options || {};
		return this.each(function() {
			var data = $.data(this, "checkbox");
			if (data) {
				$.extend(data.options, options);
			} else {
				data = $.data(this, "checkbox", {
					options : $.extend( {},
							$.fn.checkbox.defaults, 
							$.fn.checkbox.parseOptions(this), options)
				});
				renderBox(this);
				bindEvent(this);
			}
		});
	};
	
	$.fn.checkbox.parseOptions = function(target) {
		var t = $(target);
		return $.extend({},{
			disabled : (t.attr("disabled") ? true : undefined),
			hidden : (t.attr("hidden") ? t.attr("hidden")=='true' : undefined)
		});
	};
	
	$.fn.checkbox.methods = {
		disable:function(jq){
			$(jq).each(function(){
				disable(this);
			});
		},
		enable:function(jq){
			$(jq).each(function(){
				enable(this);
			});
		},
		show:function(jq){
			$(jq).each(function(){
				show(this);
			});
		},
		hide:function(jq){
			$(jq).each(function(){
				hide(this);
			});
		},
		check:function(jq){
			$(jq).each(function(){
				check(this);
			});
		},
		unCheck:function(jq){
			$(jq).each(function(){
				unCheck(this);
			});
		}
	};
	
	$.fn.checkbox.defaults = $.extend( {},{
		disabled:false,//是否可用
		hidden : false,//是否隐藏
		onclick:function(){
			
		}
	});
	
})(jQuery);