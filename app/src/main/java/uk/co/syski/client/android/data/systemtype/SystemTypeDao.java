package uk.co.syski.client.android.data.systemtype;

import java.util.List;
import java.util.UUID;
import android.arch.persistence.room.*;

@Dao
public interface SystemTypeDao {
    @Query("SELECT TypeId FROM SystemType WHERE SystemId = (:SystemId)")
    List<UUID> getTypeIds(UUID SystemId);

    @Insert
    void InsertAll(SystemType... SystemTypes);

    @Delete
    void DeleteAll(SystemType... SystemTypes);
}
