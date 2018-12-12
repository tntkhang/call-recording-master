package com.github.tntkhang;

import android.app.Application;

import com.github.tntkhang.di.AppComponent;
import com.github.tntkhang.di.DatabaseModule;

import khangtran.preferenceshelper.PreferencesHelper;
import vn.nextlogix.tntkhang.di.DaggerAppComponent;


public class BaseApplication extends Application {
    AppComponent appComponent;
    private static BaseApplication INSTANCE;

    public static BaseApplication getApplication() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        INSTANCE = this;
        initializeDependencies();
        PreferencesHelper.initHelper(this);

    }

    private void initializeDependencies() {
        appComponent = DaggerAppComponent.builder()
                .databaseModule(new DatabaseModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
