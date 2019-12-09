package com.hyc.helper.util.parrot

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FIELD

/**
 * @author: 贺宇成
 * @date: 2019-12-08 16:43
 * @desc:
 */

@kotlin.annotation.Retention(RUNTIME)
@kotlin.annotation.Target(
  FIELD
)
annotation class InitialParam(

  val key: String = "",

  val alternate: Array<String> = []
)

@kotlin.annotation.Retention(RUNTIME)
@kotlin.annotation.Target(
  FIELD
)
annotation class InitialClassParam
