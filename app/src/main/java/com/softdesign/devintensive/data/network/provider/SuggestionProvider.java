package com.softdesign.devintensive.data.network.provider;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.CancellationSignal;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mvideo on 20.07.2016.
 */

public class SuggestionProvider extends ContentProvider {

    List<String> users;

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, CancellationSignal cancellationSignal) {

        if (users == null || users.isEmpty()){
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://dl.dropboxusercontent.com/u/6802536/cidades.json")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String jsonString = response.body().string();
                JSONArray jsonArray = new JSONArray(jsonString);

                users = new ArrayList<>();

                int lenght = jsonArray.length();
                for (int i = 0; i < lenght; i++) {
                    String user = jsonArray.getString(i);
                    users.add(user);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        MatrixCursor cursor = new MatrixCursor(
                new String[] {
                        BaseColumns._ID,
                        SearchManager.SUGGEST_COLUMN_TEXT_1,
                        SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID
                }
        );
        if (users != null) {
            String query = uri.getLastPathSegment().toUpperCase();
            int limit = Integer.parseInt(uri.getQueryParameter(SearchManager.SUGGEST_PARAMETER_LIMIT));

            int lenght = users.size();
            for (int i = 0; i < lenght && cursor.getCount() < limit; i++) {
                String city = users.get(i);
                if (city.toUpperCase().contains(query)){
                    cursor.addRow(new Object[]{ i, city, i });
                }
            }
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
    //  Other methods are empty...
}