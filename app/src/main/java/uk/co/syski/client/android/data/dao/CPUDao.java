package uk.co.syski.client.android.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.entity.CPU;

@Dao
public interface CPUDao {
    @Query("SELECT * FROM CPU INNER JOIN SystemCPU WHERE SystemId IN (:Ids)")
    List<CPU> getCPUs(UUID... Ids);

    @Insert
    void InsertAll(CPU... cpus);

    @Delete
    void DeleteAll(CPU... cpus);
}
