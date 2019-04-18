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

import uk.co.syski.client.android.model.database.entity.SystemEntity;

@Dao
public abstract class SystemDao {

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public abstract void insert(SystemEntity systemEntity);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public abstract void insert(SystemEntity... systemEntities);

    @Query("SELECT * FROM SystemEntity")
    public abstract List<SystemEntity> get();

    @Query("SELECT * FROM SystemEntity WHERE Id = :uuid")
    public abstract SystemEntity get(UUID uuid);

    @Query("SELECT * FROM SystemEntity WHERE Id IN (:Ids)")
    public abstract List<SystemEntity> get(UUID... Ids);

    @Update(onConflict = OnConflictStrategy.FAIL)
    public abstract void update (SystemEntity systemEntity);

    @Update(onConflict = OnConflictStrategy.FAIL)
    public abstract void update (SystemEntity... systemEntities);

    @Delete
    public abstract void delete(SystemEntity systemEntity);

    @Delete
    public abstract void delete(SystemEntity... systemEntities);

    public void upsert(SystemEntity systemEntity) {
        try {
            insert(systemEntity);
        } catch (SQLiteConstraintException exception) {
            update(systemEntity);
        }
    }

}
