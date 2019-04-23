package uk.co.syski.client.android.model.database.dao.linking;

import android.arch.persistence.room.*;

import uk.co.syski.client.android.model.database.entity.linking.SystemTypeEntity;

@Dao
public interface SystemTypeDao {
    @Insert
    void InsertAll(SystemTypeEntity... systemTypeEntities);
}
