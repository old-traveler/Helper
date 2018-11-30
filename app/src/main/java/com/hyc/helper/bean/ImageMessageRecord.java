package com.hyc.helper.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class ImageMessageRecord {

  @Id
  private String originalPath;

  private String compressPath;

  private String cloudPath;

  @Generated(hash = 1601549894)
  public ImageMessageRecord(String originalPath, String compressPath,
          String cloudPath) {
      this.originalPath = originalPath;
      this.compressPath = compressPath;
      this.cloudPath = cloudPath;
  }

  @Generated(hash = 238124980)
  public ImageMessageRecord() {
  }

  public String getOriginalPath() {
      return this.originalPath;
  }

  public void setOriginalPath(String originalPath) {
      this.originalPath = originalPath;
  }

  public String getCompressPath() {
      return this.compressPath;
  }

  public void setCompressPath(String compressPath) {
      this.compressPath = compressPath;
  }

  public String getCloudPath() {
      return this.cloudPath;
  }

  public void setCloudPath(String cloudPath) {
      this.cloudPath = cloudPath;
  }


}
