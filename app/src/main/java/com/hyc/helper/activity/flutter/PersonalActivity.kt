package com.hyc.helper.activity.flutter

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import android.widget.ListView
import com.google.gson.Gson
import com.hyc.cuckoo_lib.CuckooNeed
import com.hyc.helper.R
import com.hyc.helper.R.layout
import com.hyc.helper.activity.InputActivity
import com.hyc.helper.activity.PersonalPublishActivity
import com.hyc.helper.base.util.ToastHelper
import com.hyc.helper.base.util.UiHelper
import com.hyc.helper.helper.Constant
import com.hyc.helper.helper.UploadImageObserver
import com.hyc.helper.helper.UploadImageObserver.OnUploadImageListener
import com.hyc.helper.model.UserModel
import com.hyc.helper.util.DensityUtil
import com.hyc.helper.util.Glide4Engine
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropActivity
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import io.flutter.plugin.common.BasicMessageChannel.Reply
import java.io.File
import java.util.ArrayList
import java.util.Objects

/**
 * @author: 贺宇成
 * @date: 2019-12-26 09:41
 * @desc:
 */
class PersonalActivity : BaseFlutterActivity() {

  private val REQUEST_CODE_CHOOSE = 2009
  private val REQUEST_CODE_USERNAME = 2010
  private val REQUEST_CODE_BIO = 2011

  private lateinit var publishPopupWindow: ListPopupWindow

  override fun getFlutterRouter(): String {
    return "Personal"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setToolBarTitle(UiHelper.getString(R.string.personal))
    initMenuPopupWindow()
  }

  private fun initMenuPopupWindow() {
    val list = ArrayList<String>()
    list.add(UiHelper.getString(R.string.school_statement))
    list.add(UiHelper.getString(R.string.second_market))
    list.add(UiHelper.getString(R.string.lost_find))
    publishPopupWindow = ListPopupWindow(this)
    publishPopupWindow.setAdapter(ArrayAdapter(this, layout.item_select_week, list))
    publishPopupWindow.width = DensityUtil.dip2px(90f)
    publishPopupWindow.height = DensityUtil.dip2px(100f)
    publishPopupWindow.isModal = true
    publishPopupWindow.setOnItemClickListener { _, _, i, _ ->
      publishPopupWindow.dismiss()
      val type: String = when (i) {
        0 -> Constant.TYPE_STATEMENT
        1 -> Constant.TYPE_SECOND
        else -> Constant.TYPE_LOST
      }
      PersonalPublishActivity.goToPersonalPublishActivity(
        this,
        userModel.curUserInfo.data.user_id.toString(), type
      )
    }
  }

  private val userModel = UserModel()

  override fun getMenuId(): Int {
    return R.menu.menu_personal
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == R.id.my_publish) {
      publishPopupWindow.anchorView = findViewById<View>(R.id.my_publish)
      publishPopupWindow.show()
      Objects.requireNonNull<ListView>(
        publishPopupWindow.listView
      ).setBackgroundColor(UiHelper.getColor(R.color.white))
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onMessage(message: String?, reply: Reply<String>) {
    when (message) {
      "fetch_user_data" -> {
        reply.reply(Gson().toJson(userModel.curUserInfo.data))
      }
      "startSelectImage" -> {
        startSelectImage()
      }
      "updateUsername" -> {
        goToOtherActivityForResult(InputActivity::class.java, REQUEST_CODE_USERNAME)
      }
      "updateBio" -> {
        goToOtherActivityForResult(InputActivity::class.java, REQUEST_CODE_BIO)
      }
    }
  }

  @CuckooNeed(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
  private fun startSelectImage() {
    Matisse.from(this)
      .choose(MimeType.ofAll(), false)
      .countable(true)
      .maxSelectable(1)
      .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
      .thumbnailScale(0.85f)
      .imageEngine(Glide4Engine())
      .forResult(REQUEST_CODE_CHOOSE)
  }

  fun startCropImage(sourceUri: Uri) {
    val headImageFilePath = "head_image.jpg"
    val destinationUri = Uri.fromFile(File(cacheDir, headImageFilePath))
    val options = UCrop.Options()
    options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL)
    options.setToolbarColor(UiHelper.getColor(R.color.colorPrimary))
    options.setStatusBarColor(UiHelper.getColor(R.color.colorPrimary))
    options.withAspectRatio(9f, 9f)
    options.withMaxResultSize(1080, 1080)
    UCrop.of(sourceUri, destinationUri)
      .withOptions(options)
      .start(this)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && data != null) {
      val result = Matisse.obtainResult(data)
      if (result != null && result.size > 0) {
        startCropImage(result[0])
      }
    } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP && data != null) {
      uploadImage(UCrop.getOutput(data))
    } else if (resultCode == UCrop.RESULT_ERROR && data != null) {
      ToastHelper.toast(Objects.requireNonNull<Throwable>(UCrop.getError(data)).message)
    } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_USERNAME && data != null) {
      updateUsername(data.extras?.getString("input"))
    } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_BIO && data != null) {
      updateBio(data.extras?.getString("input"))
    }
  }

  fun uploadImage(uri: Uri?) {
    showLoadingView()
    userModel.updateUserHeadImage(
      this, userModel.curUserInfo, uri, UploadImageObserver(1,
        object : OnUploadImageListener {
          override fun onSuccess(image: String) {
            closeLoadingView()
            userModel.updateLocalUserHeadImage(image.substring(2))
            channel.send("refresh")
          }

          override fun onFailure(e: Throwable) {
            closeLoadingView()
            ToastHelper.toast(e.message)
          }
        })
    )
  }

  @SuppressLint("CheckResult")
  private fun updateUsername(input: String?) {
    showLoadingView()
    userModel.updateUsername(userModel.curUserInfo, input)
      .subscribe({ baseRequestBean ->
        closeLoadingView()
        when {
          baseRequestBean.getCode() == 200 -> {
            userModel.updateLocalUsername(input)
            channel.send("refresh")
          }
          TextUtils.isEmpty(baseRequestBean.getMsg()) -> ToastHelper.toast(baseRequestBean.getCode().toString())
          else -> ToastHelper.toast(baseRequestBean.getMsg())
        }
      }, { throwable ->
        closeLoadingView()
        ToastHelper.toast(throwable.message)
      })
  }

  @SuppressLint("CheckResult")
  private fun updateBio(input: String?) {
    showLoadingView()
    userModel.updateBio(userModel.curUserInfo, input)
      .subscribe({ baseRequestBean ->
        closeLoadingView()
        when {
          baseRequestBean.getCode() == 200 -> {
            userModel.updateLocalUserBio(input)
            channel.send("refresh")
          }
          TextUtils.isEmpty(baseRequestBean.getMsg()) -> ToastHelper.toast(baseRequestBean.getCode().toString())
          else -> ToastHelper.toast(baseRequestBean.getMsg())
        }
      }, { throwable ->
        closeLoadingView()
        ToastHelper.toast(throwable.message)
      })
  }

}