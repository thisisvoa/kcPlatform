package com.casic27.platform.common.func.entity;

import java.util.HashMap;
import java.util.Map;

public class Function {
	/**
	 * 主键ID
	 */
	private String zjId;
	
	/**
	 * 功能名称
	 */
	private String gnmc;
	
	/**
	 * 功能代码
	 */
	private String gndm;
	
	/**
	 * 菜单主键ID
	 */
	private String cdxxZjId;
	
	/**
	 * 菜单名称
	 */
	private String cdxxCdmc;
	
	/**
	 * 功能序号
	 */
	private String gnxh;
	
	/**
	 * 使用标识
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
	 * 记录有效标志
	 */
	private String jlyxzt;
	
	/**
	 * Function与Tree树形结构之间的映射关系
	 */
	private static  Map<String, String> TreeMapper = new HashMap<String, String>();
	static{
		TreeMapper.put("nodeId", "zjId");
		TreeMapper.put("parentNodeId", "cdxxZjId");
		TreeMapper.put("label", "gnmc");
	}
	
	public String getZjId() {
		return zjId;
	}

	public void setZjId(String zjId) {
		this.zjId = zjId;
	}

	public String getGnmc() {
		return gnmc;
	}

	public void setGnmc(String gnmc) {
		this.gnmc = gnmc;
	}

	public String getGndm() {
		return gndm;
	}

	public void setGndm(String gndm) {
		this.gndm = gndm;
	}

	public String getCdxxZjId() {
		return cdxxZjId;
	}

	public void setCdxxZjId(String cdxxZjId) {
		this.cdxxZjId = cdxxZjId;
	}

	public String getCdxxCdmc() {
		return cdxxCdmc;
	}

	public void setCdxxCdmc(String cdxxCdmc) {
		this.cdxxCdmc = cdxxCdmc;
	}

	public String getGnxh() {
		return gnxh;
	}

	public void setGnxh(String gnxh) {
		this.gnxh = gnxh;
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
	
	public static Map<String, String> getTreeMapper() {
		return TreeMapper;
	}

	public static void setTreeMapper(Map<String, String> treeMapper) {
		TreeMapper = treeMapper;
	}
	
	/**
	 * 获取功能默认图标
	 * @return
	 */
	public static String getDefaultFuncIcon() {
		return "func.gif";
	}
}
