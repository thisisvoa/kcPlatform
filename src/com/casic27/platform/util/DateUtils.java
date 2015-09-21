/**
 * @(#)com.casic27.platform.util.DateUtils.java
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class DateUtils {
	
	public static Date parseStrint2Date(String dateStr, String pattern) {
		Date date = null;
		if (StringUtils.isNotBlank(dateStr)) {
			DateFormat df = new SimpleDateFormat(pattern);
			try {
				date = df.parse(dateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;
	}
	
	public static String parseDate2String(Date date, String pattern) {
		String dateStr = "";
		if (date != null) {
			DateFormat df = new SimpleDateFormat(pattern);
			dateStr = df.format(date);
		}
		return dateStr;
	}
	
	public static String appendTimeFrom(String dateStr) {
		String result = " 00:00:00.0";
		if (StringUtils.isNotBlank(dateStr)) {
			return dateStr + result;
		}
		return "";
	}
	public static String appendTimeFromSecond(String dateStr) {
		String result = " 00:00:00";
		if (StringUtils.isNotBlank(dateStr)) {
			return dateStr + result;
		}
		return "";
	}
	
	public static String appendTimeTo(String dateStr) {
		String result = " 23:59:59.999";
		if (StringUtils.isNotBlank(dateStr)) {
			return dateStr + result;
		}
		return "";
	}
	
	public static String appendTimeToSecond(String dateStr) {
		String result = " 23:59:59";
		if (StringUtils.isNotBlank(dateStr)) {
			return dateStr + result;
		}
		return "";
	}
	
	/**
	 * @param dateStr yyyy-MM-dd格式的日期
	 * @return
	 */
	public static Date parseStrint2DateFrom(String dateStr) {
		Date date = null;
		if (StringUtils.isNotBlank(dateStr)) {
			dateStr = appendTimeFrom(dateStr);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
			try {
				date = df.parse(dateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;
	}
	
	/**
	 * @param dateStr yyyy-MM-dd格式的日期
	 * @return
	 */
	public static Date parseStrint2DateTo(String dateStr) {
		Date date = null;
		if (StringUtils.isNotBlank(dateStr)) {
			dateStr = appendTimeTo(dateStr);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
			try {
				date = df.parse(dateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;
	}
	/**
	 * @param dateStr yyyy-MM-dd格式的日期
	 * @return
	 */
	public static Date parseStringformatDate(String dateStr,String format) {
		Date date = null;
		if (StringUtils.isNotBlank(dateStr)) {
			dateStr = appendTimeTo(dateStr);
			DateFormat df = new SimpleDateFormat(format);
			try {
				date = df.parse(dateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;
	}
	
	/**
	 * @param dateStr 指定年月日格式
	 * @return
	 */
	public static Date parseStrint2DateTo(String dateStr, String yyMMdd) {
		Date date = null;
		if (StringUtils.isNotBlank(dateStr)) {
			dateStr = appendTimeTo(dateStr);
			DateFormat df = new SimpleDateFormat(yyMMdd+" HH:mm:ss.S");
			try {
				date = df.parse(dateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;
	}
	
	/**
	 * 获取指定时间段的日期列表
	 * @param dateFrom
	 * @param dateTo
	 * @param pattern
	 * @return
	 */
	public static List<String> getDateRangeList(Date dateFrom, Date dateTo, String pattern) {
		List<String> dateRangeList = new ArrayList<String>();
		Date date = dateFrom;
		Calendar calendar = Calendar.getInstance();
		while (date.compareTo(dateTo) <= 0) {
			dateRangeList.add(parseDate2String(date, pattern));
			calendar.setTime(date);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+1);
			date = calendar.getTime();
		}
		return dateRangeList;
	}

	/**
	 * 获取当前应用服务器的系统日期时间
	 * @return
	 */
	public static Date getCurrentDateTime(){
		return new Date(System.currentTimeMillis());
	}


	/**
	 * 获取当前应用服务器的系统日期时间
	 * @return 日期时间字符串，格式：yyyy-MM-dd HH:mm:ss
	 */
	public static String getCurrentDateTimeStr(){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date(System.currentTimeMillis()));
	}
	
	public static String getCurrentDateFormatStr(String format){
		DateFormat df = new SimpleDateFormat(format);
		return df.format(new Date(System.currentTimeMillis()));
	}
	/**
	 * 获取当前应用服务器的系统日期时间
	 * @return 日期时间字符串，格式：yyyyMMddHHmmss
	 */
	public static String getCurrentDateTime14(){
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(new Date(System.currentTimeMillis()));
	}
	/**
	 * 获取当前应用服务器的系统日期时间
	 * @return 日期时间字符串，格式：yyyyMMdd
	 */
	public static String getCurrentDate8(){
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		return df.format(new Date(System.currentTimeMillis()));
	}
	public static String getFormatString(String date,String srcpattern,String destpattern){
		String retstr="";
		try {
			Date d = new SimpleDateFormat(srcpattern).parse(date);
			retstr = new SimpleDateFormat(destpattern).format(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retstr;
	}
	
	/**
	 * 增加或减少时间
	 * @param date
	 * @param calendarField 增加作用域映射值   如：年份为 Calendar.YEAR = 1
	 * @param amount  正数为增加，负数为减少   
	 * @return
	 */
	public static Date addDate(Date date, int calendarField, int amount){
		if(null == date){
			return date;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		//calendar.roll(calendarField, amount);
		calendar.add(calendarField, amount);
		return calendar.getTime();
		
	}
	
	public static String getCurrentDate10(){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		return formatter.format(date);
	}
	
	public static String getCurrentTime(){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		return formatter.format(date);
	}
	
	/**
	 * 获取某年某月的最后一天
	 * @param year
	 * @param month
	 * @return
	 */
	public static Date getEndDayMonth(int year, int month){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR,year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date date = cal.getTime();
		return date;
	}
	
	
	/**
	 * 获取某年某月的第一天
	 * @param year
	 * @param month
	 * @return
	 */
	public static Date getFirstDayMonth(int year, int month){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR,year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date date = cal.getTime();
		return date;
	}
	
	/**
	 * 将时间长度转化为可读模式
	 * @param millseconds
	 * @return
	 */
	public static String getTime(Long millseconds) {
		String time = "";
		if (millseconds == null) {
			return "";
		}
		int days = (int) millseconds.longValue() / 1000 / 60 / 60 / 24;
		if (days > 0) {
			time = time + days + "天";
		}
		long hourMillseconds = millseconds.longValue() - days * 1000 * 60 * 60 * 24;
		int hours = (int) hourMillseconds / 1000 / 60 / 60;
		if (hours > 0) {
			time = time + hours + "小时";
		}
		long minuteMillseconds = millseconds.longValue() - days * 1000 * 60 * 60 * 24 - hours * 1000 * 60 * 60;
		int minutes = (int) minuteMillseconds / 1000 / 60;
		if (minutes > 0) {
			time = time + minutes + "分钟";
		}
		return time;
	}
	
	/**
	 * 获取某个日期结束时间
	 * @param date
	 * @return
	 */
	public static Date getDateEndTime(Date date){
		if(date==null) return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}
	
	public static Date getTodayStart (){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
}
