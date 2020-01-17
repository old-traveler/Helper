package com.hyc.helper.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.hyc.helper.base.fragment.BaseFragment;
import java.util.List;

public class TechFragmentPageAdapter extends FragmentPagerAdapter {
  private List<? extends BaseFragment> fragmentList;

  public TechFragmentPageAdapter(FragmentManager fm, List<BaseFragment> fragmentList) {
    super(fm);
    this.fragmentList = fragmentList;
  }

  @Override
  public BaseFragment getItem(int position) {
    return fragmentList.get(position);
  }

  @Override
  public int getCount() {
    return fragmentList.size();
  }

  public <T extends BaseFragment> T getFragmentList(Class<T> cls, int index) {
    if (cls.isInstance(fragmentList.get(index))) {
      return cls.cast(fragmentList.get(index));
    }
    return null;
  }
}
