package uk.co.syski.client.android.data.type;

import java.util.List;
import java.util.UUID;

import androidx.room.*;

@Dao
public interface TypeDao {
    @Query("SELECT Name FROM Type WHERE Id IN (:Ids)")
    List<String> getTypeNames(UUID... Ids);

    @Insert
    void InsertAll(Type... Types);

    @Delete
    void DeleteAll(Type... Types);

}
