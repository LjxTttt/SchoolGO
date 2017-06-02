package newer.com.schoolgo.modle;

import io.reactivex.Observable;
import newer.com.schoolgo.Api;
import newer.com.schoolgo.bean.HttpResult;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/5/13.
 */

public interface IAddComentModle {
    String BASE_URL = Api.BASE_URL;

    @POST("servlet/AddComentSev")
    @FormUrlEncoded
    Observable<HttpResult> addComent(@Field("content") String content, @Field("tag") long tag,
                                     @Field("username") String username);
}
