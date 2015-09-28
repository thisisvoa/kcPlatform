package com.kcp.platform.common.user.entity;

import com.kcp.platform.core.entity.BaseEntity;
import com.kcp.platform.sys.constants.CommonConst;

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 */
public class User extends BaseEntity {
	/**
	 * 主键ID
	 */
	private String zjid;
	
	/**
	 * 用户名称 
	 */
	private String yhmc;
	
	/**
	 * 用户登录账号
	 */
	private String yhdlzh;
	
	/**
	 * 身份证号
	 */
	private String sfzh;
	
	/**
	 * 用户登录密码
	 */
	private String yhdlmm;
	
	/**
	 * 警员编号
	 */
	private String  jybh;
	
	/**
	 * 所属单位主键ID
	 */
	private String ssdw_zjid;
	
	/**
	 * 性别
	 */
	private String xb;
	/**
	 * 联系电话
	 */
	private String lxdh;
	
	/**
	 * 移动电话
	 */
	private String yddh;
	
	/**
	 * 电子邮箱
	 */
	private String dzyx;
	
	/**
	 * 用户类型 预留字段
	 */
	private String yhlx;
	
	/**
	 * 用户级别 预留字段
	 */
	private String yhjb;
	
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
	 * 树形图标
	 * @return
	 */
	public String getUserIcon() {
		return "user.gif";
	}
	public String getJlyxzt() {
		return jlyxzt;
	}

	public void setJlyxzt(String jlyxzt) {
		this.jlyxzt = jlyxzt;
	}
	
	public String getZjid() {
		return zjid;
	}

	public void setZjid(String zjid) {
		this.zjid = zjid;
	}

	public String getYhmc() {
		return yhmc;
	}

	public void setYhmc(String yhmc) {
		this.yhmc = yhmc;
	}

	public String getYhdlzh() {
		return yhdlzh;
	}

	public void setYhdlzh(String yhdlzh) {
		this.yhdlzh = yhdlzh;
	}

	public String getYhdlmm() {
		return yhdlmm;
	}

	public void setYhdlmm(String yhdlmm) {
		this.yhdlmm = yhdlmm;
	}

	public String getJybh() {
		return jybh;
	}

	public void setJybh(String jybh) {
		this.jybh = jybh;
	}

	public String getSsdw_zjid() {
		return ssdw_zjid;
	}

	public void setSsdw_zjid(String ssdw_zjid) {
		this.ssdw_zjid = ssdw_zjid;
	}

	public String getXb() {
		return xb;
	}

	public void setXb(String xb) {
		this.xb = xb;
	}

	public String getLxdh() {
		return lxdh;
	}

	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}

	public String getYddh() {
		return yddh;
	}

	public void setYddh(String yddh) {
		this.yddh = yddh;
	}

	public String getDzyx() {
		return dzyx;
	}

	public void setDzyx(String dzyx) {
		this.dzyx = dzyx;
	}

	public String getYhlx() {
		return yhlx;
	}

	public void setYhlx(String yhlx) {
		this.yhlx = yhlx;
	}

	public String getYhjb() {
		return yhjb;
	}

	public void setYhjb(String yhjb) {
		this.yhjb = yhjb;
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
	
	public String getSfzh() {
		return sfzh;
	}
	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}

	public boolean isSuperAdmin(){
		return CommonConst.SUPERADMIN_LOGINID.equals(this.yhdlzh);
	}
	
	public boolean equals(Object rhs){
		if ((rhs instanceof User)) {
			return this.zjid.equals(((User)rhs).getZjid());
		}
		return false;
	}
}
