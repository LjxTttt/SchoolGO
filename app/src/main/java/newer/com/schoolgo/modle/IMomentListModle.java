package newer.com.schoolgo.modle;

import java.util.List;

import io.reactivex.Observable;
import newer.com.schoolgo.Api;
import newer.com.schoolgo.bean.Moment;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2017/5/5.
 */

public interface IMomentListModle {
    String BASE_URL = Api.BASE_URL;

    @GET("servlet/MomentInfoSev")
    Observable<List<Moment>> getListMoment();
}
