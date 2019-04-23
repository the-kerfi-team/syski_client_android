package uk.co.syski.client.android.model.database.dao.linking;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.model.database.entity.linking.SystemCPUEntity;

@Dao
public interface SystemCPUDao {

    @Insert
    void insert(SystemCPUEntity... systemCPUEntities);

    @Insert
    void insert(SystemCPUEntity systemCPUEntity);

    @Query("SELECT * FROM SystemCPUEntity")
    List<SystemCPUEntity> get();

    @Update
    void update(SystemCPUEntity... systemCPUEntities);

    @Update
    void update(SystemCPUEntity systemCPUEntity);

    @Query("DELETE FROM SystemCPUEntity WHERE SystemId = :systemId")
    void deleteBySystemId(UUID systemId);

    @Delete
    void delete(SystemCPUEntity... systemCPUEntities);

    @Delete
    void delete(SystemCPUEntity systemCPUEntity);

}
