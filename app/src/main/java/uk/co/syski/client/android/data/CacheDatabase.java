package uk.co.syski.client.android.data;

import androidx.room.*;
import uk.co.syski.client.android.data.system.System;
import uk.co.syski.client.android.data.system.SystemDao;
import uk.co.syski.client.android.data.systemtype.*;
import uk.co.syski.client.android.data.type.*;

@Database(
    entities = {
        System.class,
        SystemType.class,
        Type.class
    },
    version = 1
)
public abstract class CacheDatabase extends RoomDatabase {
    public abstract SystemDao SystemDao();
    public abstract SystemTypeDao SystemTypeDao();
    public abstract TypeDao TypeDao();
}

