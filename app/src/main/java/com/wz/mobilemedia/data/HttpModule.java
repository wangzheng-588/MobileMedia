package com.wz.mobilemedia.data;

import com.wz.mobilemedia.common.Contract;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wz on 17-5-22.
 */

public class HttpModule {

    private static HttpModule sHttpModule = new HttpModule();

    public static HttpModule getsInstance(){
        if (sHttpModule==null){
            synchronized (HttpModule.class){
                if (sHttpModule==null){
                    return sHttpModule ;
                }
            }
        }
        return sHttpModule;
    }

    private HttpModule() {
    }

    private  OkHttpClient getOkHttpClient(){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20,TimeUnit.SECONDS)
                .build();
    }

    private  Retrofit getRetrofit (){
        return new Retrofit.Builder()
                .baseUrl(Contract.MOIVE_URL)
                .client(getOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }


    public  ApiNetService getApiNetService(){
        return getRetrofit().create(ApiNetService.class);
    }
}
