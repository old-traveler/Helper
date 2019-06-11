package com.hyc.helper.helper;

import android.text.TextUtils;
import org.greenrobot.greendao.converter.PropertyConverter;
import java.util.Arrays;
import java.util.List;

/**
 * 作者: 贺宇成
 * 时间: 2019-06-11
 * 描述:
 */
public class ListStringConverter implements PropertyConverter<List<String>, String> {
  @Override
  public List<String> convertToEntityProperty(String s) {
    if (TextUtils.isEmpty(s)) {
      return null;
    }
    return Arrays.asList(s.split("&"));
  }

  @Override
  public String convertToDatabaseValue(List<String> strings) {
    StringBuilder stringBuilder = new StringBuilder();
    for (String s : strings) {
      stringBuilder.append(s);
      stringBuilder.append("&");
    }
    int length = stringBuilder.length();
    if (length > 0) {
      stringBuilder.delete(length - 1, length);
    }
    return stringBuilder.toString();
  }
}
