package com.hyc.helper.activity.flutter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import com.google.gson.Gson
import com.hyc.helper.R
import com.hyc.helper.R.layout
import com.hyc.helper.activity.CourseDetailActivity
import com.hyc.helper.base.util.ToastHelper
import com.hyc.helper.base.util.UiHelper
import com.hyc.helper.bean.CourseBean
import com.hyc.helper.helper.DateHelper
import com.hyc.helper.helper.SpCacheHelper
import com.hyc.helper.model.CourseModel
import com.hyc.helper.model.UserModel
import com.hyc.helper.util.DensityUtil
import io.flutter.plugin.common.BasicMessageChannel.Reply
import java.util.ArrayList

/**
 * @author: 贺宇成
 * @date: 2019-12-27 09:44
 * @desc:
 */
class ClassCourseActivity : BaseFlutterActivity() {
  private val mUserModel = UserModel()
  private val mCourseModel = CourseModel()
  private val REQUEST_SELECT_CLASS = 1001
  private var mClassName: String? = SpCacheHelper.getString("className")
  private var mCourseBean: CourseBean? = null
  private val mGson = Gson()
  private var weekListPopWindow: ListPopupWindow? = null
  private var selectWeek: MenuItem? = null

  override fun getFlutterRouter(): String {
    return javaClass.simpleName
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_class_course, menu)
    selectWeek = menu?.findItem(R.id.week_select)
    selectWeek?.title = UiHelper.getString(R.string.week_tip, DateHelper.getCurWeek())
    initListPopView()
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.item_select_class -> goToOtherActivityForResult(
        ClassSelectActivity::class.java,
        REQUEST_SELECT_CLASS
      )
      R.id.week_select -> {
        weekListPopWindow?.anchorView = findViewById(R.id.week_select)
        weekListPopWindow?.show()
        weekListPopWindow?.listView
          ?.setBackgroundColor(UiHelper.getColor(R.color.white))
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setToolBarTitle("课表查询")
  }

  private fun initListPopView() {
    val curWeek = DateHelper.getCurWeek()
    val list = ArrayList<String>()
    for (i in 1..20) {
      list.add(UiHelper.getString(R.string.week_tip, i))
      if (curWeek == i) {
        list[i - 1] = "本周"
      }
    }
    weekListPopWindow = ListPopupWindow(this)
    weekListPopWindow?.let {
      it.setAdapter(ArrayAdapter(this, layout.item_select_week, list))
      it.width = DensityUtil.dip2px(80f)
      it.height = DensityUtil.dip2px(150f)
      it.isModal = true
      it.setOnItemClickListener { _, _, i, _ ->
        selectWeek?.title = UiHelper.getString(R.string.week_tip, i + 1)
        channel.send("switchWeek${i + 1}")
        weekListPopWindow?.dismiss()
      }
    }

  }

  private fun getClassCourse(reply: Reply<String>) {
    setToolBarTitle(if (mClassName.isNullOrEmpty()) "课表查询" else mClassName)
    mCourseBean?.let {
      reply.reply(mGson.toJson(it))
      return
    }
    mClassName ?: let {
      reply.reply("")
      return
    }
    addDisposable(
      mCourseModel.getClassCourse(
        mUserModel.studentId,
        mUserModel.curUserInfo.remember_code_app,
        mClassName
      ).subscribe({
        mCourseBean = it
        Log.d("数据返回", mGson.toJson(it))
        reply.reply(mGson.toJson(it))
      }, {
        reply.reply("")
        ToastHelper.toast(it.message)
      })
    )
  }

  override fun onMessage(message: String?, reply: Reply<String>) {
    super.onMessage(message, reply)
    if (message == "getClassCourse") {
      getClassCourse(reply)
    } else if (message?.startsWith("courseDetail") == true) {
      val id = message.substring(12)
      mCourseBean?.data?.forEach {
        if (it.courseId == id) {
          CourseDetailActivity.startCourseDetail(this, it)
          return@forEach
        }
      }
      reply.reply("")
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == REQUEST_SELECT_CLASS && resultCode == Activity.RESULT_OK) {
      data?.extras?.getString("className")?.let {
        mClassName = it
        SpCacheHelper.putString("className", it)
        mCourseBean = null
        channel.send("refresh")
      }
    }
  }

}