package com.softdesign.devintensive.ui.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.ConstantManager;

/**
 * Created by mvideo on 28.06.2016.
 */

/**
 * Базовая активность.основные тестовые функции
 */
public class BaseActivity extends AppCompatActivity{

    //- Тег отслеживания
    private static final String TAG= ConstantManager.TAG_PREFIX + "BaseActivity";

    //- Диалог прогресса
    protected ProgressDialog mProgressDialog;

    /**
     * Показать диалог прогресса
     */
    public void showProgress(){
        if (mProgressDialog == null){
            mProgressDialog = new ProgressDialog(this, R.style.custom_dialog);
            mProgressDialog.setCancelable(false);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        mProgressDialog.show();
        mProgressDialog.setContentView(R.layout.progress_splash);
    }

    /**
     * Скрыть диалог прогресса
     */
    public void hideProgress(){
        if (mProgressDialog != null){
            if (mProgressDialog.isShowing()){
                mProgressDialog.hide();
            }
        }
    }

    /**
     * Показать ошибку во всплывающем сообщении
     * @param message   - сообщение
     * @param error     - Ошибка
     */
    public void showError( String message, Exception error){
        showToast(message);
        Log.e(TAG, String.valueOf(error));
    }

    /**
     * Показать всплывающее сообщение
     * @param message   - сообщение
     */
    public void showToast( String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


}
