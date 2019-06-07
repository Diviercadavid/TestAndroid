package com.example.myapplication.application;

import android.app.Application;
import com.facebook.drawee.backends.pipeline.Fresco;

public class MyApplicationApplication extends Application {

    public MyApplicationApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
