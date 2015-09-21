package com.casic27.platform.common.menu.entity;

import java.util.HashMap;
import java.util.Map;


public class Menu {
	
	/**
	 * 主键ID
	 */
	private String zjId;
	
	/**
	 * 菜单名称
	 */
	private String cdmc;
	
	/**
	 * 菜单级别
	 */
	private String cdjb;
	
	/**
	 * 菜单序号
	 */
	private String cdxh;
	
	/**
	 * 上级菜单
	 */
	private String sjcd;
	
	/**
	 * 上级菜单名称
	 */
	private String sjcdCdmc;
	
	/**
	 * 菜单地址
	 */
	private String cddz;
	/**
	 * 是否最后一级菜单 0-否；1-是
	 */
	private String sfzhyicd;
	
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
	 * 记录删除事件
	 */
	private String jlscsj;
	
	/**
	 * 记录有效标志 -1-已归档；0-无效（已注销）；1-有效
	 */
	private String jlyxzt;
	
	/**
	 * menu与Tree树形结构之间的映射关系
	 */
	private static  Map<String, String> TreeMapper = new HashMap<String, String>();
	static{
		TreeMapper.put("nodeId", "zjId");
		TreeMapper.put("parentNodeId", "sjcd");
		TreeMapper.put("label", "cdmc");
	}
	
	public String getSfzhyicd() {
		return sfzhyicd;
	}

	public void setSfzhyicd(String sfzhyicd) {
		this.sfzhyicd = sfzhyicd;
	}

	public String getSybz() {
		return sybz;
	}

	public void setSybz(String sybz) {
		this.sybz = sybz;
	}

	public String getZjId() {
		return zjId;
	}

	public void setZjId(String zjId) {
		this.zjId = zjId;
	}

	public String getCdmc() {
		return cdmc;
	}

	public void setCdmc(String cdmc) {
		this.cdmc = cdmc;
	}

	public String getCdjb() {
		return cdjb;
	}

	public void setCdjb(String cdjb) {
		this.cdjb = cdjb;
	}

	public String getCdxh() {
		return cdxh;
	}

	public void setCdxh(String cdxh) {
		this.cdxh = cdxh;
	}

	public String getSjcd() {
		return sjcd;
	}

	public void setSjcd(String sjcd) {
		this.sjcd = sjcd;
	}

	public String getSjcdCdmc() {
		return sjcdCdmc;
	}

	public void setSjcdCdmc(String sjcdCdmc) {
		this.sjcdCdmc = sjcdCdmc;
	}

	public String getCddz() {
		return cddz;
	}

	public void setCddz(String cddz) {
		this.cddz = cddz;
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
	 * 获取菜单图标
	 * @return
	 */
	public static String getDefaultMenuIcon() {
		return "menu.gif";
	}
}
