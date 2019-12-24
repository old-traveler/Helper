package com.hyc.helper.view

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseIntArray
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.Transformation
import android.widget.LinearLayout
import android.widget.TextView
import com.hyc.helper.R.styleable
import com.hyc.helper.helper.LogHelper
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/**
 * @author: 贺宇成
 * @date: 2019-10-24 10:09
 * @desc: 可折叠布局
 */
class ExpandableLayout @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), AnimationListener, OnClickListener {

  private lateinit var mTargetView: View
  private var mTriggerView: View? = null
  var hasAnimation: Boolean = true
  var mAnimationDuration: Int
  var collapseHeight: Float = 100.0f
  private var expandHeight: Float = -1.0f
  private var animation: ExpandCollapseAnimation? = null
  private var isAnimation: Boolean = false
  private var sparseArray: SparseIntArray? = null
  private var position: Int = 0
  private var enableCollapseAfterExpand: Boolean = true
  private var maxLine = 0
  var mTriggerId: Int = -1
  private var mCurState: Int by Delegates.observable(STATE_COLLAPSE) { _: KProperty<*>, oldValue: Int, newValue: Int ->
    if (mTriggerView?.visibility == View.VISIBLE && isEnabled) {
      onExpandStateChangeListeners.forEach {
        it?.onExpandStateChange(this, oldValue, newValue)
      }
      saveState(newValue)
      if (hasAnimation) {
        isAnimation = true
        startAnimation()
      } else {
        this.ensureTarget()
        this.mTargetView.requestLayout()
        enableCollapseAfterExpand()
      }
    }
  }
  private val onExpandStateChangeListeners = mutableListOf<OnExpandStateChangeListener?>()
  private val onExpandAnimationChangeListeners = mutableListOf<OnExpandAnimationChangeListener?>()

  companion object {
    const val STATE_COLLAPSE = 1
    const val STATE_EXPAND = 2
  }

  public fun setExpandState(state: Int) {
    if (!isAnimation) {
      mCurState = state
    }
  }

  public fun setEnableCollapseAfterExpand(enable: Boolean) {
    this.enableCollapseAfterExpand = enable
    isEnabled = true
    this.requestLayout()
  }

  private fun enableCollapseAfterExpand() {
    if (mCurState == STATE_EXPAND && !enableCollapseAfterExpand && mTriggerView?.visibility == View.VISIBLE) {
      mTriggerView?.visibility = View.GONE
    }
  }

  public fun updateState(sparseArray: SparseIntArray, position: Int) {
    this.sparseArray = sparseArray
    this.position = position
    updateStateNow(sparseArray.get(position, STATE_COLLAPSE))

  }

  public fun updateStateNow(state: Int) {
    val old = hasAnimation
    hasAnimation = false
    setExpandState(state)
    hasAnimation = old
  }

  private fun saveState(state: Int) {
    sparseArray?.put(position, state)
  }

  public fun getExpandState(): Int {
    return this.mCurState
  }

  init {
    orientation = VERTICAL
    val typedArray = context.obtainStyledAttributes(attrs, styleable.ExpandableLayout)
    hasAnimation = typedArray.getBoolean(styleable.ExpandableLayout_hasAnimation, true)
    mAnimationDuration = typedArray.getInteger(styleable.ExpandableLayout_animationDuration, 200)
    collapseHeight = typedArray.getDimension(styleable.ExpandableLayout_collapseHeight, 100.0f)
    enableCollapseAfterExpand =
      typedArray.getBoolean(styleable.ExpandableLayout_enableCollapseAfterExpand, true)
    mTriggerId = typedArray.getResourceId(styleable.ExpandableLayout_expendableTriggerId, -1)
    maxLine = typedArray.getInt(styleable.ExpandableLayout_maxLine, 0)
    typedArray.recycle()
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    this.ensureTarget()
    if (!isAnimation) {
      expandHeight = getTargetViewExpandHeight(widthMeasureSpec, heightMeasureSpec)
      (this.mTargetView.layoutParams as? LayoutParams?).let {
        it?.height = when (mCurState) {
          STATE_COLLAPSE -> minOf(collapseHeight.toInt(), expandHeight.toInt())
          STATE_EXPAND -> android.view.ViewGroup.LayoutParams.WRAP_CONTENT
          else -> it?.height
        }
      }
      if (expandHeight <= collapseHeight) {
        this.mTriggerView?.visibility = View.GONE
      } else if (enableCollapseAfterExpand) {
        this.mTriggerView?.visibility = View.VISIBLE
      }
    }
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    this.ensureTarget()
    if (mTargetView is TextView) {
      mTargetView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
    }
    if (isAnimation){
      isAnimation = false
    }
  }

  private fun getTargetViewExpandHeight(
    widthMeasureSpec: Int,
    heightMeasureSpec: Int,
    measureText: Boolean = true
  ): Float {
    val layout = this.mTargetView.layoutParams as LayoutParams
    layout.height = ViewGroup.LayoutParams.WRAP_CONTENT
    this.mTargetView.layoutParams = layout
    measureChildWithMargins(this.mTargetView, widthMeasureSpec, 0, heightMeasureSpec, 0)
    val height = this.mTargetView.measuredHeight.toFloat()
    if (mTargetView is TextView && maxLine > 0 && (mTargetView as TextView).lineCount >= maxLine && measureText) {
      val textView = mTargetView as TextView
      textView.maxLines = maxLine
      val curLine = (mTargetView as TextView).lineCount
      collapseHeight = getTargetViewExpandHeight(widthMeasureSpec, heightMeasureSpec, false)
      textView.maxLines = Integer.MAX_VALUE
    }
    return height
  }

