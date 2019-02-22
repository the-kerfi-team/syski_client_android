package uk.co.syski.client.android.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.entity.SystemEntity;

@Dao
public interface SystemDao {
    @Query("SELECT * FROM SystemEntity")
    List<SystemEntity> getAllSystems();

    @Query("SELECT * FROM SystemEntity WHERE Id IN (:Ids)")
    List<SystemEntity> getSystems(UUID... Ids);

    @Insert
    void InsertAll(SystemEntity... systemEntities);

    @Delete
    void DeleteAll(SystemEntity... systemEntities);
}
