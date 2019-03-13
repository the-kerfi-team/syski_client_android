package uk.co.syski.client.android.data.dao.linking;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.sqlite.SQLiteConstraintException;

import java.util.UUID;

import uk.co.syski.client.android.data.entity.linking.SystemStorageEntity;

@Dao
public abstract class SystemStorageDao {

    @Insert
    public abstract void insert(SystemStorageEntity... systemStorageEntities);

    @Query("SELECT * FROM SystemStorageEntity WHERE SystemId = :id AND Slot = :slot")
    public abstract SystemStorageEntity get(UUID id, int slot);

    @Update(onConflict = OnConflictStrategy.FAIL)
    public abstract void update(SystemStorageEntity... systemStorageEntities);

    public void upsert(SystemStorageEntity systemStorageEntity) {
        try {
            insert(systemStorageEntity);
        } catch (SQLiteConstraintException exception) {
            update(systemStorageEntity);
        }
    }
}
