import 'package:flutter_ui/entity/course_entity.dart';
import 'package:flutter_ui/utils/strings.dart';

class CourseUtil {
  static List<List<CourseData>> getCourseModel(List<CourseData> data, curWeek) {
    List<List<CourseData>> res = List(5);
    if (curWeek == -1) {
      curWeek = getCurWeek();
    }
    for (int i = 0; i < 5; i++) {
      res[i] = List(7);
    }
    for (var value in data) {
      if (value.zs.contains(curWeek)) {
        value.qsz = "1";
      } else {
        value.qsz = "0";
      }
      int row = int.parse(value.djj) ~/ 2;
      int column = int.parse(value.xqj) - 1;
      if (res[row][column] == null || value.qsz == "1") {
        res[row][column] = value;
      }
    }
    return res;
  }

  static String getCourseName(CourseData courseData) {
    return courseData.name +
        "\n@" +
        (courseData.room.isEmpty ? "未知地点" : courseData.room);
  }

  static int getCurWeek() {
    DateTime startTime = getSchoolOpenDay();
    Duration duration = startTime.difference(new DateTime.now());
    return (duration.abs().inDays / 7 + 1).toInt();
  }

  static DateTime getSchoolOpenDay() {
    return new DateTime(2020, 2, 24);
  }

  static List<String> getCurDayOfWeek(int week) {
    DateTime schoolOpenDay = getSchoolOpenDay();
    DateTime weekDate = schoolOpenDay.add(Duration(days: (week - 1) * 7));
    List<String> date = List(7);
    for (int i = 0; i < 7; i++) {
      DateTime time = weekDate.add(Duration(days: i));
      int month = time.month;
      int day = time.day;
      date[i] = YStrings.weekDay[i] + '\n$month/$day';
    }
    return date;
  }

  static int getCurDayIndex(int week) {
    if (week != getCurWeek()) {
      return -1;
    }
    return getCurDayInCurWeekIndex();
  }

  static int getCurDayInCurWeekIndex() {
    DateTime now = new DateTime.now();
    int index = now.weekday - 1;
    if (index < 0) {
      index = YStrings.weekDay.length - 1;
    }
    return index;
  }
}
