/**
 * @(#)com.casic27.platform.util.GB2Alpha.java
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

/**
 * 
 * @author linzy
 * 
 *         create on 2008-4-9
 */
public class GB2Alpha {
	private static final char[] alphatable = { 'A', 'B', 'C', 'D', 'E', 'F',
			'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
			'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
	/**
	 * 
	 * 汉字拼音首字母编码表，可以如下方法得到：
	 * 
	 * 字母Z使用了两个标签，这里有２７个值, i, u, v都不做声母, 跟随前面的字母(因为不可以出现，所以可以随便取)
	 * 
	 * private static final char[] chartable =
	 * 
	 * {
	 * 
	 * '啊', '芭', '擦', '搭', '蛾', '发', '噶', '哈', '哈',
	 * 
	 * '击', '喀', '垃', '妈', '拿', '哦', '啪', '期', '然',
	 * 
	 * '撒', '塌', '塌', '塌', '挖', '昔', '压', '匝', '座'
	 * 
	 * };
	 * 
	 * 
	 * 
	 * private static final int[] table = new int[27];
	 * 
	 * static
	 * 
	 * {
	 * 
	 * for (int i = 0; i < 27; ++i) {
	 * 
	 * table[i] = gbValue(chartable[i]);
	 * 
	 * System.out.print(table[i]+" ");
	 * 
	 * }
	 * 
	 * }
	 */
	private static final int[] table = new int[] { 45217, 45253, 45761, 46318,
			46826, 47010, 47297, 47614, 47614, 48119, 49062, 49324, 49896,
			50371, 50614, 50622, 50906, 51387, 51446, 52218, 52218, 52218,
			52698, 52980, 53689, 54481, 55289 };

	public GB2Alpha() {
	}

	/**
	 * 
	 * 主函数, 输入字符, 得到他的声母, 英文字母返回对应的大写字母 其他非简体汉字返回 '*'
	 */
	public static char Char2Alpha(char ch) {
		if (ch >= 'a' && ch <= 'z')
			return (char) (ch - 'a' + 'A');
		if (ch >= 'A' && ch <= 'Z')
			return ch;
		int gb = gbValue(ch);
		if (gb < table[0])
			return '*';
		for (int i = 0; i < 26; ++i) {
			if (match(i, gb)) {
				if (i >= 26)
					return '*';
				else
					return alphatable[i];
			}
		}
		return '*';
	}

	/**
	 * 
	 * 根据一个包含汉字的字符串返回一个汉字拼音首字母的字符串
	 */
	public static String String2Alpha(String str) {
		String Result = "";
		try {
			for (int i = 0; i < str.length(); i++) {
				Result += Char2Alpha(str.charAt(i));
			}
		} catch (Exception e) {
			Result = " ";
		}
		return Result;
	}

	private static boolean match(int i, int gb) {
		if (gb < table[i])
			return false;
		int j = i + 1;
		// 字母Z使用了两个标签
		while (j < 26 && (table[j] == table[i]))
			++j;
		if (j == 26)
			return gb <= table[j];
		else
			return gb < table[j];
	}

	/**
	 * 
	 * 取出传入汉字的编码
	 */
	private static int gbValue(char ch) {
		String str = new String();
		str += ch;
		try {
			byte[] bytes = str.getBytes("GB2312");
			if (bytes.length < 2)
				return 0;
			return (bytes[0] << 8 & 0xff00) + (bytes[1] & 0xff);
		} catch (Exception e) {
			return '*';
		}
	}
	
	/**
	 * @param attrKey需要添加到Model中的key值，
	 * @param attrVal需要添加到Model中的val值，
	 * @param model
	 * @param keyStr直接使用字符代替上面的数组
	 * 
	 */
	public static void modelAddAttribute(String[] attrKey,Object[] attrVal,Model model,String keyStr){
		
		if(attrVal==null || attrVal.length<=0){
			return ;
		}
		if((attrKey==null || attrKey.length<=0)&&(keyStr==null || keyStr.length()<=0)){
			return ;
		}
		
		if(attrKey==null || attrKey.length<=0){
			String[] keys = keyStr.split(",");
			if(keys.length == attrVal.length){
				for(int i=0;i<attrVal.length;i++){
					model.addAttribute(keys[i],attrVal[i]);
				}
			}else{
				return ;
			}
		}else if(attrKey.length==attrVal.length){
			for(int i=0;i<attrVal.length;i++){
				model.addAttribute(attrKey[i], attrVal[i]);
			}
		}else{
			return ;
		}
		
	}
	

	public static String jjtsbn(int sj, boolean b){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE,c.get(Calendar.DATE)+sj);
		SimpleDateFormat sf = null;
		if(b){
			sf = new SimpleDateFormat("yyyy-MM-dd");
		}else{
			sf = new SimpleDateFormat("yyyyMMddHHmmss");
		}
		Date date = c.getTime();
		String temp = sf.format(date);
		return temp;
	}	
	
	public static Map<String,Object> fillInMapParameter(HttpServletRequest req,String[] prams){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		for(int i=0;i<prams.length;i++){
			if(prams[i]!=null && prams[i].lastIndexOf("_s")>0){
				String temp = prams[i].replace("_s", "");
				paramMap.put(temp, req.getParameterValues(temp));
			}else{
				paramMap.put(prams[i], req.getParameter(prams[i]));
			}
		}
		return paramMap;
	}
	
	public static Map<String,Object> fillInMapParameterUpcase(HttpServletRequest req,String[] prams){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		for(int i=0;i<prams.length;i++){
			if(prams[i]!=null && prams[i].lastIndexOf("_s")>0){
				String temp1 = prams[i].replace("_s", "");
				String temp2 = prams[i].replace("_s", "").toUpperCase();
				paramMap.put(temp2, req.getParameterValues(temp1));
			}else{
				String temp = prams[i].toUpperCase();
				paramMap.put(temp, req.getParameter(prams[i]));
			}
		}
		return paramMap;
	}
	
	public static void fillMap(String[] prams,Map<String, Object> formObj){
		for(int i=0;i<prams.length;i++){
			formObj.put(prams[i], prams[i]);
		}
	}
	
	public static Map<String,Object> WaHaHaMap(String[] key,Object[] val){
		
		Map<String,Object> rtMap = new HashMap<String, Object>();
		for(int i=0;i<val.length;i++){
			rtMap.put(key[i], val[i]);
		}
		return rtMap;
	}
	
	/**
	 * 
	 * 测试输出
	 */
	public static void main(String[] args) {
//		GB2Alpha gb2A = new GB2Alpha();
//		System.out.println("黑色头发的拼音首字母为" + gb2A.String2Alpha("高宝东"));// 输出
		String strs = "sdfasdf_s";
		System.out.println(strs.lastIndexOf("_s"));
	}
}