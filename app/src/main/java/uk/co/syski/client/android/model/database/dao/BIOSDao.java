package uk.co.syski.client.android.model.database.dao;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.sqlite.SQLiteConstraintException;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.model.database.entity.BIOSEntity;
import uk.co.syski.client.android.model.database.entity.CPUEntity;

public abstract class BIOSDao {

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public abstract void insert(BIOSEntity biosEntity);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public abstract void insert(BIOSEntity... biosEntities);

    @Query("SELECT * FROM CPUEntity")
    public abstract List<BIOSEntity> get();

    @Query("SELECT * FROM CPUEntity WHERE Id == :Id")
    public abstract BIOSEntity get(UUID Id);

    @Query("SELECT * FROM CPUEntity WHERE Id in (:Ids)")
    public abstract List<BIOSEntity> get(UUID... Ids);

    @Query("SELECT * FROM BIOSEntity INNER JOIN SystemBIOSEntity ON Id = BIOSId WHERE SystemId IN (:Ids)")
    public abstract List<BIOSEntity> getSystemBIOSs(UUID... Ids);

    @Update(onConflict = OnConflictStrategy.FAIL)
    public abstract void update(BIOSEntity biosEntity);

    @Update(onConflict = OnConflictStrategy.FAIL)
    public abstract void update(BIOSEntity... biosEntities);

    @Delete
    public abstract void delete(BIOSEntity biosEntity);

    @Delete
    public abstract void delete(BIOSEntity... biosEntities);

    public void upsert(BIOSEntity biosEntity) {
        try {
            insert(biosEntity);
        } catch (SQLiteConstraintException exception) {
            update(biosEntity);
        }
    }

}
