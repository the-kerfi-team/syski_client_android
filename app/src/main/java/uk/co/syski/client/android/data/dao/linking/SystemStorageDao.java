package uk.co.syski.client.android.data.dao.linking;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import uk.co.syski.client.android.data.entity.linking.SystemGPUEntity;
import uk.co.syski.client.android.data.entity.linking.SystemStorageEntity;

@Dao
public interface SystemStorageDao {
    @Insert
    void InsertAll(SystemStorageEntity... storageEntities);

    @Insert
    void insert(SystemStorageEntity... systemStorageEntities);
}
