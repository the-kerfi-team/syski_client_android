package uk.co.syski.client.android.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.entity.RAMEntity;

@Dao
public interface RAMDao {

    @Query("SELECT Id, ModelName, ManufacturerName, MemoryTypeName, MemoryBytes FROM RAMEntity WHERE Id == :Id")
    RAMEntity GetRAM(UUID Id);

    @Query("SELECT Id, ModelName, ManufacturerName, MemoryTypeName, MemoryBytes FROM RAMEntity INNER JOIN SystemRAM" +
            "Entity WHERE SystemId IN (:Ids)")
    List<RAMEntity> GetRAMs(UUID... Ids);

    @Insert
    void InsertAll(RAMEntity... RAMEntities);

    @Delete
    void DeleteAll(RAMEntity... RAMEntities);
}

