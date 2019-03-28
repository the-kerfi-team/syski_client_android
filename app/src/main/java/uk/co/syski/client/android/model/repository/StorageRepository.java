package uk.co.syski.client.android.model.repository;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.os.AsyncTask;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.model.api.VolleySingleton;
import uk.co.syski.client.android.model.api.requests.system.APISystemStorageRequest;
import uk.co.syski.client.android.model.database.SyskiCache;
import uk.co.syski.client.android.model.database.dao.StorageDao;
import uk.co.syski.client.android.model.database.dao.linking.SystemStorageDao;
import uk.co.syski.client.android.model.database.entity.StorageEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemStorageEntity;

public enum StorageRepository {
    INSTANCE;

    // Database DAO's
    private StorageDao mStorageDao;
    private SystemStorageDao mSystemStorageDao;

    // Storage Cache in Memory
    private HashMap<UUID, StorageEntity> mStorageEntities;
    private HashMap<UUID, List<UUID>> mSystemStorageEntities;

    // LiveData
    private MutableLiveData<HashMap<UUID, StorageEntity>> mLiveDataSystemStorageEntities;

    StorageRepository() {
        mStorageDao = SyskiCache.GetDatabase().StorageDao();
        mSystemStorageDao = SyskiCache.GetDatabase().SystemStorageDao();

        mStorageEntities = new HashMap<>();
        mSystemStorageEntities = new HashMap<>();

        mLiveDataSystemStorageEntities = new MutableLiveData<>();
        loadFromDatabase();
    }

