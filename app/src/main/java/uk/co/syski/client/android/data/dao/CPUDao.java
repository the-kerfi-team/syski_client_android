package uk.co.syski.client.android.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.entity.CPUEntity;
import uk.co.syski.client.android.data.entity.SystemEntity;

@Dao
public interface CPUDao {

    @Insert
    void insert(CPUEntity cpuEntity);

    @Insert
    void insert(CPUEntity... cpuEntities);

    @Query("SELECT * FROM CPUEntity")
    List<CPUEntity> get();

    @Query("SELECT * FROM CPUEntity WHERE Id == :Id")
    CPUEntity get(UUID Id);

    @Query("SELECT * FROM CPUEntity WHERE Id in (:Id)")
    List<CPUEntity> get(UUID... Ids);

    @Query("SELECT * FROM CPUEntity INNER JOIN SystemCPUEntity ON Id = CPUId WHERE SystemId IN (:Ids)")
    List<CPUEntity> getSystemCPUs(UUID... Ids);

    @Update
    void update (CPUEntity cpuEntity);

    @Update
    void update (CPUEntity... cpuEntities);

    @Delete
    void delete(CPUEntity cpuEntity);

    @Delete
    void delete(CPUEntity... cpuEntities);
}
