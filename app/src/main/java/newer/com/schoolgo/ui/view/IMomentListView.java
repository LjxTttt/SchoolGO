package newer.com.schoolgo.ui.view;

import java.util.List;

import newer.com.schoolgo.bean.Moment;

/**
 * Created by Administrator on 2017/5/5.
 */

public interface IMomentListView extends IBaseView {
    void onListSucess(List<Moment> moment);

    void addComentSucess();
}
