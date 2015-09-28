package com.kcp.platform.ui.util;

/**
 * <pre>
 *  类描述：
 * </pre>
 * 
 * <pre>
 * </pre>
 * 
 */
public class StringBufferUtil {
	private StringBuffer sb;

	public StringBufferUtil(StringBuffer sb) {
		this.sb = sb;
	}

	public StringBuffer getStringBuffer() {
		return this.sb;
	}

	public StringBufferUtil appendln(String str) {
		this.sb.append(str);
		this.sb.append("\r\n");
		return this;
	}

	public StringBufferUtil appendln(boolean b) {
		this.sb.append(b);
		this.sb.append("\r\n");
		return this;
	}

	public StringBufferUtil appendln(char[] c) {
		this.sb.append(c);
		this.sb.append("\r\n");
		return this;
	}

	public StringBufferUtil appendln(Object o) {
		this.sb.append(o);
		this.sb.append("\r\n");
		return this;
	}

	public StringBufferUtil appendln(int i) {
		this.sb.append(i);
		this.sb.append("\r\n");
		return this;
	}

	public StringBufferUtil appendln(double d) {
		this.sb.append(d);
		this.sb.append("\r\n");
		return this;
	}

	public StringBufferUtil appendln(float f) {
		this.sb.append(f);
		this.sb.append("\r\n");
		return this;
	}

	public StringBufferUtil appendln(long l) {
		this.sb.append(l);
		this.sb.append("\r\n");
		return this;
	}

	public StringBufferUtil append(String str) {
		this.sb.append(str);
		return this;
	}

	public StringBufferUtil append(boolean b) {
		this.sb.append(b);
		return this;
	}

	public StringBufferUtil append(char[] c) {
		this.sb.append(c);
		return this;
	}

	public StringBufferUtil append(Object o) {
		this.sb.append(o);
		return this;
	}

	public StringBufferUtil append(int i) {
		this.sb.append(i);
		return this;
	}

	public StringBufferUtil append(double d) {
		this.sb.append(d);
		return this;
	}

	public StringBufferUtil append(float f) {
		this.sb.append(f);
		return this;
	}

	public StringBufferUtil append(long l) {
		this.sb.append(l);
		return this;
	}

	public String toString() {
		return this.sb.toString();
	}
}
