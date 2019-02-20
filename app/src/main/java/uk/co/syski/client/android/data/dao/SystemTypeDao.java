package uk.co.syski.client.android.data.dao;

import java.util.List;
import java.util.UUID;
import android.arch.persistence.room.*;

import uk.co.syski.client.android.data.entity.SystemType;

@Dao
public interface SystemTypeDao {
    @Query("SELECT TypeId FROM SystemType WHERE SystemId IN (:SystemIds)")
    List<UUID> getTypeIds(UUID... SystemIds);

    @Insert
    void InsertAll(SystemType... SystemTypes);

    @Delete
    void DeleteAll(SystemType... SystemTypes);
}
