package uk.co.syski.client.android.model.database.dao.linking;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.model.database.entity.linking.SystemBIOSEntity;

@Dao
public interface SystemBIOSDao {

    @Insert
    void insert(SystemBIOSEntity... systemBIOSEntities);

    @Insert
    void insert(SystemBIOSEntity systemBIOSEntity);

    @Query("SELECT * FROM SystemBIOSEntity")
    List<SystemBIOSEntity> get();

    @Update
    void update(SystemBIOSEntity... systemBIOSEntities);

    @Update
    void update(SystemBIOSEntity systemBIOSEntity);

    @Query("DELETE FROM SystemBIOSEntity WHERE SystemId = :systemId")
    void deleteBySystemId(UUID systemId);

    @Delete
    void delete(SystemBIOSEntity... systemBIOSEntities);

    @Delete
    void delete(SystemBIOSEntity systemBIOSEntity);

}
