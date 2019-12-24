package com.hyc.cuckoo_lib

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle

/**
 * @author: 贺宇成
 * @date: 2019-12-20 10:42
 * @desc:
 */
class CuckooLifecycleCallback : ActivityLifecycleCallbacks {
  var curActivity: Activity? = null

  override fun onActivityPaused(activity: Activity?) {
  }

  override fun onActivityResumed(activity: Activity?) {
    curActivity = activity
  }

  override fun onActivityStarted(activity: Activity?) {
  }

  override fun onActivityDestroyed(activity: Activity?) {
    if (curActivity == activity) {
      curActivity = null
    }
  }

  override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
  }

  override fun onActivityStopped(activity: Activity?) {
  }

  override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
  }
}