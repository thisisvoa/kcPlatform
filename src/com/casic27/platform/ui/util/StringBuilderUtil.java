/**
 * @(#)com.casic27.platform.ui.util.StringBuilderUtil.java
 * 版本声明：航天光达科技有限公司，版权所有 违者必究
 * 
 * <br> Copyright:Copyright(c) 2012
 * <br> Company:航天光达科技有限公司
 * <br> Date:2012-5-23
 *
 *-----------------------------------
 *
 * 修改记录
 *     修改者:
 *     修改时间:
 *     修改原因:
 *-----------------------------------
 */
package com.casic27.platform.ui.util;

public class StringBuilderUtil {

	private StringBuilder sb;

	public StringBuilderUtil(StringBuilder sb) {
		this.sb = sb;
	}

	public StringBuilder getStringBuffer() {
		return this.sb;
	}

	public StringBuilderUtil appendln(String str) {
		this.sb.append(str);
		this.sb.append("\r\n");
		return this;
	}

	public StringBuilderUtil appendln(boolean b) {
		this.sb.append(b);
		this.sb.append("\r\n");
		return this;
	}

	public StringBuilderUtil appendln(char[] c) {
		this.sb.append(c);
		this.sb.append("\r\n");
		return this;
	}

	public StringBuilderUtil appendln(Object o) {
		this.sb.append(o);
		this.sb.append("\r\n");
		return this;
	}

	public StringBuilderUtil appendln(int i) {
		this.sb.append(i);
		this.sb.append("\r\n");
		return this;
	}

	public StringBuilderUtil appendln(double d) {
		this.sb.append(d);
		this.sb.append("\r\n");
		return this;
	}

	public StringBuilderUtil appendln(float f) {
		this.sb.append(f);
		this.sb.append("\r\n");
		return this;
	}

	public StringBuilderUtil appendln(long l) {
		this.sb.append(l);
		this.sb.append("\r\n");
		return this;
	}

	public StringBuilderUtil append(String str) {
		this.sb.append(str);
		return this;
	}

	public StringBuilderUtil append(boolean b) {
		this.sb.append(b);
		return this;
	}

	public StringBuilderUtil append(char[] c) {
		this.sb.append(c);
		return this;
	}

	public StringBuilderUtil append(Object o) {
		this.sb.append(o);
		return this;
	}

	public StringBuilderUtil append(int i) {
		this.sb.append(i);
		return this;
	}

	public StringBuilderUtil append(double d) {
		this.sb.append(d);
		return this;
	}

	public StringBuilderUtil append(float f) {
		this.sb.append(f);
		return this;
	}

	public StringBuilderUtil append(long l) {
		this.sb.append(l);
		return this;
	}

	public String toString() {
		return this.sb.toString();
	}
}
