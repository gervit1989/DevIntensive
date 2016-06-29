package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;

import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevIntensiveApplication;

import java.util.ArrayList;
import java.util.List;

public class PreferencesManager {

    private SharedPreferences mSharedPreferences;

    private static final String[] USER_FIELDS = {ConstantManager.USER_PHONE_KEY, ConstantManager.USER_MAIL_KEY, ConstantManager.USER_PROFILE_KEY, ConstantManager.USER_GITHUB_KEY, ConstantManager.USER_ABOUT_ME_KEY};

    public PreferencesManager() {

        this.mSharedPreferences = DevIntensiveApplication.getSharedPreferences();
    }

    public void saveUserData(List<String> userFileds){
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        for (int i = 0; i < USER_FIELDS.length; i++)
        {
            editor.putString(USER_FIELDS[i], userFileds.get(i));
        }
        editor.apply();

    }

    public List<String> loadUserData(){
        List<String> userFields = new ArrayList<>();
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_PHONE_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_MAIL_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_PROFILE_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_GITHUB_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_ABOUT_ME_KEY, "null"));
        return userFields;
    }
}
