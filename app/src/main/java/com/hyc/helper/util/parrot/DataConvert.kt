@file:Suppress("UNCHECKED_CAST")

package com.hyc.helper.util.parrot

import android.os.Bundle
import android.os.Parcelable
import com.hyc.helper.util.parrot.Parrot.logD
import com.hyc.helper.util.parrot.Parrot.logE
import java.io.Serializable

/**
 * @author: 贺宇成
 * @date: 2019-12-13 09:42
 * @desc: 数据转换类
 */
class DataConvert {

  /**
   * Json转化类，由业务自行实现
   */
  internal var jsonConvert: JsonConvert? = null

  /**
   * 获取转化后的数据
   * @param clazz 目标Field类型
   * @param originalData bundle中原始数据
   */
  fun getConvertData(clazz: Class<*>, originalData: Any?): Any? {
    originalData ?: return null
    return when {
      clazz == Any::class.java -> originalData
      getType(originalData) == clazz -> originalData
      originalData::class.java == String::class.java -> stringToData(
        clazz,
        originalData as String
      )
      clazz == String::class.java -> originalData.toString()
      originalData is Number -> return numberDataConvert(
        clazz,
        originalData
      )
      else -> null
    }
  }

  /**
   * 通过is来判断此数据的类型
   * 用来兼容Java和Kotlin的基本类型无法直接对比的问题
   */
  private fun getType(any: Any): Class<*> {
    when (any) {
      is Int -> return Int::class.java
      is Float -> return Float::class.java
      is Byte -> return Byte::class.java
      is Double -> return Double::class.java
      is Long -> return Long::class.java
      is Char -> return Char::class.java
      is Boolean -> return Boolean::class.java
      is Short -> return Short::class.java
      is String -> return String::class.java
    }
    return any::class.java
  }

  /**
   * number类型互相转化
   * 先将data转化为String，再解析出对应的Number类型
   * @param clazz 目标类型
   * @param data 原始数据
   */
  private fun numberDataConvert(clazz: Class<*>, data: Any?): Any? {
    data ?: return null
    val dataString = data.toString()
    return getDataFromString(clazz, dataString)
  }

  /**
   * 将String数据转化为对应数据类型的对象
   * @param clazz 目标类型
   * @param string 原数据
   */
  private fun stringToData(clazz: Class<*>, string: String?): Any? {
    if (string.isNullOrEmpty()) {
      return null
    }
    return getDataFromString(clazz, string).apply {
      this?.let { logD("string to $clazz success  data: $string") }
    }
  }

  /**
   * 将String转化成目标数据类型的实现方法
   * 支持将String转化为对应的Number类型
   * 支持json转化为对象(通过业务实现的JsonConvert)
   * @param clazz 目标类型
   * @param string String数据
   * @param needJsonConvert 是否需要Json转化
   */
  private fun getDataFromString(
    clazz: Class<*>,
    string: String,
    needJsonConvert: Boolean = true
  ): Any? {
    try {
      when (clazz) {
        Int::class.java -> return string.toInt()
        Float::class.java -> return string.toFloat()
        Byte::class.java -> return string.toByte()
        Double::class.java -> return string.toDouble()
        Long::class.java -> return string.toLong()
        Boolean::class.java -> return string.toBoolean()
        Short::class.java -> return string.toShort()
        String::class.java -> return string
        Char::class.java -> {
          return if (string.length == 1) {
            string[0]
          } else {
            null
          }
        }
        else -> return if (needJsonConvert) getJsonObject(
          string,
          clazz
        ) else null
      }
    } catch (e: NumberFormatException) {
      e.printStackTrace()
      logE("String to $clazz catch NumberFormatException data: $string")
    }
    return null
  }

  /**
   * 将Json转化为对应的对象
   * 业务自行实现转化行为
   */
  fun getJsonObject(data: String?, type: Class<*>): Any? {
    data ?: return null
    var filedObject: Any? = null
    try {
      filedObject = jsonConvert?.fromJson(data, type)
    } catch (e: Exception) {
      e.printStackTrace()
      logE("parse $type catch ${e::class.java} json :$data")
    }
    return filedObject
  }

