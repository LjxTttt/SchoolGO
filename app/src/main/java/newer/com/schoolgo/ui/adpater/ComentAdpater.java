package newer.com.schoolgo.ui.adpater;

import android.content.Context;

import newer.com.schoolgo.R;
import newer.com.schoolgo.bean.Coment;
import newer.com.schoolgo.ui.adpater.baseadpater.BaseApater;
import newer.com.schoolgo.ui.adpater.baseadpater.ViewHolder;

/**
 * Created by Administrator on 2017/5/12.
 */

public class ComentAdpater extends BaseApater<Coment> {
    public ComentAdpater(Context context) {
        super(context);
    }

    @Override
    public void convert(ViewHolder viewHolder, Coment coment) {
        viewHolder.setText(R.id.uesr_name_coment, coment.getComent_name());
        viewHolder.setText(R.id.coment_content, coment.getComent_content());
    }

    @Override
    protected int initLayoutId() {
        return R.layout.coment_item;
    }

}
