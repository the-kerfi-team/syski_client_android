package uk.co.syski.client.android.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.entity.RAM;

@Dao
public interface RAMDao {
    @Query("SELECT Id, ModelName, ManufacturerName, MemoryTypeName, MemoryBytes FROM RAM INNER JOIN SystemRAM" +
            " WHERE SystemId IN (:Ids)")
    List<RAM> GetRAMs(UUID... Ids);

    @Insert
    void InsertAll(RAM... RAMs);

    @Delete
    void DeleteAll(RAM... RAMs);
}

