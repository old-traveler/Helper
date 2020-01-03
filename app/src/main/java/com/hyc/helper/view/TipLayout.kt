package com.hyc.helper.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.hyc.helper.R

/**
 * @author: 贺宇成
 * @date: 2020-01-03 15:02
 * @desc:
 */
class TipLayout : ViewGroup, View.OnClickListener {

  private var mHorizontalSpacing: Int = 0

  private var mVerticalSpacing: Int = 0

  private var maxLineNumber: Int = 0

  private var lineNum: Int = 0

  private var mInvisibleNum = 0//不可见子view数量

  private var mVisibleIndexList: MutableList<Int>? = null

  private var onItemClickListener: OnItemClickListener? = null

  private var onSugLayoutListener: OnSugLayoutListener? = null

  /**
   * 获取可见子view数量
   */
  val visibleNum: Int
    get() = childCount - mInvisibleNum

  val visibleIndexList: List<Int>?
    get() = mVisibleIndexList

  fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
    this.onItemClickListener = onItemClickListener
  }

  fun setOnSugLayoutListener(onSugLayoutListener: OnSugLayoutListener) {
    this.onSugLayoutListener = onSugLayoutListener
  }

  interface OnItemClickListener {
    fun onItemClick(position: Int, view: View)
  }

  interface OnSugLayoutListener {
    fun onSugLayout(indexList: List<Int>)
  }

  constructor(context: Context) : super(context) {}

  @SuppressLint("CustomViewStyleable") constructor(context: Context, attrs: AttributeSet) : super(
    context,
    attrs
  ) {
    val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TabLayout)
    try {
      mHorizontalSpacing = typedArray.getDimensionPixelSize(
        R.styleable.TabLayout_horizontalSpacing,
        DEFAULT_HORIZONTAL_SPACING
      )
      mVerticalSpacing = typedArray.getDimensionPixelSize(
        R.styleable.TabLayout_verticalSpacing,
        DEFAULT_VERTICAL_SPACING
      )
      maxLineNumber =
        typedArray.getInteger(R.styleable.TabLayout_max_line_count, DEFAULT_MAX_LINE_NUMBER)
    } finally {
      typedArray.recycle()
    }
    clipChildren = true
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    val viewWidth = r - l
    var lineNumber = lineNum
    val paddingLeft = paddingLeft
    var paddingTop = paddingTop
    val paddingRight = paddingRight
    var childLeft = paddingLeft
    mInvisibleNum = 0
    mVisibleIndexList = mutableListOf()

    var i = 0
    val childCount = childCount
    while (i < childCount) {
      val childView = getChildAt(i)
      if (childView.visibility == View.GONE) {
        ++i
        continue
      }
      val childWidth = childView.measuredWidth
      val childHeight = childView.measuredHeight
      if (childLeft + childWidth + paddingRight <= viewWidth) {
        childView.layout(childLeft, paddingTop, childLeft + childWidth, paddingTop + childHeight)
        mVisibleIndexList!!.add(i)
        childLeft += childWidth + mHorizontalSpacing
      } else if (lineNumber > 1) {
        lineNumber--
        paddingTop += mVerticalSpacing + childHeight
        childLeft = paddingLeft
        i--
      } else {
        childView.visibility = View.GONE
        mInvisibleNum++
      }
      ++i
    }
    if (null != onSugLayoutListener) {
      onSugLayoutListener!!.onSugLayout(mVisibleIndexList!!)
    }
  }

  fun setViews(views: List<View>?) {
    removeAllViews()
    if (null != views && !views.isEmpty()) {
      for (i in views.indices) {
        val childView = views[i]
        childView.tag = i
        childView.setOnClickListener(this)
        addView(childView)
      }
    }
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    measureChildren(widthMeasureSpec, heightMeasureSpec)
    val measureWidth = measureWidth(widthMeasureSpec)
    val measureHeight = measureHeight(heightMeasureSpec, measureWidth)
    setMeasuredDimension(measureWidth, measureHeight)
  }

  private fun measureHeight(heightMeasureSpec: Int, measureWidth: Int): Int {
    val maxHeight = View.MeasureSpec.getSize(heightMeasureSpec)
    val measureMode = View.MeasureSpec.getMode(heightMeasureSpec)
    var layoutHeight = 0
    var layoutWidth = 0
    val childCount = childCount
    if (childCount == 0) return layoutHeight
    lineNum = 1
    val childHeight = getChildAt(0).measuredHeight
    for (i in 0 until childCount) {
      val childView = getChildAt(i)
      layoutWidth += childView.measuredWidth + if (i == childCount - 1) 0 else mHorizontalSpacing
      if (lineNum == maxLineNumber) {
        break
      } else if (layoutWidth > measureWidth) {
        lineNum++
        layoutWidth = childView.measuredWidth + if (i == childCount - 1) 0 else mHorizontalSpacing
      }
    }
    layoutHeight = childHeight * lineNum + (lineNum - 1) * mVerticalSpacing
    if (measureMode == View.MeasureSpec.AT_MOST || measureMode == View.MeasureSpec.UNSPECIFIED) {
      return layoutHeight + paddingTop + paddingBottom
    }
    while (lineNum > 1 && layoutHeight > maxHeight) {
      lineNum--
      layoutHeight -= childHeight + mVerticalSpacing
    }
    return maxHeight
  }

  private fun measureWidth(widthMeasureSpec: Int): Int {
    val maxWidth = View.MeasureSpec.getSize(widthMeasureSpec)
    val measureMode = View.MeasureSpec.getMode(widthMeasureSpec)
    var layoutWidth = 0
    val childCount = childCount
    if (measureMode == View.MeasureSpec.AT_MOST) {
      for (i in 0 until childCount) {
        val childView = getChildAt(i)
        layoutWidth += childView.measuredWidth
        if (layoutWidth >= maxWidth) {
          return maxWidth
        }
      }
      return layoutWidth
    } else {
      return maxWidth
    }
  }

  fun setHorizontalSpacing(mHorizontalSpacing: Int) {
    this.mHorizontalSpacing = mHorizontalSpacing
  }

  fun setVerticalSpacing(mVerticalSpacing: Int) {
    this.mVerticalSpacing = mVerticalSpacing
  }

  fun setMaxLineNumber(maxLineNumber: Int) {
    this.maxLineNumber = maxLineNumber
  }

  override fun onClick(v: View) {
    if (onItemClickListener != null) {
      onItemClickListener!!.onItemClick(v.tag as Int, v)
    }
  }

  companion object {

    private val DEFAULT_HORIZONTAL_SPACING = 10

    private val DEFAULT_VERTICAL_SPACING = 10

    private val DEFAULT_MAX_LINE_NUMBER = 2
  }
}
