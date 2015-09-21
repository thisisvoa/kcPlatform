(function($) {
	function render(jq){
		var  options = $.data(jq, "button").options;
		$(jq).addClass("ui_button");
		$(jq).hover(function(){
			if(!options.disabled){
				$(jq).addClass("ui_button_hover");
			}
		},function(){
			$(jq).removeClass("ui_button_hover");
		});
		
		if(options.disabled){
			disable(jq);
		}
		if(options.hidden){
			hide(jq);
		}
	}
	
	function disable(jq){
		var  options = $.data(jq, "button").options;
		options.disabled = true;
		$(jq).attr("disabled",true);
	}
	
	function enable(jq){
		var  options = $.data(jq, "button").options;
		options.disabled = false;
		$(jq).removeAttr("disabled");
	}
	
	function show(jq){
		var  options = $.data(jq, "button").options;
		options.hidden = false;
		$(jq).show();
	}
	
	function hide(jq){
		var  options = $.data(jq, "button").options;
		options.hidden = true;
		$(jq).hide();
	}
	$.fn.button = function(options, method){
		if (typeof options == "string") {
			var fn = $.fn.button.methods[options];
			if (fn) {
				return fn(this, method);
			}
		}
		options = options || {};
		return this.each(function() {
			var data = $.data(this, "button");
			if (data) {
				$.extend(data.options, options);
			} else {
				data = $.data(this, "button", {
					options : $.extend( {},
							$.fn.button.defaults, 
							$.fn.button.parseOptions(this), options)
				});
				render(this);
			}
		});
	};
	
	$.fn.button.parseOptions = function(target) {
		var t = $(target);
		return $.extend({},{
			disabled : (t.attr("disabled") ? true : undefined),
			hidden : (t.attr("hidden") ? t.attr("hidden")=='hidden'||t.attr("hidden")=='true' : undefined)
		});
	};
	
	$.fn.button.methods = {
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
		}
	};
	
	$.fn.button.defaults = $.extend( {}, {
		disabled:false,//是否可用
		hidden : false//是否隐藏
	});
	
})(jQuery);