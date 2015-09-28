package com.kcp.platform.bpm.entity;

import com.kcp.platform.util.StringUtils;

public class FormModel {
	/**
	 * 表单类型
	 */
	private String formType = "";

	/**
	 * 表单HTML
	 */
	private String formHtml = "";

	/**
	 * 表单明细URL
	 */
	private String detailUrl = "";

	/**
	 * 表单URL
	 */
	private String formUrl = "";
	
	/**
	 * 是否可用
	 */
	private boolean isValid = true;

	public String getFormType() {
		return this.formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	public String getFormHtml() {
		return this.formHtml;
	}

	public void setFormHtml(String formHtml) {
		this.formHtml = formHtml;
	}

	public String getDetailUrl() {
		return this.detailUrl;
	}

	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}

	public String getFormUrl() {
		return this.formUrl;
	}

	public void setFormUrl(String formUrl) {
		this.formUrl = formUrl;
	}

	public boolean isFormEmpty() {
		if(StringUtils.isEmpty(formType)){
			return true;
		}else{
			boolean rtn = false;
			int type = Integer.valueOf(formType);
			switch (type) {
				case 1:
					rtn = StringUtils.isEmpty(this.formHtml);
					break;
				case 2:
					rtn = StringUtils.isEmpty(this.formUrl);
			}
			return rtn;
		}
	}

	public boolean isValid() {
		return this.isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	
	public boolean isExtForm(){
		if(BpmNodeConfig.NODE_FORM_TYPE_URL.equals(formType)){
			return true;
		}else{
			return false;
		}
	}
}
