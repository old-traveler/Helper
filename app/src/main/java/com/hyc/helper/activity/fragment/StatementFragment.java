package com.hyc.helper.activity.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.hyc.helper.R;
import com.hyc.helper.activity.PublishStatementActivity;
import com.hyc.helper.adapter.viewholder.StatementViewHolder;
import com.hyc.helper.base.adapter.BaseRecycleAdapter;
import com.hyc.helper.base.fragment.BaseListFragment;
import com.hyc.helper.bean.CommentInfoBean;
import com.hyc.helper.bean.StatementBean;
import com.hyc.helper.bean.UserBean;
import com.hyc.helper.model.StatementModel;
import com.hyc.helper.model.UserModel;
import java.util.List;

public class StatementFragment extends
    BaseListFragment<StatementBean.StatementInfoBean, StatementBean, StatementViewHolder> {
  @BindView(R.id.et_comment)
  EditText etComment;
  @BindView(R.id.cv_comment)
  CardView cvComment;
  Unbinder unbinder;
  private int position;
  private UserModel userModel = new UserModel();
  private StatementModel statementModel = new StatementModel();

  @Override
  protected BaseRecycleAdapter<StatementBean.StatementInfoBean, StatementViewHolder> setRecycleAdapter() {
    return new BaseRecycleAdapter<>(null, R.layout.item_statement, StatementViewHolder.class);
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
    statementModel.getStatementByPage(page, userModel.getStudentId(), this);
  }

  @Override
  protected List<StatementBean.StatementInfoBean> getData(StatementBean statementBean) {
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
    StatementBean.StatementInfoBean itemData = getRecycleAdapter().getItemData(position);
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
  public void onItemClick(StatementBean.StatementInfoBean itemData, View view, int position) {
    super.onItemClick(itemData, view, position);
    if (view.getId() == R.id.v_comment) {
      showCommentInput();
      this.position = position;
    }
  }

  @OnClick({ R.id.btn_send_comment, R.id.fb_publish_statement })
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.btn_send_comment:
        sendComment();
        break;
      case R.id.fb_publish_statement:
        goToOtherActivity(PublishStatementActivity.class,false);
        break;
    }
  }
}
