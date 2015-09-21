/**
 *
 */

(function($) {
	/**
	 * 创建Combobox
	 */
	function create(jq){
		var options = $.data(jq, "combobox").options;
		$(jq).addClass("ui-combobox").addClass("combo-f").hide();
		var combo = $("<span style='display:inline-block;zoom:1;height: 21px;width:"+(options.width+3)+"px'></span>").insertAfter(jq);
		var opt_type = null;
		if(options.multiple){
			opt_type = "checkbox";
		}else if(options.hasIcon){
			opt_type = "image";
		}
		var combobox = new dhtmlXCombo(combo[0],$(jq).attr("name"), options.width, opt_type, $(jq).attr("tabIndex"));
		combobox.enableFilteringMode(options.filteringMode);
		if (!isNaN(options.dropdownWidth)) {
			combobox.setOptionWidth(options.dropdownWidth);
		}
		combobox.setOptionHeight(options.dropdownHeight);
		combobox.disable(options.disabled);
		combobox.readonly(!options.editable);
		combobox.show(options.visiable);
		
		$(jq).removeAttr("name");
		$.data(jq,"combobox").combobox = combobox;
		$.data(jq,"combobox").combo = combo;
		bindEvents(jq);
	};
	/**
	 * 添加绑定事件
	 */
	function detachEvent(jq,opt){
		var combobox = $.data(jq,"combobox").combobox;
		combobox.attachEvent(opt.event, opt.callback);
	}
	/**
	 * 绑定事件
	 */
	function bindEvents(jq){
		var combobox = $.data(jq,"combobox").combobox;
		var combo = $.data(jq,"combobox").combo;
		var options = $.data(jq, "combobox").options;
		
		$(jq).closest("form").bind("reset",function(){
			clearValue(jq);
		});
		combo.hover(function() {
			combo.find(".dhx_combo_box").addClass("textinput_hover");
		}, function() {
			combo.find(".dhx_combo_box").removeClass("textinput_hover");
		}).find(".dhx_combo_input").focus(function(){
			combo.find(".dhx_combo_box").addClass("textinput_click");
		}).blur(function(){
			combo.find(".dhx_combo_box").removeClass("textinput_click");
		});
		
		combobox.attachEvent("onChange", function(e){
			if(options.isFirst){
				options.isFirst = false;
			}else{
				options.onChange(jq);
				$(jq).trigger('change');
			}
			
		});
		
		combobox.attachEvent("onBlur", function(){
			options.onBlur();
		});
		
		combobox.attachEvent("onOpen", function(){
			options.onOpen();
		});
		
		combobox.attachEvent("onCheck", function(data){
			$(jq).trigger('change');
			return options.onCheck(data);
		});
	}
	/**
	 * 如果Select上有Option则翻译Option
	 */
	function transformOptions(jq){
		var options = $.data(jq, "combobox").options;
		var combobox = $.data(jq,"combobox").combobox;
		var dataProvider = [];
		var defaultValueStr = $(jq).attr("selValue");
		var defaultValues = [];
		if(defaultValueStr && defaultValueStr.length>0){
			defaultValues = defaultValueStr.split(options.separator);
		}
		var selIndexs = [];
		var index=0;
		var items = $(">option", jq);
		if(items.length<=0)return;
		
		items.each(
				function() {
					var option = {};
					option["value"] = $(this).attr("value") != undefined ? $(this).attr("value") : $(this).html();
					for(var i=0;i<defaultValues.length;i++){
						var defaultValue = defaultValues[i];
						if(option["value"] == defaultValue){
							selIndexs.push(index);
							break;
						}
					}
					
					option["text"] = $(this).html();
					option["img_src"] = $(this).attr(options.imgField);
					dataProvider.push(option);
					index++;
		});
		
		$.data(jq, "combobox").dataProvider = dataProvider;
		combobox.clearAll(true);
		combobox.addOption(dataProvider);
		//设置选中值
		if(options.multiple){
			for(var i=0;i<selIndexs.length;i++){
				combobox.setChecked(selIndexs[i]);
			}
		}else{
			var selIndex = 0;
			if(selIndexs.length>0){
				selIndex = selIndexs[0];
				combobox.selectOption(selIndex,null,true);
			}else{
				combobox.selectOption(0,null,true);
			}
			
		}
	}
	
	/**
	 * 转义options
	 */
	function transformData(jq){
		var options = $.data(jq, "combobox").options;
		var dataProvider = $.data(jq, "combobox").dataProvider?$.data(jq, "combobox").dataProvider:[];
		if(dataProvider.length<=0){
			return;
		}
		if(options.showEmptySelect){
			var data = {};
			data[options.textField] = options.emptySelectText;
			data[options.valueField] = "";
			dataProvider = [data].concat(dataProvider);
		}
		$.data(jq, "combobox").dataProvider = dataProvider;
		
		var combobox = $.data(jq,"combobox").combobox;
		var defaultValueStr = $(jq).attr("selValue");
		var defaultValues = [];
		if(defaultValueStr && defaultValueStr.length>0){
			defaultValues = defaultValueStr.split(options.separator);
		}
		var selIndexs = [];
		
		var dhtmlxdp = [];
		for(var i=0;i<dataProvider.length;i++){
			var item = dataProvider[i];
			var d = {};
			d["value"] = item[options.valueField];
			d["text"] = item[options.textField];
			d["img_src"] = item[options.imgField];
			for(var j=0;j<defaultValues.length;j++){
				var defaultValue = defaultValues[j];
				if(d["value"] == defaultValue){
					selIndexs.push(i);
					break;
				}
			}
			dhtmlxdp.push(d);
		}
		combobox.clearAll(true);
		combobox.addOption(dhtmlxdp);
		
		//设置选中值
		if(options.multiple){
			for(var i=0;i<selIndexs.length;i++){
				combobox.setChecked(selIndexs[i]);
			}
		}else{
			var selIndex = 0;
			if(selIndexs.length>0){
				selIndex = selIndexs[0];
				combobox.selectOption(selIndex,null,true);
			}else{
				combobox.selectOption(0,null,true);
			}
		}
	};
	
	/**
	 * 重新设置数据源
	 */
	function setDataProvider(jq, dp){
		var combobox = $.data(jq,"combobox").combobox;
		var options = $.data(jq, "combobox").options;
		var dhtmlxdp = [];
		dp = dp?dp:[];
		if(options.showEmptySelect){
			var data = {};
			data[options.textField] = options.emptySelectText;
			data[options.valueField] = "";
			dp = [data].concat(dp);
		}
		$.data(jq, "combobox").dataProvider = dp;
		var dataProvider = $.data(jq, "combobox").dataProvider;
		
		for(var i=0;i<dataProvider.length;i++){
			var item = dataProvider[i];
			var d = {};
			d["value"] = item[options.valueField];
			d["text"] = item[options.textField];
			d["img_src"] = item[options.imgField];
			dhtmlxdp.push(d);
		}
		combobox.clearAll(true);
		combobox.addOption(dhtmlxdp);
	};
	/**
	 * 加载数据
	 */
	function loadData(jq,param){
		var url = param["url"];
		var paramData = param["param"];
		var callback = param["callback"];
		var options = $.data(jq, "combobox").options;
		if (url) {
			options.url = url;
		}
		if (!options.url) {
			return;
		}
		
		$.ajax( {
			type : options.method,
			url : options.url,
			dataType : "json",
			data : paramData,
			success : function(json) {
				setDataProvider(jq, json);
				if(callback){
					callback(jq);
				}
			},
			error : function() {
				options.onLoadError.apply(this, arguments);
			}
		});
	};
	/**
	 * 初始化选中值
	 */
	function initSelValue(jq){
		var dataProvider = $.data(jq, "combobox").dataProvider;
		var options = $.data(jq, "combobox").options;
		var combobox = $.data(jq,"combobox").combobox;
		var defaultValueStr = $(jq).attr("selValue");
		var selIndexs = [];
		
		var defaultValues = [];
		if(defaultValueStr && defaultValueStr.length>0){
			defaultValues = defaultValueStr.split(options.separator);
		}
		for(var i=0;i<dataProvider.length;i++){
			var d = dataProvider[i];
			for(var j=0;j<defaultValues.length;j++){
				var defaultValue = defaultValues[j];
				if(d[options.valueField] == defaultValue){
					selIndexs.push(i);
					break;
				}
			}
		}
		//设置选中值
		if(options.multiple){
			for(var i=0;i<selIndexs.length;i++){
				combobox.setChecked(selIndexs[i]);
			}
		}else{
			var selIndex = 0;
			if(selIndexs.length>0){
				selIndex = selIndexs[0];
				combobox.selectOption(selIndex,null,true);
			}else{
				combobox.selectOption(0,null,true);
			}
		}
	};
	
	
	/**
	 * 选中项(针对单选)
	 */
	function selectOption(jq, value){
		var combobox = $.data(jq,"combobox").combobox;
		var index = combobox.getIndexByValue(value);
		if(index<0){
			combobox.unSelectOption();
			combobox.setComboText("");
		}else{
			combobox.selectOption(index, null, true);
		}
		
	};
	
	/**
	 * 选中项(针对多选)
	 */
	function checkOption(jq, value){
		var index = combobox.getIndexByValue(value);
		combobox.setChecked(index, true);
	};
	
	/**
	 * 不选中某项(很对多选)
	 */
	function uncheckOption(jq, value){
		var index = combobox.getIndexByValue(value);
		combobox.setChecked(index, false);
	};
	
	/**
	 * 清除选中值
	 */
	function clearValue(jq){
		var combobox = $.data(jq,"combobox").combobox;
		var options = $.data(jq, "combobox").options;
		if(options.multiple){
			var checkedVals = combobox.getChecked();
			for(var i=0;i<checkedVals.length;i++){
				var val = checkedVals[i];
				var index = combobox.getIndexByValue(val);
				combobox.setChecked(index, false);
			}
		}else{
			combobox.unSelectOption();
		}
		combobox.setComboText("");
	};
	
	/**
	 * 设置值
	 */
	function setValue(jq,value){
		clearValue(jq);
		var options = $.data(jq, "combobox").options;
		var combobox = $.data(jq,"combobox").combobox;
		var values = [];
		
		if(value && value.length>0){
			values = value.split(options.separator);
		}
		
		if(options.multiple){
			for(var i=0;i<values.length;i++){
				var index = combobox.getIndexByValue(values[i]);
				if(index==-1) continue;
				combobox.setChecked(index, true);
			}
		}else{
			if(values.length>0){
				selectOption(jq,values[0]);
			}else{
				combobox.unSelectOption();
			}
		}
	};
	
	/**
	 * 获取值
	 */
	function getValue(jq){
		var options = $.data(jq, "combobox").options;
		var combobox = $.data(jq,"combobox").combobox;
		if(options.multiple){
			var values = combobox.getChecked();
			return values.join(options.separator);
		}else{
			return combobox.getSelectedValue()==null?"":combobox.getSelectedValue();
		}
	};
	
	/**
	 * 销毁
	 */
	function destory(jq){
		var combobox = $.data(jq,"combobox").combobox;
		$.data(jq,"combo").remove();
		combobox.destructor();
		$(jq).remove();
	};
	
	/**
	 * 添加选项
	 */
	function addOption(jq, option){
		var dataProvider = $.data(jq, "combobox").dataProvider;
		var options = $.data(jq, "combobox").options;
		var combobox = $.data(jq,"combobox").combobox;
		
		dataProvider.push(option);
		var item = {text:option[options.textField], 
					value:option[options.valueField], 
					img_src:option[options.imgField]};
		
		combobox.addOption([item]);
	};
	
	/**
	 * 设置可用/不可用
	 */
	function disable(jq, mode){
		var combobox = $.data(jq,"combobox").combobox;
		var options = $.data(jq, "combobox").options;
		combobox.disable(mode);
		options.disabled = mode;
	};
	
	/**
	 * 设置是否可编辑
	 * @param jq
	 * @param mode true:可编辑 false:不可编辑
	 */
	function editable(jq, mode){
		var combobox = $.data(jq,"combobox").combobox;
		var options = $.data(jq, "combobox").options;
		combobox.readonly(!mode);
		options.editable = mode;
	};
	
	/**
	 * 删除选项
	 */
	function deleteOption(jq, value){
		var values = getValue(jq);
		
		var combobox = $.data(jq,"combobox").combobox;
		var options = $.data(jq, "combobox").options;
		
		var dataProvider = $.data(jq, "combobox").dataProvider;
		for(var i=0;i<dataProvider.length;i++){
			if(dataProvider[i][options.valueField]==value){
				dataProvider.splice(i,1);
				break;
			}
		}
		combobox.deleteOption(value);
		if(values){
			values = values+"";
			var vals = values.split(options.valueField);
			for(var i=0; i<vals.length; i++){
				if(vals[i]==value){
					vals.splice(i,1);
					break;
				}
			}
			setValue(jq, vals.join(options.valueField));
		}
		
	};
	
	/**
	 * 清除所有
	 */
	function clearAll(jq){
		$.data(jq, "combobox").dataProvider = [];
		var combobox = $.data(jq,"combobox").combobox;
		combobox.clearAll(true);
	};
	
	/**
	 * 显示
	 */
	function show(jq){
		var options = $.data(jq, "combobox").options;
		var combobox = $.data(jq,"combobox").combobox;
		combobox.show(true);
		options.visiable = true;
	};
	/**
	 * 隐藏
	 */
	function hide(jq){
		var options = $.data(jq, "combobox").options;
		var combobox = $.data(jq,"combobox").combobox;
		combobox.show(false);
		options.visiable = false;
	};
	
	/**
	 * 根据值获取某个选项的数据
	 */
	function getOptionByValue(jq, value){
		var dataProvider = $.data(jq, "combobox").dataProvider;
		var options = $.data(jq, "combobox").options;
		var result = null;
		for(var i=0; i<dataProvider.length;i++){
			if(dataProvider[i][options["valueField"]]==value){
				result = dataProvider[i];
				break;
			}
		}
		return result;
	};
	
	$.fn.combobox = function(options, param) {
		if (typeof options == "string") {
			var fn = $.fn.combobox.methods[options];
			if (fn) {
				return fn(this, param);
			}
		}
		
		options = options || {};
		return this.each(function() {
			var data = $.data(this, "combobox");
			if (data) {
				$.extend(data.options, options);
				create(this);
			} else {
				data = $.data(this, "combobox", {
					options : $.extend( {}, $.fn.combobox.defaults,
							$.fn.combobox.parseOptions(this), options)
				});
				data.dataProvider = [];
				create(this);
				if($(">option", this).length>0){
					transformOptions(this);
					var options = $.data(this, "combobox").options;
					options.isFirst = false;
				}
			}
			if(data.options.data){
					data.dataProvider = eval(data.options.data);
					transformData(this);
			}
			if(data.options.url!=null){
				var that = this;
				loadData(this, {url:data.options.url,
								callback:function(){
									initSelValue(that);
									var options = $.data(that, "combobox").options;
									options.isFirst = false;
									if((typeof data.options.onLoadSuccess)=='string'){
										eval(data.options.onLoadSuccess);
									}else{
										data.options.onLoadSuccess();
									}
								}});
			}
		});
	};
	
	/**
	 * API
	 */
	$.fn.combobox.methods = {
		/**
		 * 获取配置项
		 * @param jq
		 * @returns
		 */
		options:function(jq){
			var options = $.data(jq[0], "combobox").options;
			return options;
		},
		
		/**
		 * 摧毁空间
		 * @param jq
		 */
		destroy:function(jq){
			return jq.each(function(){
				destory(this);
			});
		},
		
		/**
		 * 设置下拉框高度
		 * @param jq
		 * @param height
		 * @returns
		 */
		setDropdownHeight:function(jq,height){
			return jq.each(function(){
				var combobox = $.data(this,"combobox").combobox;
				combobox.setOptionHeight(height);
			});
		},
		/**
		 * 设置下拉框的宽度
		 * @param jq
		 * @param width
		 * @returns
		 */
		setDropdownWidth:function(jq, width){
			return jq.each(function(){
				var combobox = $.data(this,"combobox").combobox;
				combobox.setOptionWidth(width);
			});
		},
		/**
		 * 获取数据源
		 * @param jq
		 * @returns
		 */
		getDataprovider:function(jq){
			var dataProvider = $.data(jq[0], "combobox").dataProvider;
			return dataProvider;
		},
		/**
		 * 设置数据源
		 * @param jq
		 * @param data
		 * @returns
		 */
		setDataProvider:function(jq, data){
			return jq.each(function(){
				setDataProvider(this, data);
			});
		},
		/**
		 * 加载数据
		 * @param jq
		 * @param param
		 * @returns
		 */
		reload:function(jq,param){
			return jq.each(function(){
				loadData(this,param);
			});
		},
		
		/**
		 * 设置值,如果是多选下拉框则值之间以指定的分隔符隔开,默认分隔符为逗号；
		 * @param jq
		 * @param value
		 * @returns
		 */
		setValue:function(jq, value){
			return jq.each(function(){
				setValue(this, value);
			});
		},
		/**
		 * 返回值，如果是说多选下拉框则返回的值以逗号隔开；
		 * @param jq
		 * @returns
		 */
		getValue:function(jq){
			return getValue(jq[0]);
		},
		/**
		 * 清除值
		 * @param jq
		 * @returns
		 */
		clearValue:function(jq){
			return jq.each(function(){
				clearValue(this);
			});
		},
		
		/**
		 * 设置是否可用
		 * @param jq
		 * @param mode true:不可用 false:可用
		 */
		disable:function(jq,mode){
			return jq.each(function(){
				disable(this, mode);
			});
		},
		/**
		 * 设置是否可编辑
		 * @param jq
		 * @param mode true:可编辑 false:不可编辑
		 */
		editable:function(jq, mode){
			return jq.each(function(){
				editable(this, mode);
			});
		},
		
		/**
		 * 添加某个选项
		 * @param jq
		 * @param option
		 */
		addOption:function(jq,option){
			return jq.each(function(){
				addOption(this, option);
			});
		},
		/**
		 * 删除某个选项
		 * @param jq
		 * @param option
		 */
		deleteOption:function(jq,value){
			return jq.each(function(){
				deleteOption(this, value);
			});
			
		},
		/**
		 * 获取某个选项数据
		 * @param jq
		 * @param value
		 */
		getOptionByValue:function(jq,value){
			return getOptionByValue(jq[0],value);
		},
		
		/**
		 * 获取当前选中值的序号(单选)
		 * @param jq
		 */
		getSelectedIndex:function(jq){
			var combobox = $.data(jq[0],"combobox").combobox;
			return combobox.getSelectedIndex();
		},
		
		/**
		 * 选中某个选项(单选)
		 */
		selectOption:function(jq,value){
			return jq.each(function(){
				selectOption(this, value);
			});
		},
		
		/**
		 * 选中某个选项(多选)
		 * @param jq
		 * @param value
		 */
		checkOption:function(jq, value){
			return jq.each(function(){
				checkOption(this, value);
			});
		},
		
		/**
		 * 不选中某个下拉选项
		 * @param jq
		 * @param value
		 */
		unCheckOption:function(jq, value){
			return jq.each(function(){
				unCheckOption(this, value);
			});
		},
		
		/**
		 * 清除所有下拉框选项
		 * @param jq
		 */
		clearAll:function(jq){
			return jq.each(function(){
				clearAll(this);
			});
		},
		/**
		 * 关闭下拉框
		 * @param jq
		 */
		closeAll:function(jq){
			return jq.each(function(){
				var combobox = $.data(this,"combobox").combobox;
				combobox.closeAll();
			});
		},
		
		/**
		 * 打开下拉框
		 * @param jq
		 */
		openSelect:function(jq){
			return jq.each(function(){
				var combobox = $.data(this,"combobox").combobox;
				combobox.openSelect();
			});
		},
		
		/**
		 * 显示/隐藏控件
		 * @param jq
		 * @param mode
		 */
		show:function(jq){
			return jq.each(function(){
				show(this);
			});
		},
		
		hide:function(jq){
			return jq.each(function(){
				hide(this);
			});
		}
		
		
	};
	/**
	 * class声明式定义属性转化为options
	 * @param {Object} target DOM对象
	 * @return {TypeName} 
	 */
	$.fn.combobox.parseOptions = function(target) {
		var t = $(target);
		return $.extend( {}, {
			width : (parseInt(target.style.width) || undefined),
			dropdownWidth : (t.attr("dropdownWidth") ? t.attr("dropdownWidth"):undefined),
			dropdownHeight : (t.attr("dropdownHeight") ? t.attr("dropdownHeight"):undefined),
			multiple : (t.attr("multiple") ? (t.attr("multiple") == "true" || t.attr("multiple") == true || t.attr("multiple") == "multiple") : undefined),
			hasIcon : (t.attr("hasIcon") ? (t.attr("hasIcon") == "true" || t.attr("hasIcon") == true) : undefined),
			editable : (t.attr("editable") ? t.attr("editable") == "true" : undefined),
			disabled : (t.attr("disabled") ? true : undefined),
			visiable : (t.attr("visiable") ? t.attr("visiable") == "true" : undefined),
			valueField : t.attr("valueField"),
			textField : t.attr("textField"),
			imgFiled : t.attr("imgField"),
			method : (t.attr("method") ? t.attr("method") : undefined),
			url : t.attr("url"),
			data : t.attr("data"),
			showEmptySelect:(t.attr("showEmptySelect") ? t.attr("showEmptySelect") == "true" : undefined),
			emptySelectText:t.attr("emptySelectText"),
			filteringMode : (t.attr("filteringMode") ? true : undefined),
			onChange : eval(t.attr("onChange")),
			onBlur : eval(t.attr("onBlur")),
			onOpen : eval(t.attr("onOpen")),
			onCheck : eval(t.attr("onCheck")),
			onLoadSuccess:eval(t.attr("onLoadSuccess")),
			onLoadError:eval(t.attr("onLoadError"))
		});
	};
	/**
	 * 默认参数设置
	 * @memberOf {TypeName} 
	 */
	$.fn.combobox.defaults = $.extend({},{
		width : 132,
		dropdownWidth : "auto",
		dropdownHeight : 150,
		multiple : false,//多选
		hasIcon : false,//每个选项前是否有图标
		separator : ",",//多选值的分隔符号
		editable : true,//可编辑
		disabled : false,//是否禁用
		visiable : true,//是否显示
		valueField : "value",//数据源值属性名称
		textField : "text",//数据源值显示文本属性名称
		imgField : "img",//图标值属性名称
		mode : "local",//加载值的方式（local:本地;remote:服务）
		method : "post",//远程获取值的方式post或get
		url : null, //远程获取值的url
		data : null,//数据源
		filteringMode : false,//是否打开查询过滤功能
		showEmptySelect:false,
		emptySelectText:"--请选择--",
		isFirst:true,
		onChange : function(){
			
		},
		onBlur : function() {
			
		},
		onOpen : function(){
			
		},
		onCheck : function(value){
			return true;
		},
		onLoadSuccess:function(){
		},
		onLoadError:function(){
			
		}
		
	});
})(jQuery);