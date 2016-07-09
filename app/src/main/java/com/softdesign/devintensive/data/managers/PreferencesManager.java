package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;
import android.net.Uri;

import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevIntensiveApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, отвечающий за сохранение/загрузку пользовательских данных
 */
public class PreferencesManager {

    /**
     * доступ к значениям
     */
    private SharedPreferences mSharedPreferences;

    /**
     * Сохраняемые пользовательские значения
     */
    private static final String[] USER_FIELDS = {ConstantManager.USER_PHONE_KEY, ConstantManager.USER_MAIL_KEY, ConstantManager.USER_PROFILE_KEY, ConstantManager.USER_GITHUB_KEY, ConstantManager.USER_ABOUT_ME_KEY};

    /**
     * Конструктор
     */
    public PreferencesManager() {

        this.mSharedPreferences = DevIntensiveApplication.getSharedPreferences();
    }

    /**
     * Сохранение пользовательских даных - поля ввода
     * @param userFileds
     */
    public void saveUserData(List<String> userFileds){
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        for (int i = 0; i < USER_FIELDS.length; i++)
        {
            editor.putString(USER_FIELDS[i], userFileds.get(i));
        }
        editor.apply();

    }

    /**
     * Загрузка пользовательских данных
     * @return
     */
    public List<String> loadUserData(){
        List<String> userFields = new ArrayList<>();
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_PHONE_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_MAIL_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_PROFILE_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_GITHUB_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_ABOUT_ME_KEY, "null"));
        return userFields;
    }

    /**
     * Сохраняем путь к сохраненному фото
     * @param uri - путь к фото
     */
    public void saveUserPhoto(Uri uri){
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putString(ConstantManager.USER_PROFILE_PHOTO_KEY, uri.toString());
        editor.apply();
    }

    /**
     * Загрузка сохраненного фото
     * @return путь к фото
     */
    public Uri loadUserPhoto(){
        return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_PROFILE_PHOTO_KEY, "android.resource://com.softdesign.devintensive/drawable/user_data"));
    }
}
