package com.hyc.helper.helper;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {

  static String DATE_OF_SCHOOL = "2018.9.10";

  public static int getCurWeek() {
    long schoolTime = getBeginSchoolTime();
    int distanceDay = (int) ((System.currentTimeMillis() - schoolTime) / Constant.ONE_DAY_TIME);
    return distanceDay / 7 + 1;
  }

  private static long getBeginSchoolTime() {
    Calendar caleEnd = Calendar.getInstance();
    String[] dateStr = DATE_OF_SCHOOL.split("\\.");
    caleEnd.set(Integer.valueOf(dateStr[0]), Integer.valueOf(dateStr[1]) - 1,
        Integer.valueOf(dateStr[2]));
    Date dateEnd = caleEnd.getTime();
    return dateEnd.getTime();
  }

  public static int[] getCurDayOfWeek(int week) {
    if (week < 1) {
      throw new RuntimeException("week can not less than 1");
    }
    long schoolTime = getBeginSchoolTime();
    long curWeekTime = schoolTime + (week - 1) * 7 * Constant.ONE_DAY_TIME;
    SimpleDateFormat format = new SimpleDateFormat("dd");
    int[] date = new int[8];
    date[7] = new Date(curWeekTime).getMonth() + 1;
    for (int i = 0; i < 7; i++) {
      String s = format.format(new Date(curWeekTime + i * Constant.ONE_DAY_TIME));
      date[i] = Integer.parseInt(s);
    }
    return date;
  }

  public static int getCurDay() {
    Calendar c = Calendar.getInstance();
    int day = c.get(Calendar.DAY_OF_WEEK) - 1;
    return day == 0 ? 7 : day;
  }

  public static int getCurYear() {
    Calendar c = Calendar.getInstance();
    return c.get(Calendar.YEAR);
  }

  public static int getCurMonth() {
    Calendar c = Calendar.getInstance();
    int month = c.get(Calendar.MONTH) + 1;
    return month;
  }

  @SuppressLint("DefaultLocale")
  public static String getDateInfo(long time) {
    long timeDis = System.currentTimeMillis() - time;
    if (timeDis < 60 * 1000L) {
      return "just";
    } else if (timeDis < 60 * 60 * 1000L) {
      return String.format("%d分钟前", timeDis / (60 * 1000L));
    } else if (timeDis < Constant.ONE_DAY_TIME) {
      return String.format("%d小时前", timeDis / (60 * 60 * 1000L));
    } else if (timeDis < Constant.ONE_DAY_TIME * 10) {
      return String.format("%d天前", timeDis / Constant.ONE_DAY_TIME);
    } else {
      @SuppressLint("SimpleDateFormat")
      SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
      return format.format(new Date(time));
    }
  }
}
