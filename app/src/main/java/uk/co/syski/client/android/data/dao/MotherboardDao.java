package uk.co.syski.client.android.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.entity.MotherboardEntity;

@Dao
public interface MotherboardDao {

    @Insert
    void insert(MotherboardEntity moboEntity);

    @Insert
    void insert(MotherboardEntity... moboEntities);

    @Query("SELECT * FROM MotherboardEntity")
    List<MotherboardEntity> get();

    @Query("SELECT * FROM MotherboardEntity WHERE Id == :Id")
    MotherboardEntity get(UUID Id);

    @Query("SELECT * FROM MotherboardEntity WHERE Id in (:Ids)")
    List<MotherboardEntity> get(UUID... Ids);

    @Query("SELECT MotherboardEntity.Id, MotherboardEntity.ManufacturerName, MotherboardEntity.ModelName, MotherboardEntity.Version FROM MotherboardEntity INNER JOIN SystemEntity ON MotherboardEntity.Id = MotherboardId  WHERE SystemEntity.Id IN (:Ids)")
    List<MotherboardEntity> GetMotherboards(UUID... Ids);

    @Update
    void update (MotherboardEntity moboEntity);

    @Update
    void update (MotherboardEntity... moboEntities);

    @Delete
    void delete(MotherboardEntity moboEntity);

    @Delete
    void delete(MotherboardEntity... moboEntities);
}
