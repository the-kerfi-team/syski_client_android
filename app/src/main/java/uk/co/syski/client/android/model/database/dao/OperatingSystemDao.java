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

import uk.co.syski.client.android.model.database.entity.OperatingSystemEntity;

@Dao
public abstract class OperatingSystemDao {

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public abstract void insert(OperatingSystemEntity osEntity);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public abstract void insert(OperatingSystemEntity... osEntities);

    @Query("SELECT * FROM OperatingSystemEntity")
    public abstract List<OperatingSystemEntity> get();

    @Query("SELECT * FROM OperatingSystemEntity WHERE Id == :Id")
    public abstract OperatingSystemEntity get(UUID Id);

    @Query("SELECT * FROM OperatingSystemEntity WHERE Id in (:Ids)")
    public abstract List<OperatingSystemEntity> get(UUID... Ids);

    //@Query("SELECT * FROM OperatingSystemEntity INNER JOIN SystemOSEntity ON Id = OSId WHERE SystemId IN (:Ids)")
    //public abstract List<OperatingSystemModel> getSystemOperatingSystems(UUID... Ids);

    @Update
    public abstract void update (OperatingSystemEntity osEntity);

    @Update
    public abstract void update (OperatingSystemEntity... osEntities);

    @Delete
    public abstract void delete(OperatingSystemEntity osEntity);

    @Delete
    public abstract void delete(OperatingSystemEntity... osEntities);

    public void upsert(OperatingSystemEntity osEntity) {
        try {
            insert(osEntity);
        } catch (SQLiteConstraintException exception) {
            update(osEntity);
        }
    }

}
