package com.hyc.helper.activity.flutter

import android.os.Bundle
import com.google.gson.Gson
import com.hyc.helper.base.util.ToastHelper
import com.hyc.helper.model.UserModel
import io.flutter.plugin.common.BasicMessageChannel.Reply

/**
 * @author: 贺宇成
 * @date: 2019-12-26 09:41
 * @desc:
 */
class PersonalActivity : BaseFlutterActivity() {
  override fun getFlutterRouter(): String {
    return "Personal"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setToolBarTitle("个人信息")
  }

  private val userModel = UserModel()

  override fun onMessage(message: String?, reply: Reply<String>) {
    if (message == "fetch_user_data") {
      reply.reply(Gson().toJson(userModel.curUserInfo.data))
      channel.send("fetch_refresh") {
        ToastHelper.toast(it)
      }
    }
  }
}