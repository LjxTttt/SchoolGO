package newer.com.schoolgo.modle;

import java.util.List;

import io.reactivex.Observable;
import newer.com.schoolgo.Api;
import newer.com.schoolgo.bean.HttpResult;
import newer.com.schoolgo.bean.School;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/3/8.
 */

public interface ISchoolItemModle {
    String BASE_URL = Api.BASE_URL;

    @GET("servlet/SchoolNoticeServlet")
    Observable<HttpResult<List<School>>> getSchoolItem(@Query("typeId") int typeId,
                                                       @Query("pageNums") int pageNums,
                                                       @Query("pageNow") int pageNow);
}
