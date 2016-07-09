package com.softdesign.devintensive.ui.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.RoundedAvatarDrawable;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.jar.Manifest;

/**
 * Главная активити
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    /**
     * Тег для отслеживания сообщений активити
     */
    private static final String TAG = ConstantManager.TAG_PREFIX + "MainActivity";

    /**
     * Элементы верстки
     */
    private ImageView user_avatar;
    private DataManager mDataManager;
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;
    private CoordinatorLayout mCoordinatorLayout;
    private RelativeLayout mProfilePlaceholderLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout mAppBarLayout;
    private ImageView mPlaceholderImage;
    /**
     * Режим редактирования
     */
    private int mCurrentEditMode = 0;

    /**
     * Плавающая кнопка для установк флага редактирования
     */
    private FloatingActionButton mFab;

    /**
     * Поля ввода информации
     */
    private EditText mUserPhone, mUserMail, mUserVk, mUserGit, mUserBio;

    /**
     * Список всех полей ввода
     */
    private List<EditText> mUserInfoViews;

    /**
     * Параметры отображения верхнего AppBarLayout
     */
    private AppBarLayout.LayoutParams mAppBarLayoutParams = null;

    private File mPhotoFile = null;

    //- Путь к изображению
    private Uri mSelectedImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Устанавливаем активити на запуск
         */
        setContentView(R.layout.activity_main);

        /**
         * Логирование для дебаг
         */
        Log.d(TAG, "onCreate");

        /**
         * Хранилище данных пользователя
         */
        mDataManager = DataManager.getINSTANCE();

        /**
         * Элементы верстки
         */
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator);
        mProfilePlaceholderLayout = (RelativeLayout) findViewById(R.id.profile_placeholder);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        mDrawer = (DrawerLayout) findViewById(R.id.navigator_drawer);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mUserPhone = (EditText) findViewById(R.id.edit_phone);
        mUserMail = (EditText) findViewById(R.id.edit_email);
        mUserVk = (EditText) findViewById(R.id.edit_vk);
        mUserGit = (EditText) findViewById(R.id.edit_git);
        mUserBio = (EditText) findViewById(R.id.edit_me);
        mPlaceholderImage = (ImageView) findViewById(R.id.user_foto);

        /**
         * Поля ввода
         */
        mUserInfoViews = new ArrayList<>();
        mUserInfoViews.add(mUserPhone);
        mUserInfoViews.add(mUserMail);
        mUserInfoViews.add(mUserVk);
        mUserInfoViews.add(mUserGit);
        mUserInfoViews.add(mUserBio);

        /**
         * Отрисовка элементов верстки
         */
        setupToolbar();     //- Тулбар
        setupDrawer();      //- Выплывающее меню
        LoadUserInfo();     //- Загрузка пользовательской информации
        makeRoundAvatar();  //- Скругление аватары
        insertProfileImage(mDataManager.getPreferencesManager().loadUserPhoto()); //- Загружаем сохраненное фото

        /**
         * Обработка нажатий
         */
        mFab.setOnClickListener(this);
        mProfilePlaceholderLayout.setOnClickListener(this);

        /**
         * Запуск приложения
         */
        if (savedInstanceState == null) {

            List<String> userData = mDataManager.getPreferencesManager().loadUserData();
            for (int i = 0; i < userData.size(); i++) {
                String s = userData.get(i);
                if (s.equals("null")){
                    switch (i)
                    {
                        case 0:
                            mUserInfoViews.get(i).setText(R.string.text_phone);
                            break;
                        case 1:
                            mUserInfoViews.get(i).setText(R.string.text_email);
                            break;
                        case 2:
                            mUserInfoViews.get(i).setText(R.string.text_vk);
                            break;
                        case 3:
                            mUserInfoViews.get(i).setText(R.string.text_github);
                            break;
                        case 4:
                            mUserInfoViews.get(i).setText(R.string.text_about_me);
                            break;

                    }
                }

            }
            mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));
