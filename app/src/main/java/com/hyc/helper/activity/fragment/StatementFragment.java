package com.hyc.helper.activity.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.hyc.helper.R;
import com.hyc.helper.activity.PublishStatementActivity;
import com.hyc.helper.activity.SearchActivity;
import com.hyc.helper.adapter.StatementAdapter;
import com.hyc.helper.adapter.viewholder.StatementViewHolder;
import com.hyc.helper.annotation.Subscribe;
import com.hyc.helper.base.adapter.BaseRecycleAdapter;
import com.hyc.helper.base.fragment.BaseListFragment;
import com.hyc.helper.base.util.ToastHelper;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.bean.CommentInfoBean;
import com.hyc.helper.bean.MessageEvent;
import com.hyc.helper.bean.StatementBean;
import com.hyc.helper.bean.StatementInfoBean;
import com.hyc.helper.bean.UserBean;
import com.hyc.helper.helper.Constant;
import com.hyc.helper.model.StatementModel;
import com.hyc.helper.model.UserModel;
import com.hyc.helper.util.RxBus;
import com.hyc.helper.util.ThreadMode;
import com.hyc.helper.util.parrot.InitParam;
import com.hyc.helper.util.parrot.Parrot;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class StatementFragment extends
    BaseListFragment<StatementInfoBean, StatementBean, StatementViewHolder> {
  @BindView(R.id.et_comment)
  EditText etComment;
  @BindView(R.id.cv_comment)
  CardView cvComment;
  @BindView(R.id.fb_publish_statement)
  FloatingActionButton floatingActionButton;
  @BindView(R.id.fm_menu)
  FloatingActionsMenu floatingActionsMenu;
  @BindView(R.id.fb_type)
  FloatingActionButton fbType;
  Unbinder unbinder;
  @InitParam(Constant.USER_ID)
  private String userId;
  private int position;
  private boolean isFire = false;
  private UserModel userModel = new UserModel();
  private StatementModel statementModel = new StatementModel();
  private Drawable mDrawable;
  @InitParam("keyWord")
  private String mKeyWord;

  public static StatementFragment newInstance(String userId) {
    StatementFragment statementFragment = new StatementFragment();
    Bundle bundle = new Bundle();
    bundle.putString(Constant.USER_ID, userId);
    statementFragment.setArguments(bundle);
    return statementFragment;
  }

  public static StatementFragment newSearchInstance(String keyWord) {
    StatementFragment statementFragment = new StatementFragment();
    Bundle bundle = new Bundle();
    bundle.putString("keyWord", keyWord);
    statementFragment.setArguments(bundle);
    return statementFragment;
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override
  protected BaseRecycleAdapter<StatementInfoBean, StatementViewHolder> setRecycleAdapter() {
    return new StatementAdapter(null, R.layout.item_statement, StatementViewHolder.class);
  }

  @Override
  protected int getRefreshLayoutId() {
    return R.id.sfl_statement;
  }

  @Override
  protected int getRecycleViewId() {
    return R.id.rv_statement;
  }

  @Override
  protected void requestListData(int page) {
    if (!TextUtils.isEmpty(mKeyWord)) {
      statementModel.searchStatementByKeyword(userModel.getStudentId(),
          userModel.getCurUserInfo().getRemember_code_app(), mKeyWord, page).subscribe(this);
    } else if (TextUtils.isEmpty(userId)) {
      if (isFire) {
        statementModel.fetchFireStatement(userModel.getStudentId(), page).subscribe(this);
      } else {
        statementModel.getStatementByPage(page, userModel.getStudentId(), this);
      }
    } else if (!userId.equals(Constant.TYPE_RELATED)) {
      statementModel.getPersonalStatement(userModel.getStudentId(), page, userId, this);
    } else {
      statementModel.fetchInteractiveStatement(userModel.getStudentId(),
          userModel.getCurUserInfo().getRemember_code_app(), page).subscribe(this);
    }
  }

  @Override
  protected List<StatementInfoBean> getData(StatementBean statementBean) {
    try {
      if (Integer.parseInt(statementBean.getCurrent_page()) < getCurPage()) {
        return null;
      }
    } catch (Exception e) {
      return statementBean.getStatement();
    }
    return statementBean.getStatement();
  }

  @Override
  protected int getLayoutId() {
    return R.layout.fragment_statement;
  }

  @Override
  protected void initLayoutView(View view) {
    super.initLayoutView(view);
    unbinder = ButterKnife.bind(this, view);
    getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (dy != 0 && cvComment.getVisibility() == View.VISIBLE) {
          closeCommentInput();
        }
      }
    });
    if (!TextUtils.isEmpty(userId) || !TextUtils.isEmpty(mKeyWord)) {
      floatingActionsMenu.setVisibility(View.GONE);
    }
  }

  private void closeCommentInput() {
    cvComment.setVisibility(View.GONE);
    hideInputWindow();
  }

  private void showCommentInput() {
    cvComment.setVisibility(View.VISIBLE);
    showInputWindow(etComment);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  private void sendComment() {
    if (TextUtils.isEmpty(etComment.getText().toString())) {
      closeCommentInput();
      return;
    }
    StatementInfoBean itemData = getRecycleAdapter().getItemData(position);
    UserBean userBean = userModel.getCurUserInfo();
    statementModel.commentStatement(userBean.getData().getStudentKH(),
        userBean.getRemember_code_app(), etComment.getText().toString(), itemData.getId(),
        baseRequestBean -> {
          if (baseRequestBean.getCode() == 200) {
            closeCommentInput();
            CommentInfoBean commentInfoBean = new CommentInfoBean();
            commentInfoBean.setComment(etComment.getText().toString());
            commentInfoBean.setUsername(userBean.getData().getUsername());
            itemData.getComments().add(commentInfoBean);
            getRecycleAdapter().refreshItemData(itemData, position);
            etComment.setText("");
          }
        });
  }

  @Override
  public void onItemClick(StatementInfoBean itemData, View view, int position) {
    super.onItemClick(itemData, view, position);
    if (view.getId() == R.id.v_comment) {
      showCommentInput();
      this.position = position;
    } else if (view.getId() == R.id.tv_delete_statement) {
      showLoadingView();
      addDisposable(statementModel.deleteStatement(userModel.getCurUserInfo(), itemData.getId())
          .subscribe(baseRequestBean -> {
            if (baseRequestBean.getCode() == 200) {
              getRecycleAdapter().removeItemFormList(position);
            } else {
              ToastHelper.toast(baseRequestBean.getCode());
            }
            closeLoadingView();
          }, throwable -> {
            ToastHelper.toast(throwable.getMessage());
            closeLoadingView();
          }));
    }
  }

  @OnClick({ R.id.btn_send_comment, R.id.fb_publish_statement, R.id.fb_type, R.id.fb_search })
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.btn_send_comment:
        sendComment();
        break;
      case R.id.fb_publish_statement:
        startActivityForResult(new Intent(getActivity(), PublishStatementActivity.class), 2010);
        floatingActionsMenu.collapse();
        break;
      case R.id.fb_type:
        isFire = !isFire;
        if (mDrawable == null) {
          mDrawable = getResources().getDrawable(R.drawable.ic_fire, null);
        }
        Drawable drawable = UiHelper.tintDrawable(mDrawable,
            UiHelper.getColor(isFire ? R.color.hot : R.color.white));
        fbType.setIconDrawable(drawable);
        refresh();
        floatingActionsMenu.collapse();
        break;
      case R.id.fb_search:
        floatingActionsMenu.collapse();
        SearchActivity.startForSearch(getActivity(), SearchActivity.statement);
        break;
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 2010 && resultCode == RESULT_OK) {
      refresh();
    } else if (requestCode == SearchActivity.searchCode
        && resultCode == RESULT_OK
        && data.getExtras() != null) {
      Parrot.initParam(data.getExtras(), this);
      refresh();
    }
  }

  @Subscribe(threadMode = ThreadMode.MAIN, eventType = { Constant.EventType.ORIGINAL_DOWNLOAD })
  public void onEvent(MessageEvent event) {
    getRecycleAdapter().notifyDataSetChanged();
  }

  @Override
  public void onStart() {
    super.onStart();
    RxBus.getDefault().register(this);
  }

  @Override
  public void onStop() {
    super.onStop();
    RxBus.getDefault().unregister(this);
  }
}
