package uk.co.syski.client.android.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.entity.StorageEntity;

@Dao
public interface StorageDao {

    @Query("SELECT Id, ModelName, ManufacturerName, MemoryTypeName, MemoryBytes FROM StorageEntity WHERE Id == :Id")
    StorageEntity GetStorage(UUID Id);

    @Query("SELECT Id, ModelName, ManufacturerName, MemoryTypeName, MemoryBytes FROM StorageEntity" +
            " INNER JOIN SystemStorageEntity WHERE SystemId IN (:Ids)")
    List<StorageEntity> GetStorages(UUID... Ids);

    @Insert
    void InsertAll(StorageEntity... storageEntities);

    @Delete
    void DeleteAll(StorageEntity... storageEntities);
}
