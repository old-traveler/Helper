import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_ui/pages/base_page_state.dart';

class ClassSelectPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return ClassSelectState('ClassSelectActivity');
  }
}

class ClassSelectState extends BaseInteractiveState<ClassSelectPage> {
  ClassSelectState(String channelName) : super(channelName);

  List<String> _list = new List();

  @override
  void initState() {
    super.initState();
    _fetchData(-1);
  }

  _fetchData(int index) async {
    final reply =
        await basicChannel.send('fetchClassList${index >= 0 ? index : ''}');
    setState(() {
      _list = reply.split('#');
    });
  }

  @override
  Future<String> handlerMessage(String message) {
    if (message == 'refresh') {
      _fetchData(-1);
    }
    return super.handlerMessage(message);
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        theme: ThemeData.light(),
        home: Material(
          child: ListView.builder(
              padding: EdgeInsets.only(top: 0),
              itemCount: _list.length,
              itemBuilder: (context, index) {
                return ListTile(
                  title: Text(_list[index]),
                  onTap: () {
                    _fetchData(index);
                  },
                );
              }),
        ));
  }
}
