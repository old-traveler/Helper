package com.hyc.helper.activity.flutter

import android.content.Intent
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.v4.app.FragmentManager
import android.view.ViewGroup.MarginLayoutParams
import com.google.gson.Gson
import com.hyc.helper.R
import com.hyc.helper.base.activity.BaseActivity
import com.hyc.helper.base.util.ToastHelper
import com.hyc.helper.model.UserModel
import io.flutter.embedding.android.FlutterFragment
import io.flutter.plugin.common.BasicMessageChannel
import io.flutter.plugin.common.BasicMessageChannel.Reply
import io.flutter.plugin.common.StringCodec

/**
 * @author: 贺宇成
 * @date: 2019-12-26 09:41
 * @desc:
 */
class PersonalActivity : BaseActivity(), BasicMessageChannel.MessageHandler<String> {

  private var flutterFragment: FlutterFragment? = null
  private val userModel  = UserModel()

  companion object {
    private const val TAG_FLUTTER_FRAGMENT = "flutter_fragment"
  }

  override fun getContentViewId(): Int {
    return R.layout.flutter_personal
  }

  override fun initViewWithIntentData(bundle: Bundle?) {
    setToolBarTitle("个人信息")
    (mToolbar.layoutParams as MarginLayoutParams).topMargin = statusHeight
    val fragmentManager: FragmentManager = supportFragmentManager

    flutterFragment = fragmentManager
      .findFragmentByTag(TAG_FLUTTER_FRAGMENT) as FlutterFragment?

    if (flutterFragment == null) {
      val newFlutterFragment =
        FlutterFragment.NewEngineFragmentBuilder().initialRoute("router1").build<FlutterFragment>()
      flutterFragment = newFlutterFragment
      fragmentManager
        .beginTransaction()
        .add(
          R.id.fragment_container,
          newFlutterFragment,
          TAG_FLUTTER_FRAGMENT
        )
        .commit()
    }
  }

  override fun onPostResume() {
    super.onPostResume()
    flutterFragment!!.onPostResume()
  }

  override fun onNewIntent(@NonNull intent: Intent) {
    flutterFragment!!.onNewIntent(intent)
  }

  override fun onBackPressed() {
    flutterFragment!!.onBackPressed()
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String?>,
    grantResults: IntArray
  ) {
    flutterFragment!!.onRequestPermissionsResult(
      requestCode,
      permissions,
      grantResults
    )
  }

  override fun onUserLeaveHint() {
    flutterFragment!!.onUserLeaveHint()
  }

  override fun onTrimMemory(level: Int) {
    super.onTrimMemory(level)
    flutterFragment!!.onTrimMemory(level)
  }

  override fun onResume() {
    super.onResume()
    registerPlugin()
  }
  private lateinit var channel : BasicMessageChannel<String>

  private fun registerPlugin() {
    channel = BasicMessageChannel(
      flutterFragment!!.flutterEngine!!.dartExecutor,
      "helper_flutter",
      StringCodec.INSTANCE
    )
    channel.setMessageHandler(this)

  }

  override fun onMessage(message: String?, reply: Reply<String>) {
    if (message == "fetch_user_data"){
      reply.reply(Gson().toJson(userModel.curUserInfo.data))
      channel.send("fetch_refresh"){
        ToastHelper.toast(it)
      }
    }
  }

}