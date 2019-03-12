package uk.co.syski.client.android.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.entity.StorageEntity;

@Dao
public interface StorageDao {

    @Insert
    void insert(StorageEntity storageEntity);

    @Insert
    void insert(StorageEntity... storageEntities);

    @Query("SELECT * FROM StorageEntity")
    List<StorageEntity> get();

    @Query("SELECT * FROM StorageEntity WHERE Id == :Id")
    StorageEntity get(UUID Id);

    @Query("SELECT * FROM StorageEntity WHERE Id in (:Ids)")
    List<StorageEntity> get(UUID... Ids);

    @Query("SELECT * FROM StorageEntity INNER JOIN SystemStorageEntity ON Id = StorageId WHERE SystemId IN (:Ids)")
    List<StorageEntity> getSystemCPUs(UUID... Ids);

    @Update
    void update (StorageEntity storageEntity);

    @Update
    void update (StorageEntity... storageEntities);

    @Delete
    void delete(StorageEntity storageEntity);

    @Delete
    void delete(StorageEntity... storageEntities);
    
}
