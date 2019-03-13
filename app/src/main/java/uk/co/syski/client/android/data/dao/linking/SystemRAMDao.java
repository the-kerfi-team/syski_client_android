package uk.co.syski.client.android.data.dao.linking;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.sqlite.SQLiteConstraintException;

import java.util.UUID;

import uk.co.syski.client.android.data.entity.linking.SystemRAMEntity;

@Dao
public abstract class SystemRAMDao {
    @Insert(onConflict = OnConflictStrategy.FAIL)
    public abstract void insert(SystemRAMEntity... systemRAMEntities);

    @Query("SELECT * FROM SystemRAMEntity WHERE SystemId = :id AND DimmSlot = :slot")
    public abstract SystemRAMEntity get(UUID id, int slot);

    @Update(onConflict = OnConflictStrategy.FAIL)
    public abstract void update(SystemRAMEntity... systemRAMEntities);

    public void upsert(SystemRAMEntity systemRAMEntity) {
        try {
            insert(systemRAMEntity);
        } catch (SQLiteConstraintException exception) {
            update(systemRAMEntity);
        }
    }
}
