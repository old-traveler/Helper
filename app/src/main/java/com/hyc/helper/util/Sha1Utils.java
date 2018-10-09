package com.hyc.helper.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha1Utils {

  /**
   * SHA加密
   * @param strSrc  明文
   * @return 加密之后的密文
   */
  public static String shaEncrypt(String strSrc) {
    MessageDigest md = null;
    String strDes = null;
    byte[] bt = strSrc.getBytes();
    try {
      md = MessageDigest.getInstance("SHA-1");
      md.update(bt);
      strDes = bytes2Hex(md.digest());
    } catch (NoSuchAlgorithmException e) {
      return null;
    }
    return strDes;
  }

  /**
   * byte数组转换为16进制字符串
   * @param bts  数据源
   * @return 16进制字符串
   */
  public static String bytes2Hex(byte[] bts) {
    String des = "";
    String tmp = null;
    for (int i = 0; i < bts.length; i++) {
      tmp = (Integer.toHexString(bts[i] & 0xFF));
      if (tmp.length() == 1) {
        des += "0";
      }
      des += tmp;
    }
    return des;
  }

}
