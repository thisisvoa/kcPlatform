package com.kcp.platform.common.role.entity;


/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 */
public class Role {
	/**
	 * 角色级别省级:10;
	 */
	public static final String ROLE_JSJB_SHENGJI = "10";
	
	/**
	 * 角色级别市级:30
	 */
	public static final String ROLE_JSJB_SHIJI = "20";
	
	/**
	 * 角色级别县级:30
	 */
	public static final String ROLE_JSJB_XIANJI = "30";
	
	/**
	 * 主键ID
	 */
	private String zjid;
	public String getZjid() {
		return zjid;
	}

	public void setZjid(String zjid) {
		this.zjid = zjid;
	}

	/**
	 * 角色名称
	 */
	private String jsmc;
	
	/**
	 * 角色类型 默认为01
	 */
	private String jslx;
	
	/**
	 * 角色级别 10表示省级、20表示市级、30表示县级
	 */
	private String jsjb;
	
	/**
	 * 使用标识 0-禁用；1-启用
	 */
	private String sybz;
	
	/**
	 * 备注
	 */
	private String bz;
	
	/**
	 * 创建用户
	 */
	private String cjyh;
	
	/**
	 * 更新用户
	 */
	private String gxyh;
	
	/**
	 * 记录新增时间
	 */
	private String jlxzsj;
	

	/**
	 * 记录更新时间
	 */
	private String jlxgsj;
	
	/**
	 * 记录删除时间
	 */
	private String jlscsj;
	
	/**
	 * 记录有效标志 -1-已归档；0-无效（已注销）；1-有效
	 */
	private String jlyxzt;
	
	/**
	 * 角色代码
	 */
	private String jsdm;
	

	public String getJsmc() {
		return jsmc;
	}

	public void setJsmc(String jsmc) {
		this.jsmc = jsmc;
	}

	public String getJslx() {
		return jslx;
	}

	public void setJslx(String jslx) {
		this.jslx = jslx;
	}

	public String getJsjb() {
		return jsjb;
	}

	public void setJsjb(String jsjb) {
		this.jsjb = jsjb;
	}

	public String getSybz() {
		return sybz;
	}

	public void setSybz(String sybz) {
		this.sybz = sybz;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getCjyh() {
		return cjyh;
	}

	public void setCjyh(String cjyh) {
		this.cjyh = cjyh;
	}

	public String getGxyh() {
		return gxyh;
	}

	public void setGxyh(String gxyh) {
		this.gxyh = gxyh;
	}


	public String getJlyxzt() {
		return jlyxzt;
	}

	public void setJlyxzt(String jlyxzt) {
		this.jlyxzt = jlyxzt;
	}

	public String getJsdm() {
		return jsdm;
	}

	public void setJsdm(String jsdm) {
		this.jsdm = jsdm;
	}
	
	public String getJlxzsj() {
		return jlxzsj;
	}

	public void setJlxzsj(String jlxzsj) {
		this.jlxzsj = jlxzsj;
	}

	public String getJlxgsj() {
		return jlxgsj;
	}

	public void setJlxgsj(String jlxgsj) {
		this.jlxgsj = jlxgsj;
	}

	public String getJlscsj() {
		return jlscsj;
	}

	public void setJlscsj(String jlscsj) {
		this.jlscsj = jlscsj;
	}
}
