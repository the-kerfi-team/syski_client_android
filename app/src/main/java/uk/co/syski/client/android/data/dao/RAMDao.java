package uk.co.syski.client.android.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.entity.CPUEntity;
import uk.co.syski.client.android.data.entity.RAMEntity;

@Dao
public interface RAMDao {

    @Query("SELECT Id, ModelName, ManufacturerName, MemoryTypeName, MemoryBytes FROM RAMEntity WHERE Id == :Id")
    RAMEntity GetRAM(UUID Id);

    @Query("SELECT * FROM RAMEntity")
    List<RAMEntity> get();

    @Query("SELECT Id, ModelName, ManufacturerName, MemoryTypeName, MemoryBytes FROM RAMEntity INNER JOIN SystemRAMEntity ON Id = RAMId WHERE SystemId IN (:Ids)")
    List<RAMEntity> getSystemRAMs(UUID... Ids);

    @Insert
    void insert(RAMEntity... ramEntities);

    @Update
    void update (RAMEntity... ramEntities);

    @Delete
    void delete(RAMEntity... RAMEntities);

}

