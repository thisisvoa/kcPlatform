package com.casic27.platform.common.para.entity;

import java.util.HashMap;
import java.util.Map;

public class Parameter {
	/**
	 * 主键ID
	 */
	private String zjId;
	
	/**
	 * 参数名称
	 */
	private String csmc;
	
	/**
	 * 参数代码
	 */
	private String csdm;
	
	/**
	 * 参数值
	 */
	private String csz;
	
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

	public String getZjId() {
		return zjId;
	}

	public void setZjId(String zjId) {
		this.zjId = zjId;
	}

	public String getCsmc() {
		return csmc;
	}

	public void setCsmc(String csmc) {
		this.csmc = csmc;
	}

	public String getCsdm() {
		return csdm;
	}

	public void setCsdm(String csdm) {
		this.csdm = csdm;
	}

	public String getCsz() {
		return csz;
	}

	public void setCsz(String csz) {
		this.csz = csz;
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
	
	private String parentNodeId;
	
	public String getParentNodeId() {
		return "0";
	}

	public void setParentNodeId(String parentNodeId) {
		this.parentNodeId = parentNodeId;
	}

	/**
	 * menu与Tree树形结构之间的映射关系
	 */
	private static  Map<String, String> TreeMapper = new HashMap<String, String>();
	static{
		TreeMapper.put("nodeId", "zjId");
		TreeMapper.put("parentNodeId", "parentNodeId");
		TreeMapper.put("label", "csmc");
	}
	public static Map<String, String> getTreeMapper() {
		return TreeMapper;
	}

	public static void setTreeMapper(Map<String, String> treeMapper) {
		TreeMapper = treeMapper;
	}
	
	/**
	 * 获取菜单图标
	 * @return
	 */
	public static String getDefaultParaIcon() {
		return "iconGraph.gif";
	}
}
