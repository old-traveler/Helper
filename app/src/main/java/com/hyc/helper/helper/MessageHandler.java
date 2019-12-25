package com.hyc.helper.helper;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import com.google.gson.Gson;
import com.hyc.helper.util.RxBus;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class MessageHandler extends BmobIMMessageHandler {

  @Override
  public void onMessageReceive(MessageEvent event) {
    super.onMessageReceive(event);
    dispatchMessage(event);
  }

  @SuppressLint("CheckResult")
  private void dispatchMessage(MessageEvent event) {
    Flowable.just(event)
        .map(messageEvent -> {
          String info = null;
          try {
            JSONObject object = new JSONObject(event.getMessage().getExtra());
            info = object.getString("info");
          } catch (JSONException e) {
            e.printStackTrace();
          }
          if (!TextUtils.isEmpty(info)) {
            BmobIMUserInfo bmobIMUserInfo = new Gson().fromJson(info, BmobIMUserInfo.class);
            BmobIMConversation conversation = event.getConversation();
            if (!notNeedUpdate(bmobIMUserInfo, conversation)) {
              conversation.setConversationTitle(bmobIMUserInfo.getName());
              conversation.setConversationIcon(bmobIMUserInfo.getAvatar());
              BmobIM.getInstance().updateUserInfo(bmobIMUserInfo);
              BmobIM.getInstance().updateConversation(conversation);
            }
          }
          return event.getMessage();
        })
        .subscribeOn(Schedulers.io())
        .subscribe(bmobIMMessage -> {
          RxBus.getDefault()
              .post(new com.hyc.helper.bean.MessageEvent<>(Constant.EventType.IM_MESSAGE,
                  event.getMessage()));
        });
  }

  private boolean notNeedUpdate(BmobIMUserInfo info, BmobIMConversation conversation) {
    return info.getAvatar().equals(conversation.getConversationIcon())
        && info.getName().equals(conversation.getConversationTitle());
  }

  @Override
  public void onOfflineReceive(OfflineMessageEvent event) {
    super.onOfflineReceive(event);
    for (List<MessageEvent> messageEvents : event.getEventMap().values()) {
      for (MessageEvent messageEvent : messageEvents) {
        dispatchMessage(messageEvent);
      }
    }
  }
}
