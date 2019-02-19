package uk.co.syski.client.android.data.dao;

import java.util.List;
import java.util.UUID;
import android.arch.persistence.room.*;

import uk.co.syski.client.android.data.entity.SystemType;

@Dao
public interface SystemTypeDao {
    @Insert
    void InsertAll(SystemType... SystemTypes);

    @Delete
    void DeleteAll(SystemType... SystemTypes);
}
