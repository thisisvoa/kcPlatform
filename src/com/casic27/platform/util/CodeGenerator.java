/**
 * @(#)com.casic27.platform.util.CodeGenerator.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：Apr 16, 2012
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.util;

import java.security.MessageDigest;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

/**
 *
 *<pre>类描述：</pre><br>
 *<pre>
 *	负责生成各种编码
 *</pre> 
 *@Author： 林斌树(linbinshu@casic27.com)
 *@Version：1.0
 */
public class CodeGenerator {
	/**
     * 数英字符串
     */   
	private static  String ALPHANUMERIC_STR;
	
	private static String DIGIST_STR;
	
    static{
        DIGIST_STR = "0123456789";
    	String aphaStr = "abcdefghijklmnopqrstuvwxyz";
    	ALPHANUMERIC_STR = DIGIST_STR + aphaStr + aphaStr.toUpperCase();
    }
    
	/**
     * 生成36个字符长度的UUID编码串，所有的字母转换为大写的格式。
     * 
     * @return 36个字符长度的UUID。
     */
    public static String getUUID()
    {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().toUpperCase();
    }

    /**
     * 生成32个字符长度的UUID编码串，所有的字母转换为大写的格式。
     *
     * @return 36个字符长度的UUID。
     */
    public static String getUUID32()
    {
        return getUUID().replace("-","");
    }
    /**
     * 通过SHA算法对srcStr进行MD5编码（十六进制表示）
     * @param srcStr 需要获取MD5的字符串，不能为null
     * @return srcStr的SHA代码（40个字符）
     */
    public static String getSHADigest(String srcStr)
    {
        return getDigest(srcStr,"SHA-1");
    }

    /**
     * 获取srcStr的MD5编码（十六进制表示）
     * @param srcStr 需要获取MD5的字符串，不能为null
     * @return srcStr的MD5代码（32个字符）
     */
    public static String getMD5Digest(String srcStr)
    {
       return getDigest(srcStr,"MD5");
    }
    

    /**
     * 产生6位英数随机数,区分大小写
     * @return
     */
    public static String getUpdateKey(){
        return getRandomStr(6);	
    }
    
    /**
     * 产生一个随机英数字符串，区分大小定
     * @param length 随机字符串的长度
     * @return
     */
    public static String getRandomStr(int length){
		int srcStrLen = ALPHANUMERIC_STR.length();
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<length;i++)
		{
			int maxnum = (int)(Math.random()*1000);
			int result = maxnum%srcStrLen;
			char temp = ALPHANUMERIC_STR.charAt(result);
			sb.append(temp);
		}
		return sb.toString();
    }
    
    /**
     * 产生一个随机英数字符串，区分大小定
     * @param length 随机字符串的长度
     * @return
     */
    public static String getRandomDigist(int length){
        int srcStrLen = DIGIST_STR.length();
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<length;i++)
        {
            int maxnum = (int)(Math.random()*1000);
            int result = maxnum%srcStrLen;
            char temp = DIGIST_STR.charAt(result);
            sb.append(temp);
        }
        return sb.toString();
    }
    
    private static String getDigest(String srcStr,String alg){
        if(StringUtils.isEmpty(srcStr) || StringUtils.isEmpty(alg))
            throw new RuntimeException("加密的串和密钥不能为空");

        try
        {
            MessageDigest alga = MessageDigest
                    .getInstance(alg);
            alga.update(srcStr.getBytes());
            byte[] digesta = alga.digest();
            return byte2hex(digesta);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }      
    }
    
    /**
     * 二进制转十六进制字符串
     * @param b
     * @return
     */
    private static String byte2hex(byte[] b)
    {
        StringBuffer hs = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < b.length; n++)
        {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1){
                hs.append("0");
            }     
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }
}
