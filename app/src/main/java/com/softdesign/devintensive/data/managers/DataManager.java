package com.softdesign.devintensive.data.managers;

/**
 * Created by mvideo on 28.06.2016.
 */

/**
 * Класс сохранения и загрузк настроек
 */
public class DataManager {

    /**
     * Экземпляр класса
     */
    private static DataManager INSTANCE = null;

    /**
     * Доступ к пользовательским значениям
     */
    private PreferencesManager mPreferencesManager;

    /**
     * Конструктор
     */
    public DataManager() {
        this.mPreferencesManager = new PreferencesManager();
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
}
