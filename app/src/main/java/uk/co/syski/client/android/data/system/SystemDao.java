package uk.co.syski.client.android.data.system;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;
import java.util.UUID;

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
