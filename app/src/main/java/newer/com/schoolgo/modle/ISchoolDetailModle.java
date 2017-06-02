package newer.com.schoolgo.modle;

import io.reactivex.Observable;
import newer.com.schoolgo.Api;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2017/4/24.
 */

public interface ISchoolDetailModle {
    String BASE_URL = Api.HNGCZY_URL;

    @GET
    Observable<String> getSchoolDetailData(@Url String content);
}
