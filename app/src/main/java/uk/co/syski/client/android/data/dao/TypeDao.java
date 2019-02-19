package uk.co.syski.client.android.data.dao;

import java.util.List;
import java.util.UUID;
import android.arch.persistence.room.*;

import uk.co.syski.client.android.data.entity.Type;

@Dao
public interface TypeDao {
    @Query("SELECT Name FROM Type INNER JOIN SystemType WHERE SystemId IN (:Ids)")
    List<String> getTypeNames(UUID... Ids);

    @Insert
    void InsertAll(Type... Types);

    @Delete
    void DeleteAll(Type... Types);

}
