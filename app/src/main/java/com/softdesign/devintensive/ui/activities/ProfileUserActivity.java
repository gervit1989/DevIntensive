package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
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
    private TextView mTextNameView;
    private TextView mUserRating, mUserCodeLines, mUserProjects;
    private ListView mRepoListView;
    private CollapsingToolbarLayout mCollapsingToolbar;
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
        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mTextNameView = (TextView)findViewById(R.id.user_full_name_txt1);
        mRepoListView = (ListView) findViewById(R.id.repository_list);
        setupToolBar();
        initProfileData();
        //mCollapsingToolBarLayout.setExpandedTitleColor(getResources().getColor(R.color.gray_light));
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
                    String gitAddress = (String) repositoriesAdapter.getItem(position);
                    if (gitAddress.contains("http://")) {
                        gitAddress = gitAddress.replaceAll("http://", "");
                    }
                    if (gitAddress.contains("https://")) {
                        gitAddress = gitAddress.replaceAll("https://", "");
                    }
                    if (!gitAddress.equals("")) {
                        Intent mGitIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + gitAddress));
                        startActivity(Intent.createChooser(mGitIntent, getString(R.string.choose_browser)));
                    }
                }
            });
            mUserBio.setText(userDTO.getBio());
            mUserRating.setText(userDTO.getRating());
            mUserCodeLines.setText(userDTO.getCodeLines());
            mUserProjects.setText(userDTO.getProjects());

            //mCollapsingToolbar.setTitle(userDTO.getFullNameOfUser());

            Picasso.with(this)
                    .load(userDTO.getPhoto())
                    .placeholder(R.drawable.user_bg)
                    .error(R.drawable.profile)
                    .fit()
                    .centerCrop()
                    .into(mProfileImage);//*/
            setMaxHeightOfListView(mRepoListView);
        }
        catch (NullPointerException e){
            showToast("Ошибочка!");
        }


    }

    public static void setMaxHeightOfListView(ListView listView) {
        ListAdapter adapter = listView.getAdapter();

        View view = adapter.getView(0, null, listView);
        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );

        int totalHeight = view.getMeasuredHeight() * adapter.getCount();

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + listView.getDividerHeight() * (adapter.getCount() - 1);
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
