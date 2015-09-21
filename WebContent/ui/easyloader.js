/**
 *========================================================
 *
 * 用于动态加载js、css文件
 * 
 *========================================================
 */
(function() {
	//将所有的插件，和插件资源和依赖文件放进modules对象中。  
	var modules = {
		draggable:{  
            js:['js/plugins/jquery.draggable.js']  
        }, 
        
        resizable : {
			js : ['js/plugins/jquery.resizable.js']
		},
		
		layout : {
			js : ['js/plugins/jquery.layout.js'],
			dependencies : [ 'resizable', 'panel' ]
		},
		
		linkbutton : {
			js:['js/plugins/jquery.linkbutton.js']
		},
		
		panel : {
			js : ['js/plugins/jquery.panel.js']
		},
		accordion:{
			js : ['js/plugins/jquery.accordion.js'],
			dependencies : [ 'panel' ]
		},
		fieldset : {
			js : ['js/plugins/jquery.fieldset.js']
		},
		
		tabs:{  
            js: ['js/plugins/jquery.tabs.js'],  
            dependencies:['panel','linkbutton']
        },
        
		toolbar:{
			js: ['js/plugins/jquery.toolbar.js'],
        	dependencies:['linkbutton']
		},
		
		window:{  
            js:['js/plugins/jquery.window.js'],  
            dependencies:['resizable','draggable','panel']  
        },
        
        messager:{  
            js:['js/plugins/jquery.messager.js'],  
            dependencies:['window',"progressbar"]  
        },
        
		progressbar : {
			js : ['js/plugins/jquery.progressbar.js']
		},
		
		menu : {
			js:['js/plugins/jquery.menu.js']
		},
		menubutton:{
			js:['js/plugins/jquery.menubutton.js'],
			dependencies:['linkbutton','menu']
		},
		/************************表格组件***************************/
		jqgrid:{
			js : ['js/jqgrid/js/jquery.jqGrid.min.js','js/jqgrid/js/i18n/grid.locale-cn.js']
		},
		
		grid:{
			js : ['js/plugins/jquery.grid.js'],
			dependencies:['jqgrid']
		},
		
		/************************树形组件***************************/
		ztree : {
			js : ['js/ztree/jquery.ztree.all-3.2.min.js']
		},
		
		tree : {
			js : ['js/plugins/jquery.tree.js'],
			dependencies:['ztree']
		},
		/************************表单组件**************************/
		validate : {
			js : ['js/plugins/jquery.validate.js']
		},
		autocomplete : {
			js:['js/plugins/jquery.autocomplete.js']
		},
		combobox : {
			js : ['js/plugins/jquery.combobox.js'],
			dependencies:['dhtmlxcommon','dhtmlxcombo']
		},
        datePicker:{
        	js:['js/plugins/jquery.datePicker.js'],
        	dependencies:['my97']
        },
        textinput:{
        	js:['js/plugins/jquery.textinput.js']
        },
        textarea:{
        	js:['js/plugins/jquery.textarea.js']
        },
        checkbox:{
        	js:['js/plugins/jquery.checkbox.js']
        },
        radiobox:{
        	js:['js/plugins/jquery.radiobox.js']
        },
        fileupload:{
        	js:['js/plugins/jquery.fileupload.js'],
        	dependencies:['swfupload']
        },
        combo:{
        	js:['js/plugins/jquery.combo.js'],
        	dependencies:['panel']
        },
        combotree:{
        	js:['js/plugins/jquery.combotree.js'],
        	dependencies:['combo','tree']
        },
        spinner:{
        	js:['js/plugins/jquery.spinner.js']
        },
        numberbox:{
        	js:['js/plugins/jquery.numberbox.js'],
        	dependencies:['textinput']
        },
        numberspinner:{
        	js:['js/plugins/jquery.numberspinner.js'],
        	dependencies:['spinner','numberbox']
        },
        timespinner:{
        	js:['js/plugins/jquery.timespinner.js'],
        	dependencies:['spinner']
        },
        treeSelector:{
        	js:['js/plugins/jquery.treeSelector.js']
        },
        htmleditor:{
			js : ['js/plugins/jquery.htmleditor.js'],
			dependencies:['ckeditor']
		},
        validate:{
        	js:['js/plugins/jquery.validate.js'],
        	dependencies:['validationEngine']
        },
        validationEngine:{
        	js:['js/validationEngine/jquery.validationEngine.js',
        	    'js/validationEngine/languages/jquery.validationEngine-zh_CN.js']
        },
        button:{
        	js:['js/plugins/jquery.button.js']
        },
		parser:{
			js:['js/plugins/jquery.parser.js']
		},
		qtip:{
			js:['js/plugins/jquery.qtip.js']
		},
		/******************第三方控件******************/
		swfupload:{
			js:['js/swfupload/swfupload_fp10/swfupload.js','js/swfupload/plugins/swfupload.queue.js']
		},
		dhtmlxcommon : {
			js : ['js/dhtmlx/dhtmlxcommon.js']
		},
		
		dhtmlxcombo : {
			js : ['js/dhtmlx/combo/dhtmlxcombo.js',
			      'js/dhtmlx/combo/ext/dhtmlxcombo_extra.js',
			      'js/dhtmlx/combo/ext/dhtmlxcombo_group.js',
			      'js/dhtmlx/combo/ext/dhtmlxcombo_whp.js']
		},
		my97:{
			js:['js/My97DatePicker/WdatePicker.js']
		},
		ckeditor:{
			js : ['js/ckeditor/ckeditor.js']
		},
		ckfinder:{
			js : ['js/ckfinder/ckfinder.js']
		}
	};


	
	//定义一个局部变量，做循环遍历时候，存放状态  
	var queues = {};

	//加载js方法  
	function loadJs(url, callback) {
		//标志变量，js是否加载并执行  
		var done = false;
		var script = document.createElement('script');//创建script dom  
		script.type = 'text/javascript';
		script.language = 'javascript';
		script.src = url;
		script.onload = script.onreadystatechange = function() { 
			//onload是firefox 浏览器事件，onreadystatechange,是ie的，为了兼容，两个都写上，这样写会导致内存泄露  
			//script.readyState只是ie下有这个属性，如果这个值为undefined，说明是在firefox,就直接可以执行下面的代码了。反之为ie，需要对script.readyState  
			//状态具体值进行判别，loaded和complete状态表示，脚本加载了并执行了。  
			if (!done
					&& (!script.readyState || script.readyState == 'loaded' || script.readyState == 'complete')) {
				done = true;

				script.onload = script.onreadystatechange = null;//释放内存，还会泄露。  
				if (callback) {//加载后执行回调  
					callback.call(script);
				}
			}
		};
		//具体加载动作，上面的onload是注册事件，  
		document.getElementsByTagName("head")[0].appendChild(script);
	}
	
	//运行js ,看代码逻辑可知，运行js,只是在js执行后，将这个script删除而已，主要用来加载国际化文件  
	function runJs(url, callback) {
		loadJs(url, function() {
			document.getElementsByTagName("head")[0].removeChild(this);
			if (callback) {
				callback();
			}
		});
	}

	//加载css没什么好说的  
	function loadCss(url, callback) {
		var link = document.createElement('link');
		link.rel = 'stylesheet';
		link.type = 'text/css';
		link.media = 'screen';
		link.href = url;
		document.getElementsByTagName('head')[0].appendChild(link);
		if (callback) {
			callback.call(link);
		}
	}
	
	//加载单一一个plugin,仔细研究module ,可以发现，pingin之间通过dependence,构造成了一颗依赖树，  
	//这个方法，就是加载具体树中的一个节点  
	function loadSingle(name, callback) {
		//把整个plugin的状态设置为loading  
		queues[name] = 'loading';

		var module = modules[name];
		//把js状态设置为loading  
		var jsStatus = 'loading';
		
		/**如果允许css,并且plugin有css,则加载css,否则设置加载过了，其实是不加载  
		var cssStatus = (easyloader.css && module['css']) ? 'loading'
				: 'loaded';
		加载css,plugin 的css，如果是全称，就用全称，否则把简写换成全称，所以简写的css文件要放入到themes/type./文件下  
		if (easyloader.css && module['css']) {
			if (/^http/i.test(module['css'])) {
				var url = module['css'];
			} else {
				var url = easyloader.base+'/'
						+ module['css'];
			}
			loadCss(url, function() {
				cssStatus = 'loaded';
				//js， css加载完，才调用回调  
				if (jsStatus == 'loaded' && cssStatus == 'loaded') {
					finish();
				}
			});
		}*/
		
		var jsFiles = module['js'];
		var index = 0;//用于判断是否加载完最后一个文件
		
		function getJsFile(fileIndex){
			var url = null;
			//加载js,全称用全称，简写补全。  
			if (/^http/i.test(jsFiles[fileIndex])) {
				url = jsFiles[fileIndex];
			} else {
				url = easyloader.base + jsFiles[fileIndex];
			}
			return url;
		}
		function loadJsFile(url){
			loadJs(url, function() {
				index++;
				if(index==jsFiles.length){
					jsStatus = 'loaded';
					if (jsStatus == 'loaded') {
						finish();
					}
				}else if(index<jsFiles.length){
					loadJsFile(getJsFile(index));
				}
			});
		}
		loadJsFile(getJsFile(0));
		
		//加载完调用的方法，改plugin状态  
		function finish() {
			queues[name] = 'loaded';
			//调用正在加载的方法，其实已经加载完了，  
			easyloader.onProgress(name);
			if (callback) {
				callback();
			}
		}
	}
	//加载主模块入口，  
	function loadModule(name, callback) {
		//定义数组，最后是形成的是依赖插件列表，最独立的插件放在首位，name是末尾  
		var mm = [];
		var doLoad = false;
		//name有两种，一种是string ,一种是string array,这样一次可以加载多个plugin,都是调用add方法进行添加  
		if (typeof name == 'string') {
			add(name);
		} else {
			for ( var i = 0; i < name.length; i++) {
				add(name[i]);
			}
		}

		function add(name) {
			//如果modules中没有这个plugin那退出  
			if (!modules[name])
				return;
			//如果有，查看它是否依赖其他plugin  
			var d = modules[name]['dependencies'];
			//如果依赖，就加载依赖的plugin.同时在加载依赖的plugin的依赖。注意循环中调用了add,是递归  
			if (d) {
				for ( var i = 0; i < d.length; i++) {
					add(d[i]);
				}
			}
			mm.push(name);
		}

		function finish() {
			if (callback) {
				callback();
			}
			//调用onLoad，传递name 为参数  
			easyloader.onLoad(name);
		}
		//超时用  
		var time = 0;
		//定义一个加载方法，定义后直接调用  
		function loadMm() {
			//如果mm有长度，长度！=0,加载plugin,为0，即加载完毕，开始加载国际化文件。  
			if (mm.length) {
				var m = mm[0]; // the first module  
				if (!queues[m]) {//状态序列中没有这个plugin的信息，说明没有加载这个plug,调用laodSingle进行加载  
					doLoad = true;
					loadSingle(m, function() {
						mm.shift();//加载完成后，将这个元素从数组去除，在继续加载，直到数组  
						loadMm();
					});
				} else if (queues[m] == 'loaded') {//如果这个plugin已经加载，就不用加载，以为mm中可能有重复项  
					mm.shift();
					loadMm();
				} else {
					if (time < easyloader.timeout) {//超时时候，10秒钟调用一次loadMn().注意arguments.callee代表函数本身  
						time += 10;
						setTimeout(arguments.callee, 10);
					}
				}
			} else {
				finish();
			}
		}

		loadMm();
	}
	//  定义一个加载器，注意，是全局变量，没有var,  
	easyloader = {
		modules : modules,
		base : '.',//该属性是为了加载js,记录文件夹路径的  
		theme : 'default', //默认主题  
		css : true,
		timeout : 2000,//加载超时事件  
		load : function(name, callback,base) {
			//如果加载是*.css文件，判断是不是以http开头，如果是，直接调用  
			if (/\.css$/i.test(name)) {
				if (/^http/i.test(name)) {
					loadCss(name, callback);
				} else {
					//不是http的，加上base.文件夹路径  
					loadCss(easyloader.base + name, callback);
				}
			}
			//加载js文件  
			else if (/\.js$/i.test(name)) {
				if (/^http/i.test(name)) {
					loadJs(name, callback);
				} else {
					loadJs(easyloader.base + name, callback);
				}
			} else {
				//如果直接传递一个插件名，就去modole数组中加载。改方法是重点，也是easyui自带的plugin加载方式  
				loadModule(name, callback);
			}
		},

		onProgress : function(name) {
		},
		onLoad : function(name) {
		}
	};
	
	//以上一直在定义函数，和变量，此处为真正执行处  
	//获取页面的所有的script,主要是为了获取我们现在解释的easyloader.js文件路径，来设置base属性  
	var scripts = document.getElementsByTagName('script');
	for ( var i = 0; i < scripts.length; i++) {
		var src = scripts[i].src;
		if (!src)
			continue;
		var m = src.match(/easyloader\.js(\W|$)/i);//判断文件是否含有easyloadr.js  
		if (m) {
			//如果有，base为easyloadr.js 的相同前缀  
			easyloader.base = src.substring(0, m.index);
		}
	}
	
	//简化调用接口  
	window.using = easyloader.load;
	
	function doKey(e){  
	    var ev = e || window.event;//获取event对象  
	    var obj = ev.target || ev.srcElement;//获取事件源  
	    var t = obj.type || obj.getAttribute('type');//获取事件源类型  
	    if(ev.keyCode == 8 && t != "password" && t != "text" && t != "textarea"){  
	        return false;  
	    }  
	}  
	//禁止后退键 作用于Firefox、Opera  
	document.onkeypress=doKey;  
	//禁止后退键  作用于IE、Chrome  
	document.onkeydown=doKey;
})();