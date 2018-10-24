package com.hyc.helper.adapter.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hyc.helper.R;
import com.hyc.helper.base.adapter.viewholder.BaseViewHolder;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.bean.ExamBean;
import com.hyc.helper.bean.ExamInfoBean;
import com.hyc.helper.helper.Constant;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExamViewHolder extends BaseViewHolder<ExamInfoBean> {

  @BindView(R.id.tv_date)
  TextView tvDate;
  @BindView(R.id.tv_week)
  TextView tvWeek;
  @BindView(R.id.tv_name)
  TextView tvName;
  @BindView(R.id.tv_location)
  TextView tvLocation;
  @BindView(R.id.tv_surplus)
  TextView tvSurplus;

  public ExamViewHolder(@NonNull View itemView) {
    super(itemView);
  }

  @Override
  protected void initItemView(View view) {
    ButterKnife.bind(this, itemView);
  }

  @Override
  public void loadItemData(Context context, ExamInfoBean data, int position) {
    String[] temp = null;
    try{
      temp = data.getStarttime().split(" ");
    }catch (Exception e){
      e.printStackTrace();
    }
    if (temp != null && temp.length > 0){
      tvDate.setText(temp[0]);
      tvWeek.setText(
          String.format("(%s周 %s-%s)", data.getWeek_Num(), temp[1], data.getEndTime().split(" ")[1]));
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
      Date date = null;
      try {
        date = formatter.parse(temp[0]);
      } catch (ParseException e) {
        e.printStackTrace();
      }
      if (date != null){
        long distance =   date.getTime() - System.currentTimeMillis();
        if (distance < 0){
          tvSurplus.setText("已结束");
        } else if (distance >= Constant.ONE_DAY_TIME){
          tvSurplus.setText(String.format(UiHelper.getString(R.string.surplus_day),(int)(distance/Constant.ONE_DAY_TIME)));
        }else {
          tvSurplus.setText(String.format(context.getString(R.string.surplus_hour),(int)(distance/Constant.ONE_HOUR_TIME)));
        }
      }else {
        tvSurplus.setText("未知");
      }
    }else {
      tvSurplus.setText("未知");
      tvDate.setText("未知");
      tvWeek.setText(String.format("(%s周)",data.getWeek_Num()));
    }

    if (TextUtils.isEmpty(data.getRoomName())){
      tvLocation.setText("未知");
    }else {
      tvLocation.setText(data.getRoomName());
    }
    if (TextUtils.isEmpty(data.getCourseName())){
      tvName.setText("未知");
    }else {
      tvName.setText(data.getCourseName());
    }

  }
}
