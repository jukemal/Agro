package com.agro.agro.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.agro.agro.entity.DateEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface DateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(DateEntity dateEntity);

    @Query("SELECT * FROM  date_entity WHERE uid=:id")
    public List<DateEntity> getDate(int id);

    @Query("SELECT * FROM  date_entity WHERE uid=:id")
    public LiveData<List<DateEntity>> getLiveDate(int id);

    @Query("SELECT * FROM  date_entity WHERE uid=:id")
    public Single<DateEntity> getLiveDateRX(int id);

    @Update()
    public Completable updateDate(DateEntity dateEntity);
}
