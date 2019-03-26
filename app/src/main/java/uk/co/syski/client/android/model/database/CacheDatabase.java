package uk.co.syski.client.android.model.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import uk.co.syski.client.android.model.database.dao.CPUDao;
import uk.co.syski.client.android.model.database.dao.GPUDao;
import uk.co.syski.client.android.model.database.dao.MotherboardDao;
import uk.co.syski.client.android.model.database.dao.OperatingSystemDao;
import uk.co.syski.client.android.model.database.dao.RAMDao;
import uk.co.syski.client.android.model.database.dao.StorageDao;
import uk.co.syski.client.android.model.database.dao.SystemDao;
import uk.co.syski.client.android.model.database.dao.TypeDao;
import uk.co.syski.client.android.model.database.dao.UserDao;
import uk.co.syski.client.android.model.database.dao.linking.SystemCPUDao;
import uk.co.syski.client.android.model.database.dao.linking.SystemGPUDao;
import uk.co.syski.client.android.model.database.dao.linking.SystemOSDao;
import uk.co.syski.client.android.model.database.dao.linking.SystemRAMDao;
import uk.co.syski.client.android.model.database.dao.linking.SystemStorageDao;
import uk.co.syski.client.android.model.database.dao.linking.SystemTypeDao;
import uk.co.syski.client.android.model.database.entity.CPUEntity;
import uk.co.syski.client.android.model.database.entity.GPUEntity;
import uk.co.syski.client.android.model.database.entity.MotherboardEntity;
import uk.co.syski.client.android.model.database.entity.OperatingSystemEntity;
import uk.co.syski.client.android.model.database.entity.RAMEntity;
import uk.co.syski.client.android.model.database.entity.StorageEntity;
import uk.co.syski.client.android.model.database.entity.SystemEntity;
import uk.co.syski.client.android.model.database.entity.TypeEntity;
import uk.co.syski.client.android.model.database.entity.UserEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemCPUEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemGPUEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemOSEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemRAMEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemStorageEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemTypeEntity;

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
        SystemStorageEntity.class,
        MotherboardEntity.class
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
    public abstract MotherboardDao MotherboardDao();
}

