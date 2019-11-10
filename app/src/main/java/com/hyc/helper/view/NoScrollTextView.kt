package com.hyc.helper.view

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.hyc.helper.helper.LogHelper

/**
 * @author: 贺宇成
 * @date: 2019-11-10 16:50
 * @desc: 可控制滚动的TextView
 */
class NoScrollTextView @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {
  var scrollAble = false

  override fun scrollTo(x: Int, y: Int) {
    if (scrollAble) {
      super.scrollTo(x, y)
    }
    LogHelper.log("scrollable :$scrollAble")
  }

}