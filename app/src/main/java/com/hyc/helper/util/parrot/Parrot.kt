@file:Suppress("UNCHECKED_CAST")

package com.hyc.helper.util.parrot

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import com.hyc.helper.BuildConfig
import java.lang.RuntimeException
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType

/**
 * @author: 贺宇成
 * @date: 2019-12-08 15:34
 * @desc: 自动注入Bundle数据到属性中
 */
object Parrot {

  private const val tag = "Parrot"
  private val enableLog = BuildConfig.DEBUG
  private val dataConvert = DataConvert()
  private val cacheAdapter = CacheAdapter(dataConvert)
  /**
   * 是否允许用field的name作为bundleKey
   */
  private var enableNameKey = true

  @JvmStatic
  fun initJsonConvert(jsonConvert: JsonConvert) {
    dataConvert.jsonConvert = jsonConvert
  }

  fun initDefaultSpname(name: String) {
    cacheAdapter.defaultSpName
  }

  fun initDefaultPrefixProvider(prefixProvider: PrefixProvider) {
    cacheAdapter.defaultPrefixProvider = prefixProvider
  }

  fun saveCacheParam(any: Any) {
    cacheAdapter.saveCacheParam(any)
  }

  @JvmStatic
  fun initParam(bundle: Bundle, any: Any) {
    val startTime = System.currentTimeMillis()
    initParamInternal(bundle, any)
    logD("initParam cost ${System.currentTimeMillis() - startTime}")
  }

  @JvmStatic
  fun initBundle(bundle: Bundle, any: Any) {
    val startTime = System.currentTimeMillis()
    initParamInternal(bundle, any, initCacheParam = false)
    logD("initParam cost ${System.currentTimeMillis() - startTime}")
  }

  fun logD(msg: String?) {
    if (enableLog) {
      Log.d(tag, msg)
    }
  }

  fun logE(msg: String?) {
    if (enableLog) {
      Log.e(tag, msg)
    }
  }

  @JvmStatic
  fun initCacheParam(any: Any){
    logD("initCacheParam")
    cacheAdapter.initCacheParam(any, any::class.java.declaredFields)
  }

  @JvmStatic
  fun initParam(any: Any) {
    var bundle: Bundle? = null
    if (any is Activity) {
      bundle = any.intent?.extras
    } else if (any is Fragment) {
      bundle = any.arguments
    }
    bundle?.let {
      initParam(it, any)
    }
  }

