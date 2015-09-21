/**
 * @(#)com.casic27.platform.common.log.entity.Gdpz
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright: Copyright(c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
 
package com.casic27.platform.common.log.entity;

import java.util.Date;
import java.util.Date;
import java.util.Date;

public class Gdpz{

	/**
	 * 配置ID
	 */
	private String zjid;

	/**
	 * 归档名称
	 */
	private String gdmc;

	/**
	 * 归档表名称
	 */
	private String gdbmc;

	/**
	 * 时间戳列名
	 */
	private String sjclm;

	/**
	 * 时间戳列数据类型(1:日期 2:字符串)
	 */
	private String sjclSjlx;

	/**
	 * 时间戳列格式
	 */
	private String sjclGs;

	/**
	 * 归档周期
	 */
	private Integer gdzq;

	/**
	 * 归档周期单位(1:日;2:月;3:年)
	 */
	private String gdzqDw;

	/**
	 * 实际归档延迟时间(单位为：天)
	 */
	private Integer ycsj;

	/**
	 * 归档执行时间
	 */
	private String zxsj;

	/**
	 * 是否启用(0:禁用;1:启用)
	 */
	private String sybz;

	/**
	 * 备注
	 */
	private String bz;

	/**
	 * 最后执行时间
	 */
	private Date zhzxsj;

	/**
	 * 创建时间
	 */
	private Date jlcjsj;

	/**
	 * 创建用户
	 */
	private String jlcjyh;

	/**
	 * 更新时间
	 */
	private Date jlgxsj;

	/**
	 * 更新用户
	 */
	private String jlgxyh;


	public String getZjid(){
		return this.zjid;
	}
	
	public void setZjid(String zjid){
		this.zjid = zjid;
	}

	public String getGdmc(){
		return this.gdmc;
	}
	
	public void setGdmc(String gdmc){
		this.gdmc = gdmc;
	}

	public String getGdbmc(){
		return this.gdbmc;
	}
	
	public void setGdbmc(String gdbmc){
		this.gdbmc = gdbmc;
	}

	public String getSjclm(){
		return this.sjclm;
	}
	
	public void setSjclm(String sjclm){
		this.sjclm = sjclm;
	}

	public String getSjclSjlx(){
		return this.sjclSjlx;
	}
	
	public void setSjclSjlx(String sjclSjlx){
		this.sjclSjlx = sjclSjlx;
	}

	public String getSjclGs(){
		return this.sjclGs;
	}
	
	public void setSjclGs(String sjclGs){
		this.sjclGs = sjclGs;
	}

	public Integer getGdzq(){
		return this.gdzq;
	}
	
	public void setGdzq(Integer gdzq){
		this.gdzq = gdzq;
	}

	public String getGdzqDw(){
		return this.gdzqDw;
	}
	
	public void setGdzqDw(String gdzqDw){
		this.gdzqDw = gdzqDw;
	}

	public Integer getYcsj(){
		return this.ycsj;
	}
	
	public void setYcsj(Integer ycsj){
		this.ycsj = ycsj;
	}

	public String getZxsj(){
		return this.zxsj;
	}
	
	public void setZxsj(String zxsj){
		this.zxsj = zxsj;
	}

	public String getSybz(){
		return this.sybz;
	}
	
	public void setSybz(String sybz){
		this.sybz = sybz;
	}

	public String getBz(){
		return this.bz;
	}
	
	public void setBz(String bz){
		this.bz = bz;
	}

	public Date getZhzxsj(){
		return this.zhzxsj;
	}
	
	public void setZhzxsj(Date zhzxsj){
		this.zhzxsj = zhzxsj;
	}

	public Date getJlcjsj(){
		return this.jlcjsj;
	}
	
	public void setJlcjsj(Date jlcjsj){
		this.jlcjsj = jlcjsj;
	}

	public String getJlcjyh(){
		return this.jlcjyh;
	}
	
	public void setJlcjyh(String jlcjyh){
		this.jlcjyh = jlcjyh;
	}

	public Date getJlgxsj(){
		return this.jlgxsj;
	}
	
	public void setJlgxsj(Date jlgxsj){
		this.jlgxsj = jlgxsj;
	}

	public String getJlgxyh(){
		return this.jlgxyh;
	}
	
	public void setJlgxyh(String jlgxyh){
		this.jlgxyh = jlgxyh;
	}
}
