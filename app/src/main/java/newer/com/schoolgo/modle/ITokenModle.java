package newer.com.schoolgo.modle;

import io.reactivex.Observable;
import newer.com.schoolgo.Api;
import newer.com.schoolgo.bean.TokenAcess;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2017/5/3.
 */

public interface ITokenModle {
    String BASE_URL = Api.BASE_URL;

    @GET("servlet/UploadToken")
    Observable<TokenAcess> getSchoolDetailData();
}
