/**
 * 输入框组件
 * 
 */
(function($) {
	function renderTextarea(jq){
		var options = $.data(jq, "textarea").options;
		$(jq).addClass("textarea");
		
		function warp(){
			var warper = null;
			if(options.watermark!="" || options.maxNum>0){
				var html = "";
				html += "<span style=\"position:relative;display:block\">";
				if(options.watermark!=""){
					html += "<span class=\"watermark\">"+
								options.watermark+
							"</span>";
				}
				if(options.maxNum>0){
					html += "<div class=\"maxNum\">"+
						     "剩余字符"+options.maxNum+
						   "</div>";
				}
				html += "</span>";
				warper = ($(html)).insertAfter($(jq));
				$(jq).insertBefore(warper.children()[0]);
			}else{
				warper = $(jq);
			}
			return warper;
		}
		
		var warper = warp();
		$.data(jq, "textarea").warper = warper;
	};
	
	/**
	 * 绑定事件
	 */
	function bindEvents(jq){
		var options = $.data(jq, "textarea").options;
		var warper = $.data(jq, "textarea").warper;
		$(jq).hover(function() {
			$(this).addClass("textarea_hover");
		}, function() {
			$(this).removeClass("textarea_hover");
		}).focus(function() {
			$(this).addClass("textarea_click");
			warper.find(".watermark").hide();
		}).blur(function(){
			$(this).removeClass("textarea_click");
			if(options.watermark!=""){
				showOrHideWatermark();
			}
		}).ready(function(){
			if(options.watermark!=""){
				warper.find(".watermark").click(function(){
					$(jq).focus();
				});
				showOrHideWatermark();
			}
			if(options.maxNum>0){
				showOrHideMaxNum();
			}
		}).keyup(function(){
			if(options.maxNum>0){
				showOrHideMaxNum();
			}
		});
		
		function showOrHideWatermark(){
			if(options.watermark!="" && $(jq).val()==""){
				warper.find(".watermark").show();
			}else{
				warper.find(".watermark").hide();
			}
		}
		
		function showOrHideMaxNum(){
			var val = $(jq).val();
			if($(jq).val()==""){
				warper.find(".maxNum").hide();
			}else{
				var length = val.length;
				if(length>options.maxNum){
					val = val.substr(0,options.maxNum);
					$(jq).val(val);
				}
				warper.find(".maxNum").html("剩余字符"+(options.maxNum-val.length));
				warper.find(".maxNum").show();
			}
		}
	}
	
	$.fn.textarea = function(options, menthod) {
		if (typeof options == "string") {
			return $.fn.textinput.methods[options](this, method);
		}
		options = options || {};
		return this.each(function() {
			var data = $.data(this, "textarea");
			if (data) {
				$.extend(data.options, options);
			} else {
				data = $.data(this, "textarea", {
					options : $.extend( {}, 
							$.fn.textarea.defaults, 
							$.fn.textarea.parseOptions(this), options)
				});
			}
			renderTextarea(this);
			bindEvents(this);
		});
	};
	
	/**
	 * 方法注册
	 * @param {Object} jq
	 * @return {TypeName} 
	 */
	$.fn.textarea.methods = {
		
	};
	
	$.fn.textarea.parseOptions = function(target) {
		var t = $(target);
		return $.extend({}, {
			width : (parseInt(target.style.width) || undefined),
			type : (t.attr("type") || "text"),
			editable : (t.attr("editable") ? t.attr("editable") == "true" : undefined),
			disabled : (t.attr("disabled") ? true : undefined),
			watermark : t.attr("watermark"),
			maxNum : t.attr("maxNum"),
			value : (t.val() || undefined)
		});
	};
	
	$.fn.textarea.defaults = {
		width : "auto",
		editable : true,//可编辑
		disabled : false,//是否禁用
		watermark : "",
		maxNum : 0,
		value : ""
	};
})(jQuery);