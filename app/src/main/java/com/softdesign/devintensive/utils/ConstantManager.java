package com.softdesign.devintensive.utils;

/**
 * Created by mvideo on 25.06.2016.
 */

/**
 * Хранилище констант
 */
public interface ConstantManager {
    String TAG_PREFIX = "DEV ";

    /**
     * Ключ редактирования
     */
    String EDIT_MODE_KEY = "EDIT_MODE_KEY";

    /**
     * Ключи для полей ввода
     */
    String USER_PHONE_KEY="USER_KEY_1";
    String USER_MAIL_KEY="USER_KEY_2";
    String USER_PROFILE_KEY="USER_KEY_3";
    String USER_GITHUB_KEY="USER_KEY_4";
    String USER_ABOUT_ME_KEY="USER_KEY_5";

    String USER_PHONE_NET_KEY="USER_KEY_1_NET";
    String USER_MAIL_NET_KEY="USER_KEY_2_NET";
    String USER_PROFILE_NET_KEY="USER_KEY_3_NET";
    String USER_GITHUB_NET_KEY="USER_KEY_4_NET";
    String USER_ABOUT_ME_NET_KEY="USER_KEY_5_NET";

    /**
     * Ключ для меню выбора источника фото
     */
    int LOAD_PROFILE_PHOTO = 1;
    int REQUEST_CAMERA_PHOTO = 99;
    int REQUEST_GALLERY_PHOTO = 88;

    /**
     * Сохраняемое фото
     */
    String USER_PROFILE_PHOTO_KEY ="USER_PROFILE_PHOTO_KEY";

    /**
     *Ключ для проверки разрешений на Android 6
     */
    int REQUEST_APP_SETTINGS = 111;

    /**
     * Ключ на запрос разрешений вручную
     */
    int REQUEST_PERMISSION_CODE =122;

    /**
     * Интенты на действия
     */

    /**
     *  Звонок 1
     */
    int REQUEST_CALL_CODE  = 101;

    /**
     * Просмотр ВК
     */
    int REQUEST_VK_CODE  = 103;

    /**
     * Просмотр ГитХаб
     */
    int REQUEST_GIT_CODE  = 105;

    /**
     * Послать письмо
     */
    int REQUEST_EMAIL_CODE  = 107;

    /**
     * Регистрационная информация
     */
    String REG_USER_LOGIN_KEY="USER_LOGIN";
    String REG_USER_ID_KEY="USER_ID";
    String REG_USER_LOGIN_TOKEN_KEY="USER_AUTH_LOGIN";
    String REG_USER_PASS_KEY="USER_PASS";


    String USER_RATING_KEY="USER_RATING_KEY";
    String USER_CODE_LINE_COUNT_KEY="USER_CODE_LINE_COUNT_KEY";
    String USER_PROJECT_COUNT_KEY="USER_PROJECT_COUNT_KEY";


    String USER_FIRST_NAME = "USER_FIRST_NAME";
    String USER_SECOND_NAME = "USER_SECOND_NAME";

    String USER_PHOTO_URL_KEY = "USER_PHOTO_URL_KEY";
    String USER_AVATAR_URL_KEY = "USER_AVATAR_URL_KEY";
	
	
    String PARCELABLE_KEY = "PARCELABLE_KEY";
}
