import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_ui/entity/user_entity.dart';
import 'package:flutter_ui/pages/base_page_state.dart';

class UserInfoPage extends StatefulWidget {
  const UserInfoPage({Key key}) : super(key: key);

  @override
  State<StatefulWidget> createState() {
    return UserInfoState("PersonalActivity");
  }
}

class UserInfoState<UserInfoPage> extends BaseInteractiveState {
  UserInfoState(String channelName) : super(channelName);
  UserData userData;

  @override
  void initState() {
    super.initState();
    _fetchData();
  }

  _fetchData() async {
    final String reply = await basicChannel.send("fetch_user_data");
    setState(() {
      userData = UserData.fromJson(json.decode(reply));
    });
  }

  @override
  Widget build(BuildContext context) {
    return userData == null
        ? Text(
            '',
            textDirection: TextDirection.ltr,
          )
        : Container(
            child: Column(
              children: <Widget>[
                _getUserInfoWidget(),
                _getUserMoreInfoWidget(),
              ],
            ),
          );
  }

  Widget _getUserInfoWidget() {
    return Card(
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.zero),
      elevation: 3,
      margin: EdgeInsets.only(left: 10, right: 10, top: 20, bottom: 10),
      child: Stack(
        alignment: Alignment.topLeft,
        children: <Widget>[
          Container(
              height: 100,
              padding: EdgeInsets.only(left: 20),
              alignment: Alignment.centerLeft,
              child: GestureDetector(
                child: ClipOval(
                  child: Image.network(
                    "http://223.111.182.121:8888/" + userData.headPicThumb,
                    width: 65,
                    height: 65,
                  ),
                ),
                onTap: () {
                  _sendCommend("startSelectImage");
                },
              )),
          Positioned(
            top: 25,
            left: 100,
            child: GestureDetector(
              child: Text(
                  userData.username.isEmpty
                      ? userData.trueName
                      : userData.username,
                  style: TextStyle(fontSize: 20),
                  textDirection: TextDirection.ltr),
              onTap: () {
                _sendCommend("updateUsername");
              },
            ),
          ),
          Positioned(
            top: 57,
            left: 100,
            child: GestureDetector(
              child: Text(userData.bio.isEmpty ? "暂无签名" : userData.bio,
                  style: TextStyle(fontSize: 14, color: Colors.grey),
                  textDirection: TextDirection.ltr),
              onTap: () {
                _sendCommend("updateBio");
              },
            ),
          )
        ],
      ),
    );
  }

  Widget _getUserMoreInfoWidget() {
    return Card(
        elevation: 3,
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.zero),
        margin: EdgeInsets.all(10),
        child: Container(
          alignment: Alignment.topLeft,
          padding: EdgeInsets.only(left: 15, right: 10, top: 15, bottom: 18),
          child: Text(
            "姓名：${userData.trueName}\n\n学号：${userData.studentKH}\n\n学院：${userData.depName}\n\n班级：${userData.className}\n\n高中：${userData.school}",
            style: TextStyle(fontSize: 16),
            textDirection: TextDirection.ltr,
          ),
        ));
  }

  _sendCommend(String commend) async {
    basicChannel.send(commend);
  }

  @override
  Future<String> handlerMessage(String message) async {
    if (message == "refresh") {
      _fetchData();
      return '';
    }
    return "UserInfoState";
  }
}
