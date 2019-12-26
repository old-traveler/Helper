import 'dart:async';
import 'dart:convert';
import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_ui/entity/user_entity.dart';
import 'package:flutter_ui/pages/user_info_page.dart';

void main() => runApp(_widgetForRoute(window.defaultRouteName));

Widget _widgetForRoute(String route) {
  switch (route) {
    case 'router1':
      return MyHomePage();
    default:
      return Center(
        child: Text('Unknown route: $route', textDirection: TextDirection.ltr),
      );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);
  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static const basicChannel =
      BasicMessageChannel("helper_flutter", StringCodec());
  UserData userData;


  @override
  void initState() {
    super.initState();
    _fetchData();
    basicChannel.setMessageHandler((String message) async{
      return '来自Fluuter的信息';
    });


  }

  _fetchData() async {
    final String reply = await basicChannel.send("fetch_user_data");
    setState(() {
      userData = UserData.fromJson(json.decode(reply));
    });
  }

  @override
  Widget build(BuildContext context) {
    return Container(
        child: userData == null
            ? Text(
                '',
                textDirection: TextDirection.ltr,
              )
            : UserInfoPage(
                userData: userData,
              ));
  }
}
