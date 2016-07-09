package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;

import java.util.List;

/**
 * Created by mvideo on 09.07.2016.
 */

/**
 * Экран авторизации
 */
public class LoginActivity  extends AppCompatActivity implements View.OnClickListener {

    /**
     * Кнопка 1.
     */
    Button mButtonLogin;

    /**
     * Координатор
     */
    private CoordinatorLayout mCoordinatorLayout;

    /**
     * Поле редактирования логина
     */
    EditText mLoginEdit;

    /**
     * Поле редактирования пасса
     */
    EditText mPassEdit;

    /**
     * Пользовательские настройки
     */
    private DataManager mDataManager;
    /**
     * На создание активити
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //- Присвоение разметки активности
        setContentView(R.layout.activity_login);

        /**
         * Хранилище данных пользователя
         */
        mDataManager = DataManager.getINSTANCE();

        //- Инициалзация кнопки
        mButtonLogin = (Button)findViewById(R.id.login_btn);
        mLoginEdit = (EditText) findViewById(R.id.login_edit);
        mPassEdit = (EditText)findViewById(R.id.pass_edit);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.login_coordinator);

        //- Назначение события нажатия
        mButtonLogin.setOnClickListener(this);

        mDataManager.getPreferencesManager().loadRegistry();
    }

    /**
     * Обработка нажатий
     * @param v- элемент, на который осуществлено нажатие
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                doGotoMainActivity();
                break;
        }
    }

    /**
     * Переход на главную Activity
     */
    private void doGotoMainActivity() {
        String s1 = mLoginEdit.getText().toString(), s2 = mPassEdit.getText().toString();
        if (s2 == null || s1==null){
            Snackbar.make(mCoordinatorLayout, "Не заполнена информация", Snackbar.LENGTH_LONG).show();
        }
        else {
            List<String> sData = mDataManager.getPreferencesManager().loadRegistry();
            if (sData.get(1).equals("null")){
                mDataManager.getPreferencesManager().saveRegistry(s1, s2);
                Snackbar.make(mCoordinatorLayout, "Регистрация успешно", Snackbar.LENGTH_LONG).show();
            }
            else {
                if (s1.equals(sData.get(0)) &&
                        s2.equals(sData.get(1))){
                    Snackbar.make(mCoordinatorLayout, "Вход успешен", Snackbar.LENGTH_LONG).show();
                }
                else {
                    Snackbar.make(mCoordinatorLayout, "Вход неуспешен", Snackbar.LENGTH_LONG).show();
                    return;
                }
            }

        }

        Intent intentGotoMainActivity = new Intent(this, MainActivity.class);
        startActivity(intentGotoMainActivity);
    }
}
