(function($) {
	function createSpinner(jq) {
		var spinner = $(
				"<span class=\"spinner\">" + "<span class=\"spinner-arrow\">"
						+ "<span class=\"spinner-arrow-up\"></span>"
						+ "<span class=\"spinner-arrow-down\"></span>"
						+ "</span>" + "</span>").insertAfter(jq);
		$(jq).addClass("spinner-text").prependTo(spinner);
		return spinner;
	};
	function resize(jq, width) {
		var options = $.data(jq, "spinner").options;
		var spinner = $.data(jq, "spinner").spinner;
		if (width) {
			options.width = width;
		}
		var temp = $("<div style=\"display:none\"></div>").insertBefore(spinner);
		spinner.appendTo("body");
		if (isNaN(options.width)) {
			options.width = $(jq).outerWidth();
		}
		spinner._outerWidth(options.width);
		$(jq)._outerWidth(spinner.width() - spinner.find(".spinner-arrow").outerWidth());
		spinner.insertAfter(temp);
		temp.remove();
	};
	function bindEvent(jq) {
		var options = $.data(jq, "spinner").options;
		var spinner = $.data(jq, "spinner").spinner;
		spinner.find(".spinner-arrow-up,.spinner-arrow-down").unbind(".spinner");
		if (!options.disabled) {
			spinner.find(".spinner-arrow-up").bind("mouseenter.spinner", function() {
				$(this).addClass("spinner-arrow-hover");
			}).bind("mouseleave.spinner", function() {
				$(this).removeClass("spinner-arrow-hover");
			}).bind("click.spinner", function() {
				options.spin.call(jq, false);
				options.onSpinUp.call(jq);
				spinner.find(".spinner-text").focus();
			});
			spinner.find(".spinner-arrow-down").bind("mouseenter.spinner",
					function() {
						$(this).addClass("spinner-arrow-hover");
					}).bind("mouseleave.spinner", function() {
				$(this).removeClass("spinner-arrow-hover");
			}).bind("click.spinner", function() {
				options.spin.call(jq, true);
				options.onSpinDown.call(jq);
				spinner.find(".spinner-text").focus();
			});
			spinner.find(".spinner-text").focus(function(){
				spinner.addClass("spinner_click");
			}).blur(function(){
				spinner.removeClass("spinner_click");
			}).hover(function() {
				spinner.addClass("spinner_hover");
			}, function() {
				spinner.removeClass("spinner_hover");
			});
		}
	};
	function disable(jq, disabled) {
		var options = $.data(jq, "spinner").options;
		if (disabled) {
			options.disabled = true;
			$(jq).attr("disabled", true);
		} else {
			options.disabled = false;
			$(jq).removeAttr("disabled");
		}
	};
	$.fn.spinner = function(options, param) {
		if (typeof options == "string") {
			var fn = $.fn.spinner.methods[options];
			if (fn) {
				return fn(this, param);
			}
		}
		options = options || {};
		return this.each(function() {
			var data = $.data(this, "spinner");
			if (data) {
				$.extend(data.options, options);
			} else {
				data = $.data(this, "spinner", {
					options : $.extend({}, $.fn.spinner.defaults, $.fn.spinner
							.parseOptions(this), options),
					spinner : createSpinner(this)
				});
				$(this).removeAttr("disabled");
			}
			$(this).val(data.options.value);
			$(this).attr("readonly", !data.options.editable);
			disable(this, data.options.disabled);
			resize(this);
			bindEvent(this);
		});
	};
	$.fn.spinner.methods = {
		options : function(jq) {
			var options = $.data(jq[0], "spinner").options;
			return $.extend(options, {
				value : jq.val()
			});
		},
		destroy : function(jq) {
			return jq.each(function() {
				var spinner = $.data(this, "spinner").spinner;
				spinner.remove();
			});
		},
		resize : function(jq, width) {
			return jq.each(function() {
				resize(this, width);
			});
		},
		enable : function(jq) {
			return jq.each(function() {
				disable(this, false);
				bindEvent(this);
			});
		},
		disable : function(jq) {
			return jq.each(function() {
				disable(this, true);
				bindEvent(this);
			});
		},
		getValue : function(jq) {
			return jq.val();
		},
		setValue : function(jq, value) {
			return jq.each(function() {
				var options = $.data(this, "spinner").options;
				options.value = value;
				$(this).val(value);
			});
		},
		clear : function(jq) {
			return jq.each(function() {
				var options = $.data(this, "spinner").options;
				options.value = "";
				$(this).val("");
			});
		}
	};
	$.fn.spinner.parseOptions = function(target) {
		var t = $(target);
		return $.extend({}, $.parser
				.parseOptions(target, [ "width", "min", "max", {
					increment : "number",
					editable : "boolean"
				} ]), {
			value : (t.val() || undefined),
			disabled : (t.attr("disabled") ? true : undefined)
		});
	};
	$.fn.spinner.defaults = $.extend({}, {
		width : "auto",
		value : "",
		min : null,
		max : null,
		increment : 1,
		editable : true,
		disabled : false,
		spin : function(_1d) {
		},
		onSpinUp : function() {
		},
		onSpinDown : function() {
		}
	});
})(jQuery);
