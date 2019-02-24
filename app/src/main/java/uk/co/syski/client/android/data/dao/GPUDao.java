package uk.co.syski.client.android.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.entity.GPUEntity;

@Dao
public interface GPUDao {
    @Query("SELECT Id, ModelName, ManufacturerName, ArchitectureName, ClockSpeed, CoreCount, " +
            "ThreadCount, MemoryTypeName, MemoryBytes FROM GPUEntity INNER JOIN SystemGPUEntity " +
            "WHERE SystemId IN (:Ids)")
    List<GPUEntity> GetGPUs(UUID... Ids);

    @Insert
    void InsertAll(GPUEntity... gpuEntities);

    @Delete
    void DeleteAll(GPUEntity... gpuEntities);
}
