/**
 * 日期格式化
 */
DateFormat = (function(){     
	var SIGN_REGEXP = /([yMdhsm])(\1*)/g;     
	var DEFAULT_PATTERN = 'yyyy-MM-dd';     
	
	function padding(s,len){     
		var len =len - (s+'').length;     
		for(var i=0;i<len;i++){s = '0'+ s;}     
			return s;     
		};
		return({
			format: function(date,pattern){
				if(date==null){
					return "";
				}
				pattern = pattern||DEFAULT_PATTERN;
				return pattern.replace(SIGN_REGEXP,function($0){
					switch($0.charAt(0)){
						case 'y' : return padding(date.getFullYear(),$0.length);
						case 'M' : return padding(date.getMonth()+1,$0.length);
						case 'd' : return padding(date.getDate(),$0.length);
						case 'w' : return date.getDay()+1;
						case 'h' : return padding(date.getHours(),$0.length);
						case 'm' : return padding(date.getMinutes(),$0.length);
						case 's' : return padding(date.getSeconds(),$0.length);
					}
				});
			},
			parse: function(dateString,pattern){
				if(dateString==""){
					return null;
				}
				var matchs1=pattern.match(SIGN_REGEXP);     
				var matchs2=dateString.match(/(\d)+/g);     
				if(matchs1.length<=matchs2.length){     
					var _date = new Date(1970,0,1);     
					for(var i=0;i<matchs1.length;i++){  
						var _int = parseInt(matchs2[i]);
						var sign = matchs1[i];
						switch(sign.charAt(0)){
							case 'y' : _date.setFullYear(_int);break;     
							case 'M' : _date.setMonth(_int-1);break;     
							case 'd' : _date.setDate(_int);break;
							case 'h' : _date.setHours(_int);break;
							case 'm' : _date.setMinutes(_int);break;
							case 's' : _date.setSeconds(_int);break;
						}
					}
					return _date;     
				}
   				return null;
  			}
 		});    
})();

/**
 * String 上添加trim方法
 * @returns
 */
String.prototype.trim= function(){  
    // 用正则表达式将前后空格  
    // 用空字符串替代。  
    return this.replace(/(^\s*)|(\s*$)/g, "");
};
/////////////////////////////////////////////////////////////////////
//
//数组添加Array函数
//
/////////////////////////////////////////////////////////////////////
if(!Array.prototype.indexOf){
    Array.prototype.indexOf=function(el, index){
        var n = this.length>>>0, i = ~~index;
        if(i < 0) i += n;
        for(; i < n; i++) if(i in this && this[i] === el) return i;
        return -1;
    };
}

/////////////////////////////////////////////////////////////////////
//
// 系统框架辅助工具
//
/////////////////////////////////////////////////////////////////////
FrameHelper = {
	getTop:function(){
		var parentWindow = window;
		var parentStr = "";
		var i=0;
		while(i++<100){
			if((typeof parentWindow)==undefined){
				break;
			}else{
				if(parentWindow.SysFrame && parentWindow.SysFrame.sign=="$sysFrame$"){
					break;
				}
				parentStr += "parent.";
				parentWindow = eval(parentStr+'window');
			}
		}
		return parentWindow;
	}	
};

/////////////////////////////////////////////////////////////////////
//
// 消息提示框
//
/////////////////////////////////////////////////////////////////////
MessageUtil = {
	show:function(msg, title, timeout, showType){
		var topWin = FrameHelper.getTop();
		title = title||"系统提示";
		topWin.using("messager",function(){
			topWin.$.messager.show({
				title : title,
				msg : msg,
				timeout:timeout,
				showType:showType
			});	
		});
	},
	progress:function(msg,title){
		msg = msg||'数据加载中...';
		title = title||'请稍候';
		using("messager",function(){
			$.messager.progress({
				title : title,
				msg : msg 
			});
		});
	},
	closeProgress:function(){
		using("messager",function(){
			$.messager.progress('close');
		});
	},
	alert:function(msg, func, w, h){
		var topWin = FrameHelper.getTop();
		topWin.Dialog.alert(msg,func,w,h);
	},
	
	confirm:function(msg, funcOK, funcCal, w, h){
		var topWin = FrameHelper.getTop();
		topWin.Dialog.confirm(msg, funcOK, funcCal, w, h);
	}
};
/////////////////////////////////////////////////////////////////////
//
// ajax封装
//
/////////////////////////////////////////////////////////////////////
$.extend($, {
	/**
	 * 封装Jquery的post方法
	 * @param url
	 * @param data
	 * @param callback
	 * @param type
	 */
	postc:function(url,data, callback, failCallback, type){
		//封装url加入ajax请求标识
		url = wrapUrl(url);
		$.ajax({url: url,
			type: 'post',
			data:data,
			dataType: type||"text",
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				switch(textStatus){
					case "parsererror":
						MessageUtil.alert("数据转化出错！");
						break;
					case "timeout":
						MessageUitl.alert("请求超时!");
						break;
					case "error":
						onException(eval("(" + XMLHttpRequest.responseText + ")"));
						break;
					default:
						onException(eval("(" + XMLHttpRequest.responseText + ")"));
				}
				failCallback(errorThrown);
			},
			success: function(result){
				if("_ajax_unlogin_flag_"==result){
					MessageUtil.alert("会话已失效，请重新登录！",function(){
						var topWin = FrameHelper.getTop();
						topWin.location.href = "tologin.html";
					});
					return;
				}
				if(callback!=null){
					callback(result);
				}
			}
		});
		/**
		 * 异常处理
		 * @param data
		 */
		function onException(data){
			MessageUtil.closeProgress();
			if(data.errorMessage!=null){
				var msg = data.errorMessage;
				if(data.errorCode!=null && data.errorCode!=""){
					msg = data.errorCode+":"+msg;
				}
				//如果是未登录则要求重新登录
				if(errorCode="unlogin"){
					MessageUtil.alert(msg,function(){
						
					});
				}else{
					MessageUtil.alert(msg);
				}
			}else{
				MessageUtil.alert("远程调用出错，请联系管理员！");
			}
		}
		
		function wrapUrl(url){
		    if (url == null)
		        return url;
		    if (url.indexOf("?") != -1)
		        url += "&_ajax_request_flag_=true";
		    else
		        url += "?_ajax_request_flag_=true";
		    return url;
		}
	}

});
$.fn.outerHTML = function(){
    // IE, Chrome & Safari will comply with the non-standard outerHTML, all others (FF) will have a fall-back for cloning
    return (!this.length) ? this : (this[0].outerHTML || (
      function(el){
          var div = document.createElement('div');
          div.appendChild(el.cloneNode(true));
          var contents = div.innerHTML;
          div = null;
          return contents;
    })(this[0]));
 
}

