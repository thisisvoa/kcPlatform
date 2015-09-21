/**
 * @(#)com.casic27.platform.common.org.entity.Org.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：2012-4-24
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.common.org.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.casic27.platform.core.entity.BaseEntity;

/**
 *
 *类描述：
 * 
 *@Author： 宗斌(zongbin@casic27.com)
 *@Version：1.0
 */
public class Org extends BaseEntity {
	
	

	/** 主键ID  */
	private  String zjid;
	
	/**单位编号*/
	private String dwbh;
	
	/**单位名称 */
	private String dwmc;
	
	/**单位简称*/
	private String dwjc;
	
	/**单位电话*/
	private String dwdh;
	
	/**单位邮箱*/
	private String dwyx;
	
	/**单位负责人*/
	private String dwfzr;
	
	/**上级单位主键ID*/
	private String sjdw_zjid;
	
	/**单位类型--预留字段*/
	
	private String dwlx;
	
	/**单位级别--预留字段*/
	private String dwjb;
	
	/**备注*/
	private String bz;
	
	/**创建用户*/
	private String cjyh;
	
	/**更新用户*/
	private String gxyh;
	
	/**记录新增时间*/
	private String jlxzsj;
	
	/**记录更新时间*/
	private String jlxgsj;
	
	/**记录删除时间*/
	private String jlscsj;
	
	/**记录有效标示：1-已归档；0-无效(已注销);1-有效*/
	private String jlyxzt;
	
	/**
	 * 树形图标
	 * @return
	 */
	public String getOrgIcon() {
		return "group.gif";
	}
	/**
	 * org与Tree树形结构之间的映射关系
	 */
	private static  Map<String, String> TreeMapper = new HashMap<String, String>();
	static{
		TreeMapper.put("nodeId", "zjid");
		TreeMapper.put("parentNodeId", "sjdw_zjid");
		TreeMapper.put("label", "dwmc");
		TreeMapper.put("icon0", "orgIcon");
		TreeMapper.put("icon1", "orgIcon");
		TreeMapper.put("icon2", "orgIcon");		
	}
	
	public String getZjid() {
		return zjid;
	}
	public void setZjid(String zjid) {
		this.zjid = zjid;
	}
	
	public String getDwbh() {
		return dwbh;
	}
	public void setDwbh(String dwbh) {
		this.dwbh = dwbh;
	}
	public String getDwmc() {
		return dwmc;
	}
	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}
	public String getDwjc() {
		return dwjc;
	}
	public void setDwjc(String dwjc) {
		this.dwjc = dwjc;
	}
	public String getDwdh() {
		return dwdh;
	}
	public void setDwdh(String dwdh) {
		this.dwdh = dwdh;
	}
	public String getDwyx() {
		return dwyx;
	}
	public void setDwyx(String dwyx) {
		this.dwyx = dwyx;
	}
	public String getDwfzr() {
		return dwfzr;
	}
	public void setDwfzr(String dwfzr) {
		this.dwfzr = dwfzr;
	}
	public String getSjdw_zjid() {
		return sjdw_zjid;
	}
	public void setSjdw_zjid(String sjdwZjid) {
		sjdw_zjid = sjdwZjid;
	}
	public String getDwlx() {
		return dwlx;
	}
	public void setDwlx(String dwlx) {
		this.dwlx = dwlx;
	}
	public String getDwjb() {
		return dwjb;
	}
	public void setDwjb(String dwjb) {
		this.dwjb = dwjb;
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
}
