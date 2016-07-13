package com.softdesign.devintensive.data.network;

import com.softdesign.devintensive.data.network.interceptors.HeaderInterceptor;
import com.softdesign.devintensive.utils.AppConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mvideo on 12.07.2016.
 */
public class ServiceGenerator {

    public static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static Retrofit.Builder sBuilder = new Retrofit.Builder()
                                                    .baseUrl(AppConfig.BASE_URL)
                                                    .addConverterFactory(GsonConverterFactory.create());
    public static <S> S createService(Class<S> serviceClass){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.addInterceptor(new HeaderInterceptor()); // добавление заголовков (токен, id-пользователя, User Agent)
        httpClient.addInterceptor(logging);
        Retrofit retrofit = sBuilder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);

    }
}
