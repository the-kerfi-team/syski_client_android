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
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.model.api.VolleySingleton;
import uk.co.syski.client.android.model.api.requests.system.APISystemStorageRequest;
import uk.co.syski.client.android.model.database.SyskiCache;
import uk.co.syski.client.android.model.database.dao.StorageDao;
import uk.co.syski.client.android.model.database.dao.linking.SystemStorageDao;
import uk.co.syski.client.android.model.database.entity.StorageEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemStorageEntity;
import uk.co.syski.client.android.model.viewmodel.SystemStorageModel;

public enum StorageRepository {
    INSTANCE;

    // Database DAO's
    private StorageDao mStorageDao;
    private SystemStorageDao mSystemStorageDao;

    // Storage Cache in Memory
    private HashMap<UUID, StorageEntity> mStorageEntities;
    private HashMap<UUID, List<SystemStorageEntity>> mSystemStorageEntities;
    private HashMap<UUID, List<SystemStorageModel>> mSystemStorageModels;

    // LiveData
    private MutableLiveData<HashMap<UUID, List<SystemStorageModel>>> mLiveDataSystemStorageEntities;

    StorageRepository() {
        mStorageDao = SyskiCache.GetDatabase().StorageDao();
        mSystemStorageDao = SyskiCache.GetDatabase().SystemStorageDao();

        mStorageEntities = new HashMap<>();
        mSystemStorageEntities = new HashMap<>();
        mSystemStorageModels = new HashMap<>();

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
            for (Map.Entry<UUID, List<SystemStorageEntity>> entry : mSystemStorageEntities.entrySet())
            {
                List<SystemStorageModel> StorageModels = mSystemStorageModels.get(entry.getKey());
                if (StorageModels == null)
                {
                    StorageModels = new LinkedList<>();
                }
                for (SystemStorageEntity systemStorageEntity : entry.getValue())
                {
                    StorageEntity storageEntity = mStorageEntities.get(systemStorageEntity.StorageId);
                    StorageModels.add(new SystemStorageModel(storageEntity.ModelName, storageEntity.ManufacturerName, storageEntity.MemoryTypeName, storageEntity.MemoryBytes));
                }
                mSystemStorageModels.put(entry.getKey(), StorageModels);
            }
            mLiveDataSystemStorageEntities.postValue(mSystemStorageModels);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void loadFromAPI(final Context context, final UUID systemId)
    {
        //TODO Load from API every user defined time.
        VolleySingleton.getInstance(context).addToRequestQueue(new APISystemStorageRequest(context, systemId));
    }

    public LiveData<List<SystemStorageModel>> getSystemStoragesLiveData(final UUID systemId, Context context)
    {
        loadFromAPI(context, systemId);
        return Transformations.map(mLiveDataSystemStorageEntities, new Function<HashMap<UUID, List<SystemStorageModel>>, List<SystemStorageModel>>() {
            @Override
            public List<SystemStorageModel> apply(HashMap<UUID, List<SystemStorageModel>> input) {
                List<SystemStorageModel> storageModelList = input.get(systemId);
                if (storageModelList == null)
                {
                    storageModelList = new LinkedList<>();
                }
                if (storageModelList.isEmpty())
                {
                    storageModelList.add(new SystemStorageModel());
                }
                return storageModelList;
            }
        });
    }

    public StorageEntity get(UUID id)
    {
        return mStorageEntities.get(id);
    }

    public void upsert(UUID systemId, List<SystemStorageEntity> systemStorageEntities, List<StorageEntity> storageEntities)
    {
        try {
            new upsertStorageAsyncTask(mStorageDao, storageEntities).execute().get();
            new upsertSystemStorageAsyncTask(mSystemStorageDao, systemId, systemStorageEntities).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<SystemStorageModel> systemStorageModels = new LinkedList<>();
        for (StorageEntity storageEntity : storageEntities)
        {
            mStorageEntities.put(storageEntity.Id, storageEntity);
            systemStorageModels.add(new SystemStorageModel(storageEntity.ModelName, storageEntity.ManufacturerName, storageEntity.MemoryTypeName, storageEntity.MemoryBytes));
        }
        mSystemStorageModels.put(systemId, systemStorageModels);
        mLiveDataSystemStorageEntities.postValue(mSystemStorageModels);
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

    private static class loadSystemStorageEntitiesAsyncTask extends AsyncTask<Void, Void, HashMap<UUID, List<SystemStorageEntity>>> {

        private SystemStorageDao mSystemStorageDao;
        private HashMap<UUID, List<SystemStorageEntity>> mSystemStorageEntities;

        loadSystemStorageEntitiesAsyncTask(SystemStorageDao systemStorageDao, HashMap<UUID, List<SystemStorageEntity>> systemStorageEntities) {
            mSystemStorageDao = systemStorageDao;
            mSystemStorageEntities = systemStorageEntities;
        }

        protected HashMap<UUID, List<SystemStorageEntity>> doInBackground(final Void... voids) {
            for (SystemStorageEntity SystemStorageEntity : mSystemStorageDao.get())
            {
                List<SystemStorageEntity> StorageEntities = mSystemStorageEntities.get(SystemStorageEntity.SystemId);
                if (StorageEntities == null)
                {
                    StorageEntities = new LinkedList<>();
                }
                StorageEntities.add(SystemStorageEntity);
                mSystemStorageEntities.put(SystemStorageEntity.SystemId, StorageEntities);
            }
            return mSystemStorageEntities;
        }

    }

    private static class upsertStorageAsyncTask extends AsyncTask<Void , Void, Void> {

        private StorageDao mStorageDao;
        private List<StorageEntity> mStorageEntities;

        upsertStorageAsyncTask(StorageDao storageDao, List<StorageEntity> storageEntities) {
            mStorageDao = storageDao;
            mStorageEntities = storageEntities;
        }

        protected Void doInBackground(final Void... voids) {
            for (StorageEntity storageEntity: mStorageEntities) {
                mStorageDao.upsert(storageEntity);
            }
            return null;
        }

    }

    private static class upsertSystemStorageAsyncTask extends AsyncTask<Void , Void, Void> {

        private SystemStorageDao mSystemStorageDao;
        private UUID mSystemId;
        private List<SystemStorageEntity> mSystemStorageEntities;

        upsertSystemStorageAsyncTask(SystemStorageDao systemStorageDao, UUID systemId, List<SystemStorageEntity> systemStorageEntities) {
            mSystemStorageDao = systemStorageDao;
            mSystemId = systemId;
            mSystemStorageEntities = systemStorageEntities;
        }

        protected Void doInBackground(final Void... voids) {
            mSystemStorageDao.deleteBySystemId(mSystemId);
            for (SystemStorageEntity systemStorageEntity: mSystemStorageEntities) {
                mSystemStorageDao.insert(systemStorageEntity);
            }
            return null;
        }

    }

}