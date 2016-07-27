package com.softdesign.devintensive.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.inspector.protocol.module.Database;
import com.softdesign.devintensive.data.storage.models.DaoMaster;
import com.softdesign.devintensive.data.storage.models.DaoSession;

/**
 * Переопределение приложения
 */
public class DevIntensiveApplication extends Application {
    private static final String TAG = ConstantManager.TAG_PREFIX + "DevintensiveApp";

    /**
     * Доступ к пользовательским значениям
     */
    public static SharedPreferences sSharedPreferences;

    /**
     * Контекст приложения
     */
    private static Context sContext;

    /**
     * Библиотечка Dao
     */
    private static DaoSession sDaoSession;


    /**
     * Публичный доступ к контексту приложения
     * @return контекст
     */
    public static Context getContext() {
        return sContext;
    }

    /**
     * При создании
     */
    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
        //- Получаем контекст
        sContext = getApplicationContext();

        //- Пользовательские значения
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //- Создаем сессию Dao
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "devintensive-db");

        //- База данных
        org.greenrobot.greendao.database.Database db = helper.getWritableDb();

        //- Сессия Дао
        sDaoSession = new DaoMaster(db).newSession();

        Stetho.initializeWithDefaults(this);
    }

    /***
     * Доступ к сессии Dao
     * @return сессии Dao
     */
    public static DaoSession getDaoSession() {
        return sDaoSession;
    }

    /**
     * Геттер для пользовательских значений
     * @return пользовательские значения
     */
    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }
}
