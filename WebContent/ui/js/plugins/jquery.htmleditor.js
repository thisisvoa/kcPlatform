(function($) {
	function init(jq){
		var options = $.data(jq, "htmleditor").options;
		var id = $(jq).attr("id");
		var instance = CKEDITOR.instances[id];
	    if(instance)
	    {
	        CKEDITOR.remove(instance);
	    }
	    var editor = CKEDITOR.replace(id,options);
		if(options.uploadEnable){
			using("ckfinder",function(){
				CKFinder.setupCKEditor(editor,easyloader.base+'js/ckfinder/');
			});
		}
		$.data(jq, "htmleditor").editor = editor;
	}
	
	$.fn.htmleditor = function(options, param) {
		if (typeof options == "string") {
			return $.fn.htmleditor.methods[options](this, param);
		}
		options = options || {};
		return this.each(function() {
			var data = $.data(this, "htmleditor");
			if (data) {
				$.extend(data.options, options);
			} else {
				$.data(this, "htmleditor", {
					options : $.extend({}, $.fn.htmleditor.defaults,
							$.fn.htmleditor.parseOptions(this), options)
				});
			}
			init(this);
		});
	};
	
	$.fn.htmleditor.methods = {
		/**
		 * 取得原生态editor
		 * @param jq
		 * @returns
		 */
		getEditor:function(jq){
			return $.data(jq[0], "htmleditor").editor;
		},
		/**
		 * 获取文本值
		 * @param jq
		 * @returns
		 */
		getText:function(jq){
			var editor = jq.htmleditor("getEditor");
			return editor.document.getBody().getText(); 
		},
		/**
		 * 获取html
		 * @param jq
		 * @returns
		 */
		getHtml:function(jq){
			var editor = jq.htmleditor("getEditor");
			return editor.document.getBody().getHtml();
		},
		/**
		 * 设置数据
		 * @param jq
		 * @param data
		 */
		setData:function(jq, data){
			var editor = jq.htmleditor("getEditor");
			editor.setData(data);
		},
		/**
		 * 插入html
		 * @param jq
		 * @param html
		 */
		insertHtml:function(jq,html){
			var editor = jq.htmleditor("getEditor");
			editor.insertHtml(html);
		},
		/**
		 * 设置只读
		 * @param jq
		 * @param readonly
		 */
		setReadOnly:function(jq, readonly){
			var editor = jq.htmleditor("getEditor");
			editor.setReadOnly(readonly);
		}
	};
	
	$.fn.htmleditor.parseOptions = function(target) {
		var t = $(target);
		var opts = $.parser.parseOptions(target, [ "language", "width","height","skin","uiColor","toolbar","toolbarStartupExpanded",
		                                             {toolbarStartupExpanded:"boolean",baseFloatZIndex:"int",uploadEnable:"boolean"}]);
		opts.toolbar_Full = t.attr("toolbar_Full")?eval("("+t.attr("toolbar_Full")+")"):undefined;
		opts.toolbar_Basic = t.attr("toolbar_Basic")?eval("("+t.attr("toolbar_Basic")+")"):undefined;
		return $.extend({},opts);
	};
	
	$.fn.htmleditor.defaults = $.extend({}, {
		width:undefined,
		height:undefined,
		language:'zh-cn',
		skin:'office2003',
		uiColor:'#fff',
		toolbar:'Basic',//Basic||Full||Custom
		toolbarStartupExpanded:true,
		toolbar_Full:[
			      		[ 'Source' , '-' , 'Save' , 'NewPage' , 'Preview' , '-' , 'Templates' ],
			    		[ 'Cut' , 'Copy' , 'Paste' , 'PasteText' , 'PasteFromWord' , '-' , 'Print' , 'SpellChecker' , 'Scayt' ],
			    		[ 'Undo' , 'Redo' , '-' , 'Find' , 'Replace' , '-' , 'SelectAll' , 'RemoveFormat' ],
			    		[ 'Form' , 'Checkbox' , 'Radio' , 'TextField' , 'Textarea' , 'Select' , 'Button' , 'ImageButton' , 'HiddenField' ],
			    		'/' ,
			    		[ 'Bold' , 'Italic' , 'Underline' , 'Strike' , '-' , 'Subscript' , 'Superscript' ],
			    		[ 'NumberedList' , 'BulletedList' , '-' , 'Outdent' , 'Indent' , 'Blockquote' ],
			    		[ 'JustifyLeft' , 'JustifyCenter' , 'JustifyRight' , 'JustifyBlock' ],
			    		[ 'Link' , 'Unlink' , 'Anchor' ],
			    		[ 'Image' , 'Flash' , 'Table' , 'HorizontalRule' , 'Smiley' , 'SpecialChar' , 'PageBreak' ],
			    		'/' ,
			    		[ 'Styles' , 'Format' , 'Font' , 'FontSize' ],
			    		[ 'TextColor' , 'BGColor' ],
			    		[ 'ShowBlocks' ]
			    	],
		toolbar_Basic : 
			    	[
			    	 ['Bold' , 'Italic' , '-' , 'NumberedList' , 'BulletedList' , '-' , 'Link' , 'Unlink']
			    	],
	    baseFloatZIndex:10000,
	    uploadEnable:true//是否启用文件(图像、Flash等)上传功能
	});
})(jQuery);
