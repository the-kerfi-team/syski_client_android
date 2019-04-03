package uk.co.syski.client.android.model.database.dao.linking;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.model.database.entity.linking.SystemMotherboardEntity;

@Dao
public interface SystemMotherboardDao {

    @Insert
    void insert(SystemMotherboardEntity... systemMotherboardEntities);

    @Insert
    void insert(SystemMotherboardEntity systemMotherboardEntity);

    @Query("SELECT * FROM SystemMotherboardEntity")
    List<SystemMotherboardEntity> get();

    @Update
    void update(SystemMotherboardEntity... systemMotherboardEntities);

    @Update
    void update(SystemMotherboardEntity systemMotherboardEntity);

    @Query("DELETE FROM SystemMotherboardEntity WHERE SystemId = :systemId")
    void deleteBySystemId(UUID systemId);

    @Delete
    void delete(SystemMotherboardEntity... systemMotherboardEntities);

    @Delete
    void delete(SystemMotherboardEntity systemMotherboardEntity);

}
