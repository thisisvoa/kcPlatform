(function($) {
	/**
	 * 渲染
	 */
	function renderBox(jq){
		var options = $.data(jq, "radiobox").options;
		var warpBox = $("<span></span>").insertBefore($(jq));
		warpBox.addClass("radiowarper");
		
		if(options.disabled){
			if($(jq).attr("checked")){
				warpBox.addClass("radiowarper-checked-disable");
			}else{
				warpBox.addClass("radiowarper-disable");
			}
		}else{
			if($(jq).attr("checked")){
				warpBox.addClass("radiowarper-checked");
			}
		}
		if(options.hidden){
			warpBox.hide();
		}
		$(jq).hide();
		if($(jq).attr("checked")){//保留初始值，供reset时候使用
			options.initChecked = true;
		}else{
			options.initChecked = false;
		}
		
		$(jq).addClass("ui-radiobox");
		$.data(jq, "radiobox").warpBox = warpBox;
	};
	
	/**
	 * 绑定事件
	 */
	function bindEvent(jq){
		var options = $.data(jq, "radiobox").options;
		var warpBox = $.data(jq, "radiobox").warpBox;
		$(jq).closest("form").bind("reset",function(){
			if(options.initChecked){
				check(jq);
			}else{
				unCheck(jq);
			}
		});
		$(warpBox).click(function(){
			if(!options.disabled){
				clearTempState();
				$(jq).attr("checked",true);
				var name = $(jq).attr("name");
				$.each($("input[name='"+name+"']"),function(){
					if(jq!=this){
						var wb = $.data(this, "radiobox").warpBox;
						wb.removeClass("radiowarper-checked");
						if($.data(this, "radiobox").options.disabled){
							wb.removeClass("radiowarper-checked-disable");
							wb.addClass("radiowarper-disable");
						}
					}
				});
				$(jq).attr("checked","true");
				warpBox.addClass("radiowarper-checked");
				$(jq).trigger("click");
			}
		}).hover(function(){
			if(!options.disabled){
				if($(jq).attr("checked")){
					warpBox.addClass("radiowarper-checked-hover");
				}else{
					warpBox.addClass("radiowarper-hover");
				}
			}
		},function(){
			warpBox.removeClass("radiowarper-hover");
			warpBox.removeClass("radiowarper-checked-hover");
		}).mousedown(function(){
			if(!options.disabled){
				if($(jq).attr("checked")){
					warpBox.addClass("radiowarper-checked-click");
				}else{
					warpBox.addClass("radiowarper-click");
				}
			}
		}).mouseup(function(){
			warpBox.removeClass("radiowarper-click");
			warpBox.removeClass("radiowarper-checked-click");
		});
		/**
		 * 清除临时的一些样式状态
		 */
		function clearTempState(){
			warpBox.removeClass("radiowarper-hover");
			warpBox.removeClass("radiowarper-checked-hover");
			warpBox.removeClass("radiowarper-click");
			warpBox.removeClass("radiowarper-checked-click");
		}
	};
	
	/**
	 * 不可用
	 */
	function disable(jq){
		var options = $.data(jq, "radiobox").options;
		var warpBox = $.data(jq, "radiobox").warpBox;
		options.disabled = true;
		warpBox.removeClass("radiowarper-checked");
		if($(jq).attr("checked")){
			warpBox.addClass("radiowarper-checked-disable");
		}else{
			warpBox.addClass("radiowarper-disable");
		}
	};
	
	/**
	 * 可用
	 */
	function enable(jq){
		var options = $.data(jq, "radiobox").options;
		var warpBox = $.data(jq, "radiobox").warpBox;
		options.disabled = false;
		warpBox.removeClass("radiowarper-disable");
		warpBox.removeClass("radiowarper-checked-disable");
		if($(jq).attr("checked")){
			warpBox.addClass("radiowarper-checked");
		}
	};
	
	/**
	 * 显示
	 */
	function show(jq){
		var options = $.data(jq, "radiobox").options;
		var warpBox = $.data(jq, "radiobox").warpBox;
		options.hidden = false;
		warpBox.show();
	};
	
	/**
	 * 隐藏
	 */
	function hide(jq){
		var options = $.data(jq, "radiobox").options;
		var warpBox = $.data(jq, "radiobox").warpBox;
		options.hidden = true;
		warpBox.hide();
	};
	
	/**
	 * 是否选中
	 */
	function isCheck(jq){
		return $(jq).attr("checked")?true:false;
	}
	
	/**
	 * 选中本选项
	 */
	function check(jq){
		var options = $.data(jq, "radiobox").options;
		var warpBox = $.data(jq, "radiobox").warpBox;
		$(jq).attr("checked",true);
		if(options.disabled){
			warpBox.addClass("radiowarper-checked-disable");
		}else{
			warpBox.addClass("radiowarper-checked");
		}
		var name = $(jq).attr("name");
		$.each($("input[name='"+name+"']"),function(){
			if(jq!=this){
				var wb = $.data(this, "radiobox").warpBox;
				wb.removeClass("radiowarper-checked");
				if($.data(this, "radiobox").options.disabled){
					wb.removeClass("radiowarper-checked-disable");
					wb.addClass("radiowarper-disable");
				}
				
			}
		});
	}
	
	function unCheck(jq){
		var options = $.data(jq, "radiobox").options;
		var warpBox = $.data(jq, "radiobox").warpBox;
		$(jq).removeAttr("checked");
		if(options.disabled){
			warpBox.removeClass("radiowarper-checked-disable");
			warpBox.addClass("radiowarper-disable");
		}else{
			warpBox.removeClass("radiowarper-checked");
		}
	}
	
	$.fn.radiobox = function(options, method){
		if (typeof options == "string") {
			var fn = $.fn.radiobox.methods[options];
			if (fn) {
				return fn(this, method);
			} 
		}
		options = options || {};
		return this.each(function() {
			var data = $.data(this, "radiobox");
			if (data) {
				$.extend(data.options, options);
			} else {
				data = $.data(this, "radiobox", {
					options : $.extend( {},
							$.fn.radiobox.defaults, 
							$.fn.radiobox.parseOptions(this), options)
				});
				renderBox(this);
				bindEvent(this);
			}
		});
	};
	
	$.fn.radiobox.parseOptions = function(target) {
		var t = $(target);
		return $.extend({},{
			disabled : (t.attr("disabled") ? true : undefined),
			hidden : (t.attr("hidden") ? t.attr("hidden")=='true' : undefined)
		});
	};
	
	$.fn.radiobox.methods = {
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
		uncheck:function(jq){
			$(jq).each(function(){
				uncheck(this);
			});
		}
	};
	
	$.fn.radiobox.defaults = $.extend( {},{
		disabled:false,//是否可用
		hidden : false,//是否隐藏
		onclick : function(){
			
		}
	});
	
})(jQuery);