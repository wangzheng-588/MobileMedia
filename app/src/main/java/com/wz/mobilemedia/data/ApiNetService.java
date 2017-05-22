package com.wz.mobilemedia.data;

import com.wz.mobilemedia.bean.BaseMoive;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by wz on 17-5-18.
 */

public interface ApiNetService {

    @GET("PageSubArea/TrailerList.api")
    Observable<BaseMoive> getNetMoive();

}
