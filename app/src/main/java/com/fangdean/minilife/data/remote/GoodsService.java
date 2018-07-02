package com.fangdean.minilife.data.remote;

import com.fangdean.minilife.model.Category;
import com.fangdean.minilife.model.Goods;
import com.fangdean.minilife.model.ResponseBean;
import com.fangdean.minilife.model.User;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GoodsService {
    @FormUrlEncoded
    @POST("/goods/category/upload")
    Call<ResponseBean<String>> uploadCategory(@Field("json") String json);

    @FormUrlEncoded
    @POST("/goods/goods/upload")
    Call<ResponseBean<String>> uploadGoods(@Field("json") String json);

    @FormUrlEncoded
    @POST("/goods/category/download")
    Single<ResponseBean<List<Category>>> downloadCategory(@Field("userId") String userId);

    @FormUrlEncoded
    @POST("/goods/goods/download")
    Single<ResponseBean<List<Goods>>> downloadGoods(@Field("userId") String userId);

}
