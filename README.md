# Helper


 helper是一款遵从Material Design风格设计且只服务于湖工大学子的App   
 flutter 版本可查看[flutter_helper](https://github.com/old-traveler/flutter_helper)
 
 [![AppVeyor](https://img.shields.io/badge/build-passing-green.svg)](https://github.com/old-traveler/Helper)&nbsp;&nbsp;
 [![Packagist](https://img.shields.io/badge/download-4.7M-red.svg)](https://raw.githubusercontent.com/old-traveler/Helper/master/app/release/app-release.apk)&nbsp;&nbsp;
 [![Packagist](https://img.shields.io/badge/API-28+-blue.svg)](https://github.com/old-traveler/Helper)&nbsp;&nbsp;
 [![Packagist](https://img.shields.io/badge/Version-1.3.0-green.svg)](https://github.com/old-traveler/Helper)&nbsp;&nbsp;
 [![Packagist](https://img.shields.io/badge/flutter-building-yellow.svg)](https://github.com/old-traveler/flutter_helper)
 
 
 ## 下载
 
 <div>
  <img src="https://raw.githubusercontent.com/old-traveler/Helper/master/img/1569832762.png" width = "200" height= "200">
 </div>
 
## 项目结构
 
项目使用mvc为架构。并封装base类支持全异步任务处理

 
* 网络请求： [Retrofit 2.0](https://github.com/square/retrofit)
* 异步任务： [RxJava 2.0](https://github.com/ReactiveX/RxJava)
* 图片加载： [Glide](https://github.com/bumptech/glide)
* 存数据库： [GreenDao](https://github.com/greenrobot/greenDAO)
* 大图加载： [Subsampling Scale Image View](https://github.com/davemorrissey/subsampling-scale-image-view)
* 权限处理： [RxPermissions](https://github.com/tbruyelle/RxPermissions)
* 图片选择： [Matisse](https://github.com/zhihu/Matisse)
* 图片压缩： [Luban](https://github.com/Curzibn/Luban)
* 头像剪裁： [Ucrop](https://github.com/Yalantis/uCrop)


## 功能介绍

- [x] 课程表
- [x] 校园说说
- [x] 二手市场
- [x] 失物招领
- [x] 查电费
- [x] 考试计划
- [x] 查看成绩
- [x] 图书馆
- [x] 校园日历
- [x] 个人信息
- [x] 查找用户
- [x] 实验课表
- [x] 私信

## 界面展示

### 课表 & 实验课

---

<div class="half">
    <img src="https://raw.githubusercontent.com/old-traveler/Helper/master/img/screener_course.png" width = "400" height= "720"><img src="https://raw.githubusercontent.com/old-traveler/Helper/master/img/screener_course_detail.png" width = "400" height= "720">
</div>

### 校园说说

---

<div class="half">
    <img src="https://raw.githubusercontent.com/old-traveler/Helper/master/img/screener_statement.png" width = "400" height = "720"><img src = "https://raw.githubusercontent.com/old-traveler/Helper/master/img/screener_mypublish.png" width = "400" height = "720">
</div>

### 二手市场

---

<div class="half">
    <img src="https://raw.githubusercontent.com/old-traveler/Helper/master/img/screener_market.png" width = "400" height = "720"><img src = "https://raw.githubusercontent.com/old-traveler/Helper/master/img/screener_second.png" width = "400" height = "720">
</div>

### 私信

---

<div class="half">
    <img src="https://raw.githubusercontent.com/old-traveler/Helper/master/img/screener_conversation.png" width = "400" height = "720"><img src = "https://raw.githubusercontent.com/old-traveler/Helper/master/img/screener_message.png" width = "400" height = "720">
</div>


### 用户信息

---

<div class="half">
    <img src="https://raw.githubusercontent.com/old-traveler/Helper/master/img/screener_info.png" width = "400" height = "720"><img src = "https://raw.githubusercontent.com/old-traveler/Helper/master/img/screener_other.png" width = "400" height = "720">
</div>


### 失物招领&侧拉信息

---

<div class="half">
    <img src="https://raw.githubusercontent.com/old-traveler/Helper/master/img/screener_lost.png"  width = "400" height = "720"><img src = "https://raw.githubusercontent.com/old-traveler/Helper/master/img/screener_left.png" width = "400" height = "720">
</div>

### 考试计划&成绩

---

<div class="half">
    <img src="https://raw.githubusercontent.com/old-traveler/Helper/master/img/screener_exam.png" width = "400" height = "720"><img src = "https://raw.githubusercontent.com/old-traveler/Helper/master/img/screener_grade.png" width = "400" height = "720">
</div>

## 版本更新

### 1.1.1

* 新增课表日期提示
* 使用队列管理Disposable防止内存泄漏
* 优化体验，如已下载原图优先加载原图


### 1.2.0

* 新增实验课表与正常课表同时查看
* 重要日期提醒
* 优化体验

### 1.3.0

* 新增私信功能
* 考试日期跑马灯首页显示
* 优化升级体验





