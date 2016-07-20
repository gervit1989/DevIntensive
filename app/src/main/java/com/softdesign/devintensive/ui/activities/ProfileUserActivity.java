package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.RepositoriesAdapter;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfileUserActivity extends BaseActivity {
    private static final String TAG = ConstantManager.TAG_PREFIX + "ProfileUserActivity";
    private Toolbar mToolBar;
    private ImageView mProfileImage;
    private EditText mUserBio;
    TextView mTextNameView;
    private TextView mUserRating, mUserCodeLines, mUserProjects;
    private ListView mRepoListView;
    private CollapsingToolbarLayout mCollapsingToolBarLayout;
	private CoordinatorLayout mCoordinatorLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mProfileImage = (ImageView) findViewById(R.id.user_photo_img1);
        mUserBio = (EditText) findViewById(R.id.about_self_et);
        mUserRating = (TextView) findViewById(R.id.rating_txt1);
        mUserCodeLines = (TextView) findViewById(R.id.code_lines_txt1);
        mUserProjects = (TextView) findViewById(R.id.projects_txt1);
        mCollapsingToolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mTextNameView = (TextView)findViewById(R.id.user_full_name_txt1);
        mRepoListView = (ListView) findViewById(R.id.repository_list);
        setupToolBar();
        initProfileData();
        mCollapsingToolBarLayout.setExpandedTitleColor(getResources().getColor(R.color.gray_light));
    }

    private void setupToolBar() {
        setSupportActionBar(mToolBar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
    }

    private void initProfileData() {
       try {
           //mCollapsingToolBarLayout.setTitleEnabled(false);
           UserDTO userDTO = getIntent().getParcelableExtra(ConstantManager.PARCELABLE_KEY);

           mTextNameView.setText(userDTO.getFullNameOfUser());
           final List<String> repositories = userDTO.getRepositories();
           final RepositoriesAdapter repositoriesAdapter = new RepositoriesAdapter(this, repositories);
           mRepoListView.setAdapter(repositoriesAdapter);
        mRepoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + repositories.get(position)));
                startActivity(intent);
            }
        });
           mUserBio.setText(userDTO.getBio());
           mUserRating.setText(userDTO.getRating());
           mUserCodeLines.setText(userDTO.getCodeLines());
           mUserProjects.setText(userDTO.getProjects());

           Picasso.with(this)
                   .load(userDTO.getPhoto())
                   .placeholder(R.drawable.user_bg)
                   .error(R.drawable.profile)
    	            .fit()
	                .centerCrop()
                   .into(mProfileImage);//*/
       }
       catch (NullPointerException e){
            showToast("Ошибочка!");
       }


    }
}
