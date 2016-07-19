package com.softdesign.devintensive.data.network;

import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

/**
 * Настройка Picasso builder для загрузки и скачивания фотографий
 */
public class PicassoCache {

    /**
     * Контекст приложения
     */
    private Context mContext;

    /**
     * Доступ к локальной копии Picasso
     */
    private Picasso mPicassoInstance;

    /**
     * Конструктор
     * @param context - контекст приложения
     */
    public PicassoCache(Context context) {
        this.mContext = context;

        /* Подключаем библиотечку */
        OkHttp3Downloader okHttp3Downloader = new OkHttp3Downloader(context, Integer.MAX_VALUE);
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(okHttp3Downloader);

        /* Устанавливаем Picasso */
        mPicassoInstance = builder.build();
        Picasso.setSingletonInstance(mPicassoInstance);
    }

    /**
     *  Публичный доступ к Picasso
     * @return доступ к Picasso
     */
    public Picasso getPicassoInstance() {
        if (mPicassoInstance == null){
            new PicassoCache(mContext);
            return mPicassoInstance;
        }
        return mPicassoInstance;
    }
}
