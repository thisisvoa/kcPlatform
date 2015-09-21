/**
 * @(#)com.casic27.platform.common.code.entity.Code.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：2012-5-15
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.common.code.entity;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *类描述：代码信息
 * 
 *@Author： 宗斌(zongbin@casic27.com)
 *@Version：1.0
 */
public class Code {
	
	
	
	
	/**
	 * 主键ID
	 */
	private String zjid;
	
	/**
	 * 代码名称
	 */
	private String dmmc;
	
	/**
	 * 代码简称
	 */
	private String dmjc;
	
	/**
	 * 是否代码项
	 */
	private String sfdmx;
	
	/**
	 * 代码项编号
	 */
	private String dmx_bh;
	
	/**
	 * 代码项名称
	 */
	private String dmx_mc;
	
	/**
	 * 排序号
	 */
	private String pxh;
	
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
	 * 代码与Tree树形结构之间的映射关系
	 */
	private static  Map<String, String> CodeTreeMapper = new HashMap<String, String>();
	static{
		CodeTreeMapper.put("nodeId", "zjid");
		CodeTreeMapper.put("label", "dmmc");
		CodeTreeMapper.put("icon0", "code");
		CodeTreeMapper.put("icon1", "code");
		CodeTreeMapper.put("icon2", "code");		
	}
	
	/**
	 * 代码项与Tree树形结构之间的映射关系
	 */
	private static  Map<String, String> CodeOptionTreeMapper = new HashMap<String, String>();
	
	/**
	 * 所有代码的父节点ID
	 */
	public static String TREEID_ALLCODE = "ALLCODE";
	public static Map<String, String> getCodeTreeMapper() {
		return CodeTreeMapper;
	}

	public static void setCodeTreeMapper(Map<String, String> codeTreeMapper) {
		CodeTreeMapper = codeTreeMapper;
	}

	public static Map<String, String> getCodeOptionTreeMapper() {
		return CodeOptionTreeMapper;
	}

	public static void setCodeOptionTreeMapper(
			Map<String, String> codeOptionTreeMapper) {
		CodeOptionTreeMapper = codeOptionTreeMapper;
	}

	static{
		CodeOptionTreeMapper.put("nodeId", "zjid");
		CodeOptionTreeMapper.put("label", "dmx_mc");
		CodeOptionTreeMapper.put("icon0", "codeOption");
		CodeOptionTreeMapper.put("icon1", "codeOption");
		CodeOptionTreeMapper.put("icon2", "codeOption");		
	}
	
	public String getZjid() {
		return zjid;
	}

	public void setZjid(String zjid) {
		this.zjid = zjid;
	}

	public String getDmmc() {
		return dmmc;
	}

	public void setDmmc(String dmmc) {
		this.dmmc = dmmc;
	}

	public String getDmjc() {
		return dmjc;
	}

	public void setDmjc(String dmjc) {
		this.dmjc = dmjc;
	}

	public String getSfdmx() {
		return sfdmx;
	}

	public void setSfdmx(String sfdmx) {
		this.sfdmx = sfdmx;
	}

	public String getDmx_bh() {
		return dmx_bh;
	}

	public void setDmx_bh(String dmxBh) {
		dmx_bh = dmxBh;
	}

	public String getDmx_mc() {
		return dmx_mc;
	}

	public void setDmx_mc(String dmxMc) {
		dmx_mc = dmxMc;
	}

	public String getPxh() {
		return pxh;
	}

	public void setPxh(String pxh) {
		this.pxh = pxh;
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

	public String getJlyxzt() {
		return jlyxzt;
	}

	public void setJlyxzt(String jlyxzt) {
		this.jlyxzt = jlyxzt;
	}

	

}
