(function($) {
	function render(jq){
		var options = $.data(jq, "tree").options;
		$(jq).addClass("ztree");
		var data = options.data;
		var setting = options.setting;
		if(data!=null){
			$.fn.zTree.init($(jq), setting, data);
		}else{
			$.fn.zTree.init($(jq), setting);
		}
	};
	
	function getZTreeObj(jq){
		return $.fn.zTree.getZTreeObj($(jq).attr("id"));
	};
	
	$.fn.tree = function(options){
		if (typeof options == "string") {
			var method = $.fn.tree.methods[options];
			if(method!=null){
				var args = arguments;
				args[0] = this;
				return method.apply(this,args);
			}
		}
		options = options || {};
		
		return this.each(function() {
			var data = $.data(this, "tree");
			if (data) {
				$.data(this, "tree", {
					options : $.extend({}, 
							$.fn.tree.defaults,
							data.options,
							options)
				});
			} else {
				data = $.data(this, "tree", {
					options : $.extend(true, {},
							$.fn.tree.defaults, 
							$.fn.tree.parseOptions(this), options)
				});
				
				var options = $.data(this, "tree").options;
				
				var setting = options.setting;
				setting.data = $.extend(true,setting.data,{
						simpleData : {
							enable : options.simpleDataEnable,
							idKey : options.idKey,
							pIdKey : options.pIdKey,
							rootPId : options.rootPId
						},
						key : {
							name : options.nameKey
						}
					}
				);
				
				setting.check = $.extend(setting.check,{
						enable : options.chkEnable,
						chkboxType : options.chkboxType,
						chkStyle : options.chkStyle,
						radioType : options.radioType
				});
				
				setting.view = $.extend(setting.view,{
						showIcon : options.showIcon,
						showLine : options.showLine,
						showTitle : options.showTitle
				});
				
				setting.async = $.extend(setting.async,{
						enable : options.asyncEnable,
						autoParam : options.autoParam,
						dataFilter : options.dataFilter,
						dataType : options.dataType,
						otherParam : options.otherParam,
						url : options.url
				});
				setting.callback = $.extend(setting.callback,{
						beforeAsync : options.beforeAsync,
						beforeCheck : options.beforeCheck,
						beforeClick : options.beforeClick,
						beforeCollapse : options.beforeCollapse,
						beforeDblClick : options.beforeDblClick,
						beforeExpand : options.beforeExpand,
						beforeMouseDown : options.beforeMouseDown,
						beforeMouseUp : options.beforeMouseUp,
						beforeRightClick : options.beforeRightClick,
						onAsyncError : options.onAsyncError,
						onAsyncSuccess : options.onAsyncSuccess,
						onCheck : options.onCheck,
						onClick : options.onClick,
						onCollapse : options.onCollapse,
						onDblClick : options.onDblClick,
						onExpand : options.onExpand,
						onMouseDown : options.onMouseDown,
						onMouseUp : options.onMouseUp,
						onRightClick : options.onRightClick,
						onNodeCreated : options.onNodeCreated
				});
			}
			render(this);
		});
	};
	
	$.fn.tree.methods = {
		getZTreeObj:function(jq){
			return getZTreeObj(jq[0]);
		},
		reload:function(jq,url){
			return jq.each(function() {
				var options = $.data(this, "tree").options;
				if (url) {
					options.url = url;
				}
				var treeObj = getZTreeObj(this);
				treeObj.setting.async.url = options.url;
				treeObj.reAsyncChildNodes(null, "refresh");
			});
		}
	};
	
	$.fn.tree.parseOptions = function(target) {
		var t = $(target);
		return $.extend({},{
			data : t.attr("data")?eval(t.attr("data")):undefined,
			simpleDataEnable : t.attr("simpleDataEnable")?t.attr("simpleDataEnable")=='true':undefined,
			idKey : t.attr("idKey"),
			pIdKey : t.attr("pIdKey"),
			rootPId : t.attr("rootPId"),
			nameKey : t.attr("nameKey"),
			chkEnable : t.attr("chkEnable")?t.attr("chkEnable")=='true':undefined,
			chkboxType : t.attr("chkboxType")?eval("("+t.attr("chkboxType")+")"):undefined,
			chkStyle : t.attr("chkStyle"),
			radioType : t.attr("radioType"),
			showIcon : t.attr("showIcon")?t.attr("showIcon")=='true':undefined,
			showLine : t.attr("showLine")?t.attr("showLine")=='true':undefined,
			showTitle : t.attr("showTitle")?t.attr("showTitle")=='true':undefined,
			asyncEnable : t.attr("asyncEnable")?t.attr("asyncEnable")=='true':undefined,
			autoParam : t.attr("autoParam")?eval("("+t.attr("autoParam")+")"):undefined,
			dataFilter : t.attr("dataFilter")?eval(t.attr("dataFilter")):undefined,
			dataType : t.attr("dataType"),
			otherParam : t.attr("otherParam")?eval("("+t.attr("otherParam")+")"):undefined,
			url : t.attr("url"),
			beforeAsync : t.attr("beforeAsync")?eval(t.attr("beforeAsync")):undefined,
			beforeCheck : t.attr("beforeCheck")?eval(t.attr("beforeCheck")):undefined,
			beforeClick : t.attr("beforeClick")?eval(t.attr("beforeClick")):undefined,
			beforeCollapse : t.attr("beforeCollapse")?eval(t.attr("beforeCollapse")):undefined,
			beforeDblClick : t.attr("beforeDblClick")?eval(t.attr("beforeDblClick")):undefined,
			beforeExpand : t.attr("beforeExpand")?eval(t.attr("beforeExpand")):undefined,
			beforeMouseDown : t.attr("beforeMouseDown")?eval(t.attr("beforeMouseDown")):undefined,
			beforeMouseUp : t.attr("beforeMouseUp")?eval(t.attr("beforeMouseUp")):undefined,
			beforeRightClick : t.attr("beforeRightClick")?eval(t.attr("beforeRightClick")):undefined,
			onAsyncError : t.attr("onAsyncError")?eval(t.attr("onAsyncError")):undefined,
			onAsyncSuccess : t.attr("onAsyncSuccess")?eval(t.attr("onAsyncSuccess")):undefined,
			onCheck : t.attr("onCheck")?eval(t.attr("onCheck")):undefined,
			onClick : t.attr("onClick")?eval(t.attr("onClick")):undefined,
			onCollapse : t.attr("onCollapse")?eval(t.attr("onCollapse")):undefined,
			onDblClick : t.attr("onDblClick")?eval(t.attr("onDblClick")):undefined,
			onExpand : t.attr("onExpand")?eval(t.attr("onExpand")):undefined,
			onMouseDown : t.attr("onMouseDown")?eval(t.attr("onMouseDown")):undefined,
			onMouseUp : t.attr("onMouseUp")?eval(t.attr("onMouseUp")):undefined,
			onRightClick : t.attr("onRightClick")?eval(t.attr("onRightClick")):undefined,
			onNodeCreated : t.attr("onNodeCreated")?eval(t.attr("onNodeCreated")):undefined,
			setting:t.attr("setting")?eval(t.attr("setting")):undefined
		});
	};
	
	$.fn.tree.defaults = $.extend( {},{
		data : null,
		simpleDataEnable : undefined,
		idKey : undefined,
		pIdKey : undefined,
		rootPId : undefined,
		nameKey : undefined,
		chkEnable : undefined,
		chkboxType : undefined,
		chkStyle : undefined,
		radioType : undefined,
		showIcon : undefined,
		showLine : undefined,
		showTitle : undefined,
		asyncEnable: undefined,
		autoParam : undefined,
		dataFilter : undefined,
		dataType : undefined,
		otherParam : undefined,
		url : undefined,
		beforeAsync : undefined,
		beforeCheck : undefined,
		beforeClick : undefined,
		beforeCollapse : undefined,
		beforeDblClick : undefined,
		beforeExpand : undefined,
		beforeMouseDown : undefined,
		beforeMouseUp : undefined,
		beforeRightClick : undefined,
		onAsyncError : undefined,
		onAsyncSuccess : undefined,
		onNodeCreated : undefined,
		onCheck : undefined,
		onClick : undefined,
		onCollapse : undefined,
		onDblClick : undefined,
		onExpand : undefined,
		onMouseDown : undefined,
		onMouseUp : undefined,
		onRightClick : undefined,
		setting:{
			data : {
				keep : {
					leaf : false,
					parent : false
				},
				key : {
					checked : "checked",
					children : "children",
					name : "name",
					title : "",
					url : "url"
				},
				simpleData : {
					enable : false,
					idKey : "id",
					pIdKey : "pId",
					rootPId : null
				}
			},
			check : {
				autoCheckTrigger : false,
				chkboxType : {"Y": "ps", "N": "ps"},
				chkStyle : "checkbox",
				enable : false,
				nocheckInherit : false,
				radioType : "level"
			},
			view : {
				addDiyDom : null,
				addHoverDom : null,
				autoCancelSelected : true,
				dblClickExpand : true,
				expandSpeed : "fast",
				fontCss : {},
				nameIsHTML : false,
				removeHoverDom : null,
				selectedMulti : true,
				showIcon : true,
				showLine : true,
				showTitle : true
			},
			async : {
				autoParam : [],
				contentType : "application...",
				dataFilter : null,
				dataType : "text",
				enable : false,
				otherParam : [],
				type : "get",
				url : ""
			},
			callback : {
				beforeAsync : null,
				beforeCheck : null,
				beforeClick : null,
				beforeCollapse : null,
				beforeDblClick : null,
				beforeDrag : null,
				beforeDragOpen : null,
				beforeDrop : null,
				beforeEditName : null,
				beforeExpand : null,
				beforeMouseDown : null,
				beforeMouseUp : null,
				beforeRemove : null,
				beforeRename : null,
				beforeRightClick : null,
				onAsyncError : null,
				onAsyncSuccess : null,
				onCheck : null,
				onClick : null,
				onCollapse : null,
				onDblClick : null,
				onDrag : null,
				onDrop : null,
				onExpand : null,
				onMouseDown : null,
				onMouseUp : null,
				onNodeCreated : null,
				onRemove : null,
				onRename : null,
				onRightClick : null
			},
			edit : {
				drag : {
					autoExpandTrigger : true,
					isCopy : true,
					isMove : true,
					prev : true,
					next : true,
					inner : true,
					borderMax : 10,
					borderMin : -5,
					minMoveSize : 5,
					maxShowNodeNum : 5,
					autoOpenTime : 500
				},
				editNameSelectAll : false,
				enable : false,
				removeTitle : "remove",
				renameTitle : "rename",
				showRemoveBtn : true,
				showRenameBtn : true
			}
		}
	});
})(jQuery);