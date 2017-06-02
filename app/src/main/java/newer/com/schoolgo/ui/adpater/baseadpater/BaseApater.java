package newer.com.schoolgo.ui.adpater.baseadpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/3.
 */

public abstract class BaseApater<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TOP_VIEW = 100;   //顶部视图
    private static final int CONTENT_VIEW = 101;  //内容视图
    protected List<T> mDatas = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener<T> mItemClickListener;

    public BaseApater(Context context) {
        mContext = context;
        mDatas = new ArrayList<T>();
    }

    protected abstract int initLayoutId();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        viewHolder = ViewHolder.creatByLayout(mContext, initLayoutId(),
                parent);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        createItemView(holder, mDatas.get(position), position);
    }

    private void createItemView(RecyclerView.ViewHolder holder, final T t, final int position) {
        final ViewHolder vh = (ViewHolder) holder;
        convert(vh, t);
        //设置点击监听
        if (mItemClickListener != null) {
            vh.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClicked(vh, mDatas.get(position), position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public abstract void convert(ViewHolder viewHolder, T t);

    public void setDatas(List<T> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener<T> itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public void setFirstData(T data) {
        mDatas.add(0, data);
        notifyItemInserted(0);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener<T> {
        void onItemClicked(ViewHolder vh, T t, int pos);
    }
}
