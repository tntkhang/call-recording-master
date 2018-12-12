package com.github.tntkhang.di;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.github.tntkhang.models.database.AppDatabase;
import com.github.tntkhang.models.database.dao.CallDetailDAO;
import com.github.tntkhang.models.database.repository.CallDetailRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import vn.nextlogix.tntkhang.R;

@Module
public class DatabaseModule {

    private Context context;

    public DatabaseModule(Context ctx){
        context = ctx;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return context;
    }

    @Singleton
    @Provides
    public Executor getExecutor(){
        return  Executors.newFixedThreadPool(2);
    }

    @Provides
    @Singleton
    public AppDatabase getAppDatabase(Context context){
       return  Room.databaseBuilder(context,
               AppDatabase.class,
               context.getString(R.string.app_name)).build();
    }

    @Provides
    @Singleton
    public CallDetailDAO provideCallDetailDAO(AppDatabase appDatabase){
        return appDatabase.callDetailDAO();
    }

    @Provides
    @Singleton
    public CallDetailRepository provideCallDetailRepository(CallDetailDAO callDetailDAO, Executor exec){
        return new CallDetailRepository(callDetailDAO, exec);
    }

}
