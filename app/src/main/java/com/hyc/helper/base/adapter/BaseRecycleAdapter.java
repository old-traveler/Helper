package com.hyc.helper.base.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hyc.helper.base.adapter.viewholder.BaseViewHolder;
import com.hyc.helper.base.listener.OnItemClickListener;
import com.hyc.helper.base.listener.OnItemLongClickListener;
import com.hyc.helper.helper.LogHelper;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class BaseRecycleAdapter<T,V extends BaseViewHolder<T>> extends RecyclerView.Adapter<V> {

  protected List<T> dataList;

  private int layoutId;

  private OnItemClickListener<T> onItemClickListener;

  private OnItemLongClickListener<T> onItemLongClickListener;

  private Context mContext;
  private Class<V> vClass;

  public BaseRecycleAdapter(List<T> dataList,int layoutId,Class<V> vClass){
    this.dataList = dataList;
    this.layoutId = layoutId;
    this.vClass = vClass;
  }

  @NonNull @Override public V onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    if (mContext == null)mContext = viewGroup.getContext();
    try {
      Constructor<V> constructor = vClass.getConstructor(View.class);
      return constructor.newInstance(newItemView(viewGroup,layoutId));
    } catch (NoSuchMethodException | IllegalAccessException
        | InstantiationException | InvocationTargetException e) {
      e.printStackTrace();
      LogHelper.log("反射失败");
    }
    return null;
  }

  private View newItemView(ViewGroup viewGroup,int resId){
    return LayoutInflater.from(viewGroup.getContext()).inflate(resId,viewGroup,false);
  }


  @Override public void onBindViewHolder(@NonNull V baseViewHolder, int i) {
    baseViewHolder.setData(dataList.get(i));
    baseViewHolder.loadItemData(mContext,dataList.get(i),i);
    if (onItemClickListener!=null){
      baseViewHolder.setOnClickListener(v
          ->onItemClickListener.onItemClick(dataList.get(i),v,i));
    }
    if (onItemLongClickListener!=null){
      baseViewHolder.itemView.setOnLongClickListener(v->
          onItemLongClickListener.onItemLongClick(dataList.get(i),i));
    }
  }

  @Override public int getItemCount() {
    return dataList==null ? 0:dataList.size();
  }

  public void appendDataToList(T data){
    if (dataList == null){
      dataList = new ArrayList<>();
    }
    dataList.add(data);
    notifyItemInserted(getItemCount()-1);
  }

  /**
   * 用于上拉加载更多更新界面
   * @param datas
   */
  public void appendDataToList(List<T> datas){
    int firstPosition = getItemCount();
    dataList.addAll(datas);
    int lastPosition = getItemCount();
    for (int i = firstPosition; i < lastPosition; i++) {
      notifyItemInserted(i);
    }
  }

  public T getItemData(int position){
    if (0<=position&&position<getItemCount()){
      return dataList.get(position);
    }
    return null;
  }

  /**
   * 用于下啦
   * @param dataList
   */
  public void setDataList(List<T> dataList) {
    this.dataList = dataList;
    notifyDataSetChanged();
  }

  public void removeItemFormList(int position){
    if (position<getItemCount()){
      dataList.remove(position);
      notifyItemRemoved(position);
    }
  }

  public void refreshItemData(T data,int position){
    dataList.set(position,data);
    notifyItemChanged(position);

  }

  public void refreshRangeData(int start,List<T> datas){
    int end = start + datas.size();
    if (start<0 || end>getItemCount()){
      return;
    }
    for (int i = start; i < end; i++) {
      dataList.set(i,datas.get(i-start));
    }
    notifyItemRangeChanged(start,datas.size());
  }

  public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  public void setOnItemLongClickListener(
      OnItemLongClickListener<T> onItemLongClickListener) {
    this.onItemLongClickListener = onItemLongClickListener;
  }
}
