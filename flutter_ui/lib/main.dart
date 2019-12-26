import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter_ui/pages/user_info_page.dart';

void main() => runApp(_widgetForRoute(window.defaultRouteName));

Widget _widgetForRoute(String route) {
  switch (route) {
    case 'Personal':
      return UserInfoPage();
    default:
      return Center(
        child: Text('Unknown route: $route', textDirection: TextDirection.ltr),
      );
  }
}
