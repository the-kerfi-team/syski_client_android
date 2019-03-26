package uk.co.syski.client.android.model.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.sqlite.SQLiteConstraintException;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.model.database.entity.RAMEntity;

@Dao
public abstract class RAMDao {

    @Query("SELECT Id, ModelName, ManufacturerName, MemoryTypeName, MemoryBytes FROM RAMEntity WHERE Id == :Id")
    public abstract RAMEntity GetRAM(UUID Id);

    @Query("SELECT * FROM RAMEntity")
    public abstract List<RAMEntity> get();

    @Query("SELECT Id, ModelName, ManufacturerName, MemoryTypeName, MemoryBytes FROM RAMEntity INNER JOIN SystemRAMEntity ON Id = RAMId WHERE SystemId IN (:Ids)")
    public abstract List<RAMEntity> getSystemRAMs(UUID... Ids);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public abstract void insert(RAMEntity... ramEntities);

    @Update(onConflict = OnConflictStrategy.FAIL)
    public abstract void update (RAMEntity... ramEntities);

    @Delete
    public abstract void delete(RAMEntity... RAMEntities);

    public void upsert(RAMEntity ramEntity) {
        try {
          insert(ramEntity);
        } catch (SQLiteConstraintException exception) {
           update(ramEntity);
        }
    }


}

