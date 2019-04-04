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
import uk.co.syski.client.android.model.api.requests.system.APISystemRAMRequest;
import uk.co.syski.client.android.model.database.SyskiCache;
import uk.co.syski.client.android.model.database.dao.RAMDao;
import uk.co.syski.client.android.model.database.dao.linking.SystemRAMDao;
import uk.co.syski.client.android.model.database.entity.RAMEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemRAMEntity;
import uk.co.syski.client.android.model.viewmodel.SystemRAMModel;

public enum RAMRepository {
    INSTANCE;

    // Database DAO's
    private RAMDao mRAMDao;
    private SystemRAMDao mSystemRAMDao;

    // RAM Cache in Memory
    private HashMap<UUID, RAMEntity> mRAMEntities;
    private HashMap<UUID, List<SystemRAMEntity>> mSystemRAMEntities;
    private HashMap<UUID, List<SystemRAMModel>> mSystemRAMModels;

    // LiveData
    private MutableLiveData<HashMap<UUID, List<SystemRAMModel>>> mLiveDataSystemRAMEntities;

    RAMRepository() {
        mRAMDao = SyskiCache.GetDatabase().RAMDao();
        mSystemRAMDao = SyskiCache.GetDatabase().SystemRAMDao();

        mRAMEntities = new HashMap<>();
        mSystemRAMEntities = new HashMap<>();
        mSystemRAMModels = new HashMap<>();

        mLiveDataSystemRAMEntities = new MutableLiveData<>();
        loadFromDatabase();
    }

