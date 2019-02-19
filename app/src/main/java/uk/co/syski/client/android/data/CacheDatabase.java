package uk.co.syski.client.android.data;

import android.arch.persistence.room.*;

import uk.co.syski.client.android.data.dao.SystemTypeDao;
import uk.co.syski.client.android.data.dao.TypeDao;
import uk.co.syski.client.android.data.entity.System;
import uk.co.syski.client.android.data.dao.SystemDao;
import uk.co.syski.client.android.data.entity.SystemType;
import uk.co.syski.client.android.data.entity.Type;

@Database(
    entities = {
        System.class,
        SystemType.class,
        Type.class
    },
    version = 1
)
@TypeConverters({Converters.class})
public abstract class CacheDatabase extends RoomDatabase {
    public abstract SystemDao SystemDao();
    public abstract SystemTypeDao SystemTypeDao();
    public abstract TypeDao TypeDao();
}

