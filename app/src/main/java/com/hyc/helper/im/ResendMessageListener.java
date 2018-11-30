package com.hyc.helper.im;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.exception.BmobException;
import com.hyc.helper.adapter.ChatAdapter;

public class ResendMessageListener extends MessageSendListener {

  private ChatAdapter chatAdapter;

  public ResendMessageListener(ChatAdapter chatAdapter){
    this.chatAdapter = chatAdapter;
  }

  @Override
  public void done(BmobIMMessage bmobIMMessage, BmobException e) {
    if (chatAdapter != null){
      chatAdapter.updateMessage(bmobIMMessage);
    }
  }
}
