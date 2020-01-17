package com.hyc.helper.helper

import android.util.Log
import android.view.View

/**
 * @author: 贺宇成
 * @date: 2019-11-20 15:44
 * @desc:
 */
object ClickHelper {
  var timeDuration = 500L
  var byView = false
  private val noFastClickImp: NoFastClick

  init {
    noFastClickImp = if (byView) {
      NoFastClickViewImp(timeDuration)
    } else {
      NoFastClickIdImp(timeDuration)
    }
  }

  @JvmStatic
  fun canClick(view: View?, className: String? = null): Boolean =
    noFastClickImp.canClick(view, className)

  @JvmStatic
  fun isFastClick(view: View?, className: String?): Boolean {
    val isFastClick = !noFastClickImp.canClick(view, className)
    if (isFastClick) {
      Log.d("ClickHelper", "intercept fast click in $className")
    }
    return isFastClick
  }

  @JvmStatic
  fun clearTimeOutView() = noFastClickImp.clearTimeOutView()

  @JvmStatic
  fun removeLimeOutView(view: View? = null, className: String? = null): Int =
    noFastClickImp.removeLimeOutView(view, className)

}