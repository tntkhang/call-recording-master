package com.github.tntkhang.models.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.github.tntkhang.models.database.entitiy.CallDetailEntity;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface CallDetailDAO {

    @Query("SELECT * FROM call_detail ORDER BY uid DESC")
    Flowable<List<CallDetailEntity>> getAll();

    @Insert
    void insert(CallDetailEntity callDetailEntity);

    @Update
    void update(CallDetailEntity callDetailEntity);

    @Delete
    void delete(CallDetailEntity callDetailEntity);
}
