
package com.kcp.platform.util.jdbc;

/**
 * 
 * 类描述：
 * 
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
