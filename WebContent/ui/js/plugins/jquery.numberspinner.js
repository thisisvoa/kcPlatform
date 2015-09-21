(function($) {
	function create(jq) {
		var options = $.data(jq, "numberspinner").options;
		$(jq).spinner(options).numberbox(options);
	}
	;
	function fixValue(jq, reduce) {
		var options = $.data(jq, "numberspinner").options;
		var v = parseFloat($(jq).numberbox("getValue") || options.value) || 0;
		if (reduce == true) {
			v -= options.increment;
		} else {
			v += options.increment;
		}
		$(jq).numberbox("setValue", v);
	}
	;
	$.fn.numberspinner = function(options, param) {
		if (typeof options == "string") {
			var fn = $.fn.numberspinner.methods[options];
			if (fn) {
				return fn(this, options);
			} else {
				return this.spinner(options, options);
			}
		}
		options = options || {};
		return this.each(function() {
			var data = $.data(this, "numberspinner");
			if (data) {
				$.extend(data.options, options);
			} else {
				$.data(this, "numberspinner", {
					options : $.extend({}, $.fn.numberspinner.defaults,
							$.fn.numberspinner.parseOptions(this), options)
				});
			}
			create(this);
		});
	};
	$.fn.numberspinner.methods = {
		options : function(jq) {
			var options = $.data(jq[0], "numberspinner").options;
			return $.extend(options, {
				value : jq.numberbox("getValue")
			});
		},
		setValue : function(jq, value) {
			return jq.each(function() {
				$(this).numberbox("setValue", value);
			});
		},
		getValue : function(jq) {
			return jq.numberbox("getValue");
		},
		clear : function(jq) {
			return jq.each(function() {
				$(this).spinner("clear");
				$(this).numberbox("clear");
			});
		}
	};
	$.fn.numberspinner.parseOptions = function(target) {
		return $.extend({}, $.fn.spinner.parseOptions(target), $.fn.numberbox
				.parseOptions(target), {});
	};
	$.fn.numberspinner.defaults = $.extend({}, $.fn.spinner.defaults,
			$.fn.numberbox.defaults, {
				spin : function(_f) {
					fixValue(this, _f);
				}
			});
})(jQuery);
