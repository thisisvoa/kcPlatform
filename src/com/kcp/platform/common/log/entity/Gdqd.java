package com.kcp.platform.common.log.entity;

import java.util.Date;

public class Gdqd{

	/**
	 * 清单ID
	 */
	private String zjid;

	/**
	 * 归档配置ID
	 */
	private String gdpzZjid;

	/**
	 * 归档表名称
	 */
	private String gdbmc;

	/**
	 * 归档后的表名称
	 */
	private String gdhbmc;

	/**
	 * 归档数据开始时间
	 */
	private Date gdsjKssj;

	/**
	 * 归档数据结束时间
	 */
	private Date gdsjJssj;

	/**
	 * 归档记录数
	 */
	private Integer gdjls;

	/**
	 * 清单创建时间
	 */
	private Date jlcjsj;
	
	private String gdmc;


	public String getGdmc() {
		return gdmc;
	}

	public void setGdmc(String gdmc) {
		this.gdmc = gdmc;
	}

	public String getZjid(){
		return this.zjid;
	}
	
	public void setZjid(String zjid){
		this.zjid = zjid;
	}

	public String getGdpzZjid(){
		return this.gdpzZjid;
	}
	
	public void setGdpzZjid(String gdpzZjid){
		this.gdpzZjid = gdpzZjid;
	}

	public String getGdbmc(){
		return this.gdbmc;
	}
	
	public void setGdbmc(String gdbmc){
		this.gdbmc = gdbmc;
	}

	public String getGdhbmc(){
		return this.gdhbmc;
	}
	
	public void setGdhbmc(String gdhbmc){
		this.gdhbmc = gdhbmc;
	}

	public Date getGdsjKssj(){
		return this.gdsjKssj;
	}
	
	public void setGdsjKssj(Date gdsjKssj){
		this.gdsjKssj = gdsjKssj;
	}

	public Date getGdsjJssj(){
		return this.gdsjJssj;
	}
	
	public void setGdsjJssj(Date gdsjJssj){
		this.gdsjJssj = gdsjJssj;
	}

	public Integer getGdjls(){
		return this.gdjls;
	}
	
	public void setGdjls(Integer gdjls){
		this.gdjls = gdjls;
	}

	public Date getJlcjsj(){
		return this.jlcjsj;
	}
	
	public void setJlcjsj(Date jlcjsj){
		this.jlcjsj = jlcjsj;
	}
}
