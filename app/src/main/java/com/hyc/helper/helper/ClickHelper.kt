package com.hyc.helper.helper

import android.view.View
import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

/**
 * @author: 贺宇成
 * @date: 2019-11-20 15:44
 * @desc:
 */
object ClickHelper {
  private const val timeDuration = 500L

  private val viewMaps = mutableListOf<ClickView>()

  @JvmStatic
  fun canClick(view: View?): Boolean {
    view ?: return true
    val index = removeLimeOutView(view)
    if (index < 0) {
      return viewMaps.add(ClickView(view))
    }
    return false
  }

  @JvmStatic
  fun clearTimeOutView() {
    removeLimeOutView(null)
  }

  private fun removeLimeOutView(view: View?): Int {
    val it = viewMaps.iterator()
    var index = 1
    while (it.hasNext()) {
      val item = it.next()
      if (item.isTimeOut(timeDuration)) {
        it.remove()
        continue
      }
      if (item.view == view) {
        index = (index - 1).inv()
      }
      if (index >= 0) index++
    }
    return index.inv()
  }
}

class ClickView(v: View) {
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