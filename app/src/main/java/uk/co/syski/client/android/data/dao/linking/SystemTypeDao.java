package uk.co.syski.client.android.data.dao.linking;

import android.arch.persistence.room.*;

import uk.co.syski.client.android.data.entity.linking.SystemType;

@Dao
public interface SystemTypeDao {
    @Insert
    void InsertAll(SystemType... SystemTypes);
}
