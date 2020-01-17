package com.hyc.helper.helper

import android.view.View
import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

/**
 * @author: 贺宇成
 * @date: 2019-11-23 12:44
 * @desc:
 */
class NoFastClickViewImp(private val timeDuration: Long) : NoFastClick {

  private val viewMaps = mutableListOf<ClickView>()

  override fun canClick(view: View?, className: String?): Boolean {
    view ?: return true
    val index = removeLimeOutView(view, className)
    if (index < 0) {
      return viewMaps.add(ClickView(view, className))
    }
    return false
  }

  override fun clearTimeOutView() {
    removeLimeOutView()
  }

  override fun removeLimeOutView(view: View?, className: String?): Int {
    val it = viewMaps.iterator()
    var index = 1
    while (it.hasNext()) {
      val item = it.next()
      if (item.isTimeOut(timeDuration)) {
        it.remove()
        continue
      }
      if (item.view == view && item.className == className) {
        index = (index - 1).inv()
      }
      if (index >= 0) index++
    }
    return index.inv()
  }
}

class ClickView(v: View, val className: String?) {
  val view by Weak { v }
  private var time: Long = now()

  fun isTimeOut(timeDuration: Long): Boolean =
    now() - time > timeDuration

  private fun now() = System.currentTimeMillis()

}

class Weak<T : Any>(initializer: () -> T?) {
  private var weakReference = WeakReference(initializer())

  operator fun getValue(thisRef: Any?, property: KProperty<*>): T? = weakReference.get()

  operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
    weakReference = WeakReference(value)
  }

}