  private fun initParamInternal(
    bundle: Bundle,
    any: Any,
    recursiveSet: MutableMap<String, Boolean>? = null,
    initCacheParam: Boolean = true
  ) {
    val fields = any.javaClass.declaredFields
    val keyMap = recursiveSet ?: bundle.toKeyMap()
    fields?.forEach { field ->
      val dataStructure = getInitDataStructure(field)
      val initialClassParam = if (dataStructure == null) getInitialClassParam(
        field
      ) else null
      when {
        dataStructure != null -> //对数据结构类型的field进行数据注入
          if (injectDataStructure(field, bundle, any, dataStructure)) {
            keyMap.signDeal(*dataStructure.value)
          }
        initialClassParam != null && initialClassParam.value.isNotEmpty() -> {
          //对此field进行构造方法数据注入
          val param = initClassParamConstructor(
            field,
            bundle,
            initialClassParam,
            any
          )
          param?.let {
            invokeObject(param, field, any)
            keyMap.signDeal(*initialClassParam.value)
          }
        }
        initialClassParam != null -> {
          //此field为一个类型参数，可将bundle数据注入到此对象中
          field.enableAccessible()
          val param = field.get(any) ?: getParamInstance(field)
          initParamInternal(bundle, param, keyMap)
          invokeObject(param, field, any)
        }
        else -> {
          //针对单个Field进行检查，如果匹配到key，则进行注入
          val paramName = getParamName(field)
          val key = paramName?.belongToSet(keyMap)
          val data = bundle.get(key)
          if (key?.isNotEmpty() == true) {
            field.enableAccessible()
            dataConvert.getConvertData(field.type, data)?.let {
              if (!any.isIntercept(key, data, it)) {
                invokeObject(it, field, any)
              }
              //不管拦截不拦截都标记为处理
              keyMap.signDeal(key)
            }
          }
        }
      }
    }

    //打印未处理的key
    recursiveSet ?: let {
      keyMap.forEach {
        if (!it.value) {
          logE(
            "key: ${it.key} not deal in ${any::class.java.name} data : ${bundle.get(
              it.key
            )}"
          )
        }
      }
    }
    if (initCacheParam) {
      logD("initCacheParam")
      cacheAdapter.initCacheParam(any, fields)
    }
  }

  private fun Bundle.toKeyMap(): MutableMap<String, Boolean> {
    val keyMap = mutableMapOf<String, Boolean>()
    this.keySet().forEach {
      keyMap[it] = false
    }
    return keyMap
  }

  private fun MutableMap<String, Boolean>.signDeal(vararg keys: String) {
    keys.forEach {
      logD("key : \"$it\"  has been processed")
      this[it] = true
    }
  }

  fun Field.enableAccessible() {
    if (!isAccessible) isAccessible = true
  }

  /**
   * 获取当前注入目标对象是否为拦截器类型，如果是拦截器类型并且拦截注入事件
   * 则停止注入行为，跳过此key。{跳过的key也会被认为已处理，不打印错误信息}
   */
  private fun Any.isIntercept(key: String, original: Any?, any: Any?): Boolean {
    if (this is InjectInterceptor) {
      val isIntercept = this.onInject(key, original, any)
      if (isIntercept) {
        logD("key : $key has been Intercepted")
      }
      return isIntercept
    }
    return false
  }

  private fun injectDataStructure(
    field: Field,
    bundle: Bundle,
    any: Any,
    initialMapParam: InitDataStructure
  ): Boolean {
    if (initialMapParam.value.isEmpty()) return false
    field.enableAccessible()
    val dataList = getDataList(field, any, initialMapParam, bundle)
    if (field.type.isArray) {
      return injectArray(dataList, field, any)
    }
    when (field.type) {
      List::class.java -> return injectList(dataList, field, any)
      Set::class.java -> return injectSet(dataList, field, any)
      Map::class.java -> return injectMap(
        dataList,
        field,
        any,
        initialMapParam
      )
      Bundle::class.java -> return injectBundle(
        dataList,
        field,
        any,
        initialMapParam
      )
    }
    return false
  }

  inline fun Field.getActualType(default: () -> Class<*>): Class<*> {
    this.type.componentType?.let { return it }
    return (this.genericType as? ParameterizedType)?.actualTypeArguments
      ?.getOrNull(if (this.type == Map::class.java) 1 else 0) as? Class<*> ?: default()
  }

  private fun getDataList(
    field: Field,
    any: Any,
    initialMapParam: InitDataStructure,
    bundle: Bundle
  ): MutableList<Any?> {
    val clazz = field.getActualType { Any::class.java }
    val isBundle = field.type == Bundle::class.java
    val resList = mutableListOf<Any?>()
    initialMapParam.value.forEach {
      val data = bundle.get(it)
      val convertData = if (isBundle) data else dataConvert.getConvertData(
        clazz,
        data
      )
      if (!any.isIntercept(it, data, convertData)) {
        resList.add(convertData)
      }
    }
    return resList
  }

  private fun injectArray(dataList: MutableList<Any?>, field: Field, any: Any): Boolean {
    dataConvert.toArray(dataList, field.type.componentType)?.let {
      invokeObject(it, field, any)
      return true
    }
    return false
  }

  private fun injectList(dataList: MutableList<Any?>, field: Field, any: Any): Boolean {
    val list: MutableList<Any?> = field.get(any) as? MutableList<Any?> ?: mutableListOf()
    list.addAll(dataList)
    invokeObject(list, field, any)
    return true
  }

  private fun injectSet(dataList: MutableList<Any?>, field: Field, any: Any): Boolean {
    val set: MutableSet<Any?> = field.get(any) as? MutableSet<Any?> ?: mutableSetOf()
    set.addAll(dataList)
    invokeObject(set, field, any)
    return true
  }

  private fun injectMap(
    dataList: MutableList<Any?>,
    field: Field,
    any: Any,
    initDataStructure: InitDataStructure
  ): Boolean {
    val length = dataList.size
    val map: MutableMap<String, Any?> =
      field.get(any) as? MutableMap<String, Any?> ?: mutableMapOf()
    for (index in 0 until length) {
      map[initDataStructure.getTargetKey(index)] =
        dataList[index]
    }
    invokeObject(map, field, any)
    return true
  }

  private fun InitDataStructure.getTargetKey(index: Int): String {
    return if (index >= 0 && index <= mapKey.lastIndex) {
      mapKey[index]
    } else {
      value[index]
    }
  }

  private fun injectBundle(
    dataList: MutableList<Any?>,
    field: Field,
    any: Any,
    initDataStructure: InitDataStructure
  ): Boolean {
    val bundle = field.get(any) as? Bundle ?: Bundle()
    var index = 0
    dataList.forEach {
      val key = initDataStructure.getTargetKey(index)
      dataConvert.putDataToBundle(bundle, key, it)
      index++
    }
    invokeObject(bundle, field, any)
    return true
  }

  private fun getInitDataStructure(field: Field): InitDataStructure? {
    return field.getAnnotation(InitDataStructure::class.java)
  }

  private fun initClassParamConstructor(
    field: Field,
    bundle: Bundle,
    initialClassParam: InitClassParam,
    any: Any
  ): Any? {
    field.enableAccessible()
    var constructor: Constructor<*>? = null
    field.type.declaredConstructors?.forEach {
      if (it.parameterTypes?.size == initialClassParam.value.size) {
        constructor = it
        return@forEach
      }
    }
    constructor ?: return null
    val parameterTypes = constructor?.parameterTypes
    val params = mutableListOf<Any?>()
    val length = initialClassParam.value.size
    for (index in 0 until length) {
      val key = initialClassParam.value[index]
      val data = bundle.get(key)
      val paramType = parameterTypes?.getOrNull(index)
      if (paramType != null) {
        val convertData = dataConvert.getConvertData(paramType, data)
        params.add(if (!any.isIntercept(key, data, convertData)) convertData else null)
      } else {
        params.add(null)
      }
    }
    return constructor?.newInstance(*params.toTypedArray())
  }

  private fun getParamInstance(
    field: Field
  ): Any {
    if (field.type.declaredConstructors.isNullOrEmpty()) {
      throw RuntimeException("class :${field.type} must have a constructor")
    }
    val constructor = field.type.declaredConstructors[0]
    val parameterTypes = constructor.parameterTypes
    if (parameterTypes.isNullOrEmpty()) {
      return constructor.newInstance()
    }
    val param = mutableListOf<Any?>()
    constructor.parameterTypes?.forEach {
      param.add(dataConvert.getDefaultValue(it))
    }
    return constructor.newInstance(*param.toTypedArray())
  }

  private fun getInitialClassParam(field: Field): InitClassParam? {
    return field.getAnnotation(InitClassParam::class.java)
  }

  private fun invokeObject(data: Any, field: Field, any: Any) {
    field.set(any, data)
  }

  private fun getParamName(field: Field): ParamName? {
    val initialParam = field.getAnnotation(InitParam::class.java)
    initialParam ?: return if (enableNameKey) ParamName(
      key = field.name
    ) else null
    val fieldNames = mutableListOf<String>()
    fieldNames.add(field.name)
    if (initialParam.value.isNotEmpty()) {
      fieldNames.addAll(initialParam.value)
    }
    return ParamName(fieldNames = fieldNames)
  }

}

data class ParamName(
  val key: String? = null,
  val fieldNames: MutableList<String>? = null
) {

  fun belongToSet(keySet: Map<String, Boolean>): String? {
    key?.let {
      if (keySet.containsKey(key)) {
        return key
      }
    }
    fieldNames?.forEach {
      if (keySet.containsKey(it)) {
        return it
      }
    }
    return null
  }

}

interface InjectInterceptor {
  /**
   * 注入参数时的回调方法，在被注入的class中实现此接口
   * 即可接收到注入事件回掉，可通过返回值拦截注入事件
   * @return true 为拦截此注入事件 otherwise 继续注入
   *
   * @param key bundleKey
   * @param original 原始数据
   * @param convertData 转化后的数据
   */
  fun onInject(key: String, original: Any?, convertData: Any?): Boolean
}