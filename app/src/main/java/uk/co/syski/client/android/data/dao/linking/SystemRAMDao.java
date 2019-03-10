package uk.co.syski.client.android.data.dao.linking;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import uk.co.syski.client.android.data.entity.linking.SystemRAMEntity;

@Dao
public interface SystemRAMDao {
    @Insert
    void Insert(SystemRAMEntity... systemRAMEntities);
}