//            активити запускается впервые
        } else {
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
            changeEditMode(mCurrentEditMode);
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        SaveUserInfo();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.call_progress: {
                showProgress();
                //runWithDelay();
                break;
            }
            case R.id.hide_progress: {
                //hideProgress();
                break;
            }
            case R.id.profile_placeholder:
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);

                break;
            case R.id.fab:
                if (mCurrentEditMode == 0) {
                    changeEditMode(1);
                    mCurrentEditMode = 1;
                } else {
                    changeEditMode(0);
                    mCurrentEditMode = 0;
                }
                break;

        }
    }

    /**
     *  Получене результата из другой Activity
     * @param requestCode   - запрос
     * @param resultCode    - ответ
     * @param data          - интент по запросу
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case ConstantManager.REQUEST_CAMERA_PHOTO:
                if (resultCode == RESULT_OK && mPhotoFile !=null){
                    mSelectedImage = Uri.fromFile(mPhotoFile);

                    insertProfileImage(mSelectedImage);
                }
                break;
            case ConstantManager.REQUEST_GALLERY_PHOTO:
                if (resultCode == RESULT_OK && data !=null){
                    mSelectedImage = data.getData();

                    insertProfileImage(mSelectedImage);
                }
                break;
        }
        // super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * В зависимости от режма устанавливаем параметры элементов.
     * @param mode - режим редактрования / просмотра
     */
    private void changeEditMode(int mode) {

        if (mode == 1) {
            mFab.setImageResource(R.drawable.ic_done_black_24dp);
            for (EditText userValue : mUserInfoViews) {
                userValue.setEnabled(true);
                userValue.setFocusable(true);
                userValue.setFocusableInTouchMode(true);
            }
            showProfilePlaceholder();
            lockToolbar();

            mCollapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        } else {
            mFab.setImageResource(R.drawable.ic_create_black_24dp);
            for (EditText userValue : mUserInfoViews) {
                userValue.setEnabled(false);
                userValue.setFocusable(false);
                userValue.setFocusableInTouchMode(false);
                SaveUserInfo();
            }
            hideProfilePlaceholder();
            unlockToolar();
            mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));

        }
    }

    private void LoadUserInfo() {
        List<String> userData = mDataManager.getPreferencesManager().loadUserData();
        for (int i = 0; i < userData.size(); i++) {
            mUserInfoViews.get(i).setText(userData.get(i));
        }
    }

    private void SaveUserInfo() {
        List<String> userData = new ArrayList<>();
        for (EditText userFieldView : mUserInfoViews) {
            userData.add(userFieldView.getText().toString());
        }
        mDataManager.getPreferencesManager().saveUserData(userData);
    }

    private void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerVisible(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Установка нового тулбара взамен стандартного
     */
    private  void setupToolbar() {
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
		ActionBar actionBar =  getSupportActionBar();

        /**
         * Параметры AppBarLayout, для последующего изменения скроллинга
         */
        mAppBarLayoutParams = (AppBarLayout.LayoutParams) mCollapsingToolbarLayout.getLayoutParams();
        if (actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Отрсовка NavDrawer - меню, как в PlayMarket
     */
    private void setupDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigator);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackBar(item.getTitle().toString());
                item.setChecked(true);
                mDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    /**
     * Создание скругленного аватара для картинки на NavigationDrawer
     */
    private void makeRoundAvatar() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigator);
        user_avatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.user_avatar);
        Bitmap btMap = BitmapFactory.decodeResource(getResources(), R.drawable.profile);
        RoundedAvatarDrawable bt = new RoundedAvatarDrawable(btMap);
        user_avatar.setImageDrawable(bt);
    }

    /**
     * Загрузка фото из камеры
     */
    private void loadPhotoFromCamera(){
        //- Проверяем разрешение на доступ к камере
        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)*/
                {
            //- Интент камеры
            Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            try {
                //- Ищем с камеры в директории нужную
                mPhotoFile = createPhotoImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                //- Если не нашли сообщаем пользователю
                showSnackBar("Картинка не найдена");
            }
            //- Проверяем файл картинки
            if (mPhotoFile != null){
                //-  Передать фото дальше
                takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));

                //- Вызываем камеру
                startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PHOTO);
            }
        }
        /*else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, ConstantManager.CAMERA_REQUEST_PERMISSION_CODE);
        }*/
    }
