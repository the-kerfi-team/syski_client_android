package uk.co.syski.client.android.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.entity.SystemEntity;

@Dao
public interface SystemDao {

    @Query("SELECT * FROM SystemEntity WHERE Id = :uuid")
    SystemEntity getSystem(UUID uuid);

    @Query("UPDATE SystemEntity SET HostName=:hostName ,ModelName=:modelName, ManufacturerName=:manufacturerName, LastUpdated=:lastUpdated WHERE Id = :uuid")
    void setSystem(UUID uuid, String hostName, String modelName, String manufacturerName, Date lastUpdated);

    @Query("UPDATE SystemEntity SET MotherboardId=:motherboardId WHERE Id = :uuid")
    void setSystemMotherboard(UUID uuid, UUID motherboardId);

    @Query("SELECT * FROM SystemEntity")
    List<SystemEntity> getAllSystems();

    @Query("SELECT * FROM SystemEntity WHERE Id IN (:Ids)")
    List<SystemEntity> getSystems(UUID... Ids);

    @Insert
    void InsertAll(SystemEntity... systemEntities);

    @Delete
    void DeleteAll(SystemEntity... systemEntities);
}
