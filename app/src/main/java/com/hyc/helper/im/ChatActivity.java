package com.hyc.helper.im;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.v3.exception.BmobException;
import com.hyc.cuckoo_lib.CuckooNeed;
import com.hyc.helper.R;
import com.hyc.helper.activity.PictureBrowsingActivity;
import com.hyc.helper.adapter.ChatAdapter;
import com.hyc.helper.adapter.viewholder.LocalImageViewHolder;
import com.hyc.helper.annotation.Subscribe;
import com.hyc.helper.base.activity.BaseActivity;
import com.hyc.helper.base.adapter.BaseRecycleAdapter;
import com.hyc.helper.base.util.ToastHelper;
import com.hyc.helper.bean.LocalImageBean;
import com.hyc.helper.bean.MessageEvent;
import com.hyc.helper.helper.Constant;
import com.hyc.helper.helper.DbSearchHelper;
import com.hyc.helper.helper.FileHelper;
import com.hyc.helper.helper.ImageRecordHelper;
import com.hyc.helper.helper.LogHelper;
import com.hyc.helper.helper.SendMessageHelper;
import com.hyc.helper.helper.VoicePlayer;
import com.hyc.helper.model.UserModel;
import com.hyc.helper.util.DensityUtil;
import com.hyc.helper.util.RxBus;
import com.hyc.helper.util.ThreadMode;
import com.hyc.helper.util.parrot.InitParam;
import com.hyc.helper.view.ChatLinearLayoutManager;
import com.hyc.helper.view.ChatSpacesItemDecoration;
import com.hyc.helper.view.EmojiItemDecoration;
import com.hyc.helper.view.VoiceRecordView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class ChatActivity extends BaseActivity
    implements TextWatcher, View.OnFocusChangeListener, View.OnLayoutChangeListener,
    ChatAdapter.OnChatMessageClickListener, SwipeRefreshLayout.OnRefreshListener,
    Observer<LocalImageBean> {

  @BindView(R.id.rv_chat)
  RecyclerView chatRecyclerView;
  @BindView(R.id.sl_chat)
  SwipeRefreshLayout refreshLayout;
  @BindView(R.id.iv_voice)
  ImageView ivVoice;
  @BindView(R.id.et_input)
  EditText etInput;
  @BindView(R.id.iv_emoji)
  ImageView ivEmoji;
  @BindView(R.id.btn_send)
  MaterialButton btnSend;
  @BindView(R.id.rv_more_emoji)
  RecyclerView rvMoreEmoji;
  @BindView(R.id.fl_more)
  CardView flMore;
  @BindView(R.id.root_view)
  LinearLayout rootView;
  @BindView(R.id.voiceView)
  VoiceRecordView voiceRecordView;
  @InitParam(Constant.CHAT_INTENT_KEY)
  private BmobIMConversation conversation;
  private ChatAdapter adapter;
  private Disposable disposable;
  private BaseRecycleAdapter<LocalImageBean, LocalImageViewHolder> emojiAdapter;
  private boolean isKeyShow = false;
  private boolean isSetHeight = false;
  private int keyHeight = 0;
  private UserModel userModel = new UserModel();
  private String userId;
  public BmobIMUserInfo info = null;
  private boolean isEmoji = false;

  @Override
  protected int getContentViewId() {
    return R.layout.activity_chat;
  }

  @Override
  public void initViewWithIntentData(Bundle bundle) {
    ButterKnife.bind(this);
    initConversation();
    setToolBarTitle(conversation.getConversationTitle());
    initChatRecyclerView();
    initEmojiLayout();
    refreshLayout.setRefreshing(true);
    loadMessages(null, 10);
    refreshLayout.setOnRefreshListener(this);
    rootView.addOnLayoutChangeListener(this);
    keyHeight = DensityUtil.getScreenWidth() / 3;
    adapter.setOnChatMessageClickListener(this);
    emojiAdapter.setOnItemClickListener((itemData, view, position) -> clickLocalImage(itemData));
    chatRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (!needShowMoreLayout
            && dy > 0 && flMore.getVisibility() == View.VISIBLE) {
          flMore.setVisibility(View.GONE);
        }
      }
    });
    initVoice();
  }

  private void initVoice() {
    voiceRecordView.setId(conversation.getConversationId());
    voiceRecordView.setListener(
        (recordTime, path) -> SendMessageHelper.sendVoiceMessage(info, conversation, path,
            recordTime,
            new SendMessageListener(new WeakReference<>(chatRecyclerView))));
  }

  private void initEmojiLayout() {
    rvMoreEmoji.setLayoutManager(new GridLayoutManager(this, 4));
    rvMoreEmoji.addItemDecoration(new EmojiItemDecoration(8, 4));
    emojiAdapter = new BaseRecycleAdapter<>(R.layout.item_local_image, LocalImageViewHolder.class);
    rvMoreEmoji.setAdapter(emojiAdapter);
  }

  private void initConversation() {
    userId = String.valueOf(userModel.getCurUserInfo().getData().getUser_id());
    info = userModel.getIMUserInfo();
    conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversation);
  }

  private void initChatRecyclerView() {
    chatRecyclerView.setItemAnimator(new DefaultItemAnimator());
    chatRecyclerView.addItemDecoration(new ChatSpacesItemDecoration(DensityUtil.dip2px(13)));
    ChatLinearLayoutManager manager = new ChatLinearLayoutManager(this);
    chatRecyclerView.setLayoutManager(manager);
    manager.setStackFromEnd(false);
    etInput.addTextChangedListener(this);
    etInput.setOnFocusChangeListener(this);
  }

  @Override
  protected void onStop() {
    super.onStop();
    RxBus.getDefault().unregister(this);
  }

  @Override
  protected void onStart() {
    super.onStart();
    RxBus.getDefault().register(this);
  }

  public void scrollBottom() {
    if (adapter.getItemCount() > 0) {
      chatRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
      chatRecyclerView.scrollBy(0, chatRecyclerView.getBottom());
    } else {
      chatRecyclerView.scrollToPosition(0);
    }
  }

  @OnClick({ R.id.iv_voice, R.id.iv_emoji, R.id.btn_send })
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.iv_voice:
        isEmoji = false;
        showVoiceLayout();
        break;
      case R.id.iv_emoji:
        isEmoji = true;
        showEmojiLayout();
        break;
      case R.id.btn_send:
        sendTxtMessage();
        break;
    }
  }

  @CuckooNeed(Manifest.permission.RECORD_AUDIO)
  private void showVoiceLayout() {
    if (flMore.getVisibility() == View.VISIBLE && voiceRecordView.getVisibility() == View.VISIBLE) {
      flMore.setVisibility(View.GONE);
      voiceRecordView.setVisibility(View.GONE);
    } else {
      rvMoreEmoji.setVisibility(View.GONE);
      if (!isKeyShow) {
        showMoreLayout();
      } else {
        needShowMoreLayout = true;
        hideInputWindow();
      }
    }
  }

  private boolean needShowMoreLayout = false;

  private void showEmojiLayout() {
    if (flMore.getVisibility() == View.VISIBLE && rvMoreEmoji.getVisibility() == View.VISIBLE) {
      flMore.setVisibility(View.GONE);
      rvMoreEmoji.setVisibility(View.GONE);
    } else {
      voiceRecordView.setVisibility(View.GONE);
      if (!isKeyShow) {
        showMoreLayout();
      } else {
        needShowMoreLayout = true;
        hideInputWindow();
      }
    }
  }

  public void showMoreLayout() {
    if (!isEmoji) {
      flMore.setVisibility(View.VISIBLE);
      voiceRecordView.setVisibility(View.VISIBLE);
      return;
    }
    getLocalImages();
  }

  @CuckooNeed({
      Manifest.permission.CAMERA,
      Manifest.permission.WRITE_EXTERNAL_STORAGE
  })
  private void getLocalImages() {
    if (disposable == null) {
      FileHelper.getLocalImages()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(this);
    }
    flMore.setVisibility(View.VISIBLE);
    rvMoreEmoji.setVisibility(View.VISIBLE);
  }

  private void sendTxtMessage() {
    String content = etInput.getText().toString();
    SendMessageHelper.sendTxtMessage(info, conversation, content,
        new SendMessageListener(new WeakReference<>(chatRecyclerView)));
    etInput.setText("");
  }

  private void loadMessages(BmobIMMessage message, int size) {
    conversation.queryMessages(message, size, new MessagesQueryListener() {
      @Override
      public void done(List<BmobIMMessage> list, BmobException e) {
        refreshLayout.setRefreshing(false);
        if (null == e) {
          if (null == adapter) {
            adapter = new ChatAdapter(userId, list);
            chatRecyclerView.setAdapter(adapter);
            scrollBottom();
          } else {
            if (list.size() > 0) {
              adapter.insertMessages(list);
            } else {
              refreshLayout.setEnabled(false);
            }
          }
        } else {
          ToastHelper.toast(e.getMessage());
        }
      }
    });
  }

  @Override
  public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

  }

  @Override
  public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

  }

  @Override
  public void afterTextChanged(Editable editable) {
    btnSend.setEnabled(editable.length() > 0);
  }

  @Override
  public void onFocusChange(View view, boolean b) {

  }

  @Subscribe(threadMode = ThreadMode.MAIN, eventType = Constant.EventType.IM_MESSAGE)
  public void onEvent(MessageEvent<BmobIMMessage> event) {
    if (event.getData().getFromId().equals(conversation.getConversationId())) {
      adapter.sendNewMessage(event.getData());
      scrollBottom();
    }
  }

  @Override
  public void onRefresh() {
    loadMessages(adapter.getFirstMessage(), Constant.MSG_LOAD_COUNT);
  }

  @Override
  public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft,
      int oldTop, int oldRight, int oldBottom) {
    if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
      scrollBottom();
      isKeyShow = true;
      setMoreLayoutHeight(oldBottom - bottom);
      flMore.setVisibility(View.GONE);
      rvMoreEmoji.setVisibility(View.GONE);
    } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
      isKeyShow = false;
      if (needShowMoreLayout) {
        showMoreLayout();
        scrollBottom();
        needShowMoreLayout = false;
      }
    }
  }

  //private void isScrollBottom() {
  //  int lastPosition = manager.findLastCompletelyVisibleItemPosition();
  //}

  private void setMoreLayoutHeight(int height) {
    if (isSetHeight) {
      return;
    }
    isSetHeight = true;
    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) flMore.getLayoutParams();
    params.height = height;
    flMore.setLayoutParams(params);
  }

  @Override
  public void onSubscribe(Disposable d) {
    disposable = d;
  }

  @Override
  public void onNext(LocalImageBean localImageBean) {
    emojiAdapter.appendDataToList(localImageBean);
  }

  @Override
  public void onError(Throwable e) {
    LogHelper.log(e.getMessage());
  }

  @Override
  public void onComplete() {
    dispose();
  }

  private void dispose() {
    if (disposable != null && !disposable.isDisposed()) {
      disposable.isDisposed();
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    conversation.updateLocalCache();
    RxBus.getDefault().post(new MessageEvent<>(Constant.EventType.IM_MESSAGE, null));
    dispose();
    VoicePlayer.getDefault().stopPlay();
  }

  @Override
  public void onChatMessageClick(View view, int position, BmobIMMessage message) {
    if (view.getId() == R.id.iv_image_left || view.getId() == R.id.iv_image_right) {
      goToPictureBrowsing(message);
    } else if (view.getId() == R.id.iv_send_error) {
      adapter.updateMessage(message);
      conversation.resendMessage(message, new ResendMessageListener(adapter));
    }
  }

  private void goToPictureBrowsing(BmobIMMessage curImage) {
    int curImagePosition = 0;
    ArrayList<String> images = new ArrayList<>();
    List<BmobIMMessage> messages = adapter.getData();
    for (BmobIMMessage message : messages) {
      if (message.getMsgType().equals(Constant.MSG_IMAGE)) {
        String content = message.getContent();
        int index = content.indexOf("&");
        images.add(index != -1 ? content.substring(0, index) : content);
        if (curImage.equals(message)) {
          curImagePosition = images.size() - 1;
        }
      }
    }
    PictureBrowsingActivity.goToPictureBrowsingActivity(this, curImagePosition, images);
  }

  public static class SendMessageListener extends MessageSendListener {

    private WeakReference<RecyclerView> recyclerView;

    SendMessageListener(WeakReference<RecyclerView> recyclerView) {
      this.recyclerView = recyclerView;
    }

    @Override
    public void onStart(BmobIMMessage msg) {
      super.onStart(msg);
      if (recyclerView != null && recyclerView.get() != null) {
        ChatAdapter chatAdapter = (ChatAdapter) recyclerView.get().getAdapter();
        if (chatAdapter != null) {
          chatAdapter.sendNewMessage(msg);
          recyclerView.get().scrollToPosition(chatAdapter.getItemCount() - 1);
          recyclerView.get().scrollBy(0, recyclerView.get().getBottom());
        }
      }
    }

    @Override
    public void onFinish() {
      super.onFinish();
    }

    @Override
    public void onProgress(int value) {
      super.onProgress(value);
    }

    @Override
    public void done(BmobIMMessage bmobIMMessage, BmobException e) {
      if (recyclerView != null && recyclerView.get() != null) {
        ((ChatAdapter) Objects.requireNonNull(recyclerView.get().getAdapter())).updateMessage(
            bmobIMMessage);
        ((ChatActivity) recyclerView.get().getContext()).info.setName(null);
      }
      if (e != null) {
        ToastHelper.toast(e.getMessage());
      } else {
        String content = bmobIMMessage.getContent();
        if (bmobIMMessage instanceof BmobIMImageMessage
            && content.contains("&")) {
          String[] imageUrl = content.split("&");
          ImageRecordHelper.saveCloudRecord(imageUrl[0], imageUrl[1]);
        }
      }
    }
  }

  private void clickLocalImage(LocalImageBean localImageBean) {
    String originalPath = localImageBean.getImageRealPath();
    addDisposable(DbSearchHelper.getCloudPath(originalPath)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(record -> {
          if (record != null && !TextUtils.isEmpty(record.getCloudPath())) {
            SendMessageHelper.sendImageMessage(info,
                conversation, record,
                new SendMessageListener(new WeakReference<>(chatRecyclerView)));
          } else if (record != null && !TextUtils.isEmpty(record.getCompressPath())) {
            SendMessageHelper.sendImageMessage(info,
                conversation, new File(record.getCompressPath()),
                new SendMessageListener(new WeakReference<>(chatRecyclerView)));
          } else {
            compressImageAndSend(originalPath);
          }
        }, throwable -> {
          LogHelper.log(throwable.getMessage());
          compressImageAndSend(originalPath);
        }));
  }

  private void compressImageAndSend(String originalPath) {
    Luban.with(this)
        .load(originalPath)
        .ignoreBy(100)
        .filter(path -> !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif")))
        .setCompressListener(new OnCompressListener() {
          @Override
          public void onStart() {
            showLoadingView();
          }

          @Override
          public void onSuccess(File file) {
            closeLoadingView();
            ImageRecordHelper.saveCompressRecord(originalPath, file.getAbsolutePath());
            SendMessageHelper.sendImageMessage(info,
                conversation, file, new SendMessageListener(new WeakReference<>(chatRecyclerView)));
          }

          @Override
          public void onError(Throwable e) {
            closeLoadingView();
            ToastHelper.toast(e.getMessage());
            LogHelper.log(e.getMessage());
          }
        }).launch();
  }
}
