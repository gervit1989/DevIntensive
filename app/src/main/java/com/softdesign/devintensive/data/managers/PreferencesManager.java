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
    private static final String[] USER_FIELDS = { ConstantManager.USER_PHONE_KEY,
                                                  ConstantManager.USER_MAIL_KEY,
                                                  ConstantManager.USER_PROFILE_KEY,
                                                  ConstantManager.USER_GITHUB_KEY,
                                                  ConstantManager.USER_ABOUT_ME_KEY};


    private static final String[] USER_NAMES = {
                                                    ConstantManager.USER_FIRST_NAME,
                                                    ConstantManager.USER_SECOND_NAME,
    };
    private static final String[] USER_VALUES = {   ConstantManager.USER_RATING_KEY,
                                                    ConstantManager.USER_CODE_LINE_COUNT_KEY,
                                                    ConstantManager.USER_PROJECT_COUNT_KEY};

    /**
     * Регистрация
     */
    private static final String[] REG_USER_FIELDS = {ConstantManager.REG_USER_LOGIN_KEY,
                                                     ConstantManager.REG_USER_PASS_KEY};

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

        for (int i = 0; i < USER_FIELDS.length; i++)    {
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
        return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_PROFILE_PHOTO_KEY, 
		        "android.resource://com.softdesign.devintensive/drawable/user_data"));
    }



    public void saveAuthToken(String authToken){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.REG_USER_LOGIN_TOKEN_KEY,authToken);
        editor.apply();
    }

    public String  getAuthtoken(){
        return mSharedPreferences.getString(ConstantManager.REG_USER_LOGIN_TOKEN_KEY, "null");
    }

    public void saveUserID(String userID){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.REG_USER_ID_KEY,userID);
        editor.apply();
    }

    public String  getUserID(){
        return mSharedPreferences.getString(ConstantManager.REG_USER_ID_KEY, "null");
    }


    public void saveUserProfileFields(int[] userValues){

        SharedPreferences.Editor editor = mSharedPreferences.edit();

        for (int i = 0; i < USER_VALUES.length; i++)
        {
            editor.putString(USER_VALUES[i], String.valueOf(userValues[i]));
        }
        editor.apply();
    }
    public List<String> loadUserProfileFields(){
        List<String> userFields = new ArrayList<>();
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_RATING_KEY, "0"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_CODE_LINE_COUNT_KEY, "0"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_PROJECT_COUNT_KEY, "0"));
        return userFields;
    }
	/**
     * Сохраняет идентификатор пользователя в Shared Preferences
     * @param userId токен
     */
    public void saveUserId(String userId) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.REG_USER_ID_KEY, userId);
        editor.apply();
    }

    /**
     * Считывает идентификатор пользователя из Shared Preferences
     * @return
     */
    public String getUserId() {
        return mSharedPreferences.getString(ConstantManager.REG_USER_ID_KEY, "null");
    }
/**
     * Считывает URI аватара пользователя из Shared Preferences
     * @return URI аватара
     */
    public Uri loadUserAvatar() {
        return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_AVATAR_URL_KEY,
                "android.resource://com.softdesign.devintensive/drawable/no_avatar"));
    }
    /**
     * Сохраняем путь к сохраненному фото
     * @param uri - путь к фото
     */
    public void saveUserAvatar(Uri uri){
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putString(ConstantManager.USER_AVATAR_URL_KEY, uri.toString());
        editor.apply();
    }

    /**
     * Сохраняет данные пользователя (рейтинг, строки кода, проекты) в Shared Preferences
     * @param userValues массив, содержащий данные
     */
    public void saveUserProfileValues(int[] userValues) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        for (int i = 0; i < USER_VALUES.length; i++) {
            editor.putString(USER_VALUES[i], String.valueOf(userValues[i]));
        }

        editor.apply();
    }

    /**
     * Считывает данные пользователя (рейтинг, строки кода, проекты) из Shared Preferences
     * @return списочный массив, содержащий данные
     */
    public List<String> loadUserProfileValues() {
        List<String> userValues = new ArrayList<>();
        userValues.add(mSharedPreferences.getString(ConstantManager.USER_RATING_KEY, "0"));
        userValues.add(mSharedPreferences.getString(ConstantManager.USER_CODE_LINE_COUNT_KEY, "0"));
        userValues.add(mSharedPreferences.getString(ConstantManager.USER_PROJECT_COUNT_KEY, "0"));
        return userValues;
    }

    /**
     * Сохраняет имя и фамилию пользователя в Shared Preferences
     * @param userNames массив, содержащий имя и фамилию
     */
    public void saveUserName(String[] userNames) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        for (int i = 0; i < USER_NAMES.length; i++) {
            editor.putString(USER_NAMES[i], userNames[i]);
        }

        editor.apply();
    }

    /**
     * Считывает имя и фамилию пользователя из Shared Preferences
     * @return списочный массив, содержащий имя и фамилию
     */
    public List<String> loadUserName() {
        List<String> userNames = new ArrayList<>();
        userNames.add(mSharedPreferences.getString(ConstantManager.USER_FIRST_NAME, " "));
        userNames.add(mSharedPreferences.getString(ConstantManager.USER_SECOND_NAME, " "));
        return userNames;
    }
}
