(function($) {
	function create(jq){
		var options = $.data(jq, "fileupload").options;
		var post_params = options.post_params;
		if(post_params==null){
			post_params = {addonBeanName:options.addonBeanName};
		}else{
			post_params.addonBeanName = options.addonBeanName;
		}
		options.post_params = post_params;
		
		if(!options.multiple){
			options.height = 30;
		}
		$(jq).addClass("Uploader").css({width:options.width+"px",height:options.height+"px"});
		
		$(jq).append("<input type=\"hidden\" " +
						"name=\""+options.name+"\" id=\""+options.name+"_value\">");
		
		$(jq).append("<div class='total_bg'>" +
						"<div class='oper'>" +
						"</div>"+
						"<div class='content' style='height:"+(options.height>52?(options.height-22):30)+"px;'>" +
						"</div>"+
					"</div>");
		var uploadBtn = $("<div class='upload_btn' style='margin-right:4px;'>" +
							"<div><b class='add'><a href='javascript:void(0)'>"+(options.btn_add_text)+"</a></b></div>" +
							"<div id='"+$(jq).attr("id")+"_addFile'></div>" +
						 "</div>");
		
		var operbar = this.$(jq).children("div").children(".oper");
		operbar.append(uploadBtn);
	    var content=this.$(jq).children("div").children(".content");
	    var valueBox = this.$(jq).children("input");
	    if(!options.multiple)	content.hide();
	    function renderSwfloader(){
	    	var swfupload = new SWFUpload({
	    		upload_url : options.upload_url,//upload_url的路径比必须是相对于SWF插件所在的路径，或者是绝对路径
	    		flash_url : options.flash_url,//SWF插件所在路径
	    		file_size_limit : options.file_size_limit,//文件大小限制，单位为KB，0表示大小无限制
	    		//file_upload_limit: options.file_upload_limit,//允许上传的最多文件数量,0表示大小无限制
	    		post_params:options.post_params,//类似于AJAX传输中所用的传参，格式一样
	    		file_types:options.file_types,//可上传的文件类型，“如*.jpg,*.gif”，用分号隔开
	            file_types_description: options.file_types_description,//可上传的文件类型描述文字
	            file_queue_limit:options.file_queue_limit,
	    		button_width: "68",
	    		button_height: "20",
	    		button_placeholder_id : $(jq).attr("id")+"_addFile",
	    		button_image_url : easyloader.base+"css/darkBlue/images/blank.gif",
	            button_cursor : SWFUpload.CURSOR.HAND,
	            button_action : (options.multiple? SWFUpload.BUTTON_ACTION.SELECT_FILES : SWFUpload.BUTTON_ACTION.SELECT_FILE),
	            button_window_mode : SWFUpload.WINDOW_MODE.TRANSPARENT,
	            prevent_swf_caching : true,
	            debug: false,
	            upload_start_handler : uploadStart
	    	});
	    	return swfupload;
	    };
	    
	    
	    var swfupload = renderSwfloader();
	    
	    /**
	     * 上传开始处理
	     */
	    function uploadStart(file){
	    	try {
	    		
	    		var fileList = $.data(jq, "fileupload").fileList;
		    	var id = file.id;
		    	if(fileList[id]==null){
		    		swfupload.cancelUpload(id);
		    		return false;
		    	}
		    	if(options.uploadStart(fileList[id])){//触发上传开始回调
		    		fileList[id].bar.removeClass("unbar");
		            fileList[id].bar.addClass("bar");
		            fileList[id].span.html("0%");
		            var li = content.children("#"+id);
		            li.children(".op_up").css("display","none");
	                li.children(".op_del").css("display","none");
	                li.children(".op_no").css("display","block");
		    	}
	    	}catch(e){
	    		alert(e);
	    	}
	    	return true;
	    };
	    
	    $.data(jq, "fileupload").operbar = operbar;
	    $.data(jq, "fileupload").content = content;
	    $.data(jq, "fileupload").swfupload = swfupload;
	    $.data(jq, "fileupload").valueBox = valueBox;
	    if(options.multiple){//如果是支持批量上传，则要多定义另两个按钮
			var btn_upload = $("<div class='upload_btn'><div><b class='upload'><a href='javascript:void(0);'>"+(options.btn_up_text)+"</a></b></div></div>");
	        var btn_cancel = $("<div class='upload_btn' style='display:none'><div><b class='cancel'> <a href='javascript:void(0);'>"+(options.btn_cancel_text)+"</a></b></div>");
			var btn_clean = $("<div class='upload_btn' style='float:right'><div><b class='clean'><a href='javascript:void(0);'>"+(options.btn_clean_text)+"</a></b></div></div>");
			operbar.append(btn_upload).append(btn_cancel).append(btn_clean);
			$.data(jq, "fileupload").btn_upload = btn_upload;
			$.data(jq, "fileupload").btn_cancel = btn_cancel;
			$.data(jq, "fileupload").btn_clean = btn_clean;
		}
	    $.data(jq, "fileupload").fileList = {};
	    bindHandler(jq);
	    bindEvents(jq);
	};
	
	function initData(jq){
		var options = $.data(jq, "fileupload").options;
		var valueBox = $.data(jq, "fileupload").valueBox;
		var value = "";
		var spliter = "";
		if(options.value!=null && options.value!="" && options.init_url!=""){
			$.postc(options.init_url,{ids:options.value},function(data){
				if(data!=null&&data!=""){
					var fileList = eval("("+data+")");
					if(fileList!=null){
						for(var i=0;i<fileList.length;i++){
							value += spliter;
							value += fileList[i]["fileId"];
							addUploadedFile(jq,fileList[i]);
							spliter = ",";
						}
					}
				}
			});
		}
		valueBox.val(value);
	}
	/**
	 * 绑定Swfloader处理器
	 */
	function bindHandler(jq) {
		var options = $.data(jq, "fileupload").options;
		var swfupload = $.data(jq, "fileupload").swfupload;
		var content = $.data(jq, "fileupload").content;
		var fileList = $.data(jq, "fileupload").fileList;
		var operbar = $.data(jq, "fileupload").operbar;
		
		swfupload.fileQueued = function(file) {
			//进行上传文件数控制
			if(options.file_upload_limit>0){
				if(getFileNum(jq)>=options.file_upload_limit){
					try{
						swfupload.cancelUpload(file.id);
					}catch(e){
						
					}
					return false;
				}
			}
			
			if(!options.multiple){
				operbar.css({height:"0px","padding-top":"0px"});
				operbar.find(".add").css("display","none");
	            content.css("display","block");
	        }
			var id = file.id;
			fileList[id] = {
				id : file.id,
				name : file.name,
				size : getFileSize(file.size),// 单位默认为KB，小数点后一位
				filestatus : file.filestatus
			};
			content.append("<li id="
							+ file.id
							+ " style='width;"
							+ (content.attr("offsetWidth") - 18)
							+ "px'><b class='"
							+ fixFileTypeIcon(file.type)
							+ "'></b><div class='labe' title='"
							+ file.name
							+ " ("
							+ fileList[id].size
							+ ")'><div class='file' style='width:"
							+ (content.attr("offsetWidth") - 85)
							+ "px'>"
							+ (fileList[id].name.length > 16 ? fileList[id].name
									.substring(0, 13)
									+ "..."
									: fileList[id].name)
							+ " ("
							+ fileList[id].size
							+ ")"
							+ "</div><div class='unbar'><div></div></div></div><span></span><b class='op_del' title='"
							+ (options.op_del_text || "Del Item")
							+ "' style='display:block'></b>"
							+ (options.showUploadBtn?("<b class='op_up' title='"+(options.op_up_text || "Upload Item")+"' style='display:block'></b>"):"") 
							+ "<b class='op_ok' title='"
							+ (options.op_ok_text || "Upload Success")
							+ "'></b>" + "<b class='op_no' title='"
							+ (options.op_no_text || "Cancel Item")
							+ "'></b><b class='op_fail' title='"
							+ (options.op_fail_text || "Cancel Item")
							+ "'></b></li>");
			fileList[id].bar = content.children("#" + file.id)
					.children("div").children(".unbar");
			fileList[id].span = content.children("#" + file.id)
					.children("span");
			$.data(jq, "fileupload").fileList = fileList;
		};
		/**
		 * 文件选择出错
		 */
		swfupload.fileQueueError = function(file, errorCode, message){
			var errorMsg = null;
			switch (errorCode) {
				case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED:
		        	errorMsg = "您上传了太多文件，最多可上传"+options.file_upload_limit+"个文件!";
		        	break;
				case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
					errorMsg = "您选择的文件太大!";
					break;
				case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
					errorMsg = "您上传的文件为空文件!";
					break;
				case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
					errorMsg = "不合法的文件类型!";
					break;
				default:
					errorMsg = "选择的文件不合法，请重新选择文件!"; 
			}
			alert(errorMsg);
		};
		
		/**
		 * 上传成功回调
		 */
		swfupload.uploadSuccess=function(file,data){
	        var id = file.id;
	        fileList[id].span.html("100%");
	        fileList[id].filestatus = file.filestatus;//更新状态
	        var li=content.children("#"+id);
	        li.children(".op_fail").css("display","none");
	        li.children(".op_no").css("display","none");
	        li.children(".op_ok").css("display","block");
	        li.children(".op_del").css("display","block");
	        try{
	        	var d = eval('(' + data + ')'); 
	        	if(d.result!=null){
	        		fileList[id]["fileId"] = d.result;//存储返回的数据
	        	}
	        }catch(e){
	        }
	        refreshValueBox(jq);
	        options.uploadSuccess(fileList[id],data);
	    };
		
	    /**
	     * 文件上传异常处理
	     */
	    swfupload.uploadError = function(file,errorCode,msg){
	        var errorMsg = null;
	        switch (errorCode) {
	        	case SWFUpload.UPLOAD_ERROR.HTTP_ERROR:
	        		errorMsg = "上传失败：服务端异常！";
	        		break;
	        	case SWFUpload.UPLOAD_ERROR.MISSING_UPLOAD_URL:
					errorMsg = "上传失败：上传URL丢失!";
					break;
	        	case SWFUpload.UPLOAD_ERROR.SECURITY_ERROR:
					errorMsg = "上传失败：安全异常!";
					break;
	        	case SWFUpload.UPLOAD_ERROR.IO_ERROR:
					errorMsg = "上传失败：IO异常!";
					break;
				case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
					errorMsg = "上传失败：上传文件太大!";
					return;
				case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
				case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
					errorMsg = "上传失败,上传过程中被停止!";
					break;
				default:
					errorMsg = "上传失败,错误代码："+errorCode;
					break;
			};
			var id = file.id;
			fileList[id].filestatus = file.filestatus;
			
	        var li=content.children("#"+id);
	        li.children(".op_no").css("display","none");
	        li.children(".op_up").css("display","none");
            li.children(".op_del").css("display","block");
	        li.children(".op_fail").css("display","block");
	        li.children(".op_fail").attr("title",errorMsg);
	    };
	    /**
	     * 上传时候的滚动条
	     */
	    swfupload.uploadProgress = function(file,complete,total){
	    	var id = file.id;
			fileList[id].filestatus = file.filestatus;
			
	        var per=Math.floor((complete/total)*100);
	        fileList[id].span.html(per+"%");
	        fileList[id].bar.children("div").width(140*((100-per)/100));//BAR为进度条的总长度
	    };
	    
	    /**
	     * 一个文件上传完成后进行下个上传
	     */
	    swfupload.uploadComplete = function(file){
	    	var id = file.id;
	    	fileList[id].filestatus = file.filestatus;
	    	if($.data(jq, "fileupload").continueUpload == false){
	    		$.data(jq, "fileupload").continueUpload = true;
	    	}else{
	    		swfupload.startUpload();
	    	}
	        if(options.multiple && swfupload.getStats().in_progress==0){
	        	var btn_upload = $.data(jq, "fileupload").btn_upload;
				var btn_cancel = $.data(jq, "fileupload").btn_cancel;
				btn_upload.css({display:"block"});
				btn_cancel.css({display:"none"});
	        }
	        if(isAllUploadComplete(jq)){
	        	options.allUploadComplete();
	        }
	        if(isAllUploadSuccess(jq)){
	        	options.allUploadSuccess();
	        }
	    };
		function getFileSize(bytes) {
			var size = (bytes / 1024).toFixed(1);// 先转为KB
			if (size > 1200) {
				size = (size / 1024).toFixed(1);// 先转为MB
				if (size > 1200) {
					size = (size / 1024).toFixed(1);//先转为GB
					return size + "g";
				}
				return size + "m";
			}
			return size + "k";
		};
	};
	
	/**
	 * 绑定事件
	 */
	function bindEvents(jq){
		var options = $.data(jq, "fileupload").options;
		var swfupload = $.data(jq, "fileupload").swfupload;
		var content = $.data(jq, "fileupload").content;
		var fileList = $.data(jq, "fileupload").fileList;
		var operbar = $.data(jq, "fileupload").operbar;
		if (options.multiple) {
			var btn_upload = $.data(jq, "fileupload").btn_upload;
			var btn_cancel = $.data(jq, "fileupload").btn_cancel;
			var btn_clean = $.data(jq, "fileupload").btn_clean;
			
			btn_upload.bind("click", function(e) {
				if(swfupload.getStats().in_progress==0 && getFileNum(jq)>0 && !isAllUploadComplete(jq)){
					swfupload.startUpload();
					btn_upload.css("display","none");
					btn_cancel.css("display","block");
				}
			});
			
			btn_cancel.bind("click", function(e) {
				if(swfupload.getStats().in_progress!=0){
					for ( var key in fileList) {
						var file = fileList[key];
						if(file.filestatus != SWFUpload.FILE_STATUS.COMPLETE){
							swfupload.cancelUpload(key);
						}
					}
					$.data(jq, "fileupload").continueUpload = false;
					btn_upload.css("display","block");
					btn_cancel.css("display","none");
				}
			});
			
			btn_clean.bind("click", function(e) {
					if (swfupload.getStats().in_progress == 0) {// 如果当前没有文件在上传
						for ( var key in fileList) {
							//全部清除
							content.children("#" + key).remove();
							var file = fileList[key];
		                    if(file.filestatus==SWFUpload.FILE_STATUS.COMPLETE && options.delete_url!=""){
		            			$.postc(Utils.wrapUrl(options.delete_url, "fileId", file.fileId), options.post_params);
		            		};
							options.onDelete(file);//调用删除回调
							delete fileList[key];
						}
						refreshValueBox(jq);
					}
				});
		}
		
		//绑定文件列表中，每一行的按钮
	    content.bind("click",function(e){
	        if(!e)	e=window.event;
	        if(e.target.tagName=="B"){
	            var tar = $(e.target);
	            var li = tar.parent();
	            var cls = tar.attr("class");
	            var id = li.attr("id");
	            switch(cls){
	                case "op_up":
	                	if(swfupload.getStats().in_progress==0){//如果当前没有文件在上传
		                    try{
		                    	$.data(jq, "fileupload").continueUpload = false;
		                    	swfupload.startUpload(id);
		                    }catch(ex){
		                    	alert(ex);
		                    }
	                    }
	                    break;//上传
	                case "op_del":
	                	deleteFile(jq,id);
	                    break;//从列表中删除
	                case "op_ok":
	                    break;//上传成功
	                case "op_no":
	                	try{
	                    	swfupload.cancelUpload(id);
	                    }catch(ex){
	                    	alert(ex);
	                    }
	                    tar.css("display","none");
	                    li.children(".op_fail").css("display","block");
	                    break;//用户取消
	                case "op_fail":
	                    break;//上传失败
	            }
	        }
	    });
		   
	}
	/**
	 * 刷新隐藏域值
	 */
	function refreshValueBox(jq){
		var successFileList = getSuccessFileList(jq);
		var valueBox = $.data(jq, "fileupload").valueBox;
		var ids = [];
		for(var i=0;i<successFileList.length;i++){
			ids.push(successFileList[i]["fileId"]);
		}
		var value = ids.join(",");
		valueBox.val(value);
	}
	/**
	 * 删除文件
	 */
	function deleteFile(jq,fileId){
		var options = $.data(jq, "fileupload").options;
		var swfupload = $.data(jq, "fileupload").swfupload;
		var content = $.data(jq, "fileupload").content;
		var fileList = $.data(jq, "fileupload").fileList;
		var operbar = $.data(jq, "fileupload").operbar;
		if (swfupload.getStats().in_progress == 0) {
			content.children("#" + fileId).remove();
			var file = fileList[fileId];
			try{
            	swfupload.cancelUpload(fileId);
            }catch(ex){
            	alert(ex);
            }
			if(!options.multiple){
                operbar.css({height:"20px","padding-top":"5px"});
                operbar.find(".add").css("display","block");
                content.css("display","none");
            }
	        if(file.filestatus==SWFUpload.FILE_STATUS.COMPLETE && options.delete_url!=""){
				$.postc(Utils.wrapUrl(options.delete_url, "ids", file.fileId), options.post_params);
			};
			options.onDelete(file);//调用删除回调
			delete fileList[fileId];
			refreshValueBox(jq);
			
		}else{
			alert("有文件处于上传状态不能进行删除！");
		}
		
	}
	
	//获取成功上传的文件列表
	function getSuccessFileList(jq){
		var result = [];
		var fileList = $.data(jq, "fileupload").fileList;
		for(var key in fileList){
			var file = fileList[key];
			if(file==null) continue;
			if(file.filestatus == SWFUpload.FILE_STATUS.COMPLETE){
				result.push(file);
			}
		}
		return result;
	}
	
	//是否全部上传成功
	function isAllUploadSuccess(jq){
		var fileList = $.data(jq, "fileupload").fileList;
		var isSuccess = true;
		for(var key in fileList){
			var file = fileList[key];
			if(file==null) continue;
			if(file.filestatus != SWFUpload.FILE_STATUS.COMPLETE){
				isSuccess = false;
				break;
			}
		}
		return isSuccess;
	}
	
	//是否全部上传完成：包括上传成功及上传失败
	function isAllUploadComplete(jq){
		var fileList = $.data(jq, "fileupload").fileList;
		var isComplete = true;
		for(var key in fileList){
			var file = fileList[key];
			if(file==null) continue;
			if(file.filestatus == SWFUpload.FILE_STATUS.QUEUED 
					|| file.filestatus == SWFUpload.FILE_STATUS.IN_PROGRESS){
				isComplete = false;
				break;
			}
		}
		return isComplete;
	}
	
	/**
	 * 获取上传文件数
	 */
	function getFileNum(jq){
		var length = 0;
		var fileList = $.data(jq, "fileupload").fileList;
		for(var key in fileList){
			if(fileList[key]!=null){
				length++;
			}
		}
		return length;
	}
	
	/**
	 * 上传所有文件
	 * @param jq
	 * @returns
	 */
	function upload(jq){
		var swfupload = $.data(jq, "fileupload").swfupload;
		swfupload.startUpload();
	};
	
	/**
	 * 获取文件列表
	 */
	function getFileList(jq){
		var fileList = $.data(jq, "fileupload").fileList;
		var result = [];
		for(var key in fileList){
			if(fileList[key]==null) continue;
			result.push(fileList[key]);
		}
		return result;
	};
	
	/**
	 * 获取上传列表中的所有文件名，用逗号隔开
	 */
	function getFileNames(jq){
		var fileList = $.data(jq, "fileupload").fileList;
        var str="";
        for(var key in fileList){
        	if(fileList[key]==null) continue;
            str += fileList[key].name+",";
        }
        if(str.indexOf(",")>-1) return str.substring(0,str.length-1);
        else return str;
    };
	
    /**
     * 添加已上传文件记录
     */
    function addUploadedFile(jq, uploadFile){
    	var fileList = $.data(jq, "fileupload").fileList;
    	var content = $.data(jq, "fileupload").content;
    	var options = $.data(jq, "fileupload").options;
    	var operbar = $.data(jq, "fileupload").operbar;
    	var file = {
    		id:uploadFile.fileId,
    		name:uploadFile.fileName,
    		size:uploadFile.fileSize,
    		type:uploadFile.fileType,
			filestatus:SWFUpload.FILE_STATUS.COMPLETE,
			fileId:uploadFile.fileId
    	};
    	fileList[file.id] = file;
    	if(!options.multiple){
			operbar.css({height:"0px","padding-top":"0px"});
			operbar.find(".add").css("display","none");
            content.css("display","block");
        }
		content.append("<li id="+file.id+" style='width;"+ (content.attr("offsetWidth") - 18)+ "px'>" 
						+ "<b class='" + fixFileTypeIcon("."+file.type) + "'></b>" 
						+ "<div class='labe' title='" + file.name + " (" + file.size + ")'>" 
						+ "<div class='file' style='width:" + (content.attr("offsetWidth") - 85) + "px'>"
						+ (file.name.length > 16?file.name.substring(0, 13)+"...":file.name)
						+ " (" + file.size + ")"
						+ "</div>" 
						+ "</div>"
						+ "<b class='op_del' title='" + (options.op_del_text || "Del Item") + "' style='display:block'></b>"
						+ "</li>");
    };
    
	/**
	 * 绑定事件
	 */
	function bind(jq, param){
		var options = $.data(jq, "fileupload").options;
		options[param["event"]] = param["callback"];
	};
	
	/**
	 * 设置提交参数
	 */
	function setPostParam(jq, param){
		var options = $.data(jq, "fileupload").options;
		var postParams = options["post_params"];
		postParams[param["key"]] = param["value"];
	};
	
	//根据文件类型，判断应该用哪种样式
    function fixFileTypeIcon(type){
       var tmp = type.split(".");
       if(tmp[1])   tmp[1]=tmp[1].substring(0,3).toLowerCase();
       else return "";
       switch(tmp[1]){
           case "doc": return "doc";   case "wps": return "doc";
           case "zip": return "zip";   case "rar": return "zip";   case "ace": return "zip";   case "7z": return "zip";
           case "swf": return "swf";   case "fla": return "swf";
           case "rmv": return "dvd";   case "rm": return "dvd";   case "wmv": return "dvd";   case "avi": return "dvd";   case "mpg": return "dvd";
           case "chm": return "book";   case "pdf": return "book";
           case "ppt": return "ppt";   case "xls": return "xls";
           case "exe": return "exe";   case "bat": return "exe";
           case "cpp": return "scr";   case "js": return "scr";   case "jav": return "scr";   case "css": return "scr";
           case "cs": return "scr";   case "h": return "scr";   case "cgi": return "scr";
           case "jpg": return "img";   case "gif": return "img";   case "png": return "img";   case "psd": return "img";   case "bmp": return "img";
           case "htm": return "htm";   case "xml": return "htm";   case "xht": return "htm";   case "sht": return "htm";
           case "asp": return "htm";   case "jsp": return "htm";   case "php": return "htm";   case "txt": return "txt";
           case "cfg": return "cfg";   case "dll": return "cfg";   case "ini": return "cfg";
           case "mp3": return "mp3";   case "wma": return "mp3";   case "ape": return "mp3";   case "wav": return "mp3";   case "mid": return "mp3";
           default: return "oth";
       }
    };
	
	$.fn.fileupload = function(options, method){
		if (typeof options == "string") {
			var fn = $.fn.fileupload.methods[options];
			if (fn) {
				return fn(this, method);
			}
		}
		options = options || {};
		return this.each(function() {
			var data = $.data(this, "fileupload");
			if (data) {
				$.extend(data.options, options);
			} else {
				data = $.data(this, "fileupload", {
					options : $.extend( {},
							$.fn.fileupload.defaults, 
							$.fn.fileupload.parseOptions(this), options)
				});
			}
			create(this);
			initData(this);
		});
	};
	
	$.fn.fileupload.parseOptions = function(target) {
		var t = $(target);
		return $.extend({},{
			width:t.attr("width"),
			height:t.attr("height"),
			name:t.attr("name"),
			multiple:t.attr("multiple")?true:undefined,
			value:t.attr("value"),
			flash_url:t.attr("flash_url"),
			upload_url:t.attr("upload_url"),
			delete_url:t.attr("delete_url"),
			init_url:t.attr("init_url"),
			post_params:t.attr("post_params")?eval("("+t.attr("post_params")+")"):undefined,
			file_size_limit:t.attr("file_size_limit"),
			file_upload_limit:t.attr("file_upload_limit")?parseInt(t.attr("file_upload_limit")):undefined,
			file_types:t.attr("file_types"),
			file_types_description:t.attr("file_types_description"),
			file_queue_limit:t.attr("file_queue_limit")?parseInt(t.attr("file_queue_limit")):undefined,
			showUploadBtn:t.attr("showUploadBtn")=='false'?false:undefined,
			uploadStart : t.attr("uploadStart")?eval(t.attr("uploadStart")):undefined,
			uploadComplete : t.attr("uploadComplete")?eval(t.attr("uploadComplete")):undefined,
			allUploadComplete:t.attr("allUploadComplete")?eval(t.attr("allUploadComplete")):undefined,
			uploadSuccess:t.attr("uploadSuccess")?eval(t.attr("uploadSuccess")):undefined,
			allUploadSuccess:t.attr("allUploadSuccess")?eval(t.attr("allUploadSuccess")):undefined,
			onDelete:t.attr("onDelete")?eval(t.attr("onDelete")):undefined,
			btn_add_text:t.attr("btn_add_text"),
			addonBeanName:t.attr("addonBeanName")
		});
	};
	
	$.fn.fileupload.methods = {
		options:function(jq){
			return $.data(jq[0], "fileupload").options;
		},
		swfupload:function(jq){
			return $.data(jq[0], "fileupload").swfupload;
		},
		getFileNames:function(jq){
			return getFileNames(jq[0]);
		},
		isAllUploadSuccess:function(jq){
			return isAllUploadSuccess(jq[0]);
		},
		isAllUploadComplete:function(jq){
			return isAllUploadComplete(jq[0]);
		},
		upload:function(jq){
			return jq.each(function(){
				upload(this);
			});
		},
		getFileList:function(jq){
			return getFileList(jq[0]);
		},
		bind:function(jq,param){
			return jq.each(function(){
				bind(this,param);
			});
		},
		addUploadedFile:function(jq,param){
			return jq.each(function(){
				addUploadedFile(this,param);
			});
		},
		deleteFile:function(jq,fileId){
			return jq.each(function(){
				deleteFile(this,fileId);
			});
		},
		setPostParam:function(jq,param){
			return jq.each(function(){
				setPostParam(this,param);
			});
		}
	};
	
	$.fn.fileupload.defaults = $.extend( {},{
		width:300,
		height:150,
		multiple:false,//是否是多文件上传
		name:"",
		value:"",
		flash_url:easyloader.base+"/js/swfupload/swfupload_fp10/swfupload.swf",//SWF插件所在路径
		upload_url:"",//文件上传的路径，必须是相对于SWF插件所在的路径，或者是绝对路径
		delete_url:"",//删除文件时调用的地址
		init_url:"",
		post_params:{},//传递的参数JSON，类型的参数
		file_size_limit:0,//文件大小限制，单位为KB，0表示无限制
		file_upload_limit:0,//允许上传的最多文件数量,0表示无限制
		file_types:"*.*",//可上传的文件类型，“如*.jpg;*.gif”，用分号隔开
		file_types_description:"All Files",//可上传的文件类型描述文字
		file_queue_limit:0,
		btn_add_text:"添加附件",
	    btn_up_text:"全部上传",
	    btn_cancel_text:"放弃",
	    btn_clean_text:"清空",
	    op_del_text:"单项删除",
	    op_up_text:"单项上传",
	    op_fail_text:"上传失败",
	    op_ok_text:"上传成功",
	    op_no_text:"取消上传",
	    addonBeanName:"",
	    showUploadBtn:true,
	    uploadStart:function(file){ //单文件上传开始时候触发
	    	return true;
	    },
	    uploadComplete:function(file){ //单文件上传完成后触发
	    },
	    allUploadComplete:function(){//所有文件上传完成后触发
	    },
	    uploadSuccess:function(file,data){//单文件上传成功后触发
	    },
	    allUploadSuccess:function(){//所有文件上传成功后触发
	    },
	    onDelete:function(file){
	    	
	    }
	});
	
})(jQuery);