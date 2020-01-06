package com.hyc.helper.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.MenuItem
import com.hyc.helper.R
import com.hyc.helper.R.layout
import com.hyc.helper.activity.fragment.LostFindFragment
import com.hyc.helper.activity.fragment.SecondHandFragment
import com.hyc.helper.activity.fragment.StatementFragment
import com.hyc.helper.base.activity.BaseActivity
import com.hyc.helper.util.parrot.InitParam

/**
 * @author: 贺宇成
 * @date: 2020-01-03 16:14
 * @desc:
 */
class SearchResultActivity : BaseActivity() {
  @InitParam("search_type")
  private lateinit var mType: String
  @InitParam("keyWord")
  private lateinit var mKeyWord: String
  private lateinit var mFragment: Fragment

  override fun getContentViewId(): Int = layout.activity_personal_publish

  override fun initViewWithIntentData(bundle: Bundle?) {
    setToolBarTitle(getTitleByType())
  }

  override fun getMenuId(): Int {
    return R.menu.menu_search
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    if (item?.itemId == R.id.item_search) {
      SearchActivity.startForSearch(mFragment, mType, true)
      return true
    }
    return super.onOptionsItemSelected(item)
  }

  private fun getTitleByType(): String? {
    return when (mType) {
      SearchActivity.statement -> "说说"
      SearchActivity.secondHand -> "二手市场"
      SearchActivity.lostAndFind -> "失物招领"
      else -> ""
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val fragment = when (mType) {
      SearchActivity.statement -> StatementFragment.newSearchInstance(mKeyWord)
      SearchActivity.secondHand -> SecondHandFragment.newSearchInstance(mKeyWord)
      SearchActivity.lostAndFind -> LostFindFragment.newSearchInstance(mKeyWord)
      else -> null
    }
    fragment?.let {
      mFragment = it
      supportFragmentManager.beginTransaction()
        .add(R.id.fl_personal_statement, fragment).commit()
    }
  }

}