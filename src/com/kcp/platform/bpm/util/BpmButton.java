package com.kcp.platform.bpm.util;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "button")
@XmlAccessorType(XmlAccessType.NONE)
public class BpmButton {

	@XmlAttribute
	protected Integer start;

	@XmlAttribute
	protected Integer first;

	@XmlAttribute
	protected Integer common;

	@XmlAttribute
	protected Integer sign;

	@XmlAttribute
	protected Integer operatortype;

	@XmlAttribute
	protected Integer script;

	@XmlAttribute
	protected String text;

	@XmlAttribute
	protected Integer init;

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getFirst() {
		return first;
	}

	public void setFirst(Integer first) {
		this.first = first;
	}

	public Integer getCommon() {
		return common;
	}

	public void setCommon(Integer common) {
		this.common = common;
	}

	public Integer getSign() {
		return sign;
	}

	public void setSign(Integer sign) {
		this.sign = sign;
	}

	public Integer getOperatortype() {
		return this.operatortype;
	}

	public void setOperatortype(Integer operatortype) {
		this.operatortype = operatortype;
	}

	public Integer getScript() {
		return this.script;
	}

	public void setScript(Integer script) {
		this.script = script;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getInit() {
		return this.init;
	}

	public void setInit(Integer init) {
		this.init = init;
	}
}