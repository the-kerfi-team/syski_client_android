package uk.co.syski.client.android.data.dao.linking;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import uk.co.syski.client.android.data.entity.linking.SystemOSEntity;

@Dao
public interface SystemOSDao {
    @Insert
    void InsertAll(SystemOSEntity... systemOSEntities);

    @Insert
    void insert(SystemOSEntity... systemOSEntities);
}
