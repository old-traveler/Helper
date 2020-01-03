package com.hyc.helper.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.TextView
import com.google.gson.Gson
import com.hyc.helper.R
import com.hyc.helper.base.activity.BaseActivity
import com.hyc.helper.base.util.ToastHelper
import com.hyc.helper.helper.SpCacheHelper
import com.hyc.helper.util.parrot.InitCache
import com.hyc.helper.util.parrot.InitParam
import com.hyc.helper.view.TipLayout.OnItemClickListener
import kotlinx.android.synthetic.main.activity_search.tipLayout
import kotlinx.android.synthetic.main.activity_search.tv_clear

/**
 * @author: 贺宇成
 * @date: 2020-01-03 14:46
 * @desc:
 */
class SearchActivity : BaseActivity() {
  @InitParam("search_type")
  private lateinit var mType: String
  @InitParam("forResult")
  private var forResult: Boolean = false
  @InitCache("search_history", prefixKey = "type")
  private var mSearchHistory: MutableList<String> = mutableListOf()

  override fun getContentViewId(): Int = R.layout.activity_search

  companion object {
    const val statement = "statement"
    const val searchCode = 4396
    @JvmOverloads
    @JvmStatic
    fun startForSearch(activity: Any?, type: String, forResult: Boolean = false) {
      activity ?: return
      Log.d("startForSearch", forResult.toString())
      val bundle = Bundle()
      bundle.putString("search_type", type)
      bundle.putBoolean("forResult", forResult)
      if (forResult) {
        if (activity is Activity) {
          activity.startActivityForResult(
            Intent(activity, SearchActivity::class.java).putExtras(
              bundle
            ), searchCode
          )
        } else if (activity is Fragment) {
          activity.startActivityForResult(
            Intent(activity.activity, SearchActivity::class.java).putExtras(
              bundle
            ), searchCode
          )
        }

      } else if (activity is Activity) {
        activity.startActivity(Intent(activity, SearchActivity::class.java).putExtras(bundle))
      }
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_conversation, menu)
    val searchItem = menu!!.findItem(R.id.action_search)
    initSearchView(searchItem.actionView as SearchView)
    return true
  }

  private fun initSearchView(searchView: SearchView) {
    searchView.queryHint = "请输入关键字"
    searchView.isIconified = false
    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(s: String): Boolean {
        if (s.isNotEmpty()) {
          backSearchKeyWord(s)
        } else {
          ToastHelper.toast("请输入关键词")
        }
        return true
      }

      override fun onQueryTextChange(s: String): Boolean {
        return false
      }
    })
  }

  private fun backSearchKeyWord(keyWord: String) {
    Log.d("backSearchKeyWord", forResult.toString())
    mSearchHistory.remove(keyWord)
    mSearchHistory.add(0, keyWord)
    SpCacheHelper.putString("search_history$mType", Gson().toJson(mSearchHistory))
    val bundle = Bundle()
    bundle.putString("keyWord", keyWord)
    if (forResult) {
      backForResult(SearchResultActivity::class.java, bundle, Activity.RESULT_OK)
    } else {
      bundle.putString("search_type", mType)
      goToOtherActivity(SearchResultActivity::class.java, bundle, true)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    SpCacheHelper.getString("search_history$mType")?.let {
      mSearchHistory = Gson().fromJson(it, mSearchHistory::class.java)
    }
    val historyTipViews = mutableListOf<View>()
    mSearchHistory.forEach {
      val view: View =
        LayoutInflater.from(this).inflate(R.layout.sug_item_single_tip, tipLayout, false)
      view.findViewById<TextView>(R.id.tv_sug_tip_content).text = it
      historyTipViews.add(view)
    }
    tipLayout.setViews(historyTipViews)
    tv_clear.setOnClickListener {
      SpCacheHelper.removeKey("search_history$mType")
      tipLayout.removeAllViews()
    }
    tipLayout.setOnItemClickListener(object : OnItemClickListener {
      override fun onItemClick(position: Int, view: View) {
        backSearchKeyWord(mSearchHistory[position])
      }
    })
  }

  override fun initViewWithIntentData(bundle: Bundle?) {
    setToolBarTitle("搜索页面")
  }

}