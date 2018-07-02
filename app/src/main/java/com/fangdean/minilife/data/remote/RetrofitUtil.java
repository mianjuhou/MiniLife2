package com.fangdean.minilife.data.remote;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

/**
 * Created by fda on 2017/7/14.
 */

public class RetrofitUtil {
    public static final String BASE_URL = "http://169.254.145.168:8888/";

    private static Retrofit retrofit;
    private static Retrofit longRetrofit;

    public static Retrofit createRetrofit(OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = createRetrofit(OkHttpUtil.getOkHttpClient());
        }
        return retrofit;
    }

    public static Retrofit getLongRetrofit() {
        if (longRetrofit == null) {
            longRetrofit = createRetrofit(OkHttpUtil.getLongOkHttpClient());
        }
        return longRetrofit;
    }

}
