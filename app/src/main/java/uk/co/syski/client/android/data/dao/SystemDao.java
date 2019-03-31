package uk.co.syski.client.android.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.entity.SystemEntity;

@Dao
public interface SystemDao {

    @Query("SELECT Id, HostName, ModelName, ManufacturerName FROM SystemEntity WHERE Id = :uuid")
    SystemEntity getSystem(UUID uuid);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SystemEntity systemEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SystemEntity... systemEntities);

    @Query("SELECT * FROM SystemEntity")
    List<SystemEntity> get();

    @Query("SELECT * FROM SystemEntity WHERE Id = :uuid")
    SystemEntity get(UUID uuid);

    @Query("SELECT * FROM SystemEntity WHERE Id IN (:Ids)")
    List<SystemEntity> get(UUID... Ids);

    @Update
    void update (SystemEntity systemEntity);

    @Update
    void update (SystemEntity... systemEntities);

    @Delete
    void delete(SystemEntity systemEntity);

    @Delete
    void delete(SystemEntity... systemEntities);

}
