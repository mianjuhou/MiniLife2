package com.fangdean.minilife.data.remote;

import retrofit2.Retrofit;

/**
 * Created by fda on 2017/7/14.
 */

public class Services {
    public static <S> S createService(Class<S> serviceClass) {
        return RetrofitUtil.getRetrofit().create(serviceClass);
    }

    public static <S> S createService(Class<S> serviceClass, Retrofit retrofit) {
        return retrofit.create(serviceClass);
    }
}
