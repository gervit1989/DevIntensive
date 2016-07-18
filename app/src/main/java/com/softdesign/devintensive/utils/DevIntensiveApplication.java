package com.softdesign.devintensive.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by mvideo on 28.06.2016.
 */

/**
 * Переопределение приложения
 */
public class DevIntensiveApplication extends Application {

    /**
     * Доступ к пользовательским значениям
     */
    public static SharedPreferences sSharedPreferences;
    private static Context sContext = getContext();

    public static Context getContext() {
        return sContext;
    }

    /**
     * При создании
     */
    @Override
    public void onCreate() {
        super.onCreate();
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    }

    /**
     * Геттер для пользовательских значений
     * @return
     */
    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }
}
