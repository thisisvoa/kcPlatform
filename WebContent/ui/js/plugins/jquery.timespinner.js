(function($) {
	function create(jq) {
		var options = $.data(jq, "timespinner").options;
		$(jq).spinner(options);
		$(jq).unbind(".timespinner");
		$(jq).bind("click.timespinner", function() {
			var start = 0;
			if (this.selectionStart != null) {
				start = this.selectionStart;
			} else {
				if (this.createTextRange) {
					var textRange = jq.createTextRange();
					var s = document.selection.createRange();
					s.setEndPoint("StartToStart", textRange);
					start = s.text.length;
				}
			}
			if (start >= 0 && start <= 2) {
				options.highlight = 0;
			} else {
				if (start >= 3 && start <= 5) {
					options.highlight = 1;
				} else {
					if (start >= 6 && start <= 8) {
						options.highlight = 2;
					}
				}
			}
			initHighlight(jq);
		}).bind("blur.timespinner", function() {
			formatValue(jq);
		});
	}
	;
	function initHighlight(jq) {
		var options = $.data(jq, "timespinner").options;
		var start = 0, end = 0;
		if (options.highlight == 0) {
			start = 0;
			end = 2;
		} else {
			if (options.highlight == 1) {
				start = 3;
				end = 5;
			} else {
				if (options.highlight == 2) {
					start = 6;
					end = 8;
				}
			}
		}
		if (jq.selectionStart != null) {
			jq.setSelectionRange(start, end);
		} else {
			if (jq.createTextRange) {
				var textRange = jq.createTextRange();
				textRange.collapse();
				textRange.moveEnd("character", end);
				textRange.moveStart("character", start);
				textRange.select();
			}
		}
		$(jq).focus();
	}
	;
	function parseDate(jq, str) {
		var options = $.data(jq, "timespinner").options;
		if (!str) {
			return null;
		}
		var vv = str.split(options.separator);
		for ( var i = 0; i < vv.length; i++) {
			if (isNaN(vv[i])) {
				return null;
			}
		}
		while (vv.length < 3) {
			vv.push(0);
		}
		return new Date(1900, 0, 0, vv[0], vv[1], vv[2]);
	}
	;
	function formatValue(jq) {
		var options = $.data(jq, "timespinner").options;
		var val = $(jq).val();
		var date = parseDate(jq, val);
		if (!date) {
			date = parseDate(jq, options.value);
		}
		if (!date) {
			options.value = "";
			$(jq).val("");
			return;
		}
		var minDate = parseDate(jq, options.min);
		var maxDate = parseDate(jq, options.max);
		if (minDate && minDate > date) {
			date = minDate;
		}
		if (maxDate && maxDate < date) {
			date = maxDate;
		}
		var tt = [ formatNum(date.getHours()), formatNum(date.getMinutes()) ];
		if (options.showSeconds) {
			tt.push(formatNum(date.getSeconds()));
		}
		var val = tt.join(options.separator);
		options.value = val;
		$(jq).val(val);
		function formatNum(num) {
			return (num < 10 ? "0" : "") + num;
		};
	};
	function spin(jq, down) {
		var options = $.data(jq, "timespinner").options;
		var val = $(jq).val();
		if (val == "") {
			val = [ 0, 0, 0 ].join(options.separator);
		}
		var vv = val.split(options.separator);
		for ( var i = 0; i < vv.length; i++) {
			vv[i] = parseInt(vv[i], 10);
		}
		if (down == true) {
			vv[options.highlight] -= options.increment;
		} else {
			vv[options.highlight] += options.increment;
		}
		$(jq).val(vv.join(options.separator));
		formatValue(jq);
		initHighlight(jq);
	}
	;
	$.fn.timespinner = function(options, param) {
		if (typeof options == "string") {
			var fn = $.fn.timespinner.methods[options];
			if (fn) {
				return fn(this, param);
			} else {
				return this.spinner(options, param);
			}
		}
		options = options || {};
		return this.each(function() {
			var data = $.data(this, "timespinner");
			if (data) {
				$.extend(data.options, options);
			} else {
				$.data(this, "timespinner", {
					options : $.extend({}, $.fn.timespinner.defaults,
							$.fn.timespinner.parseOptions(this), options)
				});
				create(this);
			}
		});
	};
	$.fn.timespinner.methods = {
		options : function(jq) {
			var options = $.data(jq[0], "timespinner").options;
			return $.extend(options, {
				value : jq.val()
			});
		},
		setValue : function(jq, value) {
			return jq.each(function() {
				$(this).val(value);
				formatValue(this);
			});
		},
		getHours : function(jq) {
			var options = $.data(jq[0], "timespinner").options;
			var vv = jq.val().split(options.separator);
			return parseInt(vv[0], 10);
		},
		getMinutes : function(jq) {
			var options = $.data(jq[0], "timespinner").options;
			var vv = jq.val().split(options.separator);
			return parseInt(vv[1], 10);
		},
		getSeconds : function(jq) {
			var options = $.data(jq[0], "timespinner").options;
			var vv = jq.val().split(options.separator);
			return parseInt(vv[2], 10) || 0;
		}
	};
	$.fn.timespinner.parseOptions = function(_26) {
		return $.extend({}, $.fn.spinner.parseOptions(_26), $.parser
				.parseOptions(_26, [ "separator", {
					showSeconds : "boolean",
					highlight : "number"
				} ]));
	};
	$.fn.timespinner.defaults = $.extend({}, $.fn.spinner.defaults, {
		separator : ":",
		showSeconds : false,
		highlight : 0,
		spin : function(down) {
			spin(this, down);
		}
	});
})(jQuery);
