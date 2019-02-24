package uk.co.syski.client.android.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import uk.co.syski.client.android.data.dao.CPUDao;
import uk.co.syski.client.android.data.dao.GPUDao;
import uk.co.syski.client.android.data.dao.OperatingSystemDao;
import uk.co.syski.client.android.data.dao.RAMDao;
import uk.co.syski.client.android.data.dao.SystemDao;
import uk.co.syski.client.android.data.dao.TypeDao;
import uk.co.syski.client.android.data.dao.UserDao;
import uk.co.syski.client.android.data.dao.linking.SystemCPUDao;
import uk.co.syski.client.android.data.dao.linking.SystemGPUDao;
import uk.co.syski.client.android.data.dao.linking.SystemOSDao;
import uk.co.syski.client.android.data.dao.linking.SystemRAMDao;
import uk.co.syski.client.android.data.dao.linking.SystemTypeDao;
import uk.co.syski.client.android.data.entity.CPUEntity;
import uk.co.syski.client.android.data.entity.GPUEntity;
import uk.co.syski.client.android.data.entity.OperatingSystemEntity;
import uk.co.syski.client.android.data.entity.RAMEntity;
import uk.co.syski.client.android.data.entity.SystemEntity;
import uk.co.syski.client.android.data.entity.TypeEntity;
import uk.co.syski.client.android.data.entity.UserEntity;
import uk.co.syski.client.android.data.entity.linking.SystemCPUEntity;
import uk.co.syski.client.android.data.entity.linking.SystemGPUEntity;
import uk.co.syski.client.android.data.entity.linking.SystemOSEntity;
import uk.co.syski.client.android.data.entity.linking.SystemRAMEntity;
import uk.co.syski.client.android.data.entity.linking.SystemTypeEntity;

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
        SystemGPUEntity.class
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
}

