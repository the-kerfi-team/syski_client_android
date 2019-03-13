package uk.co.syski.client.android.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.sqlite.SQLiteConstraintException;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.entity.StorageEntity;

@Dao
public abstract class StorageDao {

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public abstract void insert(StorageEntity storageEntity);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public abstract void insert(StorageEntity... storageEntities);

    @Query("SELECT * FROM StorageEntity")
    public abstract List<StorageEntity> get();

    @Query("SELECT * FROM StorageEntity WHERE Id == :Id")
    public abstract StorageEntity get(UUID Id);

    @Query("SELECT * FROM StorageEntity WHERE Id in (:Ids)")
    public abstract List<StorageEntity> get(UUID... Ids);

    @Query("SELECT * FROM StorageEntity INNER JOIN SystemStorageEntity ON Id = StorageId WHERE SystemId IN (:Ids)")
    public abstract List<StorageEntity> getSystemStorages(UUID... Ids);

    @Update(onConflict = OnConflictStrategy.FAIL)
    public abstract void update (StorageEntity storageEntity);

    @Update(onConflict = OnConflictStrategy.FAIL)
    public abstract void update (StorageEntity... storageEntities);

    @Delete
    public abstract void delete(StorageEntity storageEntity);

    @Delete
    public abstract void delete(StorageEntity... storageEntities);

    public void upsert(StorageEntity storageEntity) {
        try {
            insert(storageEntity);
        } catch (SQLiteConstraintException exception) {
            update(storageEntity);
        }
    }
    
}
