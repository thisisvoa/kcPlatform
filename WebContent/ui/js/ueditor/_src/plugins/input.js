/**
 * @description 输入框（按钮、单选按钮、复选框、单行文本、多行文本、下拉框等）
 * @name baidu.editor.execCommand
 * @param {String}
 *            cmdName 输入框
 * @author heyifan
 */
UE.plugins['input'] = function() {
	var me = this;
	me.commands['hiddeninput'] = {
	    execCommand : function(){
	    	alert(this.ui._dialogs['webapp']);
			return true;
	    }
	};
};
