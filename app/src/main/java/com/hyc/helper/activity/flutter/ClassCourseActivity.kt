package com.hyc.helper.activity.flutter

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.hyc.helper.R
import com.hyc.helper.base.util.ToastHelper
import com.hyc.helper.model.CourseModel
import com.hyc.helper.model.UserModel

/**
 * @author: 贺宇成
 * @date: 2019-12-27 09:44
 * @desc:
 */
class ClassCourseActivity : BaseFlutterActivity() {
  private val mUserModel = UserModel()
  private val mCourseModel = CourseModel()
  private val REQUEST_SELECT_CLASS = 1001

  override fun getFlutterRouter(): String {
    return javaClass.simpleName
  }

  override fun getMenuId(): Int {
    return R.menu.menu_class_course
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.item_select_class -> goToOtherActivityForResult(
        ClassSelectActivity::class.java,
        REQUEST_SELECT_CLASS
      )
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setToolBarTitle("课表查询")
  }

}