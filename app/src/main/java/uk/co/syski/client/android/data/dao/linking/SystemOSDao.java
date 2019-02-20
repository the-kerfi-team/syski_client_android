package uk.co.syski.client.android.data.dao.linking;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import uk.co.syski.client.android.data.entity.linking.SystemOS;

@Dao
public interface SystemOSDao {
    @Insert
    void InsertAll(SystemOS... SystemOSs);
}
