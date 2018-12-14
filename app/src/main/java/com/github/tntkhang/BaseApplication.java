package com.github.tntkhang;

import android.app.Application;

import com.github.tntkhang.di.AppComponent;
import com.github.tntkhang.di.DaggerAppComponent;
import com.github.tntkhang.di.DatabaseModule;

import khangtran.preferenceshelper.PrefHelper;

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
        PrefHelper.initHelper(this);

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
