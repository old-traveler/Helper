package com.hyc.helper.cuckoo

import android.Manifest
import android.support.v4.app.FragmentActivity
import com.hyc.cuckoo_lib.OnPermissionRefuseListener
import com.hyc.helper.HelperApplication
import com.hyc.helper.R
import com.hyc.helper.base.view.CommonDialog
import java.lang.StringBuilder

/**
 * @author: 贺宇成
 * @date: 2019-12-24 15:07
 * @desc:
 */
class PermissionRefuseListener : OnPermissionRefuseListener {
  override fun onPermissionRefuse(
    activity: FragmentActivity?,
    methodName: String,
    grant: List<String>,
    refuse: List<String>?
  ): Boolean {
    val content = StringBuilder("请打开设置给予以下权限：\n")
    refuse?.forEach {
      content.append("* ${getPermissionName(it)}\n")
    }
    CommonDialog.Builder(activity)
      .setTitle("授权失败")
      .setContent(content.toString())
      .setPositiveName(HelperApplication.getContext().resources.getString(R.string.confirm))
      .createAndShow()

    return true
  }

  private fun getPermissionName(it: String): String {
    return when (it) {
      Manifest.permission.CAMERA -> "访问相机"
      Manifest.permission.WRITE_EXTERNAL_STORAGE -> "读取内存"
      Manifest.permission.RECORD_AUDIO -> "麦克风"
      else -> it
    }
  }
}