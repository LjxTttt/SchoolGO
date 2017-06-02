package newer.com.schoolgo.presenter;

/**
 * Created by Administrator on 2017/3/8.
 */

public class BasePresenter<V> {
    V mView;

    public void attachView(V view) {
        mView = view;
    }
}
