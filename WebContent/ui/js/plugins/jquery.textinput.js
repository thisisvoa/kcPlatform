/**
 * 输入框组件
 * 
 */
(function($) {
	function renderInput(jq){
		var options = $.data(jq, "textinput").options;
		$(jq).addClass("textinput");
		
		function warp(){
			var warper = null;
			if(options.watermark!="" || options.clearable){
				var html = "<span style=\"position:relative;left:0;top:0;display:block\">";
						   
				if(options.watermark!=""){
					html += ("<span class=\"watermark\">"+
									options.watermark+
									"</span>");
				}
				
				if(options.clearable){
					html += ("<div class=\"text_clear_button\">" +
									"</div>");
				}
				
				html += "</span>";
				warper = ($(html)).insertAfter($(jq));
				$(jq).insertBefore(warper.children()[0]);
				warper.css("width",$(jq).outerWidth());
			}else{
				warper = $(jq);
			}
			return warper;
		}
		
		var warper = warp();
		
		$.data(jq, "textinput").warper = warper;
		
	};
	
	
	function bindEvents(jq){
		var options = $.data(jq, "textinput").options;
		var warper = $.data(jq, "textinput").warper;
		
		$(jq).hover(function() {
			$(this).addClass("textinput_hover");
		}, function() {
			$(this).removeClass("textinput_hover");
		}).focus(function() {
			$(this).addClass("textinput_click");
		}).blur(function(){
			$(this).removeClass("textinput_click");
			if(options.watermark!=""){
				showOrHideWatermark();
			}
		}).focus(function(){
			if(options.watermark!=""){
				warper.find(".watermark").hide();
			}
		}).ready(function(){
			if(options.watermark!=""){
				warper.find(".watermark").click(function(){
					$(jq).focus();
				});
				showOrHideWatermark();
			}
			
			if(options.clearable){
				warper.find(".text_clear_button").click(function(){
					$(jq).val("");
					showOrHideClearBtn();
					if(options.watermark!=""){
						showOrHideWatermark();
					}
				});
				showOrHideClearBtn();
			}
		}).keyup(function(){
			if(options.clearable){
				showOrHideClearBtn();
			}
		});
		
		
		function showOrHideClearBtn(){
			if($(jq).val()!="" && options.clearable){
				warper.find(".text_clear_button").css("display","inline-block");
			}else{
				warper.find(".text_clear_button").css("display","none");
			}
		}
		
		function showOrHideWatermark(){
			if($(jq).val()=="" && options.watermark!=""){
				warper.find(".watermark").css("display","block");
			}else{
				warper.find(".watermark").css("display","none");
			}
		}
		
	}
	
	$.fn.textinput = function(options, method) {
		if (typeof options == "string") {
			return $.fn.textinput.methods[options](this, method);
		}
		options = options || {};
		return this.each(function() {
			var data = $.data(this, "textinput");
			if (data) {
				$.extend(data.options, options);
			} else {
				data = $.data(this, "textinput", {
					options : $.extend( {}, 
							$.fn.textinput.defaults, 
							$.fn.textinput.parseOptions(this), options)
				});
			}
			renderInput(this);
			bindEvents(this);
		});
	};
	
	/**
	 * 方法注册
	 * @param {Object} jq
	 * @return {TypeName} 
	 */
	$.fn.textinput.methods = {
		
	};
	
	$.fn.textinput.parseOptions = function(target) {
		var t = $(target);
		return $.extend({},{
			width : (parseInt(target.style.width) || undefined),
			type : (t.attr("type") || "text"),
			editable : (t.attr("editable") ? t.attr("editable") == "true" : undefined),
			disabled : (t.attr("disabled") ? true : undefined),
			clearable: (t.attr("clearable")?t.attr("clearable")=="true":undefined),
			watermark : t.attr("watermark"),
			value : (t.val() || undefined)
		});
	};
	
	$.fn.textinput.defaults = {
		watermark:"",//是否带水印
		clearable:false,//是否可可清除内容
		type : "text",
		width : "auto",
		editable : true,//可编辑
		disabled : false,//是否禁用
		value : ""
	};
})(jQuery);