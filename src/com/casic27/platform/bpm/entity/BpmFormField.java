/**
 * @(#)com.casic27.platform.bpm.entity.BpmFormField
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright: Copyright(c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
 
package com.casic27.platform.bpm.entity;

import java.util.HashMap;
import java.util.Map;

import com.casic27.platform.util.JsonParser;
import com.casic27.platform.util.StringUtils;


public class BpmFormField{

	/**
	 * 
	 */
	private String fieldId;

	/**
	 * 所属表
	 */
	private String tableId;

	/**
	 * 字段名称
	 */
	private String fieldName;

	/**
	 * 字段类型
	 */
	private String fieldType;

	/**
	 * 是否必填
	 */
	private String isRequired;

	/**
	 * 是否列表显示
	 */
	private String isList;

	/**
	 * 是否查询显示
	 */
	private String isQuery;

	/**
	 * 列描述
	 */
	private String fieldDesc;

	/**
	 * 字符长度
	 */
	private Integer charLen;

	/**
	 * 整数长度
	 */
	private Integer intLen;

	/**
	 * 小数长度
	 */
	private Integer decimalLen;

	/**
	 * 字典表类型
	 */
	private String dictType;

	/**
	 * 是否删除
	 */
	private String isDeleted;

	/**
	 * 验证规则
	 */
	private String validRule;

	/**
	 * 字段原名
	 */
	private String originalName;

	/**
	 * 排列顺序
	 */
	private Integer sn;

	/**
	 * 值来源0 - 表单 1 - 脚本
	 */
	private String valueFrom;

	/**
	 * 默认脚本
	 */
	private String script;

	/**
	 * 0,无特殊控件
	 */
	private String controlType;

	/**
	 * 是否隐藏
	 */
	private String isHidden;

	/**
	 * 是否流程变量
	 */
	private String isFlowvar;

	/**
	 * 
	 */
	private String identity;

	/**
	 * 选项
	 */
	private String options;

	/**
	 * 控件属性
	 */
	private String ctlProperty;
	
	/**
	 * 选项数据来源
	 */
	private String optionFrom;


	public String getFieldId(){
		return this.fieldId;
	}
	
	public void setFieldId(String fieldId){
		this.fieldId = fieldId;
	}

	public String getTableId(){
		return this.tableId;
	}
	
	public void setTableId(String tableId){
		this.tableId = tableId;
	}

	public String getFieldName(){
		return this.fieldName;
	}
	
	public void setFieldName(String fieldName){
		this.fieldName = fieldName;
	}

	public String getFieldType(){
		return this.fieldType;
	}
	
	public void setFieldType(String fieldType){
		this.fieldType = fieldType;
	}

	public String getIsRequired(){
		return this.isRequired;
	}
	
	public void setIsRequired(String isRequired){
		this.isRequired = isRequired;
	}

	public String getIsList(){
		return this.isList;
	}
	
	public void setIsList(String isList){
		this.isList = isList;
	}

	public String getIsQuery(){
		return this.isQuery;
	}
	
	public void setIsQuery(String isQuery){
		this.isQuery = isQuery;
	}

	public String getFieldDesc(){
		return this.fieldDesc;
	}
	
	public void setFieldDesc(String fieldDesc){
		this.fieldDesc = fieldDesc;
	}

	public Integer getCharLen(){
		return this.charLen;
	}
	
	public void setCharLen(Integer charLen){
		this.charLen = charLen;
	}

	public Integer getIntLen(){
		return this.intLen;
	}
	
	public void setIntLen(Integer intLen){
		this.intLen = intLen;
	}

	public Integer getDecimalLen(){
		return this.decimalLen;
	}
	
	public void setDecimalLen(Integer decimalLen){
		this.decimalLen = decimalLen;
	}

	public String getDictType(){
		return this.dictType;
	}
	
	public void setDictType(String dictType){
		this.dictType = dictType;
	}

	public String getIsDeleted(){
		return this.isDeleted;
	}
	
	public void setIsDeleted(String isDeleted){
		this.isDeleted = isDeleted;
	}

	public String getValidRule(){
		return this.validRule;
	}
	
	public void setValidRule(String validRule){
		this.validRule = validRule;
	}

	public String getOriginalName(){
		return this.originalName;
	}
	
	public void setOriginalName(String originalName){
		this.originalName = originalName;
	}

	public Integer getSn(){
		return this.sn;
	}
	
	public void setSn(Integer sn){
		this.sn = sn;
	}

	public String getValueFrom(){
		return this.valueFrom;
	}
	
	public void setValueFrom(String valueFrom){
		this.valueFrom = valueFrom;
	}

	public String getScript(){
		return this.script;
	}
	
	public void setScript(String script){
		this.script = script;
	}

	public String getControlType(){
		return this.controlType;
	}
	
	public void setControlType(String controlType){
		this.controlType = controlType;
	}

	public String getIsHidden(){
		return this.isHidden;
	}
	
	public void setIsHidden(String isHidden){
		this.isHidden = isHidden;
	}

	public String getIsFlowvar(){
		return this.isFlowvar;
	}
	
	public void setIsFlowvar(String isFlowvar){
		this.isFlowvar = isFlowvar;
	}

	public String getIdentity(){
		return this.identity;
	}
	
	public void setIdentity(String identity){
		this.identity = identity;
	}

	public String getOptions(){
		return this.options;
	}
	
	public void setOptions(String options){
		this.options = options;
	}

	public String getCtlProperty(){
		return this.ctlProperty;
	}
	
	public void setCtlProperty(String ctlProperty){
		this.ctlProperty = ctlProperty;
	}
	
	public String getOptionFrom() {
		return optionFrom;
	}

	public void setOptionFrom(String optionFrom) {
		this.optionFrom = optionFrom;
	}
	
	public Map<String, String> getPropertyMap(){
		if(StringUtils.isNotEmpty(ctlProperty)){
			return JsonParser.parseJsonStr(ctlProperty);
		}else{
			return new HashMap<String, String>();
		}
	}
}
