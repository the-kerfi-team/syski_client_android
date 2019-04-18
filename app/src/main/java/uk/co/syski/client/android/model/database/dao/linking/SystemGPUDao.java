package uk.co.syski.client.android.model.database.dao.linking;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.model.database.entity.linking.SystemGPUEntity;

@Dao
public interface SystemGPUDao {

    @Insert
    void insert(SystemGPUEntity... systemGPUEntities);

    @Insert
    void insert(SystemGPUEntity systemGPUEntity);

    @Query("SELECT * FROM SystemGPUEntity")
    List<SystemGPUEntity> get();

    @Update
    void update(SystemGPUEntity... systemGPUEntities);

    @Update
    void update(SystemGPUEntity systemGPUEntity);

    @Query("DELETE FROM SystemGPUEntity WHERE SystemId = :systemId")
    void deleteBySystemId(UUID systemId);

    @Delete
    void delete(SystemGPUEntity... systemGPUEntities);

    @Delete
    void delete(SystemGPUEntity systemGPUEntity);

}
