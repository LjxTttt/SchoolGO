package newer.com.schoolgo.ui.adpater;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import newer.com.schoolgo.R;
import newer.com.schoolgo.bean.Moment;
import newer.com.schoolgo.ui.activity.LoginActivity;
import newer.com.schoolgo.util.SPUtil;

/**
 * Created by Administrator on 2017/5/2.
 */

public class FindListAdpater extends BGARecyclerViewAdapter<Moment> implements
        BGANinePhotoLayout.Delegate, View.OnFocusChangeListener {
    private OnClickNinePhotoItem mOnClickNinePhotoItem;
    private OnComentLisenter onComentLisenter;

    private Context mContext;

    public FindListAdpater(RecyclerView recyclerView, Context context) {
        super(recyclerView, R.layout.item_find_list);
        this.mContext = context;
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, final int position, Moment moment) {
        if (TextUtils.isEmpty(moment.moment_content)) {
            helper.setVisibility(R.id.tv_item_moment_content, View.GONE);
        } else {
            final ComentAdpater childAdpater = new ComentAdpater(mContext);
            final RecyclerView childRecy = helper.getView(R.id.comment_recy);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            childRecy.setLayoutManager(layoutManager);
            childRecy.setAdapter(childAdpater);
            if (!moment.coment.isEmpty()) {
                childAdpater.setDatas(moment.coment);
            }
            helper.setVisibility(R.id.tv_item_moment_content, View.VISIBLE);
            helper.setText(R.id.tv_item_moment_content, moment.moment_content);
            helper.setText(R.id.tv_item_moment_username, moment.moment_user_name);
            BGANinePhotoLayout ninePhotoLayout = helper.getView(R.id.nine_photo_layout);
            ninePhotoLayout.setDelegate(this);
            if (moment.moment_Img != null) {
                ninePhotoLayout.setData(moment.moment_Img);
            }
            final EditText editText = helper.getView(R.id.comment_content);
            final Button btn = helper.getView(R.id.commit);
            final long tag = moment.moment_tag;
            editText.setOnFocusChangeListener(this);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(final Editable editable) {
                    btn.setEnabled(canEnabled(editable.toString()));
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (view.getId() == R.id.commit) {
                                onComentLisenter.commit(editable.toString(), tag, position);
                                editable.clear();
                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
        if (mOnClickNinePhotoItem != null) {
            mOnClickNinePhotoItem.onClickNinePhotoItemByFind(ninePhotoLayout, view, position, model, models);
        }
    }


    private boolean canEnabled(String str) {
        return !(str.isEmpty() || !SPUtil.contain(LoginActivity.USER_ID));
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        EditText text = (EditText) view;
        if (!b) {
            text.setText("");
        }
    }

    public void setOnClickNinePhotoItem(OnClickNinePhotoItem listener) {
        this.mOnClickNinePhotoItem = listener;
    }

    public void setOnComentLisenter(OnComentLisenter onComentLisenter) {
        this.onComentLisenter = onComentLisenter;
    }

    public interface OnClickNinePhotoItem {
        void onClickNinePhotoItemByFind(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models);
    }

    public interface OnComentLisenter {
        void commit(String text, long tag, int pos);
    }
}
