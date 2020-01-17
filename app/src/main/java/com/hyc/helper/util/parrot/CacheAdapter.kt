package com.hyc.helper.util.parrot

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.support.v4.app.Fragment
import com.hyc.helper.util.parrot.Parrot.getActualType
import com.hyc.helper.util.parrot.Parrot.logD
import java.lang.RuntimeException
import java.lang.ref.WeakReference
import java.lang.reflect.Field

/**
 * @author: 贺宇成
 * @date: 2019-12-21 12:34
 * @desc:
 */
@Suppress("UNCHECKED_CAST")
class CacheAdapter(private val dataConvert: DataConvert) {

  var defaultSpName: String = "Parrot"

  var defaultPrefixProvider: PrefixProvider? = null

  private val spMap: MutableMap<String, WeakReference<SharedPreferences>> = mutableMapOf()

  private fun getSharedPreferences(any: Any, initCache: InitCache): SharedPreferences {
    val spName = if (initCache.spName.isNotEmpty()) initCache.spName else defaultSpName
    spMap[spName]?.get()?.let { return it }
    val context = when (any) {
      is Context -> any
      is Fragment -> any.context
      else -> throw RuntimeException("not find context")
    }
    val sharedPreferences = context!!.getSharedPreferences(spName, Context.MODE_PRIVATE)
    spMap[spName] = WeakReference(sharedPreferences)
    return sharedPreferences
  }

  private inline fun isCacheParam(field: () -> Field?): InitCache? {
    return field()?.getAnnotation(InitCache::class.java)
  }

