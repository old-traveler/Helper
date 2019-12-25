package com.hyc.cuckoo_lib

import android.support.v4.app.FragmentActivity

/**
 * @author: 贺宇成
 * @date: 2019-12-23 15:56
 * @desc:
 */
interface OnPermissionRefuseListener {

  fun onPermissionRefuse(
    activity: FragmentActivity?,
    methodName: String,
    grant: List<String>,
    refuse: List<String>?
  ): Boolean

}