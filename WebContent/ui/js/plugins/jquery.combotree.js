(function($) {
	function create(jq) {
		var options = $.data(jq, "combotree").options;
		var combo = $.data(jq, "combotree").tree;
		$(jq).addClass("ui-combotree");
		$(jq).combo(options);
		var panel = $(jq).combo("panel");
		var time = new Date();
		
		if (!combo) {
			combo = $("<ul id='comboTree_"+time.getTime()+"'></ul>").appendTo(panel);
			$.data(jq, "combotree").tree = combo;
		}
		var setting = {
				data : {
					key : {
						name : options.nameKey
					},
					simpleData : {
						enable : options.simpleDataEnable,
						idKey : options.idKey,
						pIdKey : options.pIdKey,
						rootPId : options.rootPId
					}
				},
				check : {
					chkboxType : options.chkboxType,
					chkStyle : options.chkStyle,
					enable : options.checkEnable,
					radioType : options.radioType
				},
				view : {
					showIcon : options.showIcon,
					showLine : options.showLine
				},
				callback : {
					beforeAsync : options.beforeAsync,
					beforeCheck : options.beforeCheck,
					beforeClick : options.beforeClick,
					beforeDblClick : options.beforeDblClick,
					onAsyncError : options.onAsyncError,
					onAsyncSuccess : onAsyncSuccessHandler,
					onCheck : onCheckHandler,
					onClick : onClickHandler,
					onDblClick : options.onDblClick
				}
		};
		if(options.url!=null){
			setting.async= {  
		        enable: true,  
		        type:'post',
		        url:options.url,
		        dataFilter:options.dataFilter
			 };
		}
		combo.data("tree", {options:{data:options.data, setting:setting}});
		combo.tree();
		/**
		 * 直接设置数据源的话则初始化值
		 */
		if(options.data!=null){
			if(options.selValue!=""){
				setValue(jq,options.selValue);
			}
		}
		/**
		 * 单击事件
		 */
		function onClickHandler(event, treeId, treeNode){
			if(!options.checkEnable){
				$(jq).combo("setValue",treeNode[options.valueKey]);
				$(jq).combo("setText",treeNode[options.nameKey]);
				$(jq).combo("hidePanel");
			}
			if(options.onClick){
				options.onClick(event, treeId, treeNode);
			}
			if(options.onChange){
				options.onChange();
			}
			$(jq).trigger("change");
		}
		
		/**
		 * 选中事件
		 */
		function onCheckHandler(event, treeId, treeNode){
			if(options.checkEnable){
				var treeObj = combo.tree("getZTreeObj");
				var checkedNodes = treeObj.getCheckedNodes(true);
				var vv = []; var ss= [];
				for(var i=0;i<checkedNodes.length;i++){
					vv.push(checkedNodes[i][options.valueKey]);
					ss.push(checkedNodes[i][options.nameKey]);
				}
				$(jq).combo("setValue",vv.join(options.separator));
				$(jq).combo("setText",ss.join(options.separator));
			}
			if(options.onCheck){
				options.onCheck(event, treeId, treeNode);
			}
			$(jq).trigger("change");
		}
		
		/**
		 * 加载数据回调
		 */
		function onAsyncSuccessHandler(event, treeId, treeNode, msg){
			if(options.onAsyncSuccess){
				options.onAsyncSuccess(event, treeId, treeNode, msg);
			}
			if(options.selValue!=""){
				setValue(jq,options.selValue);
			}
		}
		
	};
	
	
	function bindEvents(jq){
		
	};
	
	function setValue(target, values) {
		var options = $.data(target, "combotree").options;
		var tree = $.data(target, "combotree").tree;
		var treeObj = tree.tree("getZTreeObj");
		treeObj.checkAllNodes(false);
		var vv = [], ss = [];
		if(values!=null){
			if(options.checkEnable){
				var vals = values.split(options.separator);
				for(var i=0;i<vals.length;i++){
					var node = treeObj.getNodeByParam("id",vals[i]);
					if(node!=null){
						treeObj.checkNode(node,true,false);
					}
					
				}
				var checkedNodes = treeObj.getCheckedNodes(true);
				for(var i=0;i<checkedNodes.length;i++){
					vv.push(checkedNodes[i][options.valueKey]);
					ss.push(checkedNodes[i][options.nameKey]);
				}
			}else{
				var node = treeObj.getNodeByParam(options.valueKey,values);
				treeObj.selectNode(node);
				var selectNodes = treeObj.getSelectedNodes();
				if(selectNodes.length>0){
					vv.push(selectNodes[0][options.valueKey]);
					ss.push(selectNodes[0][options.nameKey]);
				}
			}
			
		}
		$(target).combo("setValue", vv.join(options.separator)).combo("setText", ss.join(options.separator));
	};
	$.fn.combotree = function(options, param) {
		if (typeof options == "string") {
			var method = $.fn.combotree.methods[options];
			if (method) {
				return method(this, param);
			} else {
				return this.combo(options, param);
			}
		}
		options = options || {};
		return this.each(function() {
			var state = $.data(this, "combotree");
			if (state) {
				$.extend(state.options, options);
			} else {
				$.data(this, "combotree", {
					options : $.extend( {}, $.fn.combotree.defaults,
							$.fn.combotree.parseOptions(this), options)
				});
			}
			create(this);
		});
	};
	$.fn.combotree.methods = {
		options : function(jq) {
			return $.data(jq[0], "combotree").options;
		},
		tree : function(jq) {
			return $.data(jq[0], "combotree").tree;
		},
		loadData : function(jq, data) {
			return jq.each(function() {
				var opts = $.data(this, "combotree").options;
				opts.data = data;
				var combo = $.data(this, "combotree").tree;
				combo.tree("loadData", data);
			});
		},
		reload : function(jq, url) {
			return jq.each(function() {
				var tree = $.data(this, "combotree").tree;
				var opts = $.data(this, "combotree").options;
				if (url) {
					opts.url = url;
				}
				tree.tree("reload",opts.url);
				setValue(this,"");
				
			});
		},
		setValue : function(jq, value) {
			return jq.each(function() {
				setValue(this, value);
			});
		},
		clear : function(jq) {
			return jq.each(function() {
				var combo = $.data(this, "combotree").tree;
				combo.find("div.tree-node-selected").removeClass(
						"tree-node-selected");
				$(this).combo("clear");
			});
		}
	};
	$.fn.combotree.parseOptions = function(target) {
		var t = $(target);
		return $.extend( {}, $.fn.combo.parseOptions(target),{
			url:t.attr("url"),
			data:t.attr("data")?eval(t.attr("data")):undefined,
			checkEnable:t.attr("checkEnable")?t.attr("checkEnable")=='true':undefined,
			chkboxType:t.attr("chkboxType")?eval("("+t.attr("chkboxType")+")"):undefined,
			chkStyle:t.attr("chkStyle"),
			radioType:t.attr("radioType"),
			simpleDataEnable:t.attr("simpleDataEnable")?t.attr("simpleDataEnable")=='true':undefined,
			idKey:t.attr("idKey"),
			pIdKey:t.attr("pIdKey"),
			nameKey:t.attr("nameKey"),
			valueKey:t.attr("valueKey")?t.attr("valueKey"):t.attr("idKey"),
			rootPId:t.attr("rootPId"),
			showLine:t.attr("showLine")?t.attr("showLine")=='true':undefined,
			showIcon:t.attr("showIcon")?t.attr("showIcon")=='true':undefined,
			beforeAsync:t.attr("beforeAsync")?eval(t.attr("beforeAsync")):undefined,
			beforeCheck:t.attr("beforeCheck")?eval(t.attr("beforeCheck")):undefined,
			beforeClick:t.attr("beforeClick")?eval(t.attr("beforeClick")):undefined,
			beforeDblClick:t.attr("beforeDblClick")?eval(t.attr("beforeDblClick")):undefined,
			onAsyncError:t.attr("onAsyncError")?eval(t.attr("onAsyncError")):undefined,
			onAsyncSuccess:t.attr("onAsyncSuccess")?eval(t.attr("onAsyncSuccess")):undefined,
			onCheck:t.attr("onCheck")?eval(t.attr("onCheck")):undefined,
			onClick:t.attr("onCheck")?eval(t.attr("onClick")):undefined,
			onDblClick:t.attr("onDblClick")?eval(t.attr("onDblClick")):undefined,
			dataFilter:t.attr("dataFilter") ? eval(t.attr("dataFilter")) : undefined,
			separator:t.attr("separator"),
			selValue:t.attr("selValue")
		});
	};
	$.fn.combotree.defaults = $.extend( {}, $.fn.combo.defaults,{
			panelWidth:150,
			url : null,
			data : null,
			checkEnable : false,
			chkboxType : {"Y": "", "N": ""},
			chkStyle: "checkbox",
			radioType: "level",
			simpleDataEnable:false,
			idKey : "id",
			pIdKey : "pId",
			nameKey : "name",
			valueKey: "id",
			rootPId : null,
			showLine:true,
			showIcon:true,
			beforeAsync : null,
			beforeCheck : null,
			beforeClick : null,
			beforeDblClick : null,
			onAsyncError : null,
			onAsyncSuccess : null,
			onCheck : null,
			onClick : null,
			onDblClick: null,
			separator:",",
			selValue:""//初始化值
	});
})(jQuery);
