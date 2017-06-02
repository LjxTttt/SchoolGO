package newer.com.schoolgo.modle;

import java.util.ArrayList;

import io.reactivex.Observable;
import newer.com.schoolgo.Api;
import newer.com.schoolgo.bean.HttpResult;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/5/3.
 */

public interface IMomentAddModle {
    String BASE_URL = Api.BASE_URL;

    @POST("servlet/MomentAddSev")
    @FormUrlEncoded
    Observable<HttpResult> add(@Field("imgSrc") ArrayList<String> arrayList,
                               @Field("userId") long userId,
                               @Field("content") String content,
                               @Field("tag") long tag);
}
