import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter_ui/pages/class_select_page.dart';
import 'package:flutter_ui/pages/course_table_page.dart';
import 'package:flutter_ui/pages/user_info_page.dart';

void main() => runApp(_widgetForRoute(window.defaultRouteName));

Widget _widgetForRoute(String route) {
  switch (route) {
    case 'Personal':
      return UserInfoPage();
    case 'ClassSelectActivity':
      return ClassSelectPage();
    case 'ClassCourseActivity':
      return CourseTablePage();
    default:
      return Center(
        child: Text('Unknown route: $route', textDirection: TextDirection.ltr),
      );
  }
}
