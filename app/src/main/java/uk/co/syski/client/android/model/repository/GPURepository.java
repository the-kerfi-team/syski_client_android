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
import uk.co.syski.client.android.model.api.requests.system.APISystemCPURequest;
import uk.co.syski.client.android.model.api.requests.system.APISystemGPURequest;
import uk.co.syski.client.android.model.database.SyskiCache;
import uk.co.syski.client.android.model.database.dao.CPUDao;
import uk.co.syski.client.android.model.database.dao.GPUDao;
import uk.co.syski.client.android.model.database.dao.linking.SystemCPUDao;
import uk.co.syski.client.android.model.database.dao.linking.SystemGPUDao;
import uk.co.syski.client.android.model.database.entity.CPUEntity;
import uk.co.syski.client.android.model.database.entity.GPUEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemCPUEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemGPUEntity;

public enum GPURepository {
    INSTANCE;

    // Database DAO's
    private GPUDao mGPUDao;
    private SystemGPUDao mSystemGPUDao;

    // GPU Cache in Memory
    private HashMap<UUID, GPUEntity> mGPUEntities;
    private HashMap<UUID, List<UUID>> mSystemGPUEntities;


    private MutableLiveData<HashMap<UUID, GPUEntity>> mLiveDataSystemGPUEntities;

    GPURepository() {
        mGPUDao = SyskiCache.GetDatabase().GPUDao();
        mSystemGPUDao = SyskiCache.GetDatabase().SystemGPUDao();


        mGPUEntities = new HashMap<>();
        mSystemGPUEntities = new HashMap<>();

        mLiveDataSystemGPUEntities = new MutableLiveData<>();
        loadFromDatabase();
    }

    private void loadFromDatabase()
    {
        try {
            // Load data from Database for CPU's
            mGPUEntities = new loadGPUEntitiesAsyncTask(mGPUDao, mGPUEntities).execute().get();

            // Load data from Database for System CPU's
            mSystemGPUEntities = new loadSystemGPUEntitiesAsyncTask(mSystemGPUDao, mSystemGPUEntities).execute().get();

            mLiveDataSystemGPUEntities.postValue(mGPUEntities);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void loadFromAPI(Context context, UUID systemId)
    {
        //TODO Load from API every user defined time.
        VolleySingleton.getInstance(context).addToRequestQueue(new APISystemGPURequest(context, systemId));
    }

    public void insert(GPUEntity gpuEntity)
    {
        //TODO Create insert method
    }

    public void insert(UUID systemId, GPUEntity gpuEntity)
    {
        //TODO Create insert method
    }

    public void insert(UUID systemId, List<GPUEntity> gpuEntities)
    {
        //TODO Create insert method
    }

    public LiveData<List<GPUEntity>> getSystemGPUsLiveData(final UUID systemId, Context context)
    {
        loadFromAPI(context, systemId);
        return Transformations.map(mLiveDataSystemGPUEntities, new Function<HashMap<UUID, GPUEntity>, List<GPUEntity>>() {
            @Override
            public List<GPUEntity> apply(HashMap<UUID, GPUEntity> input) {
                List<GPUEntity> result = new LinkedList<>();
                List<UUID> systemCPUEntities = mSystemGPUEntities.get(systemId);
                if (systemCPUEntities != null)
                {
                    for (UUID systemEntityId : systemCPUEntities)
                    {
                        result.add(input.get(systemEntityId));
                    }
                }
                return result;
            }
        });
    }

    public List<CPUEntity> getSystemGPUs(UUID systemId)
    {
        //TODO Create get System Component method
        return null;
    }

    public GPUEntity get(UUID id)
    {
        return mGPUEntities.get(id);
    }

    public List<GPUEntity> getAll()
    {
        //TODO Create get Components method
        return null;
    }

    public void update(GPUEntity gpuEntity)
    {
        //TODO Create update Component method
    }

    public void update(UUID systemId, GPUEntity gpuEntity)
    {
        //TODO Create update Components method
    }

    public void update(UUID systemId, List<GPUEntity> gpuEntities)
    {
        //TODO Create update Components method
    }

    public void upsert(GPUEntity gpuEntity)
    {
        //TODO Create upsert Component method
    }

    public void upsert(UUID systemId, GPUEntity gpuEntity)
    {
        //TODO Create upsert Components method
    }

    public void upsert(UUID systemId, List<GPUEntity> gpuEntities)
    {
        try {
            new upsertSystemGPUAsyncTask(mGPUDao, mSystemGPUDao, systemId, gpuEntities).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        List<UUID> systemCPUEntities = new LinkedList<>();
        for (GPUEntity gpuEntity : gpuEntities)
        {
            systemCPUEntities.add(gpuEntity.Id);
        }
        mSystemGPUEntities.put(systemId, systemCPUEntities);
        mLiveDataSystemGPUEntities.postValue(mGPUEntities);
    }

    public void delete(GPUEntity gpuEntity)
    {
        //TODO Create delete Component method
    }

    public void delete(UUID systemId, GPUEntity gpuEntity)
    {
        //TODO Create delete Component method
    }

    public void delete(UUID systemId, List<GPUEntity> gpuEntities)
    {
        //TODO Create delete Component method
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

    private static class loadSystemGPUEntitiesAsyncTask extends AsyncTask<Void, Void, HashMap<UUID, List<UUID>>> {

        private SystemGPUDao mSystemGPUDao;
        private HashMap<UUID, List<UUID>> mSystemCPUEntities;


        loadSystemGPUEntitiesAsyncTask(SystemGPUDao systemGPUDao, HashMap<UUID, List<UUID>> systemGPUEntities) {
            mSystemGPUDao = systemGPUDao;
            mSystemCPUEntities = systemGPUEntities;
        }

        protected HashMap<UUID, List<UUID>> doInBackground(final Void... voids) {
            for (SystemGPUEntity SystemGPUEntity : mSystemGPUDao.get())
            {
                List<UUID> CPUEntities = mSystemCPUEntities.get(SystemGPUEntity.SystemId);
                if (CPUEntities == null)
                {
                    CPUEntities = new LinkedList<>();
                }
                CPUEntities.add(SystemGPUEntity.GPUId);
                mSystemCPUEntities.put(SystemGPUEntity.SystemId, CPUEntities);
            }
            return mSystemCPUEntities;
        }

    }

    private static class upsertSystemGPUAsyncTask extends AsyncTask<Void, Void, Void> {

        private GPUDao mGPUDao;
        private SystemGPUDao mSystemGPUDao;
        private UUID mSystemId;

        private List<GPUEntity> mGPUEntities;

        upsertSystemGPUAsyncTask(GPUDao gpuDao, SystemGPUDao systemGPUDao, UUID systemId, List<GPUEntity> gpuEntities) {
            mGPUDao = gpuDao;
            mSystemGPUDao = systemGPUDao;
            mSystemId = systemId;

            mGPUEntities = gpuEntities;
        }

        protected Void doInBackground(final Void... voids) {
            mSystemGPUDao.deleteBySystemId(mSystemId);
            for (GPUEntity gpuEntity: mGPUEntities) {
                mGPUDao.upsert(gpuEntity);
                SystemGPUEntity systemGPUEntity = new SystemGPUEntity();
                systemGPUEntity.SystemId = mSystemId;
                systemGPUEntity.GPUId = gpuEntity.Id;
                mSystemGPUDao.insert(systemGPUEntity);
            }
            return null;
        }

    }

}
