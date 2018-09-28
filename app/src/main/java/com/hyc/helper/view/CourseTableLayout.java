package com.hyc.helper.view;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.ColorInt;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hyc.helper.R;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.bean.CourseInfoBean;
import com.hyc.helper.bean.CourseTableItemBean;
import com.hyc.helper.helper.DensityHelper;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class CourseTableLayout extends ViewGroup implements View.OnClickListener{

  private OnItemClickListener onItemClickListener;

  private List<CourseInfoBean> list;

  private int itemWidth;
  private int itemHeight;

  public void setOnItemClickListener(
      OnItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  public interface OnItemClickListener{
    void onItemClick(int position,CourseInfoBean infoBean);
  }

  public CourseTableLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public CourseTableLayout(Context context) {
    this(context,null);
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    int childCount = getChildCount();
    for (int i = 0; i < childCount; i++) {
      View itemView = getChildAt(i);
      Point point = ((CourseTableItemBean) itemView.getTag()).getPoint();
      int left =point.x*itemWidth;
      int top = point.y*itemHeight;
      itemView.layout(left+4,top+4,left+itemWidth-4,top+itemHeight-4);
    }
  }

  public void setCourseTableInfo(List<CourseInfoBean> courseInfoBeans,int curWeek){
    if (getChildCount()>0){
      removeAllViews();
    }
    this.list = courseInfoBeans;
    itemWidth = getMeasuredWidth()/7;
    itemHeight = DensityHelper.dip2px(120f);
    int[] colorId = UiHelper.getIntegerArrays(R.array.course_color);
    Random random =new Random();
    Set<Point> pointSet = new HashSet<>();
    for (CourseInfoBean courseInfoBean : courseInfoBeans) {
      if(courseInfoBean.getZs().contains(curWeek)){
        View itemView = getItemView(courseInfoBean,colorId[random.nextInt(colorId.length)]);
        pointSet.add(((CourseTableItemBean)itemView.getTag()).getPoint());
        addView(itemView);
      }
    }
    for (CourseInfoBean courseInfoBean : courseInfoBeans) {
      Point point = getPoint(courseInfoBean);
      if (!pointSet.contains(point)){
        pointSet.add(point);
        addView(getItemView(courseInfoBean,UiHelper.getColor(R.color.bg_no)));
      }
    }
  }

  private Point getPoint(CourseInfoBean courseInfoBean){
    int week = Integer.valueOf(courseInfoBean.getXqj())-1;
    int section = (Integer.valueOf(courseInfoBean.getDjj()))/2;
    return new Point(week,section);
  }

  private View getItemView(CourseInfoBean courseInfoBean,@ColorInt int color){
    CardView itemView = (CardView) UiHelper.inflater(R.layout.item_course_table,this);
    itemView.setCardBackgroundColor(color);
    LayoutParams params = new LayoutParams(itemWidth-8,itemHeight-8);
    itemView.setLayoutParams(params);
    TextView textView = itemView.findViewById(R.id.tv_course_info);
    String info = courseInfoBean.getName() + "\n@" + courseInfoBean.getRoom();
    textView.setText(info);
    itemView.setTag(new CourseTableItemBean(getPoint(courseInfoBean),courseInfoBean));
    return itemView;
  }

  public void switchWeek(int week){
    if (this.list!=null&&list.size()>0){
      setCourseTableInfo(list,week);
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    measureChildren(widthMeasureSpec,heightMeasureSpec);
    int measureWidth = measureWidth(widthMeasureSpec);
    int measureHeight = measureHeight();
    setMeasuredDimension(measureWidth,measureHeight);
  }

  private int measureHeight() {
    return 5* DensityHelper.dip2px(120f);
  }

  private int measureWidth(int widthMeasureSpec) {
    return MeasureSpec.getSize(widthMeasureSpec);
  }

  @Override
  public void onClick(View v) {
    if (onItemClickListener != null){
      onItemClickListener.onItemClick(indexOfChild(v),((CourseTableItemBean)v.getTag()).getInfoBean());
    }
  }
}
