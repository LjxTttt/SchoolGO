package newer.com.schoolgo.modle;

import io.reactivex.Observable;
import newer.com.schoolgo.Api;
import newer.com.schoolgo.bean.User;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/5/5.
 */

public interface ILoginOrRegModle {
    String BASE_URL = Api.BASE_URL;

    @POST("servlet/AppRegServlet")
    @FormUrlEncoded
    Observable<User> loginOrReg(@Field("userName") String username,
                                @Field("password") String password,
                                @Field("type") int type);
}
