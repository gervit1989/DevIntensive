package com.softdesign.devintensive.data.managers;

import android.content.Context;
import android.util.Log;

import com.softdesign.devintensive.data.network.PicassoCache;
import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.req.UserLoginRequest;
import com.softdesign.devintensive.data.network.res.UserListResponse;
import com.softdesign.devintensive.data.network.res.UserModelResponse;
import com.softdesign.devintensive.data.storage.models.DaoSession;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevIntensiveApplication;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class DataManager {
    private static final String TAG = ConstantManager.TAG_PREFIX + "DataManager";

    private Picasso mPicasso;
    private RestService mRestService;
    /**
     * Сессия Dao
     */
    private DaoSession mDaoSession;
    /**
     * Экземпляр класса
     */
    private static DataManager INSTANCE = null;

    /**
     * Доступ к пользовательским значениям
     */
    private PreferencesManager mPreferencesManager;

    /**
     * Контекст приложения
     */
    private Context mContext;

    /**
     * Конструктор
     */
    public DataManager() {
        //- Сохранение настроек приложения
        this.mPreferencesManager = new PreferencesManager();

        //- Контекст приложения
        this.mContext = DevIntensiveApplication.getContext();

        //- Шаблоны передачи данных
        this.mRestService = ServiceGenerator.createService(RestService.class);

        //- Настройка Picasso для локального хранения
        this.mPicasso = new PicassoCache(this.mContext).getPicassoInstance();

        //- Сессия Dao
        this.mDaoSession = DevIntensiveApplication.getDaoSession();
    }


    /**
     * Доступ к Picasso
     * @return возвращает Picasso
     */
    public Picasso getPicasso() {
        return mPicasso;
    }

    /**
     * Получение единственного экземпляра
     * @return Менеджер
     */
    public static DataManager getINSTANCE(){
        if (INSTANCE==null){
            INSTANCE = new DataManager();

        }
        return  INSTANCE;
    }

    /**
     * Доступ к пользовательским значениям
     * @return Менеджер пользовательских значений
     */
    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }

    /**
     * Подключение к системе
     * @param userLoginReq - хранилище данных авторизации
     * @return  возвращает результат авторизации
     */
    public Call<UserModelResponse> loginUser(UserLoginRequest userLoginReq) {
        return mRestService.loginUser(userLoginReq);
    }

    /**
     * Контекст приложения
     * @return контекст
     */
    public Context getContext() {
        return mContext;
    }


    /**
     * Получить изображение пользователя
     * @param url - адрес
     * @return Результат запроса
     */
    public Call<ResponseBody> getImage(String url) {
        return mRestService.getImage(url);
    }


    /**
     * Отправить фото
     * @param userId - id пользователя
     * @param photoFile - фото файл
     * @return результат отправки
     */
    public Call<ResponseBody> uploadPhoto(String userId, File photoFile) {
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), photoFile);
        MultipartBody.Part bodyPart =
                MultipartBody.Part.createFormData("photo", photoFile.getName(), requestBody);
        return mRestService.uploadPhoto(userId, bodyPart);
    }

    /**
     * Получить список пользователей
     * @return список пользователей
     */
    public Call<UserListResponse> getUserListFromNetwork() {
        return mRestService.getUserList();
    }

    //endregion

    //region ============= Database =============

    /**
     * Получить список пользователей из БД
     * @return список пользователей из БД
     */
    public List<User> getUserListFromDb() {
        List<User> userList = new ArrayList<>();
        try{
            userList = mDaoSession.queryBuilder(User.class)
                    .where(UserDao.Properties.Codelines.ge(0))
                    .orderDesc(UserDao.Properties.Codelines)
                    .build()
                    .list();
        }
        catch (Exception e){
            e.printStackTrace();

        }
        return userList;
    }

    /**
     * Сессия Dao
     */
    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public List<User> getUserListByName(String query) {
        Log.d(TAG,"getUserListByName");
        List<User> userList=new ArrayList<>();
        userList=mDaoSession.queryBuilder(User.class)
        .where(UserDao.Properties.Rating.gt(0),UserDao.Properties.SearchName.like("%"+query.toUpperCase()+"%"))
        .orderDesc(UserDao.Properties.Codelines)
        .build()
        .list();
        return userList;
    }

    //endregion
}
