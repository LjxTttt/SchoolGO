package newer.com.schoolgo.ui.view;

import java.util.List;

import newer.com.schoolgo.bean.School;

/**
 * Created by Administrator on 2017/4/24.
 */

public interface SchoolItemView extends IBaseView {
    void onSuccess(List<School> schools);
}
