package com.fangdean.minilife.data.remote;

import com.fangdean.minilife.model.ResponseBean;
import com.fangdean.minilife.model.User;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserService {

    @FormUrlEncoded
    @POST("user/user/login")
    Single<ResponseBean<User>> login(@Field("email") String email, @Field("password") String password);

}