    private void loadFromDatabase()
    {
        try {
            // Load data from Database for Storage's
            mStorageEntities = new loadStorageEntitiesAsyncTask(mStorageDao, mStorageEntities).execute().get();

            // Load data from Database for System Storage's
            mSystemStorageEntities = new loadSystemStorageEntitiesAsyncTask(mSystemStorageDao, mSystemStorageEntities).execute().get();

            // Set Data in LiveData
            mLiveDataSystemStorageEntities.postValue(mStorageEntities);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void loadFromAPI(Context context, UUID systemId)
    {
        //TODO Load from API every user defined time.
        VolleySingleton.getInstance(context).addToRequestQueue(new APISystemStorageRequest(context, systemId));
    }

    public void insert(StorageEntity storageEntity)
    {
        //TODO Create insert method
    }

    public void insert(UUID systemId, StorageEntity storageEntity)
    {
        //TODO Create insert method
    }

    public void insert(UUID systemId, List<StorageEntity> storageEntities)
    {
        //TODO Create insert method
    }

    public LiveData<List<StorageEntity>> getSystemStoragesLiveData(final UUID systemId, Context context)
    {
        loadFromAPI(context, systemId);
        return Transformations.map(mLiveDataSystemStorageEntities, new Function<HashMap<UUID, StorageEntity>, List<StorageEntity>>() {
            @Override
            public List<StorageEntity> apply(HashMap<UUID, StorageEntity> input) {
                List<StorageEntity> result = new LinkedList<>();
                List<UUID> systemStorageEntities = mSystemStorageEntities.get(systemId);
                if (systemStorageEntities != null)
                {
                    for (UUID systemEntityId : systemStorageEntities)
                    {
                        result.add(input.get(systemEntityId));
                    }
                }
                return result;
            }
        });
    }

    public List<StorageEntity> getSystemStorages(UUID systemId)
    {
        //TODO Create get System Component method
        return null;
    }

    public StorageEntity get(UUID id)
    {
        return mStorageEntities.get(id);
    }

    public List<StorageEntity> getAll()
    {
        //TODO Create get Components method
        return null;
    }

    public void update(StorageEntity storageEntity)
    {
        //TODO Create update Component method
    }

    public void update(UUID systemId, StorageEntity storageEntity)
    {
        //TODO Create update Components method
    }

    public void update(UUID systemId, List<StorageEntity> storageEntities)
    {
        //TODO Create update Components method
    }

    public void upsert(StorageEntity storageEntity)
    {
        //TODO Create upsert Component method
    }

    public void upsert(UUID systemId, StorageEntity storageEntity)
    {
        //TODO Create upsert Components method
    }

    public void upsert(UUID systemId, List<StorageEntity> storageEntities)
    {
        try {
            new upsertSystemStorageAsyncTask(mStorageDao, mSystemStorageDao, systemId, storageEntities).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        List<UUID> systemStorageEntities = new LinkedList<>();
        for (StorageEntity storageEntity : storageEntities)
        {
            systemStorageEntities.add(storageEntity.Id);
        }
        mSystemStorageEntities.put(systemId, systemStorageEntities);
        mLiveDataSystemStorageEntities.postValue(mStorageEntities);
    }

    public void delete(StorageEntity storageEntity)
    {
        //TODO Create delete Component method
    }

    public void delete(UUID systemId, StorageEntity storageEntity)
    {
        //TODO Create delete Component method
    }

    public void delete(UUID systemId, List<StorageEntity> storageEntities)
    {
        //TODO Create delete Component method
    }

    private static class loadStorageEntitiesAsyncTask extends AsyncTask<Void, Void, HashMap<UUID, StorageEntity>> {

        private StorageDao mStorageDao;
        private HashMap<UUID, StorageEntity> mStorageEntities;


        loadStorageEntitiesAsyncTask(StorageDao storageDao, HashMap<UUID, StorageEntity> StorageEntities) {
            mStorageDao = storageDao;
            mStorageEntities = StorageEntities;
        }

        protected HashMap<UUID, StorageEntity> doInBackground(final Void... voids) {
            for (StorageEntity storageEntity : mStorageDao.get())
            {
                mStorageEntities.put(storageEntity.Id, storageEntity);
            }
            return mStorageEntities;
        }

    }

    private static class loadSystemStorageEntitiesAsyncTask extends AsyncTask<Void, Void, HashMap<UUID, List<UUID>>> {

        private SystemStorageDao mSystemStorageDao;
        private HashMap<UUID, List<UUID>> mSystemStorageEntities;


        loadSystemStorageEntitiesAsyncTask(SystemStorageDao systemStorageDao, HashMap<UUID, List<UUID>> systemStorageEntities) {
            mSystemStorageDao = systemStorageDao;
            mSystemStorageEntities = systemStorageEntities;
        }

        protected HashMap<UUID, List<UUID>> doInBackground(final Void... voids) {
            for (SystemStorageEntity SystemStorageEntity : mSystemStorageDao.get())
            {
                List<UUID> StorageEntities = mSystemStorageEntities.get(SystemStorageEntity.SystemId);
                if (StorageEntities == null)
                {
                    StorageEntities = new LinkedList<>();
                }
                StorageEntities.add(SystemStorageEntity.StorageId);
                mSystemStorageEntities.put(SystemStorageEntity.SystemId, StorageEntities);
            }
            return mSystemStorageEntities;
        }

    }

    private static class upsertSystemStorageAsyncTask extends AsyncTask<Void , Void, Void> {

        private StorageDao mStorageDao;
        private SystemStorageDao mSystemStorageDao;
        private UUID mSystemId;

        private List<StorageEntity> mStorageEntities;

        upsertSystemStorageAsyncTask(StorageDao storageDao, SystemStorageDao systemStorageDao, UUID systemId, List<StorageEntity> storageEntities) {
            mStorageDao = storageDao;
            mSystemStorageDao = systemStorageDao;
            mSystemId = systemId;

            mStorageEntities = storageEntities;
        }

        protected Void doInBackground(final Void... voids) {
            mSystemStorageDao.deleteBySystemId(mSystemId);
            int i = 0;
            for (StorageEntity storageEntity: mStorageEntities) {
                mStorageDao.upsert(storageEntity);
                SystemStorageEntity systemStorageEntity = new SystemStorageEntity();
                systemStorageEntity.SystemId = mSystemId;
                systemStorageEntity.StorageId = storageEntity.Id;
                systemStorageEntity.Slot = i;
                mSystemStorageDao.insert(systemStorageEntity);
            }
            return null;
        }

    }

}
