package com.hyc.helper.view

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.DrawableRes
import android.support.v4.graphics.drawable.DrawableCompat
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.View.OnClickListener
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.hyc.helper.view.ExpandableLayout.OnExpandStateChangeListener
import com.hyc.helper.view.ExpandableLayout.SimpleExpandAnimationChangeImp
import com.hyc.helper.R

/**
 * @author: 贺宇成
 * @date: 2019-10-25 11:44
 * @desc: 简单触发控件实现
 * 提示icon联动动画 、基线对齐功能、提示文字随折叠状态变化
 *
 */
@Suppress("DEPRECATION")
class SimpleTriggerView @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), OnExpandStateChangeListener, OnClickListener {

  var textSize: Float = 12.0f
  var textColor: Int = Color.BLACK
  var icon: Int = R.drawable.ic_simple_down
  var expandText: String? = ""
  var collapseText: String? = ""
  var enableBaseLine: Boolean = false
  private val tvTitle: TextView
  private val ivIcon: ImageView
  private val expandAnimationChangeImp: SimpleExpandAnimationChangeImp

  init {
    val typeArray = context.obtainStyledAttributes(attrs, R.styleable.SimpleTriggerView)
    textSize = typeArray.getDimension(R.styleable.SimpleTriggerView_textSize, 12.0f)
    textColor = typeArray.getColor(R.styleable.SimpleTriggerView_textColor, Color.BLACK)
    icon = typeArray.getResourceId(R.styleable.SimpleTriggerView_icon, R.drawable.ic_simple_down)
    expandText = typeArray.getString(R.styleable.SimpleTriggerView_expandText)
    collapseText = typeArray.getString(R.styleable.SimpleTriggerView_collapseText)
    val iconSize =
      typeArray.getDimension(R.styleable.SimpleTriggerView_iconSize, getDimensionSize(16))
    val iconColor = typeArray.getColor(R.styleable.SimpleTriggerView_iconColor, Color.BLACK)
    val drawable = getDrawable(context, icon)
    if (iconColor != Color.BLACK) {
      DrawableCompat.setTint(drawable, iconColor)
    }
    enableBaseLine = typeArray.getBoolean(R.styleable.SimpleTriggerView_enableBaseLine, false)
    val textStyle = typeArray.getInt(R.styleable.SimpleTriggerView_textStyle, Typeface.NORMAL)
    typeArray.recycle()

    tvTitle = TextView(context)
    tvTitle.setTextColor(textColor)
    tvTitle.setTypeface(
      null,
      textStyle
    )
    ivIcon = ImageView(context)
    addView(tvTitle)
    addView(ivIcon)
    expandAnimationChangeImp = SimpleExpandAnimationChangeImp(targetView = ivIcon)
    ivIcon.setImageDrawable(drawable)
    (tvTitle.layoutParams as? LayoutParams)?.let {
      it.gravity = Gravity.CENTER
      it.width = android.view.ViewGroup.LayoutParams.WRAP_CONTENT
      it.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT
    }
    tvTitle.gravity = Gravity.CENTER
    (ivIcon.layoutParams as? LayoutParams)?.let {
      it.gravity = Gravity.CENTER
      it.width = iconSize.toInt()
      it.height = iconSize.toInt()
    }

  }

  private fun getDrawable(context: Context, @DrawableRes resId: Int): Drawable {
    val resources = context.resources
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      resources.getDrawable(resId, context.theme)
    } else {
      resources.getDrawable(resId)
    }
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    if (enableBaseLine) {
      val params = layoutParams as? LinearLayout.LayoutParams
      params?.topMargin = -measuredHeight
      layoutParams = params
    }
  }

  private fun getDimensionSize(value: Int): Float {
    val scale = resources.displayMetrics.density
    return value * scale + 0.5f
  }

  private fun initState(state: Int) {
    if (state == ExpandableLayout.STATE_COLLAPSE) {
      tvTitle.text = collapseText
    } else if (state == ExpandableLayout.STATE_EXPAND) {
      tvTitle.text = expandText
    }
    if (state == ExpandableLayout.STATE_EXPAND) {
      ivIcon.rotation = 180F
    } else {
      ivIcon.rotation = 0F
    }
    initLayout()
  }

  private fun initLayout() {
    (ivIcon.layoutParams as? LayoutParams)?.let {
      it.leftMargin = getRealTextViewHeight() / 2 + getDimensionSize(8).toInt()
    }
  }

  private fun getRealTextViewHeight(): Int {
    val text = tvTitle.text.toString()
    val bounds = Rect()
    val paint = tvTitle.paint
    paint.getTextBounds(text, 0, text.length, bounds)
    val textWidth = bounds.width()
    val padding = tvTitle.compoundPaddingLeft + tvTitle.compoundPaddingRight
    return textWidth + padding
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    if (parent is ExpandableLayout) {
      (parent as ExpandableLayout).apply {
        initState(this.getExpandState())
        this.addExpandStateChangeListener(this@SimpleTriggerView)
        this.addExpandAnimationChangeListener(expandAnimationChangeImp)
      }
    }
    tvTitle.setOnClickListener(this)
    ivIcon.setOnClickListener(this)
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    if (parent is ExpandableLayout) {
      (parent as ExpandableLayout).apply {
        this.removeExpandAnimationChangeListener(expandAnimationChangeImp)
        this.removeExpandStateChangeListener(this@SimpleTriggerView)
      }
    }
    tvTitle.setOnClickListener(null)
    ivIcon.setOnClickListener(null)

  }

  override fun onExpandStateChange(layout: ExpandableLayout, oldState: Int, newState: Int) {
    initState(newState)
    if (!layout.hasAnimation) {
      if (layout.getExpandState() == ExpandableLayout.STATE_EXPAND) {
        ivIcon.rotation = 180F
      } else {
        ivIcon.rotation = 0F
      }
    }
  }

  override fun onClick(v: View?) {
    (parent as? ExpandableLayout?)?.let {
      it.setExpandState(
        if (it.getExpandState() == ExpandableLayout.STATE_COLLAPSE) {
          ExpandableLayout.STATE_EXPAND
        } else {
          ExpandableLayout.STATE_COLLAPSE
        }
      )
    }
  }

}