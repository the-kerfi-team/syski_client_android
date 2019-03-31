package uk.co.syski.client.android.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.entity.GPUEntity;
import uk.co.syski.client.android.data.entity.GPUEntity;

@Dao
public interface GPUDao {

    @Query("SELECT Id, ModelName, ManufacturerName FROM GPUEntity WHERE Id IN (:Id)")
    GPUEntity GetGPU(UUID Id);

    @Query("SELECT Id, ModelName, ManufacturerName FROM GPUEntity INNER JOIN SystemGPUEntity ON Id = GPUId WHERE SystemId IN (:Ids)")
    List<GPUEntity> GetGPUs(UUID... Ids);

    @Insert
    void InsertAll(GPUEntity... gpuEntities);

    @Delete
    void DeleteAll(GPUEntity... gpuEntities);

    @Query("SELECT * FROM GPUEntity INNER JOIN SystemGPUEntity ON Id = GPUId WHERE SystemId IN (:Ids)")
    List<GPUEntity> getSystemGPUs(UUID... Ids);

    @Query("SELECT * FROM GPUEntity")
    List<GPUEntity> get();    
    
    @Query("SELECT * FROM GPUEntity WHERE Id == :Id")
    GPUEntity get(UUID Id);

    @Update
    void update (GPUEntity gpuEntity);

    @Update
    void update (GPUEntity... gpuEntities);

    @Delete
    void delete(GPUEntity gpuEntity);

    @Delete
    void delete(GPUEntity... gpuEntities);

    @Insert
    void insert(GPUEntity gpuEntity);

    @Insert
    void insert(GPUEntity... gpuEntities);
}
