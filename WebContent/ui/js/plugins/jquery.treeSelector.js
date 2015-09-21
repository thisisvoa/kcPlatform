(function($) {
	function create(jq){
		var options = $.data(jq, "treeSelector").options;
		$(jq).addClass("ui-treeSelector").hide();
		$(jq).removeAttr("name");
		var warper = $("<span><input type='text' class='textinput'/>" +
			"<input type='button' class='ui_button' style='margin-left:1px' value='请选择'/>" +
			"<input type='hidden' name='"+options.name+"'/></span>").insertAfter(jq);
		var textField = warper.find("input[type=text]");
		var valueField = warper.find("input[type=hidden]");
		var selBtn =  warper.find("input[type=button]");
		$.data(jq, "treeSelector").textField = textField;
		$.data(jq, "treeSelector").valueField = valueField;
		$.data(jq, "treeSelector").selBtn = selBtn;
		textField.css("width", options.width);
		if(options.selectedUrl!="" && options.selValue!=""){
			$.postc(options.selectedUrl,{value:options.selValue},function(data){
				var selectedList = eval("("+data+")");
				 $.data(jq, "treeSelector").selectedList = selectedList;
				 if(selectedList!=null){
					 var vaules = [];
					 var texts = [];
					 for(var i=0;i<selectedList.length;i++){
						 vaules.push(selectedList[i][options.idKey]);
						 texts.push(selectedList[i][options.nameKey]);
					 }
					 $.data(jq, "treeSelector").textField.val(texts.join(options.separator));
					 $.data(jq, "treeSelector").valueField.val(vaules.join(options.separator));
				 }
			});
		}
		$.data(jq, "treeSelector").isInit = false;
	};
	
	function bindEvent(jq){
		var options = $.data(jq, "treeSelector").options;
		var selBtn = $.data(jq, "treeSelector").selBtn;
		var textField = $.data(jq, "treeSelector").textField;
		var valueField = $.data(jq, "treeSelector").valueField;
		selBtn.bind("click",clickHandler).hover(function(){
			$(jq).addClass("ui_button_hover");
		},function(){
			$(jq).removeClass("ui_button_hover");
		});
		
		textField.bind("click",clickHandler).hover(function() {
			$(this).addClass("textinput_hover");
		}, function() {
			$(this).removeClass("textinput_hover");
		}).focus(function() {
			$(this).addClass("textinput_click");
		}).blur(function(){
			$(this).removeClass("textinput_click");
		});
		
		function clickHandler(){
			var topWin = FrameHelper.getTop();
			var isInit = $.data(jq, "treeSelector").isInit;
			if(isInit){
				var selector = $.data(jq, "treeSelector").selector;
				selector.show();
			}else{
				var selector = new topWin.Dialog();
				selector.Title = options.title;
				selector.Width = options.dialogWidth;
				selector.Height = options.dialogHeight;
				selector.URL = easyloader.base+"pages/TreeSelector.jsp?showSelected="+options.showSelected;
				selector.OnLoad = function(){
					selector.innerFrame.contentWindow.init(options, $.data(jq, "treeSelector").selectedList);
				};
				selector.RefreshHandler = function(){
					var selectedList = selector.getData();
					$.data(jq, "treeSelector").selectedList = selectedList;
					var vaules = [];
					 var texts = [];
					 if(selectedList!=null){
						 for(var i=0;i<selectedList.length;i++){
							 vaules.push(selectedList[i][options.idKey]);
							 texts.push(selectedList[i][options.nameKey]);
						 }
					 }
					 textField.val(texts.join(options.separator));
					 valueField.val(vaules.join(options.separator));
					
				};
				selector.show();
				$.data(jq, "treeSelector").isInit = true;
				$.data(jq, "treeSelector").selector = selector;
			}
		}
	};
	
	$.fn.treeSelector = function(options){
		if (typeof options == "string") {
			var method = $.fn.treeSelector.methods[options];
			if(method!=null){
				var args = arguments;
				args[0] = this;
				return method.apply(this,args);
			}
		}
		options = options || {};
		return this.each(function() {
			var data = $.data(this, "treeSelector");
			if (data) {
				$.data(this, "treeSelector", {
					options : $.extend({}, 
							$.fn.treeSelector.defaults,
							data.options,
							options)
				});
			} else {
				data = $.data(this, "treeSelector", {
					options : $.extend(true, {},
							$.fn.treeSelector.defaults,
							$.fn.treeSelector.parseOptions(this), options)
				});
			}
			create(this);
			bindEvent(this);
		});
	};
	$.fn.treeSelector.methods = {
		
			
	};
	$.fn.treeSelector.parseOptions = function(target) {
		var t = $(target);
		return $.extend({},{
			id : t.attr("id"),
			name : t.attr("name"),
			selValue : t.attr("selValue"),
			url : t.attr("value"),
			width : (parseInt(target.style.width) || undefined),
			dialogWidth : t.attr("dialogWidth")?parseInt(t.attr("dialogWidth")):undefined,
			dialogHeight : t.attr("dialogHeight")?parseInt(t.attr("dialogHeight")):undefined,
			rootPId : t.attr("rootPId"),
			idKey : t.attr("idKey"),
			nameKey : t.attr("nameKey"),
			pIdKey : t.attr("pIdKey"),
			valueKey : t.attr("valueKey"),
			autoParam : t.attr("autoParam")?eval("("+t.attr("autoParam")+")"):undefined,
			icon : t.attr("icon"),
			separator : t.attr("separator"),
			title : t.attr("title"),
			multiple : (t.attr("multiple") ? (t.attr("multiple") == "true" || t.attr("multiple") == true || t.attr("multiple") == "multiple") : undefined),
			dataFilter : t.attr("dataFilter")?eval(t.attr("dataFilter")):undefined,
			url : t.attr("url"),
			simpleDataEnable:t.attr("simpleDataEnable")?t.attr("simpleDataEnable")=='true':undefined,
			showSelected : t.attr("showSelected")?t.attr("showSelected")=='true':undefined,
			cols : t.attr("cols")?eval("("+t.attr("cols")+")"):undefined,
			selectedUrl : t.attr("selectedUrl")
		});
	};
	$.fn.treeSelector.defaults = $.extend( {},{
		id : undefined,
		name : undefined,
		selValue : "",
		url : "",
		width : "auto",
		dialogWidth : 600,
		dialogHeight : 400,
		title : "",
		rootPId: "0",
		idKey : "id",
		nameKey : "name",
		pIdKey : "pId",
		valueKey : undefined,
		autoParam : [],
		icon : "",
		separator : ",",
		multiple : false,
		dataFilter:undefined,
		url:"",
		simpleDataEnable:false,
		showSelected:true,
		cols:[],
		selectedUrl:""
	});
})(jQuery);