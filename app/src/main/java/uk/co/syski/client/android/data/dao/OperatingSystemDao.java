package uk.co.syski.client.android.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.entity.OperatingSystem;

@Dao
public interface OperatingSystemDao {
    @Query("SELECT Id, Name, ArchitectureName, Version FROM OperatingSystem INNER JOIN SystemOS " +
            "WHERE SystemId IN (:Ids)")
    List<uk.co.syski.client.android.model.OperatingSystem> GetOperatingSystems(UUID... Ids);

    @Insert
    void InsertAll(OperatingSystem... OperatingSystems);

    @Delete
    void DeleteAll(OperatingSystem... OperatingSystems);
}
