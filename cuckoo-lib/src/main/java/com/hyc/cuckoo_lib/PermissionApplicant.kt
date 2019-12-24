package com.hyc.cuckoo_lib

import android.app.Activity

/**
 * @author: 贺宇成
 * @date: 2019-12-23 15:33
 * @desc:
 */
interface PermissionApplicant {

  fun hasPermission(permission: List<String>): Boolean

  fun requestPermission(
    activity: Activity,
    permission: List<String>,
    callback: (grant: List<String>, refuse: List<String>?) -> Unit
  )

}