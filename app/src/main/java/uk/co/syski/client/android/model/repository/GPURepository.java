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
import uk.co.syski.client.android.model.api.requests.system.APISystemGPURequest;
import uk.co.syski.client.android.model.database.SyskiCache;
import uk.co.syski.client.android.model.database.dao.GPUDao;
import uk.co.syski.client.android.model.database.dao.linking.SystemGPUDao;
import uk.co.syski.client.android.model.database.entity.GPUEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemGPUEntity;
import uk.co.syski.client.android.model.viewmodel.SystemGPUModel;

public enum GPURepository {
    INSTANCE;

    // Database DAO's
    private GPUDao mGPUDao;
    private SystemGPUDao mSystemGPUDao;

    // GPU Cache in Memory
    private HashMap<UUID, GPUEntity> mGPUEntities;
    private HashMap<UUID, List<SystemGPUEntity>> mSystemGPUEntities;
    private HashMap<UUID, List<SystemGPUModel>> mSystemGPUModels;

    private MutableLiveData<HashMap<UUID, List<SystemGPUModel>>> mLiveDataSystemGPUModels;

    GPURepository() {
        mGPUDao = SyskiCache.GetDatabase().GPUDao();
        mSystemGPUDao = SyskiCache.GetDatabase().SystemGPUDao();

        mGPUEntities = new HashMap<>();
        mSystemGPUEntities = new HashMap<>();
        mSystemGPUModels = new HashMap<>();

        mLiveDataSystemGPUModels = new MutableLiveData<>();
        loadFromDatabase();
    }

