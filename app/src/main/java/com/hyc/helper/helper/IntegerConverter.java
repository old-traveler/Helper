package com.hyc.helper.helper;

import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.converter.PropertyConverter;

public class IntegerConverter implements PropertyConverter<List<Integer>, String> {
  @Override
  public List<Integer> convertToEntityProperty(String databaseValue) {
    if (databaseValue == null) {
      return null;
    }
    String[] numbers = databaseValue.split(",");
    List<Integer> integers = new ArrayList<>();
    for (String number : numbers) {
      try {
        integers.add(Integer.valueOf(number));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return integers;
  }

  @Override
  public String convertToDatabaseValue(List<Integer> entityProperty) {
    if (entityProperty == null) {
      return null;
    }
    StringBuilder str = new StringBuilder();
    for (Integer integer : entityProperty) {
      str.append(integer);
      str.append(",");
    }
    if (str.length() > 0) {
      str.deleteCharAt(str.length() - 1);
    }
    return str.toString();
  }
}