var Validator = {
	// 验证身份证号方法
	checkIdCard : function(idcard) {
		var Errors = new Array("验证通过!", "身份证号码位数不对!", "身份证号码出生日期超出范围或含有非法字符!",
				"身份证号码校验错误!", "身份证地区非法!");
		var area = { 11 : "北京", 12 : "天津", 13 : "河北", 14 : "山西",
					 15 : "内蒙古", 21 : "辽宁", 22 : "吉林", 23 : "黑龙江",
					 31 : "上海", 32 : "江苏", 33 : "浙江", 34 : "安徽",
					 35 : "福建", 36 : "江西", 37 : "山东", 41 : "河南",
					 42 : "湖北", 43 : "湖南", 44 : "广东", 45 : "广西",
					 46 : "海南", 50 : "重庆", 51 : "四川", 52 : "贵州",
					 53 : "云南", 54 : "西藏", 61 : "陕西", 62 : "甘肃",
					 63 : "青海", 64 : "宁夏", 65 : "xinjiang", 71 : "台湾",
					 81 : "香港", 82 : "澳门", 91 : "国外"
		};
		var Y, JYM;
		var S, M;
		var idcard_array = new Array();
		idcard_array = idcard.split("");
		if (area[parseInt(idcard.substr(0, 2))] == null)
			return Errors[4];
		switch (idcard.length) {
			case 15:
				if ((parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0
						|| ((parseInt(idcard.substr(6, 2)) + 1900) % 100 == 0 && (parseInt(idcard
								.substr(6, 2)) + 1900) % 4 == 0)) {
					ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;// 测试出生日期的合法性
				} else {
					ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;// 测试出生日期的合法性
				}
				if (ereg.test(idcard))
					return true;
				else{
					return Errors[2];
				}
				break;
			case 18:
				if (parseInt(idcard.substr(6, 4)) % 4 == 0
						|| (parseInt(idcard.substr(6, 4)) % 100 == 0 && parseInt(idcard
								.substr(6, 4)) % 4 == 0)) {
					ereg = /^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;// 闰年出生日期的合法性正则表达式
				} else {
					ereg = /^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;// 平年出生日期的合法性正则表达式
				}
				if (ereg.test(idcard)) {
					S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7
							+ (parseInt(idcard_array[1]) + parseInt(idcard_array[11]))
							* 9
							+ (parseInt(idcard_array[2]) + parseInt(idcard_array[12]))
							* 10
							+ (parseInt(idcard_array[3]) + parseInt(idcard_array[13]))
							* 5
							+ (parseInt(idcard_array[4]) + parseInt(idcard_array[14]))
							* 8
							+ (parseInt(idcard_array[5]) + parseInt(idcard_array[15]))
							* 4
							+ (parseInt(idcard_array[6]) + parseInt(idcard_array[16]))
							* 2 + parseInt(idcard_array[7]) * 1
							+ parseInt(idcard_array[8]) * 6 + parseInt(idcard_array[9])
							* 3;
					Y = S % 11;
					M = "F";
					JYM = "10X98765432";
					M = JYM.substr(Y, 1);
					if (M == idcard_array[17])
						return true;
					else
						return Errors[3];
				} else
					return Errors[2];
				break;
			default:
				return Errors[1];
			break;
		}
	}
};
/////////////////////////////////////////////////////////////////////
//
//经常使用的工具方法
//
/////////////////////////////////////////////////////////////////////
Utils = {
	wrapUrl:function(url,paramName,paramVal){
	    if (url == null)
	        return url;
	    if (url.indexOf("?") != -1)
	        url += "&"+paramName+"="+paramVal;
	    else
	        url += "?"+paramName+"="+paramVal;
	    return url;
	},
	escapeHtml:function(str) {
		  str = String(str);
		  str = str.replace(/&/g, '&amp;');
		  str = str.replace(/\|/g, '&#124;');
		  str = str.replace(/</g, '&lt;');
		  str = str.replace(/>/g, '&gt;');
		  str = str.replace(/"/g, '&quot;');
		  str = str.replace(/'/g, '&#039;');
		  return str;
	},
	isIE6:function(){
	    if ($.browser.msie) {
	        if ($.browser.version == "6.0") return true;
	    }
	    return false;
	}
}

