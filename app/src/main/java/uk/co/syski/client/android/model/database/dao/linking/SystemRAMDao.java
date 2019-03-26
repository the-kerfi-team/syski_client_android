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

import uk.co.syski.client.android.model.database.entity.linking.SystemCPUEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemRAMEntity;

@Dao
public interface SystemRAMDao {

    @Insert
    void insert(SystemRAMEntity... systemRAMEntities);

    @Insert
    void insert(SystemRAMEntity systemCPUEntity);

    @Query("SELECT * FROM SystemRAMEntity")
    List<SystemRAMEntity> get();

    @Update
    void update(SystemRAMEntity... systemRAMEntities);

    @Update
    void update(SystemRAMEntity systemRAMEntity);

    @Query("DELETE FROM SystemRAMEntity WHERE SystemId = :systemId")
    void deleteBySystemId(UUID systemId);

    @Delete
    void delete(SystemRAMEntity... systemRAMEntities);

    @Delete
    void delete(SystemRAMEntity systemRAMEntity);

}
