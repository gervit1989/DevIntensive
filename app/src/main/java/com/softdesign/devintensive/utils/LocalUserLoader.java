package com.softdesign.devintensive.utils;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.softdesign.devintensive.data.storage.models.MUser;
import com.softdesign.devintensive.data.storage.models.MUserDao;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mvideo on 21.07.2016.
 */
public class LocalUserLoader extends AsyncTaskLoader<List<MUser>> {
    private static final String TAG = ConstantManager.TAG_PREFIX + " LocalUserLoader";

    public LocalUserLoader(Context context) {
        super(context);
    }

    @Override
    public List<MUser> loadInBackground() {
        Log.d(TAG,"loadInBackground");

        List<MUser> userList = new ArrayList<>();
        try {
            userList = DevIntensiveApplication.getDaoSession().queryBuilder(MUser.class)
                    .where(MUserDao.Properties.Codelines.gt(0))
                    .orderDesc(MUserDao.Properties.Codelines)
                    .build()
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }
}
