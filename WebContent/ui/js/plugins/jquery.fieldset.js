/**
 * jQuery Plugin for creating collapsible fieldset
 * @requires jQuery 1.2 or later
 *
 * Copyright (c) 2010 Lucky <bogeyman2007@gmail.com>
 * Licensed under the GPL license:
 *   http://www.gnu.org/licenses/gpl.html
 *
 * "animation" and "speed" options are added by Mitch Kuppinger <dpneumo@gmail.com>
 */

(function($) {
	function setSize(jq){
		var obj = $(jq);
		var options = $.data(jq, "fieldset").options;
		if(options.autowidth){
			var parent = obj.parent();
			var parentWidth = parent.width();
			var PaddingH = parseInt(parent.css("padding-left"))+parseInt(parent.css("padding-right"));
			var selfPaddingH = parseInt(obj.css("padding-left"))+parseInt(obj.css("padding-right"));
			obj.width(parentWidth-PaddingH-selfPaddingH);
		}
	}
	
	function hideFieldsetContent(jq,animation){
		var obj = $(jq);
		var options = $.data(jq, "fieldset").options;
		animation = animation||options.animation;
		if(animation==true)
			obj.find('div').slideUp(options.speed);
		else
			obj.find('div').hide();
		
		obj.removeClass("expanded");
		obj.addClass("collapsed");
	}
	
	function showFieldsetContent(jq){
		var obj = $(jq);
		var options = $.data(jq, "fieldset").options;
		if(options.animation==true)
			obj.find('div').slideDown(options.speed);
		else
			obj.find('div').show();
		
		obj.removeClass("collapsed");
		obj.addClass("expanded");
	}
	
	$.fn.fieldset = function(options,param){
		if (typeof options == "string") {
			return $.fn.fieldset.methods[options](this, param);
		}
		options = options || {};
		return this.each(function(){
			var data = $.data(this, "fieldset");
			if (data) {
				$.extend(data.options, options);
			} else {
				data = $.data(this, "fieldset", {
					options : $.extend( {}, 
							$.fn.fieldset.defaults, 
							$.fn.fieldset.parseOptions(this), options)
				});
			}
			
			var fieldset=$(this);
			
			fieldset.addClass("ui-fieldset");
			var opts = $.data(this, "fieldset").options;
			if(opts.allowCollapsed){
				fieldset.addClass("coolfieldset");
				var legend=fieldset.children('legend');
				if(opts.collapsed==true){
					legend.toggle(
						function(){
							showFieldsetContent(fieldset[0]);
						},
						function(){
							hideFieldsetContent(fieldset[0]);
						}
					);
					hideFieldsetContent(fieldset[0],false);
				}
				else{
					legend.toggle(
						function(){
							hideFieldsetContent(fieldset[0]);
						},
						function(){
							showFieldsetContent(fieldset[0]);
						}
					);
				}
			}
		});
	};
	$.fn.fieldset.methods = {
		collapse:function(jq) {
			hideFieldsetContent(jq[0]);
		},
		expand:function(jq){
			showFieldsetContent(jq[0]);
		}
	};
	
	$.fn.fieldset.parseOptions = function(target) {
		var t = $(target);
		return $.extend({},{
			autowidth:t.attr("autowidth")=='false'?false:undefined,
			allowCollapsed:t.attr("allowCollapsed")=='false'?false:undefined,
			collapsed:t.attr("collapsed")=='true'?true:undefined,
			animation:t.attr("animation")=='true'?true:undefined,
			speed:t.attr("speed")
		});
	};
	
	$.fn.fieldset.defaults = {
		autowidth:true,
		allowCollapsed:true,
		collapsed:false,
		animation:false,
		speed:'medium'
	};
})(jQuery);