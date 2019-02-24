package uk.co.syski.client.android.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import uk.co.syski.client.android.data.dao.*;
import uk.co.syski.client.android.data.dao.linking.*;
import uk.co.syski.client.android.data.entity.*;
import uk.co.syski.client.android.data.entity.linking.*;

@Database(
    entities = {
        UserEntity.class,
        SystemEntity.class,
        SystemTypeEntity.class,
        TypeEntity.class,
        CPUEntity.class,
        SystemCPUEntity.class,
        OperatingSystemEntity.class,
        SystemOSEntity.class,
        RAMEntity.class,
        SystemRAMEntity.class,
        GPUEntity.class,
        SystemGPUEntity.class,
        StorageEntity.class,
        SystemStorageEntity.class
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
    public abstract OperatingSystemDao OperatingSystemDao();
    public abstract SystemOSDao SystemOSDao();
    public abstract RAMDao RAMDao();
    public abstract SystemRAMDao SystemRAMDao();
    public abstract GPUDao GPUDao();
    public abstract SystemGPUDao SystemGPUDao();
    public abstract StorageDao StorageDao();
    public abstract SystemStorageDao SystemStorageDao();
}

