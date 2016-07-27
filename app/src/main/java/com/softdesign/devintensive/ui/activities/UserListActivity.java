package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.res.UserListResponse;
import com.softdesign.devintensive.data.storage.models.MUser;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.CustomLoader;
import com.softdesign.devintensive.utils.RoundedAvatarDrawable;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<List<User>>{
// implements LoaderManager.LoaderCallbacks<List<User>>
    private static final String TAG = ConstantManager.TAG_PREFIX + " UserListActivity";
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private RecyclerView mRecyclerView;

    private DataManager mDataManager;
    private UsersAdapter mUsersAdapter;
    private List<User> mUsers;
    private List<MUser> mLocalUsers;
    private MenuItem mSearchItem;
    private String mQuery;
    private Handler mHandler;
    private ImageView mCircularDrawerHeaderAvatar;
    private TextView mUserEmailDrawerHeader;
    private Loader<List<User>> mLoader;
    private Loader<List<MUser>> mLocalLoader;
    private ImageView user_avatar;
    private Drawable mDummy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        mDataManager = DataManager.getINSTANCE();
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mRecyclerView = (RecyclerView) findViewById(R.id.user_list);
        mHandler = new Handler();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        setupToolbar();
        setupDrawer();
        initAvatarImage();
        if (ConstantManager.IS_LOADER){
            mLoader = getSupportLoaderManager().initLoader(1, new Bundle(), this);
        }else {
            loadUsersFromDb();
        }
    }

    private void initAvatarImage() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        user_avatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.user_avatar);
        String photoURL = getIntent().getStringExtra(ConstantManager.USER_AVATAR_URL_KEY);
        final Uri photoLocalUri = mDataManager.getPreferencesManager().loadUserAvatar();

        Call<ResponseBody> call = mDataManager.getImage(photoURL);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                    if (bitmap != null) {

                        makeRoundAvatarFromBitmap(bitmap);
                        try {
                            File file = createImageFileFromBitmap("user_avatar", bitmap);
                            if (file != null) {
                                Log.d(TAG, "onResume25");
                                mDataManager.getPreferencesManager()
                                        .saveUserAvatar(Uri.fromFile(file));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mDummy = user_avatar.getContext().getResources().getDrawable(R.drawable.profile);
                final String userPhoto = mDataManager.getPreferencesManager().loadUserAvatar().toString();
                File file = new File(Environment.
                        getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"user_avatar.jpg");
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                Log.d(TAG, userPhoto);
                if (bitmap != null) {
                    Log.d(TAG, "onResume2");
                    makeRoundAvatarFromBitmap(bitmap);
                }
            }
        });
    }

    private void makeRoundAvatarFromBitmap(Bitmap btMap) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        user_avatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.user_avatar);
        RoundedAvatarDrawable bt = new RoundedAvatarDrawable(btMap);
        user_avatar.setImageDrawable(bt);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }


    /**
     * Загрузка списка пользователей из БД
     */
    private void loadUsersFromDb() {
        Log.d(TAG, "loadUsersFromDb");
        if (mDataManager.getUserListFromDb().size() == 0) {
            showSnackBar("Список пользователей не может быть загружен");
        } else {
            showUsers(mDataManager.getUserListFromDb());
        }
	}
    private void loadLocalUsersFromDb() {
        Log.d(TAG, "loadLocalUsersFromDb");
        if (mDataManager.getLocalUserListFromDb().size() == 0) {
            showSnackBar("Список пользователей не может быть загружен");
        } else {
            mLocalUsers = mDataManager.getLocalUserListFromDb();
        }
    }

    private void setupToolbar() {
        Log.d(TAG, "setupToolbar");

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    private void setupDrawer() {
        // TODO: реализовтаь переход в другую активити при клике по элементу меню в Navigation Drawer
        Log.d(TAG, "setupDrawer");
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackBar(item.getTitle().toString());

                item.setChecked(true);
                if (item.getTitle().toString().equals("Мой профиль")) {
                    Intent loginIntent;
                    loginIntent = new Intent(UserListActivity.this, MainActivity.class);
                    startActivity(loginIntent);
                }
                initAvatarImage();
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }
	private void showUsers(List<User> users) {
        Log.d(TAG, "showUsers");
        mUsers = users;
        mUsersAdapter = new UsersAdapter(mUsers, new UsersAdapter.UserViewHolder.CustomClickListener() {
            @Override
            public void onUserItemClickListener(int position) {

                UserDTO userDTO = new UserDTO(mUsers.get(position));

                Intent profileIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
                profileIntent.putExtra(ConstantManager.PARCELABLE_KEY, userDTO);

                startActivity(profileIntent);
            }
        });
        mRecyclerView.swapAdapter(mUsersAdapter, false);
    }

    private void showUsersByQuery(String query) {
        Log.d(TAG, "showUsersByQuery");
        mQuery = query;
        Runnable seachUser = new Runnable() {
            @Override
            public void run() {
                showUsers(mDataManager.getUserListByName(mQuery));
            }
        };
        mHandler.removeCallbacks(seachUser);
        long delay = mQuery.isEmpty() ? 0L : ConstantManager.SEARCH_DELAY;
        mHandler.postDelayed(seachUser, delay);
    }


    @Override
    public Loader<List<User>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader");
        return new CustomLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<User>> loader, List<User> data) {
        Log.d(TAG, "onLoadFinished");
        mUsers = data;
        showUsers(mUsers);
    }

    @Override
    public void onLoaderReset(Loader<List<User>> loader) {
        Log.d(TAG, "onLoaderReset");
    }

    /**
     * Обработка нажатия кнопки "back". Убирает открытую NavigationDrawer
     */
    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed");
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        showUsersByQuery(newText);
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }
    private File createImageFileFromBitmap(String imageFileName, Bitmap bitmap) throws IOException {

        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = new File(storageDir, imageFileName + ".jpg");

            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }
}
