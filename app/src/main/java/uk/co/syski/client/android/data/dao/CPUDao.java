package uk.co.syski.client.android.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.entity.CPUEntity;

@Dao
public interface CPUDao {

    @Query("SELECT Id, ModelName, ManufacturerName, ArchitectureName, ClockSpeed, CoreCount, ThreadCount FROM CPUEntity INNER JOIN SystemCPUEntity WHERE SystemId == :Id")
    CPUEntity getCPU(UUID Id);

    @Query("SELECT Id, ModelName, ManufacturerName, ArchitectureName, ClockSpeed, CoreCount, ThreadCount" +
            " FROM CPUEntity INNER JOIN SystemCPUEntity WHERE SystemId IN (:Ids)")
    List<CPUEntity> getCPUs(UUID... Ids);

    @Insert
    void InsertAll(CPUEntity... cpuEntities);

    @Delete
    void DeleteAll(CPUEntity... cpuEntities);
}
