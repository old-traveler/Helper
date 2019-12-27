import 'dart:convert';
import 'dart:math';

import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_ui/entity/course_entity.dart';
import 'package:flutter_ui/pages/base_page_state.dart';
import 'package:flutter_ui/utils/colors.dart';
import 'package:flutter_ui/utils/course_util.dart';
import 'package:flutter_ui/utils/strings.dart';

class CourseTablePage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _CourseTablePageState('ClassCourseActivity');
  }
}

class _CourseTablePageState<CourseTablePage> extends BaseInteractiveState {
  CourseEntity mCouse;
  List<List<CourseData>> _courseData = List();
  int _curWeek = -1;

  _CourseTablePageState(String channelName) : super(channelName);

  @override
  void initState() {
    super.initState();
    _fetData();
  }

  void _fetData() async {
    final String reply = await basicChannel.send("getClassCourse");
    if (reply.isNotEmpty) {
      var course = CourseEntity.fromJson(json.decode(reply.toString()));
      if (course.code == 200) {
        setState(() {
          mCouse = course;
          _courseData = CourseUtil.getCourseModel(course.data, _curWeek);
        });
      }
    }
  }

  @override
  Future<String> handlerMessage(String message) {
    if (message.startsWith('switchWeek')) {
      try {
        var index = int.parse(message.substring(10));
        setState(() {
          _curWeek = index;
          if (mCouse != null) {
            _courseData = CourseUtil.getCourseModel(mCouse.data, _curWeek);
          }
        });
      } catch (FormatException) {}
    } else if (message == 'refresh') {
      _fetData();
    }
    return super.handlerMessage(message);
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: ThemeData(
          // This is the theme of your application.
          primaryColor: Color(0xffe4837f),
          primaryColorDark: Color(0xffe4837f),
          accentColor: Color(0xff5b97a4)),
      home: Material(
          child: Stack(
        children: <Widget>[
          CachedNetworkImage(
            imageUrl: YStrings.courseBackgroundUrl,
            fit: BoxFit.fitHeight,
            height: 1800,
          ),
          _courseData.length < 5
              ? Text("")
              : Column(
                  children: <Widget>[
                    _getBottomDateTip(),
                    _getItemWidget(context, 0),
                    _getItemWidget(context, 1),
                    _getItemWidget(context, 2),
                    _getItemWidget(context, 3),
                    _getItemWidget(context, 4),
                  ],
                ),
        ],
      )),
    );
  }

  Widget _getItemWidget(BuildContext context, int index) {
    return Row(mainAxisSize: MainAxisSize.max, children: _getChildren(index));
  }

  List<Widget> _getChildren(int index) {
    List<Widget> widget = List();
    List<CourseData> course = _courseData[index];
    for (var value in course) {
      if (value == null) {
        widget.add(Expanded(
          flex: 1,
          child: Text(""),
        ));
      } else {
        widget.add(Expanded(
          flex: 1,
          child: _getCourseItem(value),
        ));
      }
    }
    return widget;
  }

  Widget _getCourseItem(CourseData courseItem) {
    return Container(
      padding: EdgeInsets.zero,
      margin: EdgeInsets.zero,
      height: 120,
      child: Card(
        margin: EdgeInsets.all(2),
        elevation: 3,
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(3)),
        color: courseItem.qsz == "1"
            ? YColors.courseBgColor[Random().nextInt(6)]
            : Color(0x98A9A9A9),
        child: Padding(
          padding: EdgeInsets.only(top: 4, bottom: 4, left: 2, right: 2),
          child: Text(CourseUtil.getCourseName(courseItem),
              style: TextStyle(
                color: Colors.white,
              ),
              textAlign: TextAlign.center,
              textScaleFactor: 0.9),
        ),
      ),
    );
  }

  Widget _getBottomDateTip() {
    return Card(
      elevation: 2,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(0)),
      margin: EdgeInsets.only(bottom: 2),
      child: Row(
        children: _getDateItemTip(),
      ),
    );
  }

  List<Widget> _getDateItemTip() {
    var curWeek = CourseUtil.getCurWeek();
    var week = _curWeek == -1 ? curWeek : _curWeek;
    List<String> date =
        CourseUtil.getCurDayOfWeek(week);
    List<Widget> widget = List(date.length);
    int curDayIndex =
        curWeek == week ? CourseUtil.getCurDayInCurWeekIndex() : -1;
    for (int i = 0; i < date.length; i++) {
      widget[i] = Expanded(
        flex: 1,
        child: Container(
            alignment: Alignment.center,
            height: 35,
            decoration: i == curDayIndex
                ? BoxDecoration(
                    color: Color(0xFF96B8DE),
                    borderRadius: BorderRadius.only(
                        bottomLeft: Radius.circular(1),
                        bottomRight: Radius.circular(1)))
                : null,
            child: Text(
              date[i],
              textScaleFactor: 0.8,
              textAlign: TextAlign.center,
              style: TextStyle(
                  color: i == curDayIndex ? Colors.white : Colors.black),
            )),
      );
    }
    return widget;
  }
}
