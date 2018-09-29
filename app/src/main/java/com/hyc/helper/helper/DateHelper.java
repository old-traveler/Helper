package com.hyc.helper.helper;

import java.util.Calendar;
import java.util.Date;

public class DateHelper {

  public static String DATE_OF_SCHOOL = "2018.9.10";

  public static int getCurWeek(){
    Calendar caleEnd = Calendar.getInstance();
    String[] dateStr = DATE_OF_SCHOOL.split("\\.");
    caleEnd.set(Integer.valueOf(dateStr[0]), Integer.valueOf(dateStr[1])-1, Integer.valueOf(dateStr[2]));
    Date dateEnd = caleEnd.getTime();
    long schoolTime = dateEnd.getTime();
    int distanceDay = (int) ((System.currentTimeMillis()-schoolTime)/Constant.ONE_DAY_TIME);
    return distanceDay/7+1;
  }

  public static int getCurDay(){
    Calendar c = Calendar.getInstance();
    int day = c.get(Calendar.DAY_OF_WEEK)-1;
    return day == 0?7:day;
  }


}
