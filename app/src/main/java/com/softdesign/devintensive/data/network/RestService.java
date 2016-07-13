package com.softdesign.devintensive.data.network;

import com.softdesign.devintensive.data.network.res.UserModelResponse;
import com.softdesign.devintensive.data.network.req.UserLoginRequest;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by mvideo on 13.07.2016.
 */
public interface RestService {

    @POST("login")
    Call<UserModelResponse> loginUser(@Body UserLoginRequest req);

    /*
    @Multipart
    @POST("profile/edit")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file);
    */

    @Multipart
    @POST("user/{userId}/publicValues/profilePhoto")
    Call<ResponseBody> uploadPhoto(@Path("userId") String userId, @Part MultipartBody.Part file);

    @GET
    Call<ResponseBody> getImage(@Url String url);

}