    private void loadFromDatabase()
    {
        try {
            // Load data from Database for RAM's
            mRAMEntities = new loadRAMEntitiesAsyncTask(mRAMDao, mRAMEntities).execute().get();

            // Load data from Database for System RAM's
            mSystemRAMEntities = new loadSystemRAMEntitiesAsyncTask(mSystemRAMDao, mSystemRAMEntities).execute().get();

            // Set Data in LiveData
            for (Map.Entry<UUID, List<SystemRAMEntity>> entry : mSystemRAMEntities.entrySet())
            {
                List<SystemRAMModel> RAMModels = mSystemRAMModels.get(entry.getKey());
                if (RAMModels == null)
                {
                    RAMModels = new LinkedList<>();
                }
                for (SystemRAMEntity systemRAMEntity : entry.getValue())
                {
                    RAMEntity ramEntity = mRAMEntities.get(systemRAMEntity.RAMId);
                    RAMModels.add(new SystemRAMModel(ramEntity.ModelName, ramEntity.ManufacturerName, ramEntity.MemoryTypeName, ramEntity.MemoryBytes));
                }
                mSystemRAMModels.put(entry.getKey(), RAMModels);
            }
            mLiveDataSystemRAMEntities.postValue(mSystemRAMModels);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void loadFromAPI(final Context context, final UUID systemId)
    {
        //TODO Load from API every user defined time.
        VolleySingleton.getInstance(context).addToRequestQueue(new APISystemRAMRequest(context, systemId));
    }

    public LiveData<List<SystemRAMModel>> getSystemRAMsLiveData(final UUID systemId, Context context)
    {
        loadFromAPI(context, systemId);
        return Transformations.map(mLiveDataSystemRAMEntities, new Function<HashMap<UUID, List<SystemRAMModel>>, List<SystemRAMModel>>() {
            @Override
            public List<SystemRAMModel> apply(HashMap<UUID, List<SystemRAMModel>> input) {
                List<SystemRAMModel> ramModelList = input.get(systemId);
                if (ramModelList == null)
                {
                    ramModelList = new LinkedList<>();
                }
                if (ramModelList.isEmpty())
                {
                    ramModelList.add(new SystemRAMModel());
                }
                return ramModelList;
            }
        });
    }

    public RAMEntity get(UUID id)
    {
        return mRAMEntities.get(id);
    }

    public void upsert(UUID systemId, List<SystemRAMEntity> systemRAMEntities, List<RAMEntity> ramEntities)
    {
        try {
            new upsertRAMAsyncTask(mRAMDao, ramEntities).execute().get();
            new upsertSystemRAMAsyncTask(mSystemRAMDao, systemId, systemRAMEntities).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<SystemRAMModel> systemRAMModels = new LinkedList<>();
        for (RAMEntity ramEntity : ramEntities)
        {
            mRAMEntities.put(ramEntity.Id, ramEntity);
            systemRAMModels.add(new SystemRAMModel(ramEntity.ModelName, ramEntity.ManufacturerName, ramEntity.MemoryTypeName, ramEntity.MemoryBytes));
        }
        mSystemRAMModels.put(systemId, systemRAMModels);
        mLiveDataSystemRAMEntities.postValue(mSystemRAMModels);
    }

    private static class loadRAMEntitiesAsyncTask extends AsyncTask<Void, Void, HashMap<UUID, RAMEntity>> {

        private RAMDao mRAMDao;
        private HashMap<UUID, RAMEntity> mRAMEntities;


        loadRAMEntitiesAsyncTask(RAMDao ramDao, HashMap<UUID, RAMEntity> RAMEntities) {
            mRAMDao = ramDao;
            mRAMEntities = RAMEntities;
        }

        protected HashMap<UUID, RAMEntity> doInBackground(final Void... voids) {
            for (RAMEntity ramEntity : mRAMDao.get())
            {
                mRAMEntities.put(ramEntity.Id, ramEntity);
            }
            return mRAMEntities;
        }

    }

    private static class loadSystemRAMEntitiesAsyncTask extends AsyncTask<Void, Void, HashMap<UUID, List<SystemRAMEntity>>> {

        private SystemRAMDao mSystemRAMDao;
        private HashMap<UUID, List<SystemRAMEntity>> mSystemRAMEntities;

        loadSystemRAMEntitiesAsyncTask(SystemRAMDao systemRAMDao, HashMap<UUID, List<SystemRAMEntity>> systemRAMEntities) {
            mSystemRAMDao = systemRAMDao;
            mSystemRAMEntities = systemRAMEntities;
        }

        protected HashMap<UUID, List<SystemRAMEntity>> doInBackground(final Void... voids) {
            for (SystemRAMEntity SystemRAMEntity : mSystemRAMDao.get())
            {
                List<SystemRAMEntity> RAMEntities = mSystemRAMEntities.get(SystemRAMEntity.SystemId);
                if (RAMEntities == null)
                {
                    RAMEntities = new LinkedList<>();
                }
                RAMEntities.add(SystemRAMEntity);
                mSystemRAMEntities.put(SystemRAMEntity.SystemId, RAMEntities);
            }
            return mSystemRAMEntities;
        }

    }

    private static class upsertRAMAsyncTask extends AsyncTask<Void , Void, Void> {

        private RAMDao mRAMDao;
        private List<RAMEntity> mRAMEntities;

        upsertRAMAsyncTask(RAMDao ramDao, List<RAMEntity> ramEntities) {
            mRAMDao = ramDao;
            mRAMEntities = ramEntities;
        }

        protected Void doInBackground(final Void... voids) {
            for (RAMEntity ramEntity: mRAMEntities) {
                mRAMDao.upsert(ramEntity);
            }
            return null;
        }

    }

    private static class upsertSystemRAMAsyncTask extends AsyncTask<Void , Void, Void> {

        private SystemRAMDao mSystemRAMDao;
        private UUID mSystemId;
        private List<SystemRAMEntity> mSystemRAMEntities;

        upsertSystemRAMAsyncTask(SystemRAMDao systemRAMDao, UUID systemId, List<SystemRAMEntity> systemRAMEntities) {
            mSystemRAMDao = systemRAMDao;
            mSystemId = systemId;
            mSystemRAMEntities = systemRAMEntities;
        }

        protected Void doInBackground(final Void... voids) {
            mSystemRAMDao.deleteBySystemId(mSystemId);
            for (SystemRAMEntity systemRAMEntity: mSystemRAMEntities) {
                mSystemRAMDao.insert(systemRAMEntity);
            }
            return null;
        }

    }

}