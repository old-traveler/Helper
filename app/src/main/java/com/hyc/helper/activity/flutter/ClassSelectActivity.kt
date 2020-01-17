package com.hyc.helper.activity.flutter

import android.app.Activity
import android.os.Bundle
import android.util.Pair
import android.view.MenuItem
import com.hyc.helper.R
import com.hyc.helper.base.util.ToastHelper
import com.hyc.helper.bean.ClassCourseBean
import com.hyc.helper.helper.SpCacheHelper
import com.hyc.helper.model.CourseModel
import com.hyc.helper.model.UserModel
import io.flutter.plugin.common.BasicMessageChannel.Reply

/**
 * @author: 贺宇成
 * @date: 2019-12-27 10:55
 * @desc:
 */
class ClassSelectActivity : BaseFlutterActivity() {
  private val mUserModel = UserModel()
  private val mCourseModel = CourseModel()
  private var classLevel: Boolean = false
  private var depIndex = -1
  private var mData: MutableList<Pair<String, MutableList<String>>>? = null

  override fun getFlutterRouter(): String {
    return javaClass.simpleName
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setToolBarTitle("班级选择")
  }

  override fun getMenuId(): Int {
    return R.menu.menu_class_select
  }

  private fun fetchClassList(reply: Reply<String>) {
    val courseBean = if (mData != null) null else SpCacheHelper.getClassFromSp(
      "ClassCourseBean",
      ClassCourseBean::class.java
    )
    courseBean?.let {
      mData = mCourseModel.jsonToList(it)
    }
    mData?.let {
      setToolBarTitle("班级选择")
      reply.reply(getDepList(mData!!))
      return
    }

    showLoadingView()
    addDisposable(
      mCourseModel.getClassListInfo(
        mUserModel.studentId,
        mUserModel.curUserInfo.remember_code_app
      ).subscribe({ t ->
        mData = t
        reply.reply(getDepList(t))
        closeLoadingView()
      }, { t ->
        reply.reply("")
        closeLoadingView()
        run {
          t?.let {
            ToastHelper.toast(it.message)
            it.printStackTrace()
          }
        }
      })
    )
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    if (item?.itemId == android.R.id.home) {
      onBackPressed()
      return true
    } else if (item?.itemId == R.id.item_refresh) {
      mData = null
      classLevel = false
      setToolBarTitle("班级选择")
      SpCacheHelper.removeKey("ClassCourseBean")
      channel.send("refresh")
    }
    return super.onOptionsItemSelected(item)
  }

  private fun fetchDepClassList(index: Int, reply: Reply<String>) {
    setToolBarTitle(mData!![index].first)
    depIndex = index
    val list = mData!![index].second
    val stringBuilder = StringBuilder()
    list.forEach {
      stringBuilder.append("$it#")
    }
    if (stringBuilder.isNotEmpty()) {
      stringBuilder.deleteCharAt(stringBuilder.lastIndex)
    }
    reply.reply(stringBuilder.toString())
  }

  override fun onBackPressed() {
    if (classLevel) {
      classLevel = false
      channel.send("refresh")
      return
    }
    super.onBackPressed()
  }

  private fun getDepList(list: MutableList<Pair<String, MutableList<String>>>): String {
    val stringBuilder = StringBuilder()
    list.forEach {
      stringBuilder.append("${it.first}#")
    }
    if (stringBuilder.isNotEmpty()) {
      stringBuilder.deleteCharAt(stringBuilder.lastIndex)
    }
    return stringBuilder.toString()
  }

  override fun onMessage(message: String?, reply: Reply<String>) {
    super.onMessage(message, reply)
    if (message == "fetchClassList") {
      fetchClassList(reply)
    } else if (message?.startsWith("fetchClassList") == true) {
      val index = message.substring(14).toIntOrNull()
      if (!classLevel) {
        classLevel = true
        fetchDepClassList(index!!, reply)
      } else {
        val className = mData!![depIndex].second[index!!]
        val bundle = Bundle()
        bundle.putString("className", className)
        backForResult(ClassCourseActivity::class.java, bundle, Activity.RESULT_OK)
      }
    }
  }

}