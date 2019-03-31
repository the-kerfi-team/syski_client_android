package uk.co.syski.client.android.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.entity.OperatingSystemEntity;
import uk.co.syski.client.android.model.OperatingSystemModel;

@Dao
public interface OperatingSystemDao {

    @Query("SELECT Id, Name FROM OperatingSystemEntity WHERE Id == :Id")
    OperatingSystemEntity GetOperatingSystems(UUID Id);

    @Query("SELECT Id, Name, ArchitectureName, Version FROM OperatingSystemEntity INNER JOIN SystemOSEntity ON Id = OSId WHERE SystemId IN (:Ids)")
    List<OperatingSystemModel>GetOperatingSystems(UUID... Ids);

    @Insert
    void InsertAll(OperatingSystemEntity... operatingSystemEntities);

    @Delete
    void DeleteAll(OperatingSystemEntity... operatingSystemEntities);

    @Insert
    void insert(OperatingSystemEntity osEntity);

    @Insert
    void insert(OperatingSystemEntity... osEntities);

    @Query("SELECT * FROM OperatingSystemEntity")
    List<OperatingSystemEntity> get();

    @Query("SELECT * FROM OperatingSystemEntity WHERE Id == :Id")
    OperatingSystemEntity get(UUID Id);

    @Query("SELECT * FROM OperatingSystemEntity WHERE Id in (:Ids)")
    List<OperatingSystemEntity> get(UUID... Ids);

    @Query("SELECT * FROM OperatingSystemEntity INNER JOIN SystemOSEntity ON Id = OSId WHERE SystemId IN (:Ids)")
    List<OperatingSystemModel> getSystemOperatingSystems(UUID... Ids);

    @Update
    void update (OperatingSystemEntity osEntity);

    @Update
    void update (OperatingSystemEntity... osEntities);

    @Delete
    void delete(OperatingSystemEntity osEntity);

    @Delete
    void delete(OperatingSystemEntity... osEntities);

}
