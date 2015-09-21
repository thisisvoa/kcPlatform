(function($) {
	function createField(jq) {
		var v = $("<input type=\"hidden\">").insertAfter(jq);
		var name = $(jq).attr("name");
		if (name) {
			v.attr("name", name);
			$(jq).removeAttr("name").attr("numberboxName", name);
		}
		$(jq).textinput();
		return v;
	};
	
	function initValue(jq) {
		var options = $.data(jq, "numberbox").options;
		var fn = options.onChange;
		options.onChange = function() {
		};
		setValue(jq, options.parser.call(jq, options.value));
		options.onChange = fn;
	};
	function getValue(jq) {
		return $.data(jq, "numberbox").field.val();
	};
	function setValue(jq, value) {
		var data = $.data(jq, "numberbox");
		var options = data.options;
		var oldVal = getValue(jq);
		value = options.parser.call(jq, value);
		options.value = value;
		data.field.val(value);
		$(jq).val(options.formatter.call(jq, value));
		if (oldVal != value) {
			options.onChange.call(jq, value, oldVal);
		}
	};
	function bindEvent(jq) {
		var options = $.data(jq, "numberbox").options;
		$(jq).unbind(".numberbox").bind(
				"keypress.numberbox",
				function(e) {
					if (e.which == 45) {
						if ($(this).val().indexOf("-") == -1) {
							return true;
						} else {
							return false;
						}
					}
					if (e.which == 46) {
						if ($(this).val().indexOf(".") == -1) {
							return true;
						} else {
							return false;
						}
					} else {
						if ((e.which >= 48 && e.which <= 57
								&& e.ctrlKey == false && e.shiftKey == false)
								|| e.which == 0 || e.which == 8) {
							return true;
						} else {
							if (e.ctrlKey == true
									&& (e.which == 99 || e.which == 118)) {
								return true;
							} else {
								return false;
							}
						}
					}
				}).bind("paste.numberbox", function() {
			if (window.clipboardData) {
				var s = clipboardData.getData("text");
				if (!/\D/.test(s)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}).bind("dragenter.numberbox", function() {
			return false;
		}).bind("blur.numberbox", function() {
			setValue(jq, $(this).val());
			$(this).val(options.formatter.call(jq, getValue(jq)));
		}).bind("focus.numberbox", function() {
			var vv = getValue(jq);
			if ($(this).val() != vv) {
				$(this).val(vv);
			}
		});
	}
	;
	function disable(jq, disabled) {
		var options = $.data(jq, "numberbox").options;
		if (disabled) {
			options.disabled = true;
			$(jq).attr("disabled", true);
		} else {
			options.disabled = false;
			$(jq).removeAttr("disabled");
		}
	}
	;
	$.fn.numberbox = function(options, param) {
		if (typeof options == "string") {
			var fn = $.fn.numberbox.methods[options];
			if (fn) {
				return fn(this, param);
			}
		}
		options = options || {};
		return this.each(function() {
			var data = $.data(this, "numberbox");
			if (data) {
				$.extend(data.options, options);
			} else {
				data = $.data(this, "numberbox", {
					options : $.extend({}, $.fn.numberbox.defaults,
							$.fn.numberbox.parseOptions(this), options),
					field : createField(this)
				});
				$(this).removeAttr("disabled");
				$(this).css({
					imeMode : "disabled"
				});
			}
			disable(this, data.options.disabled);
			bindEvent(this);
			initValue(this);
		});
	};
	$.fn.numberbox.methods = {
		options : function(jq) {
			return $.data(jq[0], "numberbox").options;
		},
		destroy : function(jq) {
			return jq.each(function() {
				$.data(this, "numberbox").field.remove();
				$(this).remove();
			});
		},
		disable : function(jq) {
			return jq.each(function() {
				disable(this, true);
			});
		},
		enable : function(jq) {
			return jq.each(function() {
				disable(this, false);
			});
		},
		fix : function(jq) {
			return jq.each(function() {
				setValue(this, $(this).val());
			});
		},
		setValue : function(jq, _1d) {
			return jq.each(function() {
				setValue(this, _1d);
			});
		},
		getValue : function(jq) {
			return getValue(jq[0]);
		},
		clear : function(jq) {
			return jq.each(function() {
				var data = $.data(this, "numberbox");
				data.field.val("");
				$(this).val("");
			});
		}
	};
	$.fn.numberbox.parseOptions = function(target) {
		var t = $(target);
		return $.extend({}, $.parser.parseOptions(target, [ "decimalSeparator", "groupSeparator",
						"prefix", "suffix", {
							min : "number",
							max : "number",
							precision : "number"
						} ]), {
			disabled : (t.attr("disabled") ? true : undefined),
			value : (t.val() || undefined)
		});
	};
	$.fn.numberbox.defaults = $
			.extend({}, {
						disabled : false,
						value : "",
						min : null,
						max : null,
						precision : 0,
						decimalSeparator : ".",
						groupSeparator : "",
						prefix : "",
						suffix : "",
						formatter : function(val) {
							if (!val) {
								return val;
							}
							val = val + "";
							var options = $(this).numberbox("options");
							var s1 = val, s2 = "";
							var index = val.indexOf(".");
							if (index >= 0) {
								s1 = val.substring(0, index);
								s2 = val.substring(index + 1, val.length);
							}
							if (options.groupSeparator) {
								var p = /(\d+)(\d{3})/;
								while (p.test(s1)) {
									s1 = s1.replace(p, "$1"
											+ options.groupSeparator + "$2");
								}
							}
							if (s2) {
								return options.prefix + s1 + options.decimalSeparator
										+ s2 + options.suffix;
							} else {
								return options.prefix + s1 + options.suffix;
							}
						},
						parser : function(s) {
							s = s + "";
							var options = $(this).numberbox("options");
							if (options.groupSeparator) {
								s = s.replace(new RegExp("\\"
										+ options.groupSeparator, "g"), "");
							}
							if (options.decimalSeparator) {
								s = s.replace(new RegExp("\\"
										+ options.decimalSeparator, "g"), ".");
							}
							if (options.prefix) {
								s = s.replace(new RegExp("\\"
										+ $.trim(options.prefix), "g"), "");
							}
							if (options.suffix) {
								s = s.replace(new RegExp("\\"
										+ $.trim(options.suffix), "g"), "");
							}
							s = s.replace(/\s/g, "");
							var val = parseFloat(s).toFixed(options.precision);
							if (isNaN(val)) {
								val = "";
							} else {
								if (typeof (options.min) == "number"
										&& val < options.min) {
									val = options.min.toFixed(options.precision);
								} else {
									if (typeof (options.max) == "number"
											&& val > options.max) {
										val = options.max.toFixed(options.precision);
									}
								}
							}
							return val;
						},
						onChange : function(value, oldVal) {
						}
					});
})(jQuery);
