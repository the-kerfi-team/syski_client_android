package uk.co.syski.client.android.model.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.sqlite.SQLiteConstraintException;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.model.database.entity.CPUEntity;
import uk.co.syski.client.android.model.database.entity.StorageEntity;

@Dao
public abstract class CPUDao {

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public abstract void insert(CPUEntity cpuEntity);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public abstract void insert(CPUEntity... cpuEntities);

    @Query("SELECT * FROM CPUEntity")
    public abstract List<CPUEntity> get();

    @Query("SELECT * FROM CPUEntity WHERE Id == :Id")
    public abstract CPUEntity get(UUID Id);

    @Query("SELECT * FROM CPUEntity WHERE Id in (:Ids)")
    public abstract List<CPUEntity> get(UUID... Ids);

    @Query("SELECT * FROM CPUEntity INNER JOIN SystemCPUEntity ON Id = CPUId WHERE SystemId IN (:Ids)")
    public abstract List<CPUEntity> getSystemCPUs(UUID... Ids);

    @Update(onConflict = OnConflictStrategy.FAIL)
    public abstract void update(CPUEntity cpuEntity);

    @Update(onConflict = OnConflictStrategy.FAIL)
    public abstract void update(CPUEntity... cpuEntities);

    @Delete
    public abstract void delete(CPUEntity cpuEntity);

    @Delete
    public abstract void delete(CPUEntity... cpuEntities);

    public void upsert(CPUEntity cpuEntitiy) {
        try {
            insert(cpuEntitiy);
        } catch (SQLiteConstraintException exception) {
            update(cpuEntitiy);
        }
    }

}
