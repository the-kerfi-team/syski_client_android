package uk.co.syski.client.android.data.system;

import java.util.List;
import java.util.UUID;
import android.arch.persistence.room.*;

@Dao
public interface SystemDao {
    @Query("SELECT * FROM System")
    List<System> indexSystems();

    @Query("SELECT * FROM System WHERE Id IN (:Ids)")
    List<System> getSystems(UUID... Ids);

    @Insert
    void InsertAll(System... Systems);

    @Delete
    void DeleteAll(System... Systems);
}
