package com.hyc.helper.bean;

import java.util.List;

public class GoodsDetailBean extends BaseRequestBean{

  /**
   * data : {"tit":"轮滑鞋","content":"女生穿的38码，原来在社团买的，奈何人懒，一次都没有穿过，原价380买的，现价350转让，全新的。","prize":"350","user_id":"10115402","attr":"99成新","pics":["/uploads/userpics/201809/1538188185.393181346_thumb.jpg","/uploads/userpics/201809/1538188186.768178658_thumb.jpg","/uploads/userpics/201809/1538188188.424416775_thumb.jpg"],"created_on":"2018-09-29 10:29:48","phone":"17607310753","address":"工大东门","pics_src":["/uploads/userpics/201809/1538188185.393181346.jpg","/uploads/userpics/201809/1538188186.768178658.jpg","/uploads/userpics/201809/1538188188.424416775.jpg"],"username":"张婷 - 会计1704"}
   */

  private DataBean data;

  public DataBean getData() {
    return data;
  }

  public void setData(DataBean data) {
    this.data = data;
  }

  public static class DataBean {
    /**
     * tit : 轮滑鞋
     * content : 女生穿的38码，原来在社团买的，奈何人懒，一次都没有穿过，原价380买的，现价350转让，全新的。
     * prize : 350
     * user_id : 10115402
     * attr : 99成新
     * pics : ["/uploads/userpics/201809/1538188185.393181346_thumb.jpg","/uploads/userpics/201809/1538188186.768178658_thumb.jpg","/uploads/userpics/201809/1538188188.424416775_thumb.jpg"]
     * created_on : 2018-09-29 10:29:48
     * phone : 17607310753
     * address : 工大东门
     * pics_src : ["/uploads/userpics/201809/1538188185.393181346.jpg","/uploads/userpics/201809/1538188186.768178658.jpg","/uploads/userpics/201809/1538188188.424416775.jpg"]
     * username : 张婷 - 会计1704
     */

    private String tit;
    private String content;
    private String prize;
    private String user_id;
    private String attr;
    private String created_on;
    private String phone;
    private String address;
    private String username;
    private List<String> pics;
    private List<String> pics_src;

    public String getTit() {
      return tit;
    }

    public void setTit(String tit) {
      this.tit = tit;
    }

    public String getContent() {
      return content;
    }

    public void setContent(String content) {
      this.content = content;
    }

    public String getPrize() {
      return prize;
    }

    public void setPrize(String prize) {
      this.prize = prize;
    }

    public String getUser_id() {
      return user_id;
    }

    public void setUser_id(String user_id) {
      this.user_id = user_id;
    }

    public String getAttr() {
      return attr;
    }

    public void setAttr(String attr) {
      this.attr = attr;
    }

    public String getCreated_on() {
      return created_on;
    }

    public void setCreated_on(String created_on) {
      this.created_on = created_on;
    }

    public String getPhone() {
      return phone;
    }

    public void setPhone(String phone) {
      this.phone = phone;
    }

    public String getAddress() {
      return address;
    }

    public void setAddress(String address) {
      this.address = address;
    }

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public List<String> getPics() {
      return pics;
    }

    public void setPics(List<String> pics) {
      this.pics = pics;
    }

    public List<String> getPics_src() {
      return pics_src;
    }

    public void setPics_src(List<String> pics_src) {
      this.pics_src = pics_src;
    }
  }
}
