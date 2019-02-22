package uk.co.syski.client.android.data.dao;

import java.util.List;
import java.util.UUID;
import android.arch.persistence.room.*;

import uk.co.syski.client.android.data.entity.TypeEntity;

@Dao
public interface TypeDao {
    @Query("SELECT Name FROM TypeEntity INNER JOIN SystemTypeEntity WHERE SystemId IN (:Ids)")
    List<String> getTypeNames(UUID... Ids);

    @Insert
    void InsertAll(TypeEntity... typeEntities);

    @Delete
    void DeleteAll(TypeEntity... typeEntities);

}