  private fun ensureTarget() {
    mTargetView = getChildAt(0)
    mTriggerView = getChildAt(1)
    if (mTriggerId != -1) {
      findViewById<View>(mTriggerId).setOnClickListener(this)
    }
  }

  private fun startAnimation() {
    animation = if (mCurState == STATE_EXPAND) {
      ExpandCollapseAnimation(this.mTargetView, collapseHeight, expandHeight)
    } else {
      ExpandCollapseAnimation(this.mTargetView, expandHeight, collapseHeight)
    }
    animation?.fillAfter = true
    animation?.setAnimationListener(this)
    clearAnimation()
    super.startAnimation(animation)
  }

  internal inner class ExpandCollapseAnimation(
    private val mTargetView: View,
    private val mStartHeight: Float,
    private val mEndHeight: Float
  ) :
    Animation() {

    init {
      duration = mAnimationDuration.toLong()
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
      if (isAnimation) {
        val newHeight = ((mEndHeight - mStartHeight) * interpolatedTime + mStartHeight).toInt()
        mTargetView.layoutParams.height = newHeight
        mTargetView.requestLayout()
        onExpandAnimationChangeListeners.forEach {
          it?.onAnimationChange(
            this@ExpandableLayout,
            mTriggerView,
            interpolatedTime
          )
        }
      }
    }

    override fun willChangeBounds(): Boolean {
      return true
    }
  }

  override fun onClick(v: View?) {
    if (v?.id == mTriggerId) {
      setExpandState(
        if (mCurState == STATE_COLLAPSE) {
          STATE_EXPAND
        } else {
          STATE_COLLAPSE
        }
      )
    }
  }

  fun addExpandStateChangeListener(listener: OnExpandStateChangeListener?) {
    this.onExpandStateChangeListeners.add(listener)
  }

  fun removeExpandStateChangeListener(listener: OnExpandStateChangeListener?) =
    this.onExpandStateChangeListeners.remove(listener)

  fun addExpandAnimationChangeListener(listener: OnExpandAnimationChangeListener?) {
    this.onExpandAnimationChangeListeners.add(listener)
  }

  fun removeExpandAnimationChangeListener(listener: OnExpandAnimationChangeListener?) =
    this.onExpandAnimationChangeListeners.remove(listener)

  interface OnExpandStateChangeListener {
    fun onExpandStateChange(layout: ExpandableLayout, oldState: Int, newState: Int)
  }

  interface OnExpandAnimationChangeListener {
    fun onAnimationChange(layout: ExpandableLayout, mTriggerView: View?, interpolatedTime: Float)
  }

  override fun onAnimationEnd(animation: Animation?) {
    isAnimation = false
    enableCollapseAfterExpand()
  }

  override fun onAnimationStart(animation: Animation?) {
  }

  override fun onAnimationRepeat(animation: Animation?) {
  }

  class SimpleExpandAnimationChangeImp(
    private val targetView: View? = null,
    private val viewId: Int = -1
  ) :
    OnExpandAnimationChangeListener {
    override fun onAnimationChange(
      layout: ExpandableLayout, mTriggerView: View?, interpolatedTime: Float
    ) {
      val target = if (viewId < 0) {
        targetView
      } else {
        mTriggerView?.findViewById(viewId)
      }
      if (layout.getExpandState() == STATE_EXPAND) {
        target?.rotation = interpolatedTime * 180
      } else {
        target?.rotation = interpolatedTime * 180 + 180
      }
    }
  }

  override fun onSaveInstanceState(): Parcelable? {
    val superState = super.onSaveInstanceState()
    val savedState = SavedState(superState)
    savedState.curState = this.mCurState
    savedState.hasAnimation = this.hasAnimation
    savedState.mAnimationDuration = this.mAnimationDuration
    savedState.collapseHeight = this.collapseHeight
    savedState.enableCollapseAfterExpand = this.enableCollapseAfterExpand
    return savedState
  }

  override fun onRestoreInstanceState(state: Parcelable?) {
    val savedState = state as? SavedState?
    super.onRestoreInstanceState(savedState?.superState)
    savedState?.let {
      this.hasAnimation = it.hasAnimation
      this.mAnimationDuration = it.mAnimationDuration
      this.collapseHeight = it.collapseHeight
      this.enableCollapseAfterExpand = it.enableCollapseAfterExpand
      //防止未初始化之前更新状态
      isEnabled = false
      this.mCurState = it.curState
      isEnabled = true
    }
  }

  internal inner class SavedState : BaseSavedState {
    var curState: Int = STATE_COLLAPSE
    var hasAnimation: Boolean = true
    var mAnimationDuration: Int = 200
    var collapseHeight: Float = 100.0f
    var enableCollapseAfterExpand: Boolean = true

    constructor(source: Parcelable?) : super(source)

    constructor(parcel: Parcel?) : super(parcel) {
      parcel?.let {
        curState = it.readInt()
        hasAnimation = it.readInt() == 1
        mAnimationDuration = it.readInt()
        collapseHeight = it.readFloat()
        enableCollapseAfterExpand = it.readInt() == 1
      }
    }

    override fun writeToParcel(out: Parcel?, flags: Int) {
      super.writeToParcel(out, flags)
      out?.let {
        it.writeInt(curState)
        it.writeInt(if (hasAnimation) 1 else 0)
        it.writeInt(mAnimationDuration)
        it.writeFloat(collapseHeight)
        it.writeInt(if (enableCollapseAfterExpand) 1 else 0)
      }
    }
  }
}