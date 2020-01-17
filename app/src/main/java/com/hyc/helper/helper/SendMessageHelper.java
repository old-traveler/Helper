package com.hyc.helper.helper;

import cn.bmob.newim.bean.BmobIMAudioMessage;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.MessageSendListener;
import com.google.gson.Gson;
import com.hyc.helper.bean.ImageMessageRecord;
import com.hyc.helper.bean.ImageSizeBean;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SendMessageHelper {

  public static void sendTxtMessage(BmobIMUserInfo imUserInfo, BmobIMConversation c
      , String content, MessageSendListener listener) {
    BmobIMTextMessage msg = new BmobIMTextMessage();
    msg.setContent(content);
    msg.setFromId(imUserInfo.getUserId());
    if (imUserInfo.getName() != null) {
      Map<String, Object> map = new HashMap<>(1);
      map.put("info", new Gson().toJson(imUserInfo));
      msg.setExtraMap(map);
    }
    c.sendMessage(msg, listener);
  }

  public static void sendVoiceMessage(BmobIMUserInfo imUserInfo, BmobIMConversation c
      , String local, int length, MessageSendListener listener) {
    BmobIMAudioMessage audio = new BmobIMAudioMessage(local);
    audio.setDuration(length);
    audio.setFromId(imUserInfo.getUserId());
    if (imUserInfo.getName() != null) {
      Map<String, Object> map = new HashMap<>(1);
      map.put("info", new Gson().toJson(imUserInfo));
      audio.setExtraMap(map);
    }
    c.sendMessage(audio, listener);
  }

  public static void sendImageMessage(BmobIMUserInfo imUserInfo, BmobIMConversation conversation,
      File file, MessageSendListener listener) {
    BmobIMImageMessage imImageMessage = new BmobIMImageMessage(file);
    Map<String, Object> map = new HashMap<>(2);
    ImageSizeBean sizeBean = FileHelper.getImageSize(file.getAbsolutePath());
    map.put("width", sizeBean.getWidth());
    map.put("height", sizeBean.getHeight());
    if (imUserInfo.getName() != null) {
      map.put("info", new Gson().toJson(imUserInfo));
    }
    imImageMessage.setExtraMap(map);
    imImageMessage.setFromId(imUserInfo.getUserId());
    conversation.sendMessage(imImageMessage, listener);
  }

  public static void sendImageMessage(BmobIMUserInfo imUserInfo, BmobIMConversation conversation,
      ImageMessageRecord record, MessageSendListener listener) {
    BmobIMImageMessage imImageMessage = new BmobIMImageMessage();
    imImageMessage.setRemoteUrl(record.getCloudPath());
    Map<String, Object> map = new HashMap<>(2);
    ImageSizeBean sizeBean = FileHelper.getImageSize(record.getCompressPath());
    map.put("width", sizeBean.getWidth());
    map.put("height", sizeBean.getHeight());
    if (imUserInfo.getName() != null) {
      map.put("info", new Gson().toJson(imUserInfo));
    }
    imImageMessage.setExtraMap(map);
    imImageMessage.setFromId(imUserInfo.getUserId());
    conversation.sendMessage(imImageMessage, listener);
  }
}
