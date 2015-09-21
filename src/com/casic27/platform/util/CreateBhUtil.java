/**
 * @(#)com.casic27.platform.util.CreateBhUtil.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：Apr 12, 2012
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.util;

import java.text.SimpleDateFormat;
import java.util.Date;




public class CreateBhUtil {
	private static int FKXXBH=1;
	private static int LDXXBH=13546545;
	private static String date = new SimpleDateFormat("yyMMdd").format(new Date());//年月日
	private static String dateyymm = new SimpleDateFormat("yyyyMM").format(new Date());//年月
	/**
	 * 传入机构代码的前6位获取处置反馈信息编号
	 * 1-6位顺序号，7-12位发布日期，13-22位顺序号，每日零点归零，从0000000001
	 * @param jgdm
	 * @return
	 */
	public static String getFkxxbh(String jgdm){
		String currDate=new SimpleDateFormat("yyMMdd").format(new Date());
		if(!currDate.equals(date)){//和上次获取反馈信息编号的日期不一样，从1开始
			FKXXBH=1;
		}
		String fkxxbh=covertIntToString(FKXXBH,10);
		date=currDate;
		FKXXBH++;
		return jgdm+currDate+fkxxbh;
	}
	public static String getFkxxbh(String jgdm,int number){
		String currDate=new SimpleDateFormat("yyMMdd").format(new Date());
		String fkxxbh=covertIntToString(number,10);
		return jgdm+currDate+fkxxbh;
	}
	
	public static String getLdxxbh(String jgdm){
		String currDate=new SimpleDateFormat("yyyyMM").format(new Date());
		if(!currDate.equals(dateyymm)){
			LDXXBH=1;
		}
		String ldxxbh =covertIntToString(LDXXBH, 10);
		dateyymm=currDate;
		LDXXBH++;
		return jgdm+"23000"+currDate+ldxxbh;
	}
	
	public static String getLdxxbh(String jgdm,int number){
		String currDate=new SimpleDateFormat("yyyyMM").format(new Date());
		String ldxxbh =covertIntToString(number, 10);
		return jgdm+"23000"+currDate+ldxxbh;
	}
	private static String covertIntToString(int val,int length){
		String ret = val+"";
		if(ret.length()<length){
			String tmp="";
			for(int i=0;i<length-ret.length();i++){
				tmp+="0";
			}
			ret=tmp+val;
		}
		return ret;
	}
	public static void main(String[] args) {
		
		System.out.println(CreateBhUtil.getFkxxbh("520001"));
	}
}
