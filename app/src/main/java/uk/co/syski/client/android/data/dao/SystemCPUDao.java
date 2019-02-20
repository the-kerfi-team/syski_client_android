package uk.co.syski.client.android.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.entity.SystemCPU;

@Dao
public interface SystemCPUDao {
    @Query("SELECT CPUId FROM SystemCPU WHERE SystemId IN (:Ids)")
    List<UUID> GetCPUIds(UUID... Ids);

    @Insert
    void InsertAll(SystemCPU... SystemCPUs);

    @Delete
    void DeleteAll(SystemCPU... SystemCPUs);
}
