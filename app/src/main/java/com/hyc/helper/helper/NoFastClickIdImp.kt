package com.hyc.helper.helper

import android.view.View

/**
 * @author: 贺宇成
 * @date: 2019-11-23 12:52
 * @desc:
 */
class NoFastClickIdImp(private val timeDuration: Long) : NoFastClick {

  private val viewKeysMap = mutableMapOf<String, Long>()

  override fun canClick(view: View?, className: String?): Boolean {
    val key = "${className}@${view?.id}"
    val canClick = (now() - (viewKeysMap[key] ?: 0L) >= timeDuration)
    if (canClick) viewKeysMap[key] = now()
    return canClick
  }

  override fun clearTimeOutView() {
    val iterator = viewKeysMap.iterator()
    while (iterator.hasNext()) {
      if (now() - iterator.next().value >= timeDuration) {
        iterator.remove()
      }
    }
  }

  override fun removeLimeOutView(view: View?, className: String?): Int {
    val key = "${className}@${view?.id}"
    return if (viewKeysMap.remove(key) != null) 1 else -1
  }

  private fun now() = System.currentTimeMillis()
}