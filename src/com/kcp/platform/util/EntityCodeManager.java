package com.kcp.platform.util;

import org.springframework.util.Assert;

public class EntityCodeManager {

    static String[] DIGITCHARS = new String[] { "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "j", "k",
            "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L",
            "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

    /**
     * 
     * @param l
     * @param digit
     * @return
     */
    private static String toDigitString(Long l, int digit) {
        Assert.isTrue((digit > 0 && digit <= DIGITCHARS.length), "进制无效");
        StringBuilder builder = new StringBuilder("");
        long tmp = l;
        while (tmp / digit >= 1) {
            builder.append(DIGITCHARS[Long.valueOf(tmp % digit).intValue()]);
            tmp /= digit;
        }
        builder.append(DIGITCHARS[(int) tmp % digit]);
        return builder.reverse().toString();
    }

    /**
     * 获取序列号
     * @return
     */
    public static String getSequenceNo() {
        return toDigitString(System.nanoTime(), DIGITCHARS.length);
    }

    /**
     * 获取一个无重复的获取序列号
     * @return
     */
    public static String getRoleCode() {
        return "R" + toDigitString(System.nanoTime(), DIGITCHARS.length);
    }
    
    
    /**
     * 获取一个无重复的获取序列号
     * @return
     */
    public static String getPoliceCode() {
        return "P" + toDigitString(System.nanoTime(), DIGITCHARS.length);
    }
    
    /**
     * 获取一个无重复的用户编号
     * @return
     */
    public static String getUserCode() {
        return "U" + toDigitString(System.nanoTime(), DIGITCHARS.length);
    }
}
