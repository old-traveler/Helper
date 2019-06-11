package com.hyc.helper.bean;

import com.hyc.helper.helper.ListStringConverter;
import java.util.List;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 作者: 贺宇成
 * 时间: 2019-06-11
 * 描述:
 */
@Entity
public class StatementInfoBean {

  private String id;
  private String user_id;
  private String content;
  private String created_on;
  private String is_top;
  private String likes;
  private String view_cnt;
  private String username;
  private String bio;
  private String dep_name;
  private String head_pic;
  private String head_pic_thumb;
  private boolean is_like;
  @Convert(columnType = String.class, converter = ListStringConverter.class)
  private List<String> pics;
  @Transient
  private List<CommentInfoBean> comments;

  @Generated(hash = 2012236072)
  public StatementInfoBean(String id, String user_id, String content,
          String created_on, String is_top, String likes, String view_cnt,
          String username, String bio, String dep_name, String head_pic,
          String head_pic_thumb, boolean is_like, List<String> pics) {
      this.id = id;
      this.user_id = user_id;
      this.content = content;
      this.created_on = created_on;
      this.is_top = is_top;
      this.likes = likes;
      this.view_cnt = view_cnt;
      this.username = username;
      this.bio = bio;
      this.dep_name = dep_name;
      this.head_pic = head_pic;
      this.head_pic_thumb = head_pic_thumb;
      this.is_like = is_like;
      this.pics = pics;
  }

  @Generated(hash = 791664414)
  public StatementInfoBean() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUser_id() {
    return user_id;
  }

  public void setUser_id(String user_id) {
    this.user_id = user_id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getCreated_on() {
    return created_on;
  }

  public void setCreated_on(String created_on) {
    this.created_on = created_on;
  }

  public String getIs_top() {
    return is_top;
  }

  public void setIs_top(String is_top) {
    this.is_top = is_top;
  }

  public String getLikes() {
    return likes;
  }

  public void setLikes(String likes) {
    this.likes = likes;
  }

  public String getView_cnt() {
    return view_cnt;
  }

  public void setView_cnt(String view_cnt) {
    this.view_cnt = view_cnt;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }

  public String getDep_name() {
    return dep_name;
  }

  public void setDep_name(String dep_name) {
    this.dep_name = dep_name;
  }

  public String getHead_pic() {
    return head_pic;
  }

  public void setHead_pic(String head_pic) {
    this.head_pic = head_pic;
  }

  public String getHead_pic_thumb() {
    return head_pic_thumb;
  }

  public void setHead_pic_thumb(String head_pic_thumb) {
    this.head_pic_thumb = head_pic_thumb;
  }

  public boolean isIs_like() {
    return is_like;
  }

  public void setIs_like(boolean is_like) {
    this.is_like = is_like;
  }

  public List<String> getPics() {
    return pics;
  }

  public void setPics(List<String> pics) {
    this.pics = pics;
  }

  public List<CommentInfoBean> getComments() {
    return comments;
  }

  public void setComments(List<CommentInfoBean> comments) {
    this.comments = comments;
  }

  public boolean getIs_like() {
      return this.is_like;
  }
}