    private void loadFromDatabase()
    {
        try {
            // Load data from Database for OS's
            mGPUEntities = new loadGPUEntitiesAsyncTask(mGPUDao, mGPUEntities).execute().get();

            // Load data from Database for System OS's
            mSystemGPUEntities = new loadSystemGPUEntitiesAsyncTask(mSystemGPUDao, mSystemGPUEntities).execute().get();

            // Set Data in LiveData
            for (Map.Entry<UUID, List<SystemGPUEntity>> entry : mSystemGPUEntities.entrySet())
            {
                List<SystemGPUModel> GPUModels = mSystemGPUModels.get(entry.getKey());
                if (GPUModels == null)
                {
                    GPUModels = new LinkedList<>();
                }
                for (SystemGPUEntity systemGPUEntity : entry.getValue())
                {
                    GPUEntity gpuEntity = mGPUEntities.get(systemGPUEntity.GPUId);
                    GPUModels.add(new SystemGPUModel(gpuEntity.ModelName, gpuEntity.ManufacturerName));
                }
                mSystemGPUModels.put(entry.getKey(), GPUModels);
            }
            mLiveDataSystemGPUModels.postValue(mSystemGPUModels);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void loadFromAPI(Context context, UUID systemId)
    {
        //TODO Load from API every user defined time.
        VolleySingleton.getInstance(context).addToRequestQueue(new APISystemGPURequest(context, systemId));
    }

    public LiveData<List<SystemGPUModel>> getSystemGPUsLiveData(final UUID systemId, Context context)
    {
        loadFromAPI(context, systemId);
        return Transformations.map(mLiveDataSystemGPUModels, new Function<HashMap<UUID, List<SystemGPUModel>>, List<SystemGPUModel>>() {
            @Override
            public List<SystemGPUModel> apply(HashMap<UUID, List<SystemGPUModel>> input) {
                List<SystemGPUModel> gpuModelList = input.get(systemId);
                if (gpuModelList == null)
                {
                    gpuModelList = new LinkedList<>();
                }
                if (gpuModelList.isEmpty())
                {
                    gpuModelList.add(new SystemGPUModel());
                }
                return gpuModelList;
            }
        });
    }

    public GPUEntity getGPU(UUID id)
    {
        return mGPUEntities.get(id);
    }

    public void upsert(UUID systemId, List<SystemGPUEntity> systemGPUEntities, List<GPUEntity> gpuEntities)
    {
        try {
            new upsertGPUAsyncTask(mGPUDao, gpuEntities).execute().get();
            new upsertSystemGPUAsyncTask(mSystemGPUDao, systemId, systemGPUEntities).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<SystemGPUModel> systemGPUModels = new LinkedList<>();
        for (GPUEntity gpuEntity : gpuEntities)
        {
            mGPUEntities.put(gpuEntity.Id, gpuEntity);
        }
        for (SystemGPUEntity systemGPUEntity : systemGPUEntities)
        {
            GPUEntity gpuEntity = mGPUEntities.get(systemGPUEntity.GPUId);
            systemGPUModels.add(new SystemGPUModel(gpuEntity.ModelName, gpuEntity.ManufacturerName));
        }
        mSystemGPUEntities.put(systemId, systemGPUEntities);
        mSystemGPUModels.put(systemId, systemGPUModels);
        mLiveDataSystemGPUModels.postValue(mSystemGPUModels);
    }

    private static class loadGPUEntitiesAsyncTask extends AsyncTask<Void, Void, HashMap<UUID, GPUEntity>> {

        private GPUDao mGPUDao;
        private HashMap<UUID, GPUEntity> mGPUEntities;


        loadGPUEntitiesAsyncTask(GPUDao cpuDao, HashMap<UUID, GPUEntity> GPUEntities) {
            mGPUDao = cpuDao;
            mGPUEntities = GPUEntities;
        }

        protected HashMap<UUID, GPUEntity> doInBackground(final Void... voids) {
            for (GPUEntity cpuEntity : mGPUDao.get())
            {
                mGPUEntities.put(cpuEntity.Id, cpuEntity);
            }
            return mGPUEntities;
        }

    }

    private static class loadSystemGPUEntitiesAsyncTask extends AsyncTask<Void, Void, HashMap<UUID, List<SystemGPUEntity>>> {

        private SystemGPUDao mSystemGPUDao;
        private HashMap<UUID, List<SystemGPUEntity>> mSystemGPUEntities;


        loadSystemGPUEntitiesAsyncTask(SystemGPUDao systemGPUDao, HashMap<UUID, List<SystemGPUEntity>> systemGPUEntities) {
            mSystemGPUDao = systemGPUDao;
            mSystemGPUEntities = systemGPUEntities;
        }

        protected HashMap<UUID, List<SystemGPUEntity>> doInBackground(final Void... voids) {
            for (SystemGPUEntity SystemGPUEntity : mSystemGPUDao.get())
            {
                List<SystemGPUEntity> GPUEntities = mSystemGPUEntities.get(SystemGPUEntity.SystemId);
                if (GPUEntities == null)
                {
                    GPUEntities = new LinkedList<>();
                }
                GPUEntities.add(SystemGPUEntity);
                mSystemGPUEntities.put(SystemGPUEntity.SystemId, GPUEntities);
            }
            return mSystemGPUEntities;
        }

    }

    private static class upsertGPUAsyncTask extends AsyncTask<Void, Void, Void> {

        private GPUDao mGPUDao;
        private List<GPUEntity> mGPUEntities;

        upsertGPUAsyncTask(GPUDao gpuDao, List<GPUEntity> gpuEntities) {
            mGPUDao = gpuDao;
            mGPUEntities = gpuEntities;
        }

        protected Void doInBackground(final Void... voids) {
            for (GPUEntity gpuEntity: mGPUEntities) {
                mGPUDao.upsert(gpuEntity);
            }
            return null;
        }

    }

    private static class upsertSystemGPUAsyncTask extends AsyncTask<Void, Void, Void> {

        private SystemGPUDao mSystemGPUDao;
        private UUID mSystemId;
        private List<SystemGPUEntity> mSystemGPUEntities;

        upsertSystemGPUAsyncTask(SystemGPUDao systemGPUDao, UUID systemId, List<SystemGPUEntity> systemGPUEntities) {
            mSystemGPUDao = systemGPUDao;
            mSystemId = systemId;
            mSystemGPUEntities = systemGPUEntities;
        }

        protected Void doInBackground(final Void... voids) {
            mSystemGPUDao.deleteBySystemId(mSystemId);
            for (SystemGPUEntity systemGPUEntity: mSystemGPUEntities) {
                mSystemGPUDao.insert(systemGPUEntity);
            }
            return null;
        }

    }

}