/**
 +     * Проверка разрешений
 +     * @param requestCode
 +     * @param permissions
 +     * @param grantResults
 +     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ConstantManager.CAMERA_REQUEST_PERMISSION_CODE && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
        {
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }
    /**
     * Загрузка фото из галереи
     */
    private void loadPhotoFromGallery(){
        //- Интент галереи
        Intent takeGalleryPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        //- Тип изображения
        takeGalleryPhotoIntent.setType("image/*");

        //- Запускаемся
        startActivityForResult(Intent.createChooser(takeGalleryPhotoIntent, getString(R.string.profile_photo_gallery_caption)), ConstantManager.REQUEST_GALLERY_PHOTO);
    }

    /**
     * Скрыть элемент редактированя фото профиля за фото
     */
    private void hideProfilePlaceholder(){
        mProfilePlaceholderLayout.setVisibility(View.GONE);

    }

    /**
     * показывать элемент редактированя фото профиля
     */
    private void showProfilePlaceholder(){
        mProfilePlaceholderLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Заблокировать верхний тулбар от сворачивания
     */
    private void lockToolbar(){

        /**
         * При переходе в режим редактирования разворот происходит с анимацией
         */
        mAppBarLayout.setExpanded(true, true);
        /**
         * отключаем скролл, следовательно не будет схлопываться
         */
        mAppBarLayoutParams.setScrollFlags(0);

        /**
         * Устанавливаем свойства
         */
        mCollapsingToolbarLayout.setLayoutParams(mAppBarLayoutParams);
    }

    /**
     * Разблокировать верхний тулбар
     */
    private void unlockToolar(){
        /**
         * Включаем скролл  обратно
         */
        mAppBarLayoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);

        /**
         * Устанавливаем свойства
         */
        mCollapsingToolbarLayout.setLayoutParams(mAppBarLayoutParams);

    }

    /**
     * Реализуем меню выбора источника для фото профиля
     * @param id
     * @return
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id)
        {
            case ConstantManager.LOAD_PROFILE_PHOTO: {

                /**
                 * Строки меню
                 */
                String[] selectItems = {getString(R.string.load_photo_menu_camera), getString(R.string.load_photo_menu_gallery), getString(R.string.load_photo_menu_cancel)};

                /**
                 * Построитель меню
                 */
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

                /**
                 * Указываем заголовок меню
                 */
                mBuilder.setTitle(getString(R.string.change_profile_photo_caption));

                /**
                 * Заполняем значения строк меню и вешаем обработчик на нажатия
                 */
                mBuilder.setItems(selectItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int choiceItem) {
                        switch (choiceItem) {
                            case 0:
                                // Сделать фото
                                loadPhotoFromCamera();
                                break;
                            case 1:
                                // Из галереи
                                loadPhotoFromGallery();
                                break;
                            case 2:
                                //Отмена
                                dialog.cancel();
                                break;
                        }
                    }
                });
                return mBuilder.create();
            }
            default:
                return null;
        }
    }

    /**
     * По дефолту создается пустое изображение, а на самом деле будет файл с камеры
     * @return изображение
     * @throws IOException
     */
    private File createPhotoImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format( new Date());
        String FileImageName = "JPEG_" + timeStamp + "_";

        // Где лежать будет файл
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        // Создаем файл
        File image = File.createTempFile(FileImageName, ".jpg", storageDir);

        return image;
    }

    /**
     *  Вставляет изображение в тулбар - заместо картинки пользователя
     * @param selectedImage - вставляемое изображение
     */
    private void insertProfileImage(Uri selectedImage) {
        Picasso.with(this)
                .load(selectedImage)
                .into(mPlaceholderImage);
        mDataManager.getPreferencesManager().saveUserPhoto(selectedImage);
    }

    /**
     * Проверка разрешений на доступ
     */
    public void openApplicationSetings(){

        /**
         * Интент на доступ к разрешениям программы
         */
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+getPackageName()));

        /**
         * Запрашиваем ответ на запрос доступа к разрешениям
         */
        startActivityForResult(appSettingsIntent, ConstantManager.REQUEST_APP_SETTINGS);

    }

}