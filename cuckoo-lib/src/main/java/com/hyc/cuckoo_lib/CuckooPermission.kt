package com.hyc.cuckoo_lib

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.support.v4.app.FragmentActivity
import java.lang.RuntimeException
import java.lang.reflect.Method

/**
 * @author: 贺宇成
 * @date: 2019-12-15 17:58
 * @desc:
 */
@SuppressLint("StaticFieldLeak")
object CuckooPermission {
  private val mPermissionMap = mutableMapOf<String, Boolean>()

  private var lifecycleCallback = CuckooLifecycleCallback()
  private var mApplication: Application? = null
  private var mPermissionApplicant: PermissionApplicant? = null
  private var mDefaultRefuseListener: OnPermissionRefuseListener? = null

  @JvmStatic
  public fun init(
    application: Application,
    permissionApplicant: PermissionApplicant,
    defaultRefuseListener: OnPermissionRefuseListener
  ) {
    mApplication = application
    this.mPermissionApplicant = permissionApplicant
    this.mDefaultRefuseListener = defaultRefuseListener
    if (VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH) {
      application.registerActivityLifecycleCallbacks(lifecycleCallback)
    }
  }

  @JvmStatic
  public fun initPermission(
    permission: String,
    any: Any,
    methodName: String,
    paramList: List<Any?>
  ): Boolean {
    mApplication ?: throw RuntimeException("init method must be called first")
    val permissionList = getPermissions(permission)
    if (hasPermission(permissionList)) {
      return false
    } else if (mPermissionApplicant!!.hasPermission(permissionList)) {
      permissionList.forEach {
        mPermissionMap[it] = true
      }
      return false
    }
    mPermissionApplicant!!.requestPermission(
      lifecycleCallback.curActivity!!,
      permissionList
    ) { grant, refuse ->
      grant.forEach { mPermissionMap[it] = true }
      if (refuse.isNullOrEmpty()) {
        invokeExecute(any, methodName, paramList)
      } else {
        dispatchPermissionRefuse(any, methodName, grant, refuse)
      }
    }
    return true
  }

  private fun dispatchPermissionRefuse(
    any: Any,
    methodName: String,
    grant: List<String>,
    refuse: List<String>
  ) {
    var deal = false
    if (any is OnPermissionRefuseListener) {
      deal = any.onPermissionRefuse(
        lifecycleCallback.curActivity as FragmentActivity,
        methodName,
        grant,
        refuse
      )
    } else if (any is String) {
      val targetClass = Class.forName(any)
      try {
        deal = targetClass.getDeclaredMethod(
          "onPermissionRefuse",
          String::class.java,
          List::class.java,
          List::class.java
        ).invoke(null, methodName, grant, refuse) as Boolean
      } catch (e: NoSuchMethodException) {
        deal = false
      } catch (e: NullPointerException) {
        if (e.message == "null receiver") {
          val constructor = targetClass?.getDeclaredConstructor()
          if (constructor?.isAccessible == false) constructor.isAccessible = true
          val targetObject = constructor?.newInstance()
          deal = targetClass.getDeclaredMethod(
            "onPermissionRefuse",
            String::class.java,
            List::class.java,
            List::class.java
          ).invoke(targetObject, methodName, grant, refuse) as Boolean
        } else {
          throw NullPointerException(e.message)
        }
      }
    }
    assert(mDefaultRefuseListener != null)
    if (!deal) {
      mDefaultRefuseListener!!.onPermissionRefuse(
        lifecycleCallback.curActivity as FragmentActivity,
        methodName,
        grant,
        refuse
      )
    }
  }

  private fun hasPermission(permission: List<String>): Boolean {
    permission.forEach {
      if (mPermissionMap[it] != true) {
        return false
      }
    }
    return true
  }

  private fun getPermissions(permission: String): List<String> {
    return permission.split("&")
  }

  private fun invokeExecute(any: Any, methodName: String, paramList: List<Any?>) {
    var method: Method? = null
    val clazz =
      if (any is String) {
        Class.forName(any)
      } else {
        any::class.java
      }
    clazz.declaredMethods.forEach {
      if (it.name == methodName && it.parameterTypes.size == paramList.size) {
        method = it
        return@forEach
      }
    }
    method ?: throw RuntimeException("not find $methodName method")
    if (!method!!.isAccessible) method!!.isAccessible = true
    try {
      if (paramList.isEmpty()) {
        method!!.invoke(if (any is String) null else any)
      } else {
        method!!.invoke(if (any is String) null else any, *paramList.toTypedArray())
      }
    } catch (e: NullPointerException) {
      if (e.message == "null receiver") {
        val constructor = clazz.getDeclaredConstructor()
        if (constructor?.isAccessible == false) constructor.isAccessible = true
        val targetObject = constructor?.newInstance()
        if (paramList.isEmpty()) {
          method!!.invoke(targetObject)
        } else {
          method!!.invoke(targetObject, *paramList.toTypedArray())
        }
      } else {
        throw NullPointerException(e.message)
      }
    }
  }

}