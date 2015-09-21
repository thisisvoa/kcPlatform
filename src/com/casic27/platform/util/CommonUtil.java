/**
 * @(#)com.casic27.platform.util.CommonUtil.java
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

public class CommonUtil {
	/**
	 * 根据组织机构代码判断所属的级别
	 * 
	 * @param unitCode
	 * @return 10 省级 20 市级 30县级
	 */
	public static int getUnitHierarchical(String unitCode) {
		int code = 99;
		if (unitCode == null || "".equals(unitCode)) {
			return code;
		}
		if (unitCode.matches("^5200\\d{8,8}$")) {
			code = 10;
		} else if (unitCode.matches("^52\\d{2,2}00\\d{6,6}$")) {
			code = 20;
		} else {
			code = 30;
		}
		return code;
	}
	
	/**
	 * 根据组织机构代码计算查询使用的SQL,如52%,5201%,520102%
	 * @param unitCode  组织机构代码
	 * @return
	 */
	public static String getUnitHierarchicalSQL(String unitCode){
		String code = null;
		if (unitCode == null || "".equals(unitCode)) {
			return code;
		}
		if (unitCode.matches("^5200\\d{8,8}$")) {
			code = "52%";
		} else if (unitCode.matches("^52\\d{2,2}00\\d{6,6}$")) {
			code = unitCode.replaceAll("^(52\\d{2,2})00\\d{6,6}$", "$1%");
		} else {
			code = unitCode.replaceAll("^(52\\d{4,4})\\d{6,6}$", "$1%");
		}
		return code;
	}
	
	public static void main(String[] args){
		System.out.println(CommonUtil.getUnitHierarchical("520202780000"));
		System.out.println(CommonUtil.getUnitHierarchical("520100010000"));
		System.out.println(CommonUtil.getUnitHierarchical("520101010000"));
		System.out.println(CommonUtil.getUnitHierarchicalSQL("520202780000"));
	}
}
