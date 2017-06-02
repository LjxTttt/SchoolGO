package newer.com.schoolgo.rx;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/3/22.
 */

public abstract class RxObserver<T> implements Observer<T> {

    @Override
    public void onNext(T t) {
        _OnNext(t);
    }

    @Override
    public void onError(Throwable e) {
        if (e.getMessage().equals("The mapper function returned a null value.")) {
            //ToastUtil.showToast(SchoolGoApplication.getContext(),"没有更多数据了.");
            _OnError(e);
        } else {
            //ToastUtil.showToast(SchoolGoApplication.getContext(),"网络连接出错，请检查网络");
            _OnError(e);
        }
    }

    @Override
    public void onComplete() {

    }

    public abstract void _OnNext(T t);

    public abstract void _OnError(Throwable e);

    @Override
    public void onSubscribe(Disposable d) {
    }

}
