import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';

abstract class BaseInteractiveState<P extends StatefulWidget> extends State<P> {
  BasicMessageChannel<String> basicChannel;

  BaseInteractiveState(String channelName) {
    basicChannel = BasicMessageChannel(channelName, StringCodec());
    basicChannel.setMessageHandler(handlerMessage);
  }

  Future<String> handlerMessage(String message) async {
    return '';
  }
}
