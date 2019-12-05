package com.hyc.helper.helper

import android.view.View

/**
 * @author: 贺宇成
 * @date: 2019-11-23 12:48
 * @desc:
 */
interface NoFastClick {

  fun canClick(view: View?, className: String? = null): Boolean

  fun clearTimeOutView()

  fun removeLimeOutView(view: View? = null, className: String? = null): Int

}