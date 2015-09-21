(function($) {
	function render(jq){
		var options = $.data(jq, "grid").options;
		$(jq).addClass("grid");
		if(options.fit){
			options.autowidth = true;
		}
		$(jq).jqGrid(options);
		setSize(jq);
		$(jq).bind("_resize", function() {
			if(!$(jq).parents(".ui-jqgrid").is(":hidden")){
			    setSize(jq);
			 }
		});
	}
	
	function setSize(jq){
		var options = $.data(jq, "grid").options;
		if(options.fit){
			var topParent = $(jq).parents(".ui-jqgrid").parent();
			var hdiv = $(jq.grid.hDiv);
			var pager = $(jq.p.pager);
			var parentHeight = topParent.height();
			var parentWidth = topParent.width();
			var PaddingV = parseInt(topParent.css("padding-top"))+parseInt(topParent.css("padding-bottom"));
			var PaddingH = parseInt(topParent.css("padding-left"))+parseInt(topParent.css("padding-right"));
			$(jq).jqGrid("setGridHeight", parentHeight-hdiv.outerHeight()-pager.outerHeight()-PaddingV-3);
			$(jq).jqGrid("setGridWidth", parentWidth-PaddingH-2);
			
		}else{
			if($(jq).jqGrid("getGridParam","autowidth")){
				var topParent = $(jq).parent().parent().parent().parent().parent();
				var parentWidth = topParent.width();
				var PaddingH = parseInt(topParent.css("padding-left"))+parseInt(topParent.css("padding-right"));
				$(jq).jqGrid("setGridWidth", parentWidth-PaddingH-2);
			}
		}
	};
	/**
	 * 保存编辑后的数据
	 */
	function saveEditedData(jq,rowid){
		var data = getEditedData(jq,rowid);
		$(jq).jqGrid("setRowData",rowid, data);
		
		var ind = $(jq).jqGrid("getInd",rowid,true);
		$(ind).attr("editable","0");
	};
	
	/*
	 * 获取编辑后的数据
	 */
	function getEditedData(jq,rowid){
		var colM = $(jq).grid("getGridParam", "colModel");
		var oldD = $("#localList").grid("getRowData", rowid);
		var data = {};
		for ( var i = 0; i < colM.length; i++) {
			var cm = colM[i];
			var nm = cm["name"];
			if (cm.editable) {
				switch (cm.edittype) {
				case "checkbox":
					var cbv = [ "Yes", "No" ];
					if (cm.editoptions) {
						cbv = cm.editoptions.value.split(":");
					}
					data[nm] = $("#" + rowid + "_" + nm).is(":checked") ? cbv[0]
							: cbv[1];
					break;
				case 'text':
				case 'password':
				case 'textarea':
				case "button":
					data[nm] = $("#" + rowid + "_" + nm).val();
					break;
				case 'select':
					if (!cm.editoptions.multiple) {
						data[nm] = $("#" + rowid + "_" + nm + " option:selected").val();
					} else {
						data[nm] = $("#" + rowid + "_" + nm + " option:selected").val();
					}
					break;
				}
			} else {
				data[nm] = oldD[nm];
			}
		}
		return data;
	}
	
	$.fn.grid = function(options){
		if (typeof options == "string") {
			var method = $.fn.grid.methods[options];
			if(method!=null){
				var args = arguments;
				args[0] = this;
				return method.apply(this,args);
			}else{
				var fn = $(this[0]).jqGrid;
				return fn.apply(this,arguments);
			}
			
		}
		options = options || {};
		return this.each(function() {
			var data = $.data(this, "grid");
			if (data) {
				$.data(this, "grid", {
					options : $.extend({}, 
							$.fn.grid.defaults,
							data.options,
							options)
				});
				
			} else {
				data = $.data(this, "grid", {
					options : $.extend( {},
							$.fn.grid.defaults, 
							$.fn.grid.parseOptions(this), options)
				});
			}
			render(this);
		});
	};
	
	$.fn.grid.methods = {
		resize:function(jq){
			return jq.each(function() {
				setSize(this);
			});
		},
		setGroupHeaders:function(jq,opt){
			return jq.each(function() {
				var fn = $(this).jqGrid;
				fn.apply($(this),["setGroupHeaders",opt]);
				setSize(this);
			});
		},
		destroyGroupHeader:function(jq, state){
			return jq.each(function() {
				var fn = $(this).jqGrid;
				fn.apply($(this),["destroyGroupHeader",state]);
				setSize(this);
			});
		},
		saveEditedData:function(jq, rowid){
			return saveEditedData(jq[0],rowid);
		}
	};
	
	$.fn.grid.parseOptions = function(target) {
		var t = $(target);
		return $.extend({},{
		});
	};
	
	$.fn.grid.defaults = $.extend( {},{
		rownumbers:true,
		mtype:'post',
		fit: false,
		multiboxonly:true,
		prmNames: {page:"_PAGE_NO_",rows:"_PAGE_SIZE_", sort: "sidx",order: "sord", search:"_search", nd:"nd", id:"id",oper:"oper",editoper:"edit",addoper:"add",deloper:"del", subgridid:"id", npage: null, totalrows:"totalrows"},
		jsonReader: {
		  root: "rows", // json中代表实际模型数据的入口   
		  page: "page", // json中代表当前页码的数据   
		  total: "total", // json中代表页码总数的数据   
		  records: "records", // json中代表数据行总数的数据  
		  repeatitems: false
		}
	});
})(jQuery);