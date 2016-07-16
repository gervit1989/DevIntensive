package com.softdesign.devintensive.data.managers;

/**
 * Created by mvideo on 28.06.2016.
 */

import android.content.Context;

import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.req.UserLoginRequest;
import com.softdesign.devintensive.data.network.res.UserListResponse;
import com.softdesign.devintensive.data.network.res.UserModelResponse;
import com.softdesign.devintensive.utils.DevIntensiveApplication;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Part;

public class DataManager {

    private RestService mRestService;
    /**
     * Экземпляр класса
     */
    private static DataManager INSTANCE = null;

    /**
     * Доступ к пользовательским значениям
     */
    private PreferencesManager mPreferencesManager;

    private Context mContext;

    /**
     * Конструктор
     */
    public DataManager() {
        this.mPreferencesManager = new PreferencesManager();
        this.mContext = DevIntensiveApplication.getContext();
        this.mRestService = ServiceGenerator.createService(RestService.class);
    }

    /**
     * Получение единственного экземпляра
     * @return
     */
    public static DataManager getINSTANCE(){
        if (INSTANCE==null){
            INSTANCE = new DataManager();

        }
        return  INSTANCE;
    }

    /**
     * Доступ к пользовательским значениям
     * @return
     */
    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }

    public Call<UserModelResponse> loginUser(UserLoginRequest userLoginReq) {
        return mRestService.loginUser(userLoginReq);
    }

    public Context getContext() {
        return mContext;
    }

    public Call<ResponseBody> getImage(String url) {
        return mRestService.getImage(url);
    }

    public Call<ResponseBody> uploadPhoto(String userId, File photoFile) {
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), photoFile);
        MultipartBody.Part bodyPart =
                MultipartBody.Part.createFormData("photo", photoFile.getName(), requestBody);
        return mRestService.uploadPhoto(userId, bodyPart);
    }

    public Call<UserListResponse> getUserList() {
        return mRestService.getUserList();
    }

    //endregion

    //region ============= Database =============

    //endregion
}
