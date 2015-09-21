package com.casic27.platform.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DateUtil
{
  public static final String DATE_FORMAT_FULL = "yyyy-MM-dd HH:mm:ss";
  public static final String DATE_FORMAT_YMD = "yyyy-MM-dd";
  private static final Log logger = LogFactory.getLog(DateUtil.class);

  public static Calendar setStartDay(Calendar cal)
  {
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal;
  }

  public static Calendar setEndDay(Calendar cal) {
    cal.set(Calendar.HOUR_OF_DAY, 23);
    cal.set(Calendar.MINUTE, 59);
    cal.set(Calendar.SECOND, 59);
    cal.set(Calendar.MILLISECOND, 999);
    return cal;
  }

  public static void copyYearMonthDay(Calendar destCal, Calendar sourceCal)
  {
    destCal.set(1, sourceCal.get(1));
    destCal.set(2, sourceCal.get(2));
    destCal.set(5, sourceCal.get(5));
  }

  public static String formatEnDate(Date date)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

    return sdf.format(date).replaceAll("上午", "AM").replaceAll("下午", "PM");
  }

  public static Date parseDate(String dateString) {
    Date date = null;
    try {
      date = DateUtils.parseDate(dateString, new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" });
    } catch (Exception ex) {
      logger.error(new StringBuilder().append("Pase the Date(").append(dateString).append(") occur errors:").append(ex.getMessage()).toString());
    }

    return date;
  }

  public static String addOneDay(String date)
  {
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendar = Calendar.getInstance();
    try {
      Date dd = format.parse(date);
      calendar.setTime(dd);
      calendar.add(5, 1);
    }
    catch (ParseException e) {
      e.printStackTrace();
    }
    String tmpDate = format.format(calendar.getTime());
    return new StringBuilder().append(tmpDate.substring(5, 7)).append("/").append(tmpDate.substring(8, 10)).append("/").append(tmpDate.substring(0, 4)).toString();
  }

  public static String addOneHour(String date)
  {
    String amPm = date.substring(20, 22);

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Calendar calendar = Calendar.getInstance();

    int hour = Integer.parseInt(date.substring(11, 13));
    try {
      if (amPm.equals("PM")) {
        hour += 12;
      }
      date = new StringBuilder().append(date.substring(0, 11)).append(hour >= 10 ? Integer.valueOf(hour) : new StringBuilder().append("0").append(hour).toString()).append(date.substring(13, 19)).toString();
      Date dd = format.parse(date);
      calendar.setTime(dd);
      calendar.add(11, 1);
    }
    catch (ParseException e) {
      e.printStackTrace();
    }
    String tmpDate = format.format(calendar.getTime());

    hour = Integer.parseInt(tmpDate.substring(11, 13));
    amPm = (hour >= 12) && (hour != 0) ? "PM" : "AM";
    if (amPm.equals("PM")) {
      hour -= 12;
    }
    tmpDate = new StringBuilder().append(tmpDate.substring(5, 7)).append("/").append(tmpDate.substring(8, 10)).append("/").append(tmpDate.substring(0, 4)).append(" ").append(hour >= 10 ? Integer.valueOf(hour) : new StringBuilder().append("0").append(hour).toString()).append(tmpDate.substring(13, tmpDate.length())).append(" ").append(amPm).toString();

    return tmpDate;
  }

  public static String timeStrToDateStr(String timeStr)
  {
    String dateStr = new StringBuilder().append(timeStr.substring(24, 28)).append("-").toString();

    String mon = timeStr.substring(4, 7);
    if (mon.equals("Jan"))
      dateStr = new StringBuilder().append(dateStr).append("01").toString();
    else if (mon.equals("Feb"))
      dateStr = new StringBuilder().append(dateStr).append("02").toString();
    else if (mon.equals("Mar"))
      dateStr = new StringBuilder().append(dateStr).append("03").toString();
    else if (mon.equals("Apr"))
      dateStr = new StringBuilder().append(dateStr).append("04").toString();
    else if (mon.equals("May"))
      dateStr = new StringBuilder().append(dateStr).append("05").toString();
    else if (mon.equals("Jun"))
      dateStr = new StringBuilder().append(dateStr).append("06").toString();
    else if (mon.equals("Jul"))
      dateStr = new StringBuilder().append(dateStr).append("07").toString();
    else if (mon.equals("Agu"))
      dateStr = new StringBuilder().append(dateStr).append("08").toString();
    else if (mon.equals("Sep"))
      dateStr = new StringBuilder().append(dateStr).append("09").toString();
    else if (mon.equals("Oct"))
      dateStr = new StringBuilder().append(dateStr).append("10").toString();
    else if (mon.equals("Nov"))
      dateStr = new StringBuilder().append(dateStr).append("11").toString();
    else if (mon.equals("Dec")) {
      dateStr = new StringBuilder().append(dateStr).append("12").toString();
    }

    dateStr = new StringBuilder().append(dateStr).append("-").append(timeStr.substring(8, 10)).toString();

    return dateStr;
  }

  public static int getExtraDayOfWeek(String sDate)
  {
    try
    {
      String formater = "yyyy-MM-dd";
      SimpleDateFormat format = new SimpleDateFormat(formater);
      Date date = format.parse(sDate);
      String weekday = date.toString().substring(0, 3);
      if (weekday.equals("Mon"))
        return 1;
      if (weekday.equals("Tue"))
        return 2;
      if (weekday.equals("Wed"))
        return 3;
      if (weekday.equals("Thu"))
        return 4;
      if (weekday.equals("Fri"))
        return 5;
      if (weekday.equals("Sat")) {
        return 6;
      }
      return 0;
    }
    catch (Exception ex) {
    }
    return 0;
  }

  public static String getDateWeekDay(String sDate)
  {
    try
    {
      String formater = "yyyy-MM-dd";
      SimpleDateFormat format = new SimpleDateFormat(formater);
      Date date = format.parse(sDate);
      return date.toString().substring(0, 3);
    }
    catch (Exception ex)
    {
    }
    return "";
  }

  public static List<String> getUpDownFiveYear(Calendar cal)
  {
    List yearlist = new ArrayList();

    int curyear = cal.get(1);
    yearlist.add(String.valueOf(curyear - 2));
    yearlist.add(String.valueOf(curyear - 1));
    yearlist.add(String.valueOf(curyear));
    yearlist.add(String.valueOf(curyear + 1));
    yearlist.add(String.valueOf(curyear + 2));

    return yearlist;
  }

  public static List<String> getTwelveMonth()
  {
    List monthlist = new ArrayList();

    for (int idx = 1; idx <= 12; idx++) {
      monthlist.add(String.valueOf(idx));
    }

    return monthlist;
  }

  public static String[] getDaysBetweenDate(String startTime, String endTime)
  {
    String[] dateArr = null;
    try
    {
      String stime = timeStrToDateStr(startTime);
      String etime = timeStrToDateStr(endTime);

      Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(stime);
      Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(etime);

      long day = (date1.getTime() - date2.getTime()) / 86400000L > 0L ? (date1.getTime() - date2.getTime()) / 86400000L : (date2.getTime() - date1.getTime()) / 86400000L;

      dateArr = new String[Integer.valueOf(String.valueOf(day + 1L)).intValue()];
      for (int idx = 0; idx < dateArr.length; idx++) {
        if (idx == 0) {
          dateArr[idx] = stime;
        } else {
          stime = addOneDay(stime);
          stime = new StringBuilder().append(stime.substring(6, 10)).append("-").append(stime.substring(0, 2)).append("-").append(stime.substring(3, 5)).toString();

          dateArr[idx] = stime;
        }
      }
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }

    return dateArr;
  }
}