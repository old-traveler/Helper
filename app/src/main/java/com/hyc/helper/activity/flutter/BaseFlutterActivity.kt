package com.hyc.helper.activity.flutter

import android.content.Intent
import android.os.Bundle
import android.support.annotation.NonNull
import android.view.ViewGroup.MarginLayoutParams
import com.hyc.helper.R
import com.hyc.helper.base.activity.BaseActivity
import io.flutter.embedding.android.FlutterFragment
import io.flutter.plugin.common.BasicMessageChannel
import io.flutter.plugin.common.BasicMessageChannel.Reply
import io.flutter.plugin.common.StringCodec

/**
 * @author: 贺宇成
 * @date: 2019-12-26 17:09
 * @desc:
 */
abstract class BaseFlutterActivity : BaseActivity(),
  BasicMessageChannel.MessageHandler<String> {

  private lateinit var flutterFragment: FlutterFragment

  lateinit var channel: BasicMessageChannel<String>

  private fun registerPlugin() {
    channel = BasicMessageChannel(
      flutterFragment.flutterEngine!!.dartExecutor,
      javaClass.simpleName,
      StringCodec.INSTANCE
    )
    channel.setMessageHandler(this)

  }

  override fun getContentViewId(): Int {
    return R.layout.activity_base_flutter
  }


  override fun setToolBarTitle(title: String?) {
    super.setToolBarTitle(title)
    (mToolbar.layoutParams as MarginLayoutParams).topMargin = statusHeight
  }

  override fun initViewWithIntentData(bundle: Bundle?) {
    flutterFragment =
      FlutterFragment.NewEngineFragmentBuilder().initialRoute(getFlutterRouter())
        .build()
    supportFragmentManager
      .beginTransaction()
      .add(
        R.id.fragment_container,
        flutterFragment
      )
      .commit()
  }

  override fun onResume() {
    super.onResume()
    registerPlugin()
  }

  abstract fun getFlutterRouter(): String

  override fun onPostResume() {
    super.onPostResume()
    flutterFragment.onPostResume()
  }

  override fun onNewIntent(@NonNull intent: Intent) {
    flutterFragment.onNewIntent(intent)
  }

  override fun onBackPressed() {
    flutterFragment.onBackPressed()
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String?>,
    grantResults: IntArray
  ) {
    flutterFragment.onRequestPermissionsResult(
      requestCode,
      permissions,
      grantResults
    )
  }

  override fun onUserLeaveHint() {
    flutterFragment.onUserLeaveHint()
  }

  override fun onTrimMemory(level: Int) {
    super.onTrimMemory(level)
    flutterFragment.onTrimMemory(level)
  }

  override fun onMessage(message: String?, reply: Reply<String>) {

  }

}