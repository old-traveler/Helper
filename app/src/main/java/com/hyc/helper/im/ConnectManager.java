package com.hyc.helper.im;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.v3.exception.BmobException;
import com.hyc.helper.bean.MessageEvent;
import com.hyc.helper.helper.Constant;
import com.hyc.helper.helper.LogHelper;
import com.hyc.helper.model.UserModel;
import com.hyc.helper.util.RxBus;

public class ConnectManager extends ConnectStatusChangeListener {

  private volatile static ConnectManager connectManager;

  private boolean isConnecting = false;

  private ConnectManager() {
    BmobIM.getInstance().setOnConnectStatusChangeListener(this);
  }

  public synchronized static ConnectManager getDefault() {
    if (connectManager == null) {
      synchronized (ConnectManager.class) {
        if (connectManager == null) {
          connectManager = new ConnectManager();
        }
      }
    }
    return connectManager;
  }

  public synchronized void connect() {
    LogHelper.log("connect");
    if (!isConnecting && !getCurrentStatus().equals(ConnectionStatus.CONNECTED)) {
      isConnecting = true;
      BmobIM.connect(String.valueOf(new UserModel().getCurUserInfo().getData().getUser_id()),
          new ConnectListener() {
            @Override
            public void done(String s, BmobException e) {
              BmobIMUserInfo info = new UserModel().getIMUserInfo();
              isConnecting = false;
              if (e != null) {
                LogHelper.log(e.getMessage());
              } else if (info != null) {
                BmobIM.getInstance().updateUserInfo(info);
              }
            }
          });
    }
  }

  @Override
  public void onChange(ConnectionStatus connectionStatus) {
    LogHelper.log(connectionStatus.getMsg());
    RxBus.getDefault()
        .post(new MessageEvent<>(Constant.EventType.CHANGE_CONNECT, connectionStatus));
  }

  public ConnectionStatus getCurrentStatus() {
    return BmobIM.getInstance().getCurrentStatus();
  }
}
