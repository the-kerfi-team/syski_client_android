package uk.co.syski.client.android.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.entity.OperatingSystemEntity;
import uk.co.syski.client.android.data.thread.entity.operatingsystem.statement.GetOperatingSystems;
import uk.co.syski.client.android.model.OperatingSystemModel;

@Dao
public interface OperatingSystemDao {

    @Query("SELECT Id, Name FROM OperatingSystemEntity WHERE Id == :Id")
    OperatingSystemEntity GetOperatingSystems(UUID Id);

    @Query("SELECT Id, Name, ArchitectureName, Version FROM OperatingSystemEntity INNER JOIN SystemOSEntity " +
            "WHERE SystemId IN (:Ids)")
    List<OperatingSystemModel>GetOperatingSystems(UUID... Ids);

    @Insert
    void InsertAll(OperatingSystemEntity... operatingSystemEntities);

    @Delete
    void DeleteAll(OperatingSystemEntity... operatingSystemEntities);
}
