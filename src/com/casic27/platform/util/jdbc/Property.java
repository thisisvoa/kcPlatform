/**
 * @(#)IViewer.constants.sfddsf.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：2012-7-4
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */

package com.casic27.platform.util.jdbc;

/**
 * 
 * 类描述：
 * 
 * @Author： 宗斌(zongbin@casic27.com)
 * @Version：1.0
 */

public class Property {

	private String localName;
	private String displayName;
	private Boolean LNDateTime;
	private Boolean isLabel;
	private Boolean isToolTips;
	private String codeTab;

	public Property() {
		localName = "";
		displayName = "";
		codeTab = "";
	}

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Boolean getLNDateTime() {
		return LNDateTime;
	}

	public void setLNDateTime(Boolean dateTime) {
		LNDateTime = dateTime;
	}

	public Boolean getIsLabel() {
		return isLabel;
	}

	public void setIsLabel(Boolean isLabel) {
		this.isLabel = isLabel;
	}

	public Boolean getIsToolTips() {
		return isToolTips;
	}

	public void setIsToolTips(Boolean isToolTips) {
		this.isToolTips = isToolTips;
	}

	public String getCodeTab() {
		return codeTab;
	}

	public void setCodeTab(String codeTab) {
		this.codeTab = codeTab;
	}

}
