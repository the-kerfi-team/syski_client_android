package uk.co.syski.client.android.model.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.sqlite.SQLiteConstraintException;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.model.database.entity.GPUEntity;

@Dao
public abstract class GPUDao {

    @Insert
    public abstract void insert(GPUEntity gpuEntity);

    @Insert
    public abstract void insert(GPUEntity... gpuEntities);

    @Query("SELECT * FROM GPUEntity INNER JOIN SystemGPUEntity ON Id = GPUId WHERE SystemId IN (:Ids)")
    public abstract List<GPUEntity> getSystemGPUs(UUID... Ids);

    @Query("SELECT * FROM GPUEntity")
    public abstract List<GPUEntity> get();
    
    @Query("SELECT * FROM GPUEntity WHERE Id == :Id")
    public abstract GPUEntity get(UUID Id);

    @Update
    public abstract void update (GPUEntity gpuEntity);

    @Update
    public abstract void update (GPUEntity... gpuEntities);

    @Delete
    public abstract void delete(GPUEntity gpuEntity);

    @Delete
    public abstract void delete(GPUEntity... gpuEntities);

    public void upsert(GPUEntity gpuEntity) {
        try {
            insert(gpuEntity);
        } catch (SQLiteConstraintException exception) {
            update(gpuEntity);
        }
    }

}
