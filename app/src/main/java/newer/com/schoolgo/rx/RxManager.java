package newer.com.schoolgo.rx;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import newer.com.schoolgo.bean.HttpResult;

/**
 * Created by Administrator on 2017/3/22.
 */
public class RxManager {
    private final static RxManager ourInstance = new RxManager();

    public static RxManager getInstance() {
        return ourInstance;
    }

    public <T> void doSubscribe(Observable<T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public <T> void doSubscribeByResult(Observable<HttpResult<T>> observable, Observer<T> observer) {
        observable
                .map(new Function<HttpResult<T>, T>() {
                    @Override
                    public T apply(@NonNull HttpResult<T> tHttpResult) throws Exception {
                        if (tHttpResult.getMessage().equals("200")) {
                            return tHttpResult.getData();
                        } else {
                            return null;
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
