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
    String REG_USER_PASS_KEY="USER_PASS";

}
