package com.hyc.helper.bean;

import java.util.List;

public class StatementBean extends BaseRequestBean{

  /**
   * code : 200
   * current_page : 1
   * pageination : 589
   * statement
   */

  private String current_page;
  private int pageination;
  private List<StatementInfoBean> statement;


  public String getCurrent_page() {
    return current_page;
  }

  public void setCurrent_page(String current_page) {
    this.current_page = current_page;
  }

  public int getPageination() {
    return pageination;
  }

  public void setPageination(int pageination) {
    this.pageination = pageination;
  }

  public List<StatementInfoBean> getStatement() {
    return statement;
  }

  public void setStatement(List<StatementInfoBean> statement) {
    this.statement = statement;
  }

  public static class StatementInfoBean {
    /**
     * id : 24851
     * user_id : 10119750
     * content : 五食堂的校园网是炸了么，一直连不上去🙃
     * pics : []
     * created_on : 2018-09-27
     * is_top : 0
     * likes : 0
     * view_cnt : 32
     * username : Huskie
     * bio :
     * dep_name : 理学院
     * head_pic :
     * head_pic_thumb : /uploads/headpics/201711/1510704481.529567092_thumb.jpg
     * is_like : false
     * comments : []
     */

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
    private List<String> pics;
    private List<CommentInfoBean> comments;

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
  }
}
