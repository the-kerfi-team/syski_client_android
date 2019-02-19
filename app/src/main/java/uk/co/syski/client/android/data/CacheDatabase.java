package uk.co.syski.client.android.data;

import android.arch.persistence.room.*;

import uk.co.syski.client.android.data.dao.CPUDao;
import uk.co.syski.client.android.data.dao.SystemCPUDao;
import uk.co.syski.client.android.data.dao.SystemTypeDao;
import uk.co.syski.client.android.data.dao.TypeDao;
import uk.co.syski.client.android.data.entity.CPU;
import uk.co.syski.client.android.data.entity.System;
import uk.co.syski.client.android.data.dao.SystemDao;
import uk.co.syski.client.android.data.entity.SystemCPU;
import uk.co.syski.client.android.data.entity.SystemType;
import uk.co.syski.client.android.data.entity.Type;

@Database(
    entities = {
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
    public abstract SystemDao SystemDao();
    public abstract SystemTypeDao SystemTypeDao();
    public abstract TypeDao TypeDao();
    public abstract CPUDao CPUDao();
    public abstract SystemCPUDao SystemCPUDao();
}