  private fun getRealKey(any: Any, initCache: InitCache, index: Int = 0): String {
    val originKey = initCache.value[index]
    return when {
      any is PrefixProvider && initCache.prefixKey.isNotEmpty() -> "${any.getKeyPrefix(
        originKey,
        initCache.prefixKey
      )}-$originKey"
      defaultPrefixProvider != null && initCache.prefixKey.isNotEmpty() -> "${defaultPrefixProvider!!.getKeyPrefix(
        originKey,
        initCache.prefixKey
      )}-$originKey"
      else -> originKey
    }
  }

  private fun getRealKeyList(any: Any, initCache: InitCache): List<String> {
    val list = mutableListOf<String>()
    for (index in initCache.value.indices) {
      list.add(getRealKey(any, initCache, index))
    }
    return list
  }

  fun initCacheParam(
    any: Any,
    fields: Array<out Field>
  ) {
    fields.forEach { field ->
      isCacheParam { field }?.let {
        initCacheParam(any, field, it)
        logD("init cache field : ${field.name}")
      }
    }
  }

  private fun initCacheParam(
    any: Any,
    field: Field,
    initCache: InitCache
  ) {
    val sharedPreferences = getSharedPreferences(any, initCache)
    if (!field.isAccessible) field.isAccessible = true
    when {
      initCache.value.isEmpty() -> throw RuntimeException("initCache must have a key")
      initCache.value.size == 1 -> {
        getDataByClass(
          field.type,
          sharedPreferences,
          getRealKey(any, initCache),
          field.get(any)
        )?.let {
          field.set(any, it)
        }
      }
      field.type.isArray -> initArray(any, field, initCache, sharedPreferences)
      else -> when (field.type) {
        List::class.java -> initList(any, field, initCache, sharedPreferences)
        Map::class.java -> initMap(any, field, initCache, sharedPreferences)
        else -> throw RuntimeException("unknown type ${field.type}")
      }
    }
  }

  fun saveCacheParam(any: Any) {
    val startTime = System.currentTimeMillis()
    val fields = any.javaClass.declaredFields
    fields.forEach { field ->
      isCacheParam { field }?.let {
        if (!it.onlyRead) {
          saveCacheParamInternal(any, field, it)
        }
      }
    }
    logD("saveCacheParam cost ${System.currentTimeMillis() - startTime}")
  }

  private fun saveCacheParamInternal(any: Any, field: Field, initCache: InitCache) {
    if (!field.isAccessible) field.isAccessible = true
    val editor = getSharedPreferences(any, initCache).edit()
    when {
      initCache.value.isEmpty() -> throw RuntimeException("initCache must have a key")
      initCache.value.size == 1 -> {
        saveDataByClass(field.type, getRealKey(any, initCache), field.get(any), editor)
      }
      field.type.isArray -> {
        saveArray(
          field.get(any),
          field.getActualType { Any::class.java },
          getRealKeyList(any, initCache),
          editor
        )
      }
      else -> when (field.type) {
        List::class.java -> saveList(
          field.get(any) as List<*>,
          field.getActualType { Any::class.java },
          getRealKeyList(any, initCache),
          editor
        )
        Map::class.java -> saveMap(
          field.get(any) as Map<String, *>,
          field.getActualType { Any::class.java },
          getRealKeyList(any, initCache),
          editor
        )
        else -> throw RuntimeException("unknown type ${field.type}")
      }
    }
    editor.apply()
  }

  private fun saveList(list: List<*>, clazz: Class<*>, keyList: List<String>, editor: Editor) {
    var index = 0
    keyList.forEach { key ->
      list[index++]?.let { saveDataByClass(clazz, key, it, editor) }
    }
  }

  private fun saveMap(map: Map<String, *>, clazz: Class<*>, keyList: List<String>, editor: Editor) {
    keyList.forEach { key ->
      map[key]?.let { saveDataByClass(clazz, key, it, editor) }
    }
  }

  private fun saveArray(array: Any, clazz: Class<*>, keyList: List<String>, editor: Editor) {
    var index = 0
    when (array) {
      is Array<*> -> {
        keyList.forEach { key ->
          array[index++]?.let {
            saveDataByClass(clazz, key, it, editor)
          }
        }
      }
      is IntArray -> {
        keyList.forEach { key ->
          saveDataByClass(clazz, key, array[index++], editor)
        }
      }
      is LongArray -> {
        keyList.forEach { key ->
          saveDataByClass(clazz, key, array[index++], editor)
        }
      }
      is FloatArray -> {
        keyList.forEach { key ->
          saveDataByClass(clazz, key, array[index++], editor)
        }
      }
      is BooleanArray -> {
        keyList.forEach { key ->
          saveDataByClass(clazz, key, array[index++], editor)
        }
      }
    }
  }

  private fun initArray(
    any: Any,
    field: Field,
    initCache: InitCache,
    sharedPreferences: SharedPreferences
  ) {
    val clazz = field.getActualType { Any::class.java }
    val list = mutableListOf<Any?>()
    getRealKeyList(any, initCache).forEach {
      list.add(
        getDataByClass(
          clazz,
          sharedPreferences,
          it,
          dataConvert.getDefaultValue(clazz)
        )
      )
    }
    field.set(any, dataConvert.toArray(list, clazz))
  }

  private fun initList(
    any: Any,
    field: Field,
    initCache: InitCache,
    sharedPreferences: SharedPreferences
  ) {
    val clazz = field.getActualType { Any::class.java }
    val list = mutableListOf<Any?>()
    getRealKeyList(any, initCache).forEach {
      list.add(
        getDataByClass(
          clazz,
          sharedPreferences,
          it,
          dataConvert.getDefaultValue(clazz)
        )
      )
    }
    field.set(any, list)
  }

  private fun initMap(
    any: Any,
    field: Field,
    initCache: InitCache,
    sharedPreferences: SharedPreferences
  ) {
    val clazz = field.getActualType { Any::class.java }
    val map = mutableMapOf<String, Any?>()
    getRealKeyList(any, initCache).forEach {
      map[it] = getDataByClass(clazz, sharedPreferences, it, dataConvert.getDefaultValue(clazz))
    }
    field.set(any, map)
  }

  private fun getDataByClass(
    clazz: Class<*>,
    sharedPreferences: SharedPreferences,
    key: String,
    default: Any?
  ): Any? {
    logD("get cache data $key ")
    return when (clazz) {
      Int::class.java -> sharedPreferences.getInt(key, default as Int)
      Float::class.java -> sharedPreferences.getFloat(key, default as Float)
      Long::class.java -> sharedPreferences.getLong(key, default as Long)
      Boolean::class.java -> sharedPreferences.getBoolean(key, default as Boolean)
      String::class.java -> sharedPreferences.getString(key, default.castToString())
      MutableSet::class.java -> sharedPreferences.getStringSet(key, default.castToStringSet())
      else -> {
        sharedPreferences.getString(key, null)?.let {
          dataConvert.getJsonObject(it, clazz)
        }
      }
    }
  }

  private fun Any?.castToString(): String? {
    return if (this != null) this as String else null
  }

  private fun Any?.castToStringSet(): MutableSet<String>? {
    return if (this != null) this as MutableSet<String> else null
  }

  private fun saveDataByClass(
    clazz: Class<*>,
    key: String,
    value: Any?,
    editor: Editor
  ) {
    logD("save $key   value $value")
    when (clazz) {
      Int::class.java -> editor.putInt(key, value as Int)
      Float::class.java -> editor.putFloat(key, value as Float)
      Long::class.java -> editor.putLong(key, value as Long)
      Boolean::class.java -> editor.putBoolean(key, value as Boolean)
      String::class.java -> editor.putString(key, value.castToString())
      MutableSet::class.java -> editor.putStringSet(key, value.castToStringSet())
      else -> {
        value?.let {
          editor.putString(key, dataConvert.jsonConvert?.toJson(it))
          return
        }
        editor.remove(key)
      }
    }
  }

}

interface PrefixProvider {
  fun getKeyPrefix(key: String, prefixKey: String): String
}