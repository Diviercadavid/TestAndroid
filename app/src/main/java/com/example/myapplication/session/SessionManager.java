package com.example.myapplication.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private Context mContext;

    private static final String AUTH_TOKEN = "authToken";
    private static final String PREF_NAME = "Pref";
    // Constructor
    public SessionManager(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }
    /**
     * Create login session
     */
    public void setToken(String token) {
        mEditor.putString(AUTH_TOKEN, token);
        mEditor.commit();
    }

    public String getToken() {
        return mSharedPreferences.getString(AUTH_TOKEN, "");
    }
    public void clearDatabase() {
        mEditor.clear();
        mEditor.commit();
    }

    }
