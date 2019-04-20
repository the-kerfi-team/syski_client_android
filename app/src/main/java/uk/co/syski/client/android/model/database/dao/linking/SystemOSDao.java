package uk.co.syski.client.android.model.database.dao.linking;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.model.database.entity.linking.SystemOSEntity;

@Dao
public interface SystemOSDao {

    @Insert
    void insert(SystemOSEntity... systemOSEntities);

    @Insert
    void insert(SystemOSEntity systemOSEntity);

    @Query("SELECT * FROM SystemOSEntity")
    List<SystemOSEntity> get();

    @Update
    void update(SystemOSEntity... systemOSEntities);

    @Update
    void update(SystemOSEntity systemOSEntity);

    @Query("DELETE FROM SystemOSEntity WHERE SystemId = :systemId")
    void deleteBySystemId(UUID systemId);

    @Delete
    void delete(SystemOSEntity... systemOSEntities);

    @Delete
    void delete(SystemOSEntity systemOSEntity);

}
