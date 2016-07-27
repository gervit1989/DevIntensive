package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.req.UserLoginRequest;
import com.softdesign.devintensive.data.network.res.UserListResponse;
import com.softdesign.devintensive.data.network.res.UserModelResponse;
import com.softdesign.devintensive.data.storage.models.Repository;
import com.softdesign.devintensive.data.storage.models.RepositoryDao;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.utils.AppConfig;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.NetworkStatusChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    TextView mTextView;

    /**
     * Пользовательские настройки
     */
    private DataManager mDataManager;

    private RepositoryDao mRepositoryDao;

    private UserDao mUserDao;

    //- Тег отслеживания
    private static final String TAG = ConstantManager.TAG_PREFIX + "LoginActivity";

    /**
     * На создание активити
     * @param savedInstanceState - сохраненное состояние
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

        mUserDao = mDataManager.getDaoSession().getUserDao();

        mRepositoryDao = mDataManager.getDaoSession().getRepositoryDao();

        //- Инициализация кнопки
        mButtonLogin = (Button)findViewById(R.id.login_login_button);
        mLoginEdit = (EditText) findViewById(R.id.login_edit);
        mPassEdit = (EditText)findViewById(R.id.pass_edit);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.login_coordinator);
        mTextView = (TextView) findViewById(R.id.remind_pass);

        //- Назначение события нажатия
        mButtonLogin.setOnClickListener(this);
        mTextView.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
	/**
     * Обработка нажатий
     * @param v- элемент, на который осуществлено нажатие
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remind_pass:
                remindPass();
                break;
            case R.id.login_login_button:
                signIn();
                break;
        }
    }

    private void remindPass() {
        Intent rememberIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(rememberIntent);
    }


    private void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }
    private void loginSuccess(
            final UserModelResponse userModel) {
        showSnackBar(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveAuthToken(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveUserID(userModel.getData().getUser().getId());
        saveUserProfileValues(userModel);
        saveUserData(userModel);
        saveUserName(userModel);
        saveUserDataInDb();
        android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Intent loginIntent = new Intent(this, MainActivity.class);
                Intent loginIntent = new Intent(LoginActivity.this, UserListActivity.class);
                loginIntent.putExtra(ConstantManager.USER_PHOTO_URL_KEY,
                        userModel.getData().getUser().getPublicInfo().getPhoto());
                loginIntent.putExtra(ConstantManager.USER_AVATAR_URL_KEY,
                        userModel.getData().getUser().getPublicInfo().getAvatar());
                startActivity(loginIntent);
            }
        }, AppConfig.START_DELAY);

    }

    private void signIn() {
        if (NetworkStatusChecker.isNetworkAvailable(this)) {

            Call<UserModelResponse> call = mDataManager.loginUser(
                    new UserLoginRequest(mLoginEdit.getText().toString(), mPassEdit.getText().toString()));
            // асинхронный вызов
            call.enqueue(new Callback<UserModelResponse>() {
                @Override
                public void onResponse(Call<UserModelResponse> call, Response<UserModelResponse> response) {
                    if (response.code() == 200) {
                        loginSuccess(response.body());
                    } else if (response.code() == 404) {
                        showSnackBar("Неверный логин или пароль");
                    } else {
                        showSnackBar("Все пропало Шеф!!!");
                    }
                }

                @Override
                public void onFailure(Call<UserModelResponse> call, Throwable t) {
                    // TODO: 10.07.2016 Обработать ошибки
                }
            });

        } else {
            showSnackBar("Сеть на данный момент недоступна, попробуйте позже");
        }
    }
    private void saveUserProfileValues(UserModelResponse userModel){
        int[] userValues ={
                userModel.getData().getUser().getProfileValues().getRating(),
        userModel.getData().getUser().getProfileValues().getLinesCode(),
        userModel.getData().getUser().getProfileValues().getProjectCount()
        };

        mDataManager.getPreferencesManager().saveUserProfileValues(userValues);
    }

    /**S
     * Извлекает данные профиля пользователя из модели {@link UserModelResponse}
     * и сохраняет их в Shared Preferences
     * @param userModel модель данных пользователя
     */
    private void saveUserData(UserModelResponse userModel) {
        List<String> userFields = new ArrayList<>();
        userFields.add(userModel.getData().getUser().getContacts().getPhone());
        userFields.add(userModel.getData().getUser().getContacts().getEmail());
        userFields.add(userModel.getData().getUser().getContacts().getVk());
        userFields.add(userModel.getData().getUser().getRepositories().getRepo().get(0).getGit());
        String bio = userModel.getData().getUser().getPublicInfo().getBio();
        userFields.add(bio.isEmpty() ? getString(R.string.text_about_me) : bio);

        mDataManager.getPreferencesManager().saveUserData(userFields);
    }

    /**
     * Извлекает имя и фамилию пользователя из модели {@link UserModelResponse}
     * и сохраняет их в Shared Preferences
     * @param userModel модель данных пользователя
     */
    private void saveUserName(UserModelResponse userModel) {
        String[] userNames = {
                userModel.getData().getUser().getFirstName(),
                userModel.getData().getUser().getSecondName()
        };

        mDataManager.getPreferencesManager().saveUserName(userNames);
    }


    /**
     * Сохранение данных в БД
     */
    private void saveUserDataInDb(){

        Call<UserListResponse> call = mDataManager.getUserListFromNetwork();

        call.enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {

                try {
                    if(response.code()==200){
                        //- Список репозиториев
                        List<Repository> allRepositories = new ArrayList<>();

                        //- Список пользователей
                        List<User> allUsers = new ArrayList<>();

                        for (UserListResponse.UserData userRes: response.body().getData()) {
                            allRepositories.addAll(getRepoListFromUserRes(userRes));
                            allUsers.add(new User(userRes));
                        }
                        mRepositoryDao.insertOrReplaceInTx(allRepositories);
                        mUserDao.insertOrReplaceInTx(allUsers);

                    }
                    else {
                        showSnackBar("Список пользователей не может быть получен");
                        Log.e(TAG,"onResponse: " + String.valueOf(response.errorBody().source()));
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    showSnackBar("Something going wrong");
                }
            }

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {

            }
        });
    }

    /**
     * Получить список репозиториев у одного пользователя
     * @return
     */
    private List<Repository> getRepoListFromUserRes(UserListResponse.UserData userData){
        //- Получаем id пользователя
        final String userId = userData.getId();

        //- Спислк репозиториев
        List<Repository> repositories = new ArrayList<>();

        for (UserModelResponse.Repo rep : userData.getRepositories().getRepo()) {
            repositories.add(new Repository(rep, userId));
        }
        return repositories;
    }
}
