package uk.co.syski.client.android.data.dao.linking;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.UUID;

import uk.co.syski.client.android.data.entity.linking.SystemRAMEntity;

@Dao
public interface SystemRAMDao {
    @Insert
    void insert(SystemRAMEntity... systemRAMEntities);

    @Query("SELECT * FROM SystemRAMEntity WHERE SystemId = :id AND DimmSlot = :slot")
    SystemRAMEntity get(UUID id, int slot);

    @Update
    void update(SystemRAMEntity... systemRAMEntities);
}
