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

import uk.co.syski.client.android.model.database.entity.MotherboardEntity;

@Dao
public abstract class MotherboardDao {

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public abstract void insert(MotherboardEntity motherboardEntitiy);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public abstract void insert(MotherboardEntity... motherboardEntities);

    @Query("SELECT * FROM MotherboardEntity")
    public abstract List<MotherboardEntity> get();

    @Query("SELECT * FROM MotherboardEntity WHERE Id == :Id")
    public abstract MotherboardEntity get(UUID Id);

    @Query("SELECT * FROM MotherboardEntity WHERE Id in (:Ids)")
    public abstract List<MotherboardEntity> get(UUID... Ids);

    @Query("SELECT * FROM MotherboardEntity INNER JOIN SystemMotherboardEntity ON Id = MotherboardId WHERE SystemId IN (:Ids)")
    public abstract List<MotherboardEntity> getSystemMotherboards(UUID... Ids);

    @Update(onConflict = OnConflictStrategy.FAIL)
    public abstract void update(MotherboardEntity motherboardEntities);

    @Update(onConflict = OnConflictStrategy.FAIL)
    public abstract void update(MotherboardEntity... cpuEntities);

    @Delete
    public abstract void delete(MotherboardEntity motherboardEntity);

    @Delete
    public abstract void delete(MotherboardEntity... motherboardEntities);

    public void upsert(MotherboardEntity motherboardEntitiy) {
        try {
            insert(motherboardEntitiy);
        } catch (SQLiteConstraintException exception) {
            update(motherboardEntitiy);
        }
    }

}
