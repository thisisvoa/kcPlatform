package com.kcp.platform.common.user.entity;

import java.util.HashMap;
import java.util.Map;

import com.kcp.platform.core.entity.BaseEntity;
import com.kcp.platform.sys.constants.CommonConst;

/**
 * <pre>
 * 类描述：用户组
 * </pre>
 * <pre>
 * </pre>
 */
public class UserGroup extends BaseEntity {
	/**
	 * 主键ID
	 */
	private String zjid;
	/**
	 * 用户组名称
	 */
	private String yhzmc;
	
	/**
	 * 用户组类型 预留字段
	 */
	private String yhzlx;
	
	/**
	 * 用户组级别 预留字段
	 */
	private String yhzjb;
	
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
	 * UserGroup与Tree树形结构之间的映射关系
	 */
	private static  Map<String, String> TreeMapper = new HashMap<String, String>();
	static{
		TreeMapper.put("nodeId", "zjid");
		TreeMapper.put("label", "yhzmc");
		TreeMapper.put("parentNodeId", "parentNodeId");
		TreeMapper.put("icon0", "userGroupIcon");
		TreeMapper.put("icon1", "userGroupIcon");
		TreeMapper.put("icon2", "userGroupIcon");		
	}
	
	
	/**
	 * UserGroup与Tree树形结构之间的映射关系(包含用户组中用户人数)
	 */
	private static  Map<String, String> userGroupTreeMapper = new HashMap<String, String>();
	static{
		userGroupTreeMapper.put("nodeId", "ZJID");
		userGroupTreeMapper.put("label", "YHZYHCOUNT");
		userGroupTreeMapper.put("parentNodeId", "PARENTNODEID");
		userGroupTreeMapper.put("icon0", "userGroupIcon");
		userGroupTreeMapper.put("icon1", "userGroupIcon");
		userGroupTreeMapper.put("icon2", "userGroupIcon");		
	}
	
	
	/**
	 * 树形图标
	 * @return
	 */
	public String getUserGroupIcon() {
		return "group.gif";
	}
	
	/**
	 * 父节点ID：用于构造树形用户组使用
	 * @return
	 */
	public String getParentNodeId(){
		return CommonConst.TREE_ROOT_ID;
	}
	
	
	public String getZjid() {
		return zjid;
	}

	public void setZjid(String zjid) {
		this.zjid = zjid;
	}

	public String getYhzmc() {
		return yhzmc;
	}

	public void setYhzmc(String yhzmc) {
		this.yhzmc = yhzmc;
	}

	public String getYhzlx() {
		return yhzlx;
	}

	public void setYhzlx(String yhzlx) {
		this.yhzlx = yhzlx;
	}

	public String getYhzjb() {
		return yhzjb;
	}

	public void setYhzjb(String yhzjb) {
		this.yhzjb = yhzjb;
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

	public static Map<String, String> getUserGroupTreeMapper() {
		return userGroupTreeMapper;
	}

	public static void setUserGroupTreeMapper(
			Map<String, String> userGroupTreeMapper) {
		UserGroup.userGroupTreeMapper = userGroupTreeMapper;
	}
	
}
