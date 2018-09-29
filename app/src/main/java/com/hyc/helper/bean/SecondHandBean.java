package com.hyc.helper.bean;

import java.util.List;

public class SecondHandBean extends BaseRequestBean {

  /**
   * code : 200
   * current_page : 1
   * pageination : 95
   * goods : [{"id":"8654","user_id":"36987","tit":"衬衫","prize":"150.00","created_on":"1天内","image":"/uploads/userpics/201809/1538181930.533883648_thumb.jpg"},{"id":"8653","user_id":"10131403","tit":"champion卫衣","prize":"329.00","created_on":"1天内","image":"/uploads/userpics/201809/1538152145.611939763_thumb.jpg"},{"id":"8652","user_id":"10122628","tit":"美瞳200度精灵紫","prize":"150.00","created_on":"1天内","image":"/uploads/userpics/201809/1538142021.6922511_thumb.jpg"},{"id":"8651","user_id":"10122628","tit":"健身卡","prize":"880.00","created_on":"1天内","image":"/uploads/userpics/201809/1538141672.06528153_thumb.jpg"},{"id":"8650","user_id":"10131403","tit":"champion卫衣","prize":"329.00","created_on":"1天内","image":"/uploads/userpics/201809/1538138457.783836590_thumb.jpg"},{"id":"8648","user_id":"31952","tit":"工大正装出租","prize":"1.00","created_on":"1天内","image":"/uploads/userpics/201809/1538137937.971318316_thumb.jpg"},{"id":"8647","user_id":"10119102","tit":"出售锐捷路由器，送10米长网线。","prize":"90.00","created_on":"1天内","image":"/uploads/userpics/201809/1538134578.033874953_thumb.jpg"},{"id":"8646","user_id":"10131403","tit":"DW手表正品保真","prize":"766.00","created_on":"1天内","image":"/uploads/userpics/201809/1538133766.049432494_thumb.jpg"},{"id":"8645","user_id":"24014","tit":"出个gtx750显卡","prize":"100.00","created_on":"1天内","image":"/uploads/userpics/201809/1538133202.98693169_thumb.jpg"},{"id":"8644","user_id":"28302","tit":"大东女鞋凉鞋","prize":"50.00","created_on":"1天内","image":"/uploads/userpics/201809/1538132951.002581224_thumb.jpg"},{"id":"8643","user_id":"34257","tit":"出自行车","prize":"400.00","created_on":"1天内","image":"/uploads/userpics/201809/1538131901.283891706_thumb.jpg"},{"id":"8642","user_id":"10122596","tit":"饮水机","prize":"50.00","created_on":"1天内","image":"/uploads/userpics/201809/1538128946.721397926_thumb.jpg"},{"id":"8641","user_id":"10131403","tit":"匡威橄榄绿高帮","prize":"399.00","created_on":"1天内","image":"/uploads/userpics/201809/1538125713.4436371_thumb.jpg"},{"id":"8638","user_id":"28780","tit":"英语二黄皮书10-18年真题（经典试卷版）","prize":"45.00","created_on":"1天内","image":"/uploads/userpics/201809/1538117398.471327791_thumb.jpg"},{"id":"8636","user_id":"10121846","tit":"买17级教材","prize":"10.00","created_on":"1天内","image":"/uploads/userpics/201809/1538116965.62754680_thumb.jpg"},{"id":"8635","user_id":"36753","tit":"电话卡","prize":"90.00","created_on":"1天内","image":"/uploads/userpics/201809/1538114896.42447992_thumb.jpg"},{"id":"8631","user_id":"10121494","tit":"卖学校跆拳道服165-170都可以穿","prize":"50.00","created_on":"1天内","image":"/uploads/userpics/201809/1538109749.56520703_thumb.jpg"},{"id":"8630","user_id":"27330","tit":"结构力学朱慈勉","prize":"18.00","created_on":"1天内","image":"/uploads/userpics/201809/1538105035.361958927_thumb.jpg"},{"id":"8629","user_id":"10119102","tit":"骑了一个月左右，买了新自行车，打算出售这辆车。","prize":"140.00","created_on":"1天内","image":"/uploads/userpics/201809/1538104172.424471007_thumb.jpg"},{"id":"8628","user_id":"27802","tit":"任天堂switch+塞尔达+你裁我剪","prize":"2,300.00","created_on":"1天内","image":"/uploads/userpics/201809/1538103967.783875969_thumb.jpg"}]
   */

  private int current_page;
  private int pageination;
  private List<GoodsBean> goods;

  public int getCurrent_page() {
    return current_page;
  }

  public void setCurrent_page(int current_page) {
    this.current_page = current_page;
  }

  public int getPageination() {
    return pageination;
  }

  public void setPageination(int pageination) {
    this.pageination = pageination;
  }

  public List<GoodsBean> getGoods() {
    return goods;
  }

  public void setGoods(List<GoodsBean> goods) {
    this.goods = goods;
  }

  public static class GoodsBean {
    /**
     * id : 8654
     * user_id : 36987
     * tit : 衬衫
     * prize : 150.00
     * created_on : 1天内
     * image : /uploads/userpics/201809/1538181930.533883648_thumb.jpg
     */

    private String id;
    private String user_id;
    private String tit;
    private String prize;
    private String created_on;
    private String image;

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

    public String getTit() {
      return tit;
    }

    public void setTit(String tit) {
      this.tit = tit;
    }

    public String getPrize() {
      return prize;
    }

    public void setPrize(String prize) {
      this.prize = prize;
    }

    public String getCreated_on() {
      return created_on;
    }

    public void setCreated_on(String created_on) {
      this.created_on = created_on;
    }

    public String getImage() {
      return image;
    }

    public void setImage(String image) {
      this.image = image;
    }
  }
}
