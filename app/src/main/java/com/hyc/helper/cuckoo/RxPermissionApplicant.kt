package com.hyc.helper.cuckoo

import android.annotation.SuppressLint
import android.app.Activity
import android.support.v4.app.FragmentActivity
import com.hyc.cuckoo_lib.PermissionApplicant
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 * @author: 贺宇成
 * @date: 2019-12-24 15:08
 * @desc:
 */
class RxPermissionApplicant : PermissionApplicant {

  override fun hasPermission(permission: List<String>): Boolean {
    return false
  }

  @SuppressLint("CheckResult")
  override fun requestPermission(
    activity: Activity,
    permission: List<String>,
    callback: (grant: List<String>, refuse: List<String>?) -> Unit
  ) {
    RxPermissions(activity as FragmentActivity).request(*permission.toTypedArray()).subscribe {
      if (it) {
        callback(permission, null)
      } else {
        callback(mutableListOf(), permission)
      }
    }
  }

}