(function($) {
	/**
	 * 渲染
	 */
	function render(jq){
		var options = $.data(jq, "datePicker").options;
		var datebox = renderCombo();
		options.id = "#"+$(jq).attr("id");
		$.data(jq, "datePicker").datebox = datebox;
		$(jq).addClass("ui-datePicker");//用于标识该组件
		function renderCombo() {
			var datebox = $(jq).wrap("<div class=\"dateInput\"><div>").parent().parent();
			($(jq).parent()).append($("<span class=\"combo-arrow-date\"></span>"));
			$(jq).addClass("datebox-text");
			datebox.css("width", $(jq).width()+18);
			if(options.hidden){
				datebox.css("display","none");
			};
			if(options.readonly==false){
				$(jq).attr("readonly", "readonly");
			}
			return datebox;
		};
	};
	
	function bindEvent(jq){
		var options = $.data(jq, "datePicker").options;
		var datebox = $.data(jq, "datePicker").datebox;
		var optPicked = options.onpicked;
		options.onpicked = function(){
			$(jq).trigger('change');
			if(optPicked!=null){
				optPicked();
			}
		};
		datebox.find(".combo-arrow").bind("click",function(){
			if(options.disabled){
				return ;
			}
			options.el = jq;
			WdatePicker(options);
			$(jq).focus();
		});
		datebox.hover(function() {
			if(!options.disabled){
				datebox.addClass("datebox_hover");
			}
		}, function() {
			if(!options.disabled){
				datebox.removeClass("datebox_hover");
			}
		});
		
		$(jq).click(function(){
			if(!options.disabled){
				options.el = jq;
				WdatePicker(options);
			}
		});
		datebox.find(".combo-arrow-date").click(function(e){
			e.target = jq;
			if(!options.disabled){
				options.el = jq;
				WdatePicker(options);
			}
			return false;
		});
		$(jq).focus(function(){
			datebox.addClass("datebox_click");
		}).blur(function(){
			datebox.removeClass("datebox_click");
		});
	}
	
	/**
	 * 显示
	 */
	function show(jq){
		var datebox = $.data(jq, "datePicker").datebox;
		datebox.show();
	}
	
	/**
	 * 隐藏
	 */
	function hide(jq){
		var datebox = $.data(jq, "datePicker").datebox;
		datebox.hide();
	}
	
	$.fn.datePicker = function(options, method) {
		if (typeof options == "string") {
			var fn = $.fn.datePicker.methods[options];
			if (fn) {
				return fn(this, method);
			}
		}
		options = options || {};
		return this.each(function() {
			var data = $.data(this, "datePicker");
			if (data) {
				$.extend(data.options, options);
			} else {
				$.data(this, "datePicker", {
					options : $.extend( {}, $.fn.datePicker.defaults, $.fn.datePicker
							.parseOptions(this), options)
				});
			}
			render(this);
			bindEvent(this);
		});
	};
	
	$.fn.datePicker.methods = {
		show:function(jq){
			jq.each(function(){
				show(this);
			});
		},
		hide:function(jq){
			jq.each(function(){
				hide(this);
			});
		}
	};
	
	$.fn.datePicker.parseOptions = function(target) {
		var t = $(target);
		return $.extend({},{
			dateFmt:t.attr("dateFmt"),
			minDate:t.attr("minDate"),
			maxDate:t.attr("maxDate"),
			startDate:t.attr("startDate"),
			doubleCalendar:t.attr("doubleCalendar")?t.attr("doubleCalendar")=='true':undefined,
			enableKeyboard:t.attr("enableKeyboard")?t.attr("enableKeyboard")=='true':undefined,
			enableInputMask:t.attr("enableInputMask")?t.attr("enableInputMask")=='true':undefined,
			autoUpdateOnChanged:t.attr("autoUpdateOnChanged")?t.attr("autoUpdateOnChanged")=='true':undefined,
			firstDayOfWeek:t.attr("firstDayOfWeek")?parseInt(t.attr("firstDayOfWeek")):undefined,
			isShowWeek:t.attr("isShowWeek")?t.attr("isShowWeek")=='true':undefined,
			isShowClear:t.attr("isShowClear")?t.attr("isShowClear")=='true':undefined,
			isShowToday:t.attr("isShowToday")?t.attr("isShowToday")=='true':undefined,
			readOnly:t.attr("readOnly")?t.attr("readOnly")=='true':undefined,
			disabledDays:t.attr("disabledDays")?eval(t.attr("disabledDays")):undefined,
			disabledDates:t.attr("disabledDates")?eval(t.attr("disabledDates")):undefined,
			onpicking:t.attr("onpicking")?eval(t.attr("onpicking")):undefined,
			onpicked:t.attr("onpicked")?eval(t.attr("onpicked")):undefined,
			onclearing:t.attr("onpicking")?eval(t.attr("onclearing")):undefined,
			oncleared:t.attr("oncleared")?eval(t.attr("oncleared")):undefined,
			opposite:t.attr("opposite")?t.attr("opposite")=='true':undefined,
			disabled:(t.attr("disabled") ? true : undefined),
			hidden:t.attr("hidden") ? t.attr("hidden")=='true' : undefined
		});
	};
	$.fn.datePicker.defaults = $.extend( {}, {
		dateFmt:'yyyy-MM-dd',//日期显示格式
		minDate:'1900-01-01 00:00:00',//最小日期
		maxDate:'2099-12-31 23:59:59',//最大日期
		startDate:'',//起始日期,既点击日期框时显示的起始日期,	为空时,使用今天作为起始日期(默认值),否则使用传入的日期作为起始日期(注意要与上面的real日期相匹配)
		doubleCalendar:false,//是否是双月模式,如果该属性为true,则弹出同时显示2个月的日期框
		enableKeyboard:true,//键盘控制开关
		enableInputMask:true,//文本框输入启用掩码开关
		autoUpdateOnChanged:null,//在修改年月日时分秒等元素时,自动更新到el,默认是关闭的(即:需要点击确定或点击日期才更新),为false时 不自动更新,	为true时 自动更新,为null时(默认值) 如果有日元素且不隐藏确定按钮时 为false,其他情况为true
		skin:"default",//样式皮肤
		firstDayOfWeek:0,//周的第一天 0表示星期日 1表示星期一
		isShowWeek:false,//是否显示周
		isShowClear:true,//是否显示清空按钮
		isShowToday:true,//是否显示今天按钮
		readOnly:false,//是否只读
		disabledDays:null,//可以使用此功能禁用周日至周六所对应的日期,0至6 分别代表 周日至周六
		disabledDates:null,//可以使用此功能禁用所指定的一个或多个日期
		opposite:false,//为true时,无效日期变成有效日期,该属性对无效天,特殊天不起作用
		onpicking:null,
		onpicked:null,
		onclearing:null,
		oncleared:null,
		disabled:false,
		hidden:false
	});
})(jQuery);