package uk.co.syski.client.android.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import uk.co.syski.client.android.data.dao.CPUDao;
import uk.co.syski.client.android.data.dao.SystemDao;
import uk.co.syski.client.android.data.dao.TypeDao;
import uk.co.syski.client.android.data.dao.UserDao;
import uk.co.syski.client.android.data.dao.linking.SystemCPUDao;
import uk.co.syski.client.android.data.dao.linking.SystemTypeDao;
import uk.co.syski.client.android.data.entity.CPU;
import uk.co.syski.client.android.data.entity.System;
import uk.co.syski.client.android.data.entity.Type;
import uk.co.syski.client.android.data.entity.User;
import uk.co.syski.client.android.data.entity.linking.SystemCPU;
import uk.co.syski.client.android.data.entity.linking.SystemType;

@Database(
    entities = {
        User.class,
        System.class,
        SystemType.class,
        Type.class,
        CPU.class,
        SystemCPU.class
    },
    version = 1
)
@TypeConverters({Converters.class})
public abstract class CacheDatabase extends RoomDatabase {
    public abstract UserDao UserDao();
    public abstract SystemDao SystemDao();
    public abstract SystemTypeDao SystemTypeDao();
    public abstract TypeDao TypeDao();
    public abstract CPUDao CPUDao();
    public abstract SystemCPUDao SystemCPUDao();
}

