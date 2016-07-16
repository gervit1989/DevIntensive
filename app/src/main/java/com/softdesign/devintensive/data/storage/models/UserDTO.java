package com.softdesign.devintensive.data.storage.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.softdesign.devintensive.data.network.res.UserListResponse;
import com.softdesign.devintensive.data.network.res.UserModelResponse;

import java.util.ArrayList;
import java.util.List;

public class UserDTO implements Parcelable {

    private String mPhoto;
    private String mName;
    private String mFamily;
    private String mFullName;
    private String mRating;
    private String mCodeLines;
    private String mProjects;
    private String mBio;
    private List<String> mRepositories;

    public UserDTO(UserListResponse.UserData userData) {
        List<String> repoLink = new ArrayList<>();

        mPhoto = userData.getPublicInfo().getPhoto();
        mFullName = userData.getFullName();
        mName = userData.getFirstName();
        mFamily = userData.getSecondName();
        mRating = String.valueOf(userData.getProfileValues().getRating());
        mCodeLines = String.valueOf(userData.getProfileValues().getLinesCode());
        mProjects = String.valueOf(userData.getProfileValues().getProjects());
        mBio = userData.getPublicInfo().getBio();

        for (UserModelResponse.Repo gitLink : userData.getRepositories().getRepo()) {
            repoLink.add(gitLink.getGit());
        }

        mRepositories = repoLink;
    }

    protected UserDTO(Parcel in) {
        mPhoto = in.readString();
        mFullName = in.readString();
        mName = in.readString();
        mFamily = in.readString();
        mRating = in.readString();
        mCodeLines = in.readString();
        mProjects = in.readString();
        mBio = in.readString();
        if (in.readByte() == 0x01) {
            mRepositories = new ArrayList<String>();
            in.readList(mRepositories, String.class.getClassLoader());
        } else {
            mRepositories = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPhoto);
        dest.writeString(mFullName);
        dest.writeString(mName);
        dest.writeString(mFamily);
        dest.writeString(mRating);
        dest.writeString(mCodeLines);
        dest.writeString(mProjects);
        dest.writeString(mBio);
        if (mRepositories == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mRepositories);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<UserDTO> CREATOR = new Parcelable.Creator<UserDTO>() {
        @Override
        public UserDTO createFromParcel(Parcel in) {
            return new UserDTO(in);
        }

        @Override
        public UserDTO[] newArray(int size) {
            return new UserDTO[size];
        }
    };

    public String getPhoto() {
        return mPhoto;
    }

    public String getFullName() {
        return mFullName;
    }

    public String getName() {
        return mName;
    }

    public String getFamily() {
        return mFamily;
    }

    public String getRating() {
        return mRating;
    }

    public String getCodeLines() {
        return mCodeLines;
    }

    public String getProjects() {
        return mProjects;
    }

    public String getBio() {
        return mBio;
    }

    public List<String> getRepositories() {
        return mRepositories;
    }
}