  /**
   * 将list转化为Array类型, 基本类型无法使用Array<*>
   * 需要转化成对应的数组类型，兼容Java基本类型的封装类
   */
  fun toArray(list: MutableList<Any?>, clazz: Class<*>): Any? {
    val length = list.size
    when (clazz) {
      String::class.java -> return (list as? MutableList<String?>)?.toTypedArray()
      Int::class.java -> {
        val array = (list as? MutableList<Int?>)?.toTypedArray()
        return IntArray(length) { i -> array?.get(i) ?: 0 }
      }
      Integer::class.java -> return (list as? MutableList<Int?>)?.toTypedArray()
      java.lang.Double::class.java -> return (list as? MutableList<Double?>)?.toTypedArray()
      Double::class.java -> {
        val array = (list as? MutableList<Double?>)?.toTypedArray()
        return DoubleArray(length) { i -> array?.get(i) ?: 0.0 }
      }
      java.lang.Float::class.java -> return (list as? MutableList<Float?>)?.toTypedArray()
      Float::class.java -> {
        val array = (list as? MutableList<Float?>)?.toTypedArray()
        return FloatArray(length) { i -> array?.get(i) ?: 0.0f }
      }
      java.lang.Byte::class.java -> return (list as? MutableList<Byte?>)?.toTypedArray()
      Byte::class.java -> {
        val array = (list as? MutableList<Byte?>)?.toTypedArray()
        return ByteArray(length) { i -> array?.get(i) ?: 0 }
      }
      java.lang.Long::class.java -> return (list as? MutableList<Long?>)?.toTypedArray()
      Long::class.java -> {
        val array = (list as? MutableList<Long?>)?.toTypedArray()
        return LongArray(length) { i -> array?.get(i) ?: 0 }
      }
      java.lang.Character::class.java -> return (list as? MutableList<Char?>)?.toTypedArray()
      Char::class.java -> {
        val array = (list as? MutableList<Char?>)?.toTypedArray()
        return CharArray(length) { i -> array?.get(i) ?: ' ' }
      }
      java.lang.Boolean::class.java -> return (list as? MutableList<Boolean?>)?.toTypedArray()
      Boolean::class.java -> {
        val array = (list as? MutableList<Boolean?>)?.toTypedArray()
        return BooleanArray(length) { i -> array?.get(i) ?: false }
      }
      java.lang.Short::class.java -> return (list as? MutableList<Short?>)?.toTypedArray()
      Short::class.java -> {
        val array = (list as? MutableList<Short?>)?.toTypedArray()
        return ShortArray(length) { i -> array?.get(i) ?: 0 }
      }
      Parcelable::class.java -> return (list as? MutableList<Parcelable?>)?.toTypedArray()
      Serializable::class.java -> return (list as? MutableList<Serializable?>)?.toTypedArray()
    }
    return null
  }

  /**
   * 判断数据类型并且调用想用的Bundle方法注入数据
   * @param bundle 需要被注入的Bundle对象
   * @param key 对应的bundle key
   * @param data 数据
   */
  fun putDataToBundle(bundle: Bundle, key: String, data: Any?) {
    when (data) {
      is Int -> bundle.putInt(key, data)
      is Float -> bundle.putFloat(key, data)
      is Byte -> bundle.putByte(key, data)
      is Double -> bundle.putDouble(key, data)
      is Long -> bundle.putLong(key, data)
      is Char -> bundle.putChar(key, data)
      is Boolean -> bundle.putBoolean(key, data)
      is Short -> bundle.putShort(key, data)
      is String -> bundle.putString(key, data)
      is Parcelable -> bundle.putParcelable(key, data)
      is Serializable -> bundle.putSerializable(key, data)
      is Bundle -> bundle.putBundle(key, data)
      is IntArray -> bundle.putIntArray(key, data)
      is FloatArray -> bundle.putFloatArray(key, data)
      is ByteArray -> bundle.putByteArray(key, data)
      is DoubleArray -> bundle.putDoubleArray(key, data)
      is LongArray -> bundle.putLongArray(key, data)
      is CharArray -> bundle.putCharArray(key, data)
      is BooleanArray -> bundle.putBooleanArray(key, data)
      is ShortArray -> bundle.putShortArray(key, data)
      is Array<*> -> bundle.putStringArrays(key, data)
      is ArrayList<*> -> bundle.putArrayList(key, data)
    }
  }

  private fun Bundle.putStringArrays(key: String, array: Array<*>) {
    if (array::class.java.componentType == String::class.java) {
      putStringArray(key, array as? Array<String>?)
    } else if (array::class.java.componentType == Parcelable::class.java) {
      putParcelableArray(key, array as? Array<Parcelable>?)
    }
  }

  private fun Bundle.putArrayList(key: String, array: ArrayList<*>) {
    when (array.getOrNull(0)) {
      is String -> putStringArrayList(key, array as? ArrayList<String>?)
      is Int -> putIntegerArrayList(key, array as? ArrayList<Int>)
      is Parcelable -> putParcelableArrayList(key, array as? ArrayList<Parcelable>)
      is CharSequence -> putCharSequenceArrayList(key, array as? ArrayList<CharSequence>)
    }
  }

  /**
   * 获取各种类型的默认数值
   */
  fun getDefaultValue(clazz: Class<*>): Any? {
    return when (clazz) {
      Int::class.java -> 0
      Integer::class.java -> 0
      Float::class.java -> 0
      java.lang.Float::class.java -> 0.0f
      Byte::class.java -> 0
      java.lang.Byte::class.java -> 0
      Double::class.java -> 0
      java.lang.Double::class.java -> 0
      Long::class.java -> 0L
      java.lang.Long::class.java -> 0L
      Char::class.java -> 0
      java.lang.Character::class.java -> 0
      Boolean::class.java -> false
      java.lang.Boolean::class.java -> false
      Short::class.java -> 0
      java.lang.Short::class.java -> 0
      String::class.java -> ""
      else -> null
    }
  }

}

interface JsonConvert {

  @Throws(Exception::class)
  fun <T> fromJson(json: String, classOfT: Class<T>): T?

  fun toJson(src: Any): String?

}