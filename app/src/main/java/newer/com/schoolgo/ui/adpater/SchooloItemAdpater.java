package newer.com.schoolgo.ui.adpater;

import android.content.Context;

import java.util.List;

import newer.com.schoolgo.R;
import newer.com.schoolgo.bean.School;
import newer.com.schoolgo.ui.adpater.baseadpater.BaseApater;
import newer.com.schoolgo.ui.adpater.baseadpater.ViewHolder;

/**
 * Created by Administrator on 2017/3/8.
 */

public class SchooloItemAdpater extends BaseApater<School> {
    private Context mContext;

    public SchooloItemAdpater(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.recy_school_item;
    }

    @Override
    public void convert(ViewHolder viewHolder, School data) {
        viewHolder.setText(R.id.text_title, data.getNotice_titile());
        viewHolder.setText(R.id.text_content_lose, data.getContent());
        //Log.d("TAG",data.getImgSrc().toString());
        if (data.getImgSrc() != null) {
            viewHolder.setImgNine(R.id.bga_nine_layout, data.getImgSrc());
        }
        viewHolder.setText(R.id.sc_time, data.getNotice_time());
    }

    public void setNewSchoolNoticeData(List<School> datas) {
//        if(datas.get(0).getNotice_id() == mDatas.get(0).getNotice_id() ||
//                datas.isEmpty()){
//            ToastUtil.showToast(mContext,"没有更多数据");
//        }else{
//            mDatas.addAll(datas);
//        }
        mDatas.addAll(datas);
    }
}
