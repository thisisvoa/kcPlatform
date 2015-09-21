/**
 * 字段类型改变
 */
function onFieldTypeChange(){
	var fieldType= $("#fieldType").combobox("getValue");
	switch(fieldType){
		case "varchar":
			$("#spanCharLen").show();
			$("#spanIntLen").hide();
			$("#spanDecimalLen").hide();
			$("#trDateFormat").hide();
			$("#controlType").combobox("disable",false);
			break;
		case "number":
			$("#spanCharLen").hide();
			$("#spanIntLen").show();
			$("#spanDecimalLen").show();
			$("#trDateFormat").hide();
			$("#controlType").combobox("disable",false);
			break;
		case "date":
			$("#spanCharLen").hide();
			$("#spanIntLen").hide();
			$("#spanDecimalLen").hide();
			$("#trDateFormat").show();
			$("#controlType").combobox("setValue","8");
			$("#controlType").combobox("disable",true);
			break;
		case "clob":
			$("#spanCharLen").hide();
			$("#spanIntLen").hide();
			$("#spanDecimalLen").hide();
			$("#trDateFormat").hide();
			$("#controlType").combobox("disable",false);
			break;
	}
}

/**
 * 控件类型改变
 */
function controlTypeChange(){
	var controlType= $("#controlType").combobox("getValue");
	switch(controlType){
		case '1':
			$("#spanValidRule").hide();
			$("#trOptionFrom").hide();
			$("#trDictType").hide();
			$("#trOptions").hide();
			$("#trUrl").hide();
			break;
		case '2':
		case '3':
		case '8':
		case '9':
		case '10':
		case '11':
		case '12':
			$("#spanValidRule").show();
			$("#trOptionFrom").hide();
			$("#trDictType").hide();
			$("#trOptions").hide();
			$("#trUrl").hide();
			break;
		case '4':
		case '5':
		case '6':
		case '7':
			$("#spanValidRule").show();
			$("#trOptionFrom").show();
			optionFromChange();
			break;
			
	}
}
/**
 * 选项来源选项改变
 */
function optionFromChange(){
	var optionForm = $("#optionFrom").combobox("getValue");
	switch(optionForm){
		case "1":
			$("#trDictType").hide();
			$("#trOptions").show();
			$("#trUrl").hide();
			$("#trKeyValue").hide();
			$("#trPId").hide();
			break;
		case "2":
			$("#trDictType").show();
			$("#trOptions").hide();
			$("#trUrl").hide();
			$("#trKeyValue").hide();
			$("#trPId").hide();
			break;
	}
}
/**
 * 获取数据
 * @returns
 */

function getFieldValue(){
	var field = {fieldId:"",fieldDesc:"",fieldName:"",fieldType:"",charLen:"",intLen:"", decimalLen:"", 
					isRequired:"0", isList:"0", isQuery:"0", isFlowvar:"0",valueFrom:"",controlType:"", validRule:"",
					optionFrom:"",dictType:"",options:"",url:"",ctlProperty:{}};
	field.fieldId = $("#fieldId").val();
	field.fieldDesc = $("#fieldDesc").val();
	field.fieldName = $("#fieldName").val();
	field.fieldType = $("#fieldType").combobox("getValue");
	switch(field.fieldType){
		case "varchar":
			field.charLen = $("#charLen").val();
			break;
		case "number":
			field.intLen = $("#intLen").val();
			field.decimalLen = $("#decimalLen").val();
			break;
		case "date":
			var ctlProperty = field.ctlProperty;
			ctlProperty.dateFormat = $("#dateFormat").combobox("getValue");
			if($("#isCurrentDate").attr("checked")){
				ctlProperty.isCurrentDate = "1";
			}else{
				ctlProperty.isCurrentDate = "0";
			}
			break;
		case "clob":
			break;
	}
	if($("#isHidden").attr("checked")){
		field.isHidden="1";
	}else{
		field.isHidden="0";
	}
	
	if($("#isRequired").attr("checked")){
		field.isRequired="1";
	}else{
		field.isRequired="0";
	}
	if($("#isList").attr("checked")){
		field.isList="1";
	}else{
		field.isList="0";
	}
	if($("#isQuery").attr("checked")){
		field.isQuery="1";
	}
	if($("#isFlowvar").attr("checked")){
		field.isFlowvar="1";
	}else{
		field.isFlowvar="0";
	}
	field.valueFrom = $("#valueFrom").val();
	field.controlType = $("#controlType").combobox("getValue");
	switch(field.controlType){
		case '1':
			break;
		case '2':
		case '3':
		case '8':
		case '9':
		case '10':
		case '11':
		case '12':
			field.validRule = $("#validRule").combobox("getValue");
			break;
		case '4':
		case '5':
		case '6':
		case '7':
			field.optionFrom = $("#optionFrom").combobox("getValue");
			switch(field.optionFrom){
				case "1":
					field.options = $("#options").val();
					break;
				case "2":
					field.dictType = $("#dictType").combobox("getValue");
					break;
			}
	}
	field.ctlProperty = JSON.stringify(field.ctlProperty);
	return field;
}
/**
 * 初始化数据
 * @param field
 */
function initFieldValue(field){
	if(field.ctlProperty==null||field.ctlProperty==""){
		field.ctlProperty = {};
	}else{
		field.ctlProperty = eval("("+field.ctlProperty+")");
	}

	$("#fieldId").val(field.fieldId);
	$("#fieldDesc").val(field.fieldDesc);
	$("#fieldName").val(field.fieldName);
	$("#fieldType").combobox("setValue",field.fieldType);
	switch(field.fieldType){
		case "varchar":
			 $("#charLen").val(field.charLen);
			break;
		case "number":
			$("#intLen").val(field.intLen);
			$("#decimalLen").val(field.decimalLen);
			break;
		case "date":
			var ctlProperty = field.ctlProperty;
			$("#dateFormat").combobox("setValue",ctlProperty.dateFormat);
			if(ctlProperty.isCurrentDate=="1"){
				$("#isCurrentDate").checkbox("check");
			}
			break;
		case "clob":
			break;
	}
	if(field.isHidden=="1"){
		$("#isHidden").checkbox("check");
	}
	
	if(field.isRequired=="1"){
		$("#isRequired").checkbox("check");
	}
	if(field.isList=="1"){
		$("#isList").checkbox("check");
	}
	if(field.isQuery=="1"){
		$("#isQuery").checkbox("check");
	}
	if(field.isFlowvar=="1"){
		$("#isFlowvar").checkbox("check");
	}
	$("#valueFrom").val(field.valueFrom);
	$("#controlType").combobox("setValue", field.controlType);
	switch(field.controlType){
		case '1':
			break;
		case '2':
		case '3':
		case '8':
		case '9':
		case '10':
		case '11':
		case '12':
			//$("#validRule").combobox("setValue",field.validRule);
			$("#validRule").attr("selValue",field.validRule);
			break;
		case '4':
		case '5':
		case '6':
		case '7':
			$("#optionFrom").combobox("setValue",field.optionFrom);
			switch(field.optionFrom){
				case "1":
					$("#options").val(field.options);
					break;
				case "2":
					//$("#dictType").combobox("setValue",field.dictType);
					$("#dictType").attr("selValue", field.dictType);
					break;
			}
	}
}