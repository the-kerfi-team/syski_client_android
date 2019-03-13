package uk.co.syski.client.android.data.dao.linking;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.UUID;

import uk.co.syski.client.android.data.entity.linking.SystemGPUEntity;
import uk.co.syski.client.android.data.entity.linking.SystemRAMEntity;
import uk.co.syski.client.android.data.entity.linking.SystemStorageEntity;

@Dao
public interface SystemStorageDao {

    @Insert
    void insert(SystemStorageEntity... systemStorageEntities);

    @Query("SELECT * FROM SystemStorageEntity WHERE SystemId = :id AND Slot = :slot")
    SystemStorageEntity get(UUID id, int slot);
}
