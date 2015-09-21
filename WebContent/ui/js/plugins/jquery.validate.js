(function($) {
	
	function init(jq){
		var form = $(jq);
		var options = $.data(jq, "validate").options;
		form.validationEngine(options);
	}
	
	/**
	 * 添加校验规则
	 */
	function addRule(jq,option){
		var options = $(jq).data('jqv');
		options.allrules[option.name] = option.rule;
	};
	/**
	 * 验证
	 */
	function validate(jq){
		return $(jq).validationEngine("validate");
	} 
	$.fn.validate = function(options, method){
		if (typeof options == "string") {
			var fn = $.fn.validate.methods[options];
			if(fn){
				return fn(this, method);
			}
		}
		options = options || {};
		return this.each(function() {
			var data = $.data(this, "validate");
			if (data) {
				$.extend(data.options, options);
			} else {
				data = $.data(this, "validate", {
					options : $.extend( {}, 
							$.fn.validate.defaults, 
							$.fn.validate.parseOptions(this), options)
				});
			}
			init(this);
		});
	};
	
	$.fn.validate.methods = {
		addRule:function(jq,option){
			jq.each(function(){
				addRule(this,option);
			});
		},
		validate:function(jq){
			return validate(jq[0]);
		},
		detach:function(jq){
			jq.validationEngine("detach");
		},
		closePrompt:function(jq, field){
			jq.validationEngine("closePrompt",field);
		},
		closeAllPrompt:function(jq,field){
			jq.validationEngine("closeAllPrompt");
		}
	};
	$.fn.validate.parseOptions = function(target){
		var t = $(target);
		return $.extend({}, {
			binded:t.attr("binded")?t.attr("binded")=='true':undefined,
			onValidationComplete:t.attr("onValidationComplete")?eval(t.attr("onValidationComplete")):undefined,
			ajaxFormValidation:t.attr("ajaxFormValidation")?t.attr("ajaxFormValidation")=='true':undefined,
			onAjaxFormComplete:t.attr("onAjaxFormComplete")?eval(t.attr("onAjaxFormComplete")):undefined,
			onBeforeAjaxFormValidation:t.attr("onBeforeAjaxFormValidation")?eval(t.attr("onBeforeAjaxFormValidation")):undefined,
			ajaxFormValidationURL:t.attr("ajaxFormValidationURL"),
			ajaxFormValidationMethod:t.attr("ajaxFormValidationMethod")
		});
	};
	
	$.fn.validate.defaults = {
		binded:true,//是否在控件验证事件触发的时候进行验证,若为false则只在form提交时候进行验证
		onValidationComplete:null,//表单校验完成回调
		ajaxFormValidation:false,//表单提交时候是否进行ajax校验
		onAjaxFormComplete:null,//ajax校验完成回调
		onBeforeAjaxFormValidation:null,//ajax表单校验
		ajaxFormValidationURL:null,
		ajaxFormValidationMethod:"post"
	};
	
})(jQuery);