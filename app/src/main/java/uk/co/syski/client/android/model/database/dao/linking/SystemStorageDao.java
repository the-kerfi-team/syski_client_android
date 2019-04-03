package uk.co.syski.client.android.model.database.dao.linking;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.sqlite.SQLiteConstraintException;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.model.database.entity.linking.SystemRAMEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemStorageEntity;

@Dao
public interface SystemStorageDao {

    @Insert
    void insert(SystemStorageEntity... systemStorageEntities);

    @Insert
    void insert(SystemStorageEntity systemStorageEntity);

    @Query("SELECT * FROM SystemStorageEntity")
    List<SystemStorageEntity> get();

    @Update
    void update(SystemStorageEntity... systemStorageEntities);

    @Update
    void update(SystemStorageEntity systemStorageEntity);

    @Query("DELETE FROM SystemStorageEntity WHERE SystemId = :systemId")
    void deleteBySystemId(UUID systemId);

    @Delete
    void delete(SystemStorageEntity... systemStorageEntities);

    @Delete
    void delete(SystemStorageEntity systemStorageEntity);

}
