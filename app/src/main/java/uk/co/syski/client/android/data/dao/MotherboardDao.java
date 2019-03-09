package uk.co.syski.client.android.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.entity.MotherboardEntity;

@Dao
public interface MotherboardDao {

    @Query("SELECT * FROM MotherboardEntity WHERE Id == :Id")
    MotherboardEntity GetMotherboard(UUID Id);

    @Query("SELECT MotherboardEntity.Id, MotherboardEntity.ManufacturerName, MotherboardEntity.ModelName, MotherboardEntity.Version FROM MotherboardEntity INNER JOIN SystemEntity ON MotherboardEntity.Id = MotherboardId  WHERE SystemEntity.Id IN (:Ids)")
    List<MotherboardEntity> GetMotherboards(UUID... Ids);

    @Insert
    void InsertAll(MotherboardEntity... motherboardEntities);

    @Delete
    void DeleteAll(MotherboardEntity... motherboardEntities);
}